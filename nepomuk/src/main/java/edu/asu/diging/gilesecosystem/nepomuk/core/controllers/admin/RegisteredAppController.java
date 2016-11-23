package edu.asu.diging.gilesecosystem.nepomuk.core.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.gilesecosystem.nepomuk.core.service.apps.IRegisteredAppManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IIdentityProviderRegistry;
import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;

@Controller
public class RegisteredAppController {
    
    @Autowired
    private IRegisteredAppManager appManager;
    
    @Autowired
    private IIdentityProviderRegistry providerRegistry;

    @RequestMapping(value = "/admin/apps")
    public String showRegisteredApps(Model model) {
        model.addAttribute("apps", appManager.getRegisteredApps());
        return "admin/apps";
    }
    
    @RequestMapping(value = "/admin/apps/{id}")
    public String showApp(Model model, @PathVariable String id) {
        IRegisteredApp app = appManager.getApp(id);
        if (app == null) {
            return "notFound";
        }
        
        model.addAttribute("providerName", providerRegistry.getProviderName(app.getProviderId()));
        
        model.addAttribute("app", app);
        return "admin/apps/app";
    }

}
