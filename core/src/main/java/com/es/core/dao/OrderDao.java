package com.es.core.dao;

public interface OrderDao {
    void updateReservedValueById(int updateReserved, long id);

    int getReservedValueById(long id);

    int getStockValueById(long id);
}
