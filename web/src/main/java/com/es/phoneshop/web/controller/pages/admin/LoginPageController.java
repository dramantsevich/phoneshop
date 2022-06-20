package com.es.phoneshop.web.controller.pages.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginPageController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
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
