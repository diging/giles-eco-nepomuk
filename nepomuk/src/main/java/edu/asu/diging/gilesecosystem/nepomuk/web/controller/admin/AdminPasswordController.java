package edu.asu.diging.gilesecosystem.nepomuk.web.controller.admin;

import java.security.Principal;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import edu.asu.diging.gilesecosystem.nepomuk.web.pages.AdminPassword;
import edu.asu.diging.gilesecosystem.nepomuk.web.validators.AdminPasswordValidator;
import edu.asu.diging.gilesecosystem.septemberutil.service.impl.SystemMessageHandler;
import edu.asu.diging.gilesecosystem.util.exceptions.BadPasswordException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnauthorizedException;
import edu.asu.diging.gilesecosystem.util.users.IAdminUserManager;
import edu.asu.diging.gilesecosystem.util.users.impl.GecoGrantedAuthority;

@Controller
public class AdminPasswordController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AdminPasswordValidator validator;

    @Autowired
    private IAdminUserManager adminManager;

    @Autowired
    private SystemMessageHandler messageHandler;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @RequestMapping(value = "/admin/system/admin")
    public String showPage(Model model, Principal principal) {

        AdminPassword password = new AdminPassword();
        password.setUsername(principal.getName());
        model.addAttribute("adminUser", password);

        return "admin/system/admin";
    }

    @RequestMapping(value = "/admin/system/admin", method = RequestMethod.POST)
    public String changePassword(
            @Validated @ModelAttribute("adminUser") AdminPassword adminPassword,
            BindingResult results, Principal principal, RedirectAttributes redirectAttrs,
            Locale locale) {

        if (!principal.getName().equals(adminPassword.getUsername())) {
            redirectAttrs.addFlashAttribute("show_alert", true);
            redirectAttrs.addFlashAttribute("alert_type", "danger");
            redirectAttrs.addFlashAttribute("alert_msg", messageSource.getMessage(
                    "admin_user_change_password_not_allowed",
                    new String[] { adminPassword.getUsername() }, locale));

            return "redirect:/";
        }

        if (results.hasErrors()) {
            adminPassword.setOldPassword("");
            adminPassword.setNewPassword("");
            adminPassword.setRetypedPassword("");
            return "admin/system/admin";
        }

        boolean success = false;
        try {
            success = adminManager.updatePassword(adminPassword.getUsername(),
                    adminPassword.getOldPassword(), adminPassword.getNewPassword(),
                    GecoGrantedAuthority.ROLE_ADMIN);
        } catch (BadPasswordException | UnauthorizedException e) {
            // this should never happen because it should be caught by the
            // validator
            messageHandler.handleError("Could not update password.", e);
        }

        if (success) {
            redirectAttrs.addFlashAttribute("show_alert", true);
            redirectAttrs.addFlashAttribute("alert_type", "success");
            redirectAttrs.addFlashAttribute("alert_msg", messageSource.getMessage(
                    "admin_user_change_password_success",
                    new String[] { adminPassword.getUsername() }, locale));
        } else {
            redirectAttrs.addFlashAttribute("show_alert", true);
            redirectAttrs.addFlashAttribute("alert_type", "danger");
            redirectAttrs.addFlashAttribute("alert_msg", messageSource.getMessage(
                    "admin_user_change_password_failure",
                    new String[] { adminPassword.getUsername() }, locale));
        }

        return "redirect:/";
    }
}
