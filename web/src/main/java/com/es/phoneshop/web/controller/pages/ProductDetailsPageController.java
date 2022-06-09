package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.stock.StockDao;
import com.es.core.model.cart.Cart;
import com.es.core.dao.cart.CartService;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    @Resource
    private CartService cartService;

    @Autowired
    private StockDao stockDao;

    @RequestMapping(value = "/{phoneId}", method = RequestMethod.GET)
    public String productDetails(@PathVariable Long phoneId, Model model, HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        Stock phone = stockDao.getPhoneById(phoneId);

        model.addAttribute("cart", cart);
        model.addAttribute("stock", phone);

        return "productDetails";
    }
}
