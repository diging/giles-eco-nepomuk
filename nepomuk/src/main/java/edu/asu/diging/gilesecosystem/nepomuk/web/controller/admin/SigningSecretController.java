package edu.asu.diging.gilesecosystem.nepomuk.web.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.Properties;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.system.ISigningSecretGenerator;
import edu.asu.diging.gilesecosystem.util.exceptions.PropertiesStorageException;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

@Controller
public class SigningSecretController {
    
private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISigningSecretGenerator secretGenerator;

    @Autowired
    private IPropertiesManager propertiesManager;
    
    @RequestMapping(value="/admin/system/auth")
    public String showPage(Model model) {
    
        return "admin/system/auth";
    }
    
    @RequestMapping(value="/admin/system/auth", method=RequestMethod.POST)
    public String generateSecrets(Model model) {
        
        String secretTokens = secretGenerator.generateSigningSecret();
        
        Map<String, String> props = new HashMap<String, String>();
        props.put(Properties.NEPOMUK_SIGNING_KEY, secretTokens);
        
        try {
            propertiesManager.updateProperties(props);
        } catch (PropertiesStorageException e) {
            logger.error("Properties could not be stored.", e);
        }
        
        return "admin/system/auth/done";
    }
    
}
