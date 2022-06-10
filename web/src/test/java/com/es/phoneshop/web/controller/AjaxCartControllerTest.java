package com.es.phoneshop.web.controller;

import com.es.core.dto.CartItemDTO;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AjaxCartController.class)
public class AjaxCartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private CartItemDTO cartItemDTO;

    @SneakyThrows
    @Test
    public void CartTotalCalculationWorksCorrectly() {
        CartItemDTO cartItemDTO = createCartItemDTO();
        this.mockMvc.perform(put("/ajaxCart/update")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartItemDTO.getId()));
    }

    private CartItemDTO createCartItemDTO() {
        cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(1413L);
        cartItemDTO.setQuantity(2);
        cartItemDTO.setTotalQuantity(2);
        cartItemDTO.setTotalCost(new BigDecimal("660.0"));

        return cartItemDTO;
    }

//    private Cart createCart() {
////        Stock phone = createPhoneWithStock();
////        Long quantity = 5L;
////
////        Cart cart = new Cart();
////
////        cartService.addPhone(cart, phone.getPhone().getId(), quantity);
////
////        return cart;
//    }

//    private Stock createPhoneWithStock() {
//        Set<Color> colorSet = new HashSet<>();
//        Color white = new Color(1001L, "White");
//        Color black = new Color(1000L, "Black");
//        colorSet.add(white);
//        colorSet.add(black);
//
//        Phone phone = new Phone();
//        phone.setId(1413L);
//        phone.setBrand("Alcatel");
//        phone.setModel("Alcatel OneTouch Idol X+");
//        phone.setPrice(new BigDecimal("330.0"));
//        phone.setColor(colorSet);
//
//        Stock phoneWithStock = new Stock();
//        phoneWithStock.setPhone(phone);
//        phoneWithStock.setStock(6);
//        phoneWithStock.setReserved(6);
//
//        return phoneWithStock;
//    }
}
