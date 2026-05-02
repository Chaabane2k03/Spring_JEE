package tn.fst.classroom_crb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * URI de base du contrôleur: /ui
 */
@Controller
@RequestMapping("/ui")
public class DashboardController {

    @GetMapping
    public String dashboard() {
        return "dashboard";
    }
}
