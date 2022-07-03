package com.es.phoneshop.web.controller.pages.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginPageController {
    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String goToAdminOrdersPage() {
        return "adminOrders";
    }

    @RequestMapping("/logout")
    public String logout()
    {
        return "logout";
    }

    @RequestMapping("/error")
    public String error(Model model)
    {
        model.addAttribute("errors", "Invalid data");
        return "login";
    }
}
