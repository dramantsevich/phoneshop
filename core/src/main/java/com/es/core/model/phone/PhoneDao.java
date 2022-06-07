package com.es.core.model.phone;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    Stock getPhoneById(Long key);

    void save(Phone phone);

    List<Phone> findWithLimit(int offset, int limit);

    List<Stock> findAllWithStock();

    Page<Stock> findAll(Pageable pageable);

    Page<Stock> findByKeyword(Pageable pageable, String keyword);

    Page<Stock> findSortedPhonesByKeyword(Pageable pageable, String property, String direction, String keyword);

    Page<Stock> findSortedPhones(Pageable pageable, String property, String direction);
}
