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
                      BindingResult result, Model model, HttpServletRequest request) {
        Cart cart = cartService.getCart(request);

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "There where errors");
        }

        for (int i = 0; i < validQuickCoderEntryList.getList().size(); i++) {
            QuickCoderEntryDTO item = validQuickCoderEntryList.getList().get(i);

            if (!hasIdOrQuantityError(result, i)) {
                try {
                    cartService.addPhone(cart, item.getId(), item.getQuantity());

                    item.setId(null);
                    item.setQuantity(null);

                    model.addAttribute("successMessage", "item's added to cart");
                } catch (EmptyResultDataAccessException | OutOfStockException e) {
                    handleErrors(result, i, e);
                }
            }
        }


        model.addAttribute(result.getModel());
        model.addAttribute("cart", cart);

        return "quickCoderEntry";
    }

    private boolean hasIdOrQuantityError(BindingResult result, int i) {
        String field = "list[" + i + "]";

        return result.hasFieldErrors(field + ".id") || result.hasFieldErrors(field + ".quantity");
    }

    private void handleErrors(BindingResult result, int i, Exception e) {
        String idField = "list[" + i + "].id";
        String errorCode = "error.id";

        if (e.getClass() == EmptyResultDataAccessException.class) {
            result.rejectValue(idField, errorCode, "Product not found");
        } else if (e.getClass() == OutOfStockException.class) {
            result.rejectValue(idField, errorCode, "Out of stock");
        }

    }
}