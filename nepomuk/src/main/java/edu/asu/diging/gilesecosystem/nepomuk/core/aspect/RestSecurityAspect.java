package edu.asu.diging.gilesecosystem.nepomuk.core.aspect;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.gilesecosystem.nepomuk.core.aspect.annotations.AppTokenCheck;
import edu.asu.diging.gilesecosystem.nepomuk.core.aspect.tokens.IChecker;
import edu.asu.diging.gilesecosystem.nepomuk.core.aspect.tokens.impl.AppTokenChecker;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.InvalidTokenException;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.ITokenContents;


@Aspect
@Component
public class RestSecurityAspect {

    private Logger logger = LoggerFactory.getLogger(RestSecurityAspect.class);
    
    @Autowired
    private List<IChecker> checkers;
    
    private Map<String, IChecker> tokenCheckers;
    
    @PostConstruct
    public void init() {
        tokenCheckers = new HashMap<>();       
        checkers.forEach(checker -> tokenCheckers.put(checker.getId(), checker));
    }

    @Around("within (edu.asu.diging.gilesecosystem.nepomuk.rest..*) && @annotation(tokenCheck)")
    public Object checkAppTokenAccess(ProceedingJoinPoint joinPoint,
            AppTokenCheck tokenCheck) throws Throwable {
 
        logger.debug("Checking App access token for REST endpoint.");

        UserTokenObject userTokenObj = extractUserTokenInfo(joinPoint, tokenCheck.value(), null);        
        String token = userTokenObj.token;
        TokenHolder holder = new TokenHolder();
        
        ResponseEntity<String> authResult = checkAuthorization(token, AppTokenChecker.ID, holder);
        if (authResult != null) {
            return authResult;
        }
        
        return joinPoint.proceed();
    }
    
    private ResponseEntity<String> checkAuthorization(String token, String provider, TokenHolder tokenHolder) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        CheckerResult validationResult = null;
        
        try {
            validationResult = tokenCheckers.get(provider).validateToken(token);
            tokenHolder.checkResult = validationResult;
            tokenHolder.tokenContents = validationResult.getPayload();
        } catch (InvalidTokenException e) {
            logger.error("Token is invalid.", e);
            Map<String, String> msgs = new HashMap<String, String>();
            msgs.put("errorMsg", e.getLocalizedMessage());
            msgs.put("provider", provider);
            
            return generateResponse(msgs, HttpStatus.UNAUTHORIZED);
        }
        
        if (validationResult == null || validationResult.getPayload() == null) {
            Map<String, String> msgs = new HashMap<String, String>();
            msgs.put("errorMsg", "Missing or invalid token.");
            msgs.put("errorCode", "401");
            msgs.put("provider", provider);
            return generateResponse(msgs, HttpStatus.UNAUTHORIZED);
        }
        
        if (validationResult.getResult() == ValidationResult.EXPIRED) {
            Map<String, String> msgs = new HashMap<String, String>();
            msgs.put("errorMsg", "The sent token is expired.");
            msgs.put("errorCode", "600");
            msgs.put("provider", provider);
            return generateResponse(msgs, HttpStatus.UNAUTHORIZED);
        }
        
        if (validationResult.getResult() != ValidationResult.VALID) {
            Map<String, String> msgs = new HashMap<String, String>();
            msgs.put("errorMsg", validationResult.getResult().name());
            msgs.put("errorCode", "401");
            msgs.put("provider", provider);
            return generateResponse(msgs, HttpStatus.UNAUTHORIZED);
        }
        
        return null;
    }
    
    private ResponseEntity<String> generateResponse(Map<String, String> msgs, HttpStatus status) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectNode root = mapper.createObjectNode();
        for (String key : msgs.keySet()) {
            root.put(key, msgs.get(key));
        }
        
        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, root);
        } catch (IOException e) {
            logger.error("Could not write json.", e);
            return new ResponseEntity<String>(
                    "{\"errorMsg\": \"Could not write json result.\", \"errorCode\": \"errorCode\": \"500\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<String>(sw.toString(), status);
    }

    private UserTokenObject extractUserTokenInfo(ProceedingJoinPoint joinPoint, String tokenParameter, String parameterName) {
        Object[] args = joinPoint.getArgs();
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        String[] argNames = sig.getParameterNames();
        Class<?>[] argTypes = sig.getParameterTypes();

        String token = null;
        for (int i = 0; i < argNames.length; i++) {
            if (argNames[i].equals(tokenParameter)) {
                token = (String) args[i];
            }
            if (HttpServletRequest.class.isAssignableFrom(argTypes[i])) {
                String tokenHeader = ((HttpServletRequest)args[i]).getHeader(HttpHeaders.AUTHORIZATION);
                if (tokenHeader != null) {
                    token = tokenHeader.substring(6);
                }
            }
        }
        
        return new UserTokenObject(token);
    }
    
    /*
     * Helper classes just for this aspect.
     */   
    class UserTokenObject {
        
        public String token;
        
        public UserTokenObject(String token) {
            super();
            this.token = token;
        }
    }
    
    class TokenHolder {
        public CheckerResult checkResult;
        public ITokenContents tokenContents;
    }
    
}
