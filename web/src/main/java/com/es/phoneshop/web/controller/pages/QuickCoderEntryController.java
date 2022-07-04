package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.*;
import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/quickCoderEntry")
public class QuickCoderEntryController {
    @Resource
    private CartService cartService;

    @GetMapping
    public String getQuickCoderEntryPage() {
        return "quickCoderEntry";
    }

    @PostMapping
    public String addToCart(@RequestParam String[] code,
                            @RequestParam String[] quantity,
                            Model model,
                            HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        Map<Long, String> errorsCode = new HashMap<>();
        Map<Long, String> errorsQuantity = new HashMap<>();
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        long quantityLong = 0;

        for (int i = 0; i < code.length; i++) {
            try {
                quantityLong = numberFormat.parse(quantity[i]).longValue();

                cartService.addPhone(cart, Long.valueOf(code[i]), quantityLong);
            } catch (EmptyResultDataAccessException ex) {
                handleCodeErrors(errorsCode, Long.valueOf(code[i]), ex);
            } catch (QuantityNullException | PhonePriceException | OutOfStockException | NegativeQuantityException
                     | NumberFormatException | ArrayIndexOutOfBoundsException | ParseQuantityException |
                     ParseException ex) {

                handleQuantityErrors(errorsQuantity, Long.valueOf(code[i]), ex);
            }
        }

        model.addAttribute("cart", cart);
        model.addAttribute("code", code);
        model.addAttribute("quantity", quantity);
        model.addAttribute("errorsCode", errorsCode);
        model.addAttribute("errorsQuantity", errorsQuantity);

        return "quickCoderEntry";
    }

    private void handleCodeErrors(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass().equals(EmptyResultDataAccessException.class)) {
            errors.put(productId, "For this product stock is null");
        }
    }

    private void handleQuantityErrors(Map<Long, String> errors, Long quantity, Exception e) {
        if (e.getClass().equals(QuantityNullException.class)) {//e.instanceof
            errors.put(quantity, "Field quantity is empty");
        } else if (e.getClass().equals(PhonePriceException.class)) {
            errors.put(quantity, "Price is null, not available now");
        } else if (e.getClass().equals(OutOfStockException.class)) {
            errors.put(quantity, "No such quantity available");
        } else if (e.getClass().equals(NegativeQuantityException.class)) {
            errors.put(quantity, "Should be grater then 0");
        } else if (e.getClass().equals(NumberFormatException.class)) {
            errors.put(quantity, "Enter number");
        } else if (e.getClass().equals(ArrayIndexOutOfBoundsException.class)) {
            errors.put(quantity, "Enter data");
        } else if (e.getClass().equals(ParseQuantityException.class)) {
            errors.put(quantity, "Enter number data");
        } else if (e.getClass().equals(ParseException.class)) {
            errors.put(quantity, "Enter number data");
        }
    }
}