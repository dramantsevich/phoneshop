package com.es.core.dao;

import com.es.core.model.phone.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StockDao {
    Optional<Stock> getPhoneById(Long key);

    List<Stock> findAllWithStock(String property);

    Page<Stock> findAll(Pageable pageable);

    Page<Stock> findByKeyword(Pageable pageable, String keyword);

    Page<Stock> findSortedPhonesByKeyword(Pageable pageable, String property, String direction, String keyword);

    Page<Stock> findSortedPhones(Pageable pageable, String property, String direction);
}
