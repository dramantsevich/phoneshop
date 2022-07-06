package com.es.phoneshop.web.controller.pages;

import com.es.core.dto.CartDTO;
import com.es.core.dto.ValidList;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import com.es.core.service.impl.cart.DefaultCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;

    @Resource
    private DefaultCartService defaultCartService;

    @GetMapping
    public String getCart(HttpServletRequest request, Model model) {
        Cart cart = cartService.getCart(request);

        ValidList cartDTOList = getCartDTOValidList(cart);

        model.addAttribute("cartDTOList", cartDTOList);
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

    @PostMapping(value = "/update")
    public String updateCart(@ModelAttribute("cartDTOList") @Valid ValidList cartDTOValidList, BindingResult result,
                             Model model, HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        ValidList validList;

        if (result.hasErrors()) {
            validList = getCartDTOValidListHasErrors(cart, cartDTOValidList);
        } else {
            try {
                cartDTOValidList.getList()
                        .forEach(item -> cartService.update(cart, item.getItemId(), item.getQuantity()));

                model.addAttribute("cart", cart);
                model.addAttribute("cartDTOList", cartDTOValidList);

                return "redirect:/cart";
            } catch (OutOfStockException e) {
                validList = getCartDTOValidListHasErrors(cart, cartDTOValidList);

                model.addAttribute("errorMessage", "Out of stock");
            }
        }
        model.addAttribute("cart", cart);
        model.addAttribute("cartDTOList", validList);

        return "cart";
    }

    private ValidList getCartDTOValidList(Cart cart) {
        ValidList cartDTOList = new ValidList();

        List<CartDTO> cartList = cart.getItems()
                .stream()
                .map(i -> new CartDTO(i.getStock().getPhone().getId(),
                        i.getStock(),
                        i.getQuantity()))
                .collect(Collectors.toList());

        cartDTOList.setList(cartList);

        return cartDTOList;
    }

    private ValidList getCartDTOValidListHasErrors(Cart cart, ValidList cartDTOValidList) {
        List<CartDTO> cartListWithoutQuantity = cart.getItems()
                .stream()
                .map(i -> new CartDTO(i.getStock().getPhone().getId(),
                        defaultCartService.getPhone(i.getStock().getPhone().getId())))
                .collect(Collectors.toList());
        List<CartDTO> cartList = IntStream.range(0, cartDTOValidList.getList().size())
                .mapToObj(i -> new CartDTO(cartListWithoutQuantity.get(i).getItemId(),
                        cartListWithoutQuantity.get(i).getStock(),
                        cartDTOValidList.getList().get(i).getQuantity()))
                .collect(Collectors.toList());

        cartDTOValidList.setList(cartList);

        return cartDTOValidList;
    }
}
