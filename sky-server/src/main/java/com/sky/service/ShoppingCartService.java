package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * @description:    添加购物车
     * @author: liangguang
     * @date: 2024/9/4 0004 22:02
     * @param: [shoppingCart]
     * @return: void
     **/
    void addShoppingCart(ShoppingCartDTO shoppingCart);

    /**
     * @description:    查看购物车
     * @author: liangguang
     * @date: 2024/9/4 0004 22:58
     * @param: []
     * @return: java.util.List<com.sky.entity.ShoppingCart>
     **/
    List<ShoppingCart> showShoppingCart();

    /**
     * @description:    清空购物车
     * @author: liangguang
     * @date: 2024/9/4 0004 23:07
     * @param: []
     * @return: void
     **/
    void cleanShoppingCart();

    /**
     * @description:    删除购物车中一个商品
     * @author: liangguang
     * @date: 2024/9/5 0005 13:37
     * @param: [shoppingCartDTO]
     * @return: void
     **/
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
