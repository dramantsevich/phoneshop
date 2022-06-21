package com.es.phoneshop.web.controller.pages;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.es.core.dao.StockDao;
import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import com.es.core.model.phone.Stock;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private StockDao stockDao;

    @Resource
    private CartService cartService;

    @RequestMapping(value = "/{pageNo}", method = RequestMethod.GET)
    public String showProductList(
            @PathVariable(value = "pageNo") Integer pageNo,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            Model model,
            HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        model.addAttribute("cart", cart);

        Pageable firstPage = PageRequest.of(pageNo - 1, size);
        Page<Stock> stockPage = null;

        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

        if (StringUtils.isEmpty(sort) && StringUtils.isEmpty(order)) {
            stockPage = stockDao.findAll(firstPage);
        } else {
            stockPage = stockDao.findSortedPhones(firstPage, sort, order);
        }

        setAttributesToModel(model, pageNo, stockPage);

        return "productList";
    }

    private void setAttributesToModel(Model model, Integer pageNo, Page<Stock> stockPage) {
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", stockPage.getTotalPages());
        model.addAttribute("phones", stockPage);
        model.addAttribute("numbers", IntStream.range(1, stockPage.getTotalPages()).toArray());
    }
}
