package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;

    @GetMapping
    public String getCart(HttpServletRequest request, Model model) {
        Cart cart = cartService.getCart(request);

        model.addAttribute("cart", cart);

        return "cart";
    }

    @PostMapping(value = "/{phoneId}")
    public String deleteCartItem(@PathVariable Long phoneId, Model model, HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        cartService.remove(cart, phoneId);
        model.addAttribute("cart", cart);

        return "redirect:/cart";
    }

//    @PostMapping(value = "/update")
//    public String updateCart(
//            @ModelAttribute("cartItemDTO") @Valid CartItemDTO cartItemDTO,
//    BindingResult result,
//                             Model model,
//                             HttpServletRequest request) {
//        //2. переделать cart -> @Valid @RequestParam CartItem
//        Cart cart = cartService.getCart(request);
////        Map<Long, String> errors = new HashMap<>();
//
//        if(result.hasErrors()) {
//            model.addAttribute("cart", cart);
//
//            return "cart";
//        } else {
//            cartService.update(cart, cartItemDTO.getId(), cartItemDTO.getQuantity());
//            model.addAttribute("cart", cart);
//
//            return "redirect:/cart";
//        }
////
////        for (int i = 0; i < productId.length; i++) {
////            try {
////                cartService.update(cart, productId[i], quantity[i]);
////            } catch (QuantityNullException | PhonePriceException | OutOfStockException | NegativeQuantityException
////                     | NumberFormatException | ArrayIndexOutOfBoundsException ex) {
////                handleUpdateErrors(errors, productId[i], ex);
////            }
////        }
//
//
////        if(errors.isEmpty()){
////            return "redirect:/cart";
////        } else{
////            model.addAttribute("errors", errors);
////            return "cart";
////        }
//
//    }
}
