package edu.asu.diging.gilesecosystem.nepomuk.web.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;
import edu.asu.diging.gilesecosystem.nepomuk.core.apps.impl.RegisteredApp;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.apps.IRegisteredAppManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.validators.RegisteredAppValidator;
import edu.asu.diging.gilesecosystem.septemberutil.properties.MessageType;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.TokenGenerationErrorException;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.IAppToken;

@Controller
public class AddRegisteredAppController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IRegisteredAppManager appManager;

    @Autowired
    private ISystemMessageHandler messageHandler;
    
    @InitBinder("app")
    public void init(WebDataBinder binder) {
        binder.addValidators(new RegisteredAppValidator());
    }

    @RequestMapping(value = "/admin/apps/register", method = RequestMethod.GET)
    public String showRegisterAppPage(Model model) {
        model.addAttribute("app", new RegisteredApp());
        return "admin/apps/register";
    }
    
    @RequestMapping(value = "/admin/apps/register", method = RequestMethod.POST)
    public String registerApp(@Validated @ModelAttribute("app") RegisteredApp app, BindingResult results, Model model, RedirectAttributes redirectAttrs) {
        
        if (results.hasErrors()) {
            return "admin/apps/register";
        }
        
        IRegisteredApp newApp = appManager.storeApp(app);
        
        IAppToken token = null;
        try {
            token = appManager.createToken(newApp);
        } catch (TokenGenerationErrorException e) {
            messageHandler.handleMessage("Token generation failed.", e, MessageType.ERROR);
            redirectAttrs.addFlashAttribute("show_alert", true);
            redirectAttrs.addFlashAttribute("alert_type", "danger");
            redirectAttrs.addFlashAttribute("alert_msg", "You app has been registered, but token generation failed.");   
        }
        redirectAttrs.addFlashAttribute("token", token);
        
        return "redirect:/admin/apps/" + app.getId();
    }
}
