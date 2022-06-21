package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.StockDao;
import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import com.es.core.model.phone.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.IntStream;

@RequestMapping(value = "/search")
@Controller
public class SearchController {
    @Resource
    private StockDao stockDao;

    @Resource
    private CartService cartService;

    @RequestMapping(value = "/{pageNo}", method = RequestMethod.GET)
    public String search(@PathVariable(value = "pageNo") Integer pageNo,
                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "sort", required = false) String sort,
                         @RequestParam(value = "order", required = false) String order,
                         Model model,
                         HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        Pageable firstPage = PageRequest.of(pageNo - 1, size);
        Page<Stock> stockPage = null;
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("cart", cart);

        if ((sort != null && !sort.equals("")) && (order != null && !order.equals(""))) {
            stockPage = stockDao.findSortedPhonesByKeyword(firstPage, sort, order, keyword);
        } else {
            stockPage = stockDao.findByKeyword(firstPage, keyword);
        }

        setAttributesToModel(model, pageNo, stockPage, keyword);

        return "search";
    }

    private void setAttributesToModel(Model model, Integer pageNo, Page<Stock> stockPage, String keyword) {
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", stockPage.getTotalPages());
        model.addAttribute("phones", stockPage);
        model.addAttribute("numbers", IntStream.range(1, stockPage.getTotalPages()).toArray());
        model.addAttribute("keyword", keyword);
    }
}
