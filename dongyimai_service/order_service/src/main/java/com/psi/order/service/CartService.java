package com.psi.order.service;

import com.psi.order.entity.Cart;

import java.util.List;

public interface CartService {

    /***
     * 添加商品到购物车
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    List<Cart> addGoodsToCart(List<Cart> cartList, Long itemId, Integer num);

    /***
     * 格局用户名从redis里查询购物车
     * @param username
     * @return
     */
    List<Cart> findCartListFromRedis(String username);

    /***
     * 把用户的购物车保存到redis
     * @param username
     * @param cartList
     */
    void saveCartListToRedis(String username, List<Cart> cartList);
}
