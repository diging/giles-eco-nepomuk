package edu.asu.diging.gilesecosystem.nepomuk.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.apps.IRegisteredAppManager;

@Controller
public class RemoveRegisteredAppController {

    @Autowired
    private IRegisteredAppManager appManager;
    
    @RequestMapping(value = "/admin/apps/{id}/delete", method = RequestMethod.GET)
    public String showRemoveAppPage(Model model, @PathVariable String id) {
        IRegisteredApp app = appManager.getApp(id);
        if (app == null) {
            return "notFound";
        }
        
        model.addAttribute("app", app);
        return "admin/apps/delete";
    }
    
    @RequestMapping(value = "/admin/apps/{id}/delete", method = RequestMethod.POST)
    public String deleteApp(@PathVariable String id, RedirectAttributes redirectAttrs) {
        IRegisteredApp app = appManager.getApp(id);
        if (app == null) {
            return "notFound";
        }
        
        appManager.deleteApp(id);
        redirectAttrs.addFlashAttribute("show_alert", true);
        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", "The app has been deleted.");
        
        return "redirect:/admin/apps";
    }
}
