package com.es.phoneshop.web.controller.pages;

import com.es.core.dto.QuickCoderEntryDTO;
import com.es.core.dto.ValidQuickCoderEntryList;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/quickCoderEntry")
public class QuickCoderEntryController {
    @Resource
    private CartService cartService;

    @GetMapping
    public String getQuickCoderEntryPage(Model model, HttpServletRequest request) {
        Cart cart = cartService.getCart(request);

        model.addAttribute("cart", cart);
        model.addAttribute("quickCoderEntryList", new ValidQuickCoderEntryList());

        return "quickCoderEntry";
    }

    @PostMapping
    public String add(@ModelAttribute("quickCoderEntryList") @Valid ValidQuickCoderEntryList validQuickCoderEntryList,
                      BindingResult result,
                      Model model,
                      HttpServletRequest request) {
        Cart cart = cartService.getCart(request);

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "There where errors");
        } else {
            int i = 0;

            try {
                for (QuickCoderEntryDTO item : validQuickCoderEntryList.getList()) {
                    cartService.addPhone(cart, item.getId(), item.getQuantity());

                    item.setId(null);
                    item.setQuantity(null);

                    i++;
                }

                model.addAttribute("successMessage", i + " entity added to cart");
            } catch (EmptyResultDataAccessException e) {
                result.rejectValue("list[" + i + "].id", "error.id", "Product not found");

                model.addAttribute(result.getModel());
                model.addAttribute("errorMessage", "There where errors -> " + (i + 1) +" product not found");
            } catch (OutOfStockException e) {
                model.addAttribute("cart", cart);

                return "error";
            }
        }
        model.addAttribute("cart", cart);

        return "quickCoderEntry";
    }
}