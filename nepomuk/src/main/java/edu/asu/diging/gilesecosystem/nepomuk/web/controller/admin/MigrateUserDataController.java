package edu.asu.diging.gilesecosystem.nepomuk.web.controller.admin;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.migrate.impl.MigrationManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.migrate.impl.MigrationResult;

@Controller
public class MigrateUserDataController {

    @Autowired
    private MigrationManager migrateManager;
    
    @Autowired
    private IFilesManager filesManager;
    
    @RequestMapping(value = "/admin/migrate")
    public String showPage(Model model) throws InterruptedException, ExecutionException {
        MigrationResult result = migrateManager.checkResults();
        if (result == null) {
            return "admin/migrate/running";
        }
        
        List<String> usernames = filesManager.getKnownUsernames();
        model.addAttribute("usernames", usernames.toArray(new String[usernames.size()]));
        model.addAttribute("result", result);
        return "admin/migrate";
    }
    
    @RequestMapping(value = "/admin/migrate", method=RequestMethod.POST)
    public String startMigration(@RequestParam String username) throws UnstorableObjectException {
        if (username == null) {
            return "redirect:/admin/migrate";
        }
        migrateManager.runMigrations(username);
        return "redirect:/admin/migrate";
    }
}
