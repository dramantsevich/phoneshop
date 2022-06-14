package com.es.phoneshop.web.controller.pages.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping(value = "/login")
public class LoginPageController {
    @RequestMapping(method = RequestMethod.GET)
    public String getLoginPage() {
        return "adminLogin";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String goToAdminOrdersPage(HttpServletRequest request,
                                      Model model) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        Map<String, String> errors = new HashMap<>();
        errorHandler(errors, userName, "userName");
        errorHandler(errors, password, "password");

        if(errors.isEmpty()) {
            if(Objects.equals(userName, "Admin") && Objects.equals(password, "Admin")) {
                return "redirect:/admin/orders";
            } else {
                errors.put("Invalid data", "Authorization error");
                model.addAttribute("errors", errors);

                return "adminLogin";
            }
        } else {
            model.addAttribute("errors", errors);

            return "adminLogin";
        }
    }

    private Map<String, String> errorHandler(Map<String, String> errors, String value, String parameter) {
        if(value == null || value.isEmpty()) {
            errors.put(parameter, " is empty");
        }

        return errors;
    }
}
