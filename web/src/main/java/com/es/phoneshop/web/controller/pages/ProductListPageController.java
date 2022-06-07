package com.es.phoneshop.web.controller.pages;

import javax.annotation.Resource;

import com.es.core.model.phone.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.es.core.model.phone.PhoneDao;

import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneDao phoneDao;

    @RequestMapping(value = "/{pageNo}", method = RequestMethod.GET)
    public String showProductList(
            @PathVariable(value = "pageNo") Integer pageNo,
            Model model,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order) {
        Pageable firstPage = PageRequest.of(pageNo - 1, size);
        Page<Stock> stockPage = null;

        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

        if ((sort != null && !sort.equals("")) && (order != null && !order.equals(""))) {
            stockPage = phoneDao.findSortedPhones(firstPage, sort, order);
        } else {
            stockPage = phoneDao.findAll(firstPage);
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
