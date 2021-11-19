package com.psi.order.controller;

import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.order.entity.Cart;
import com.psi.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 根据用户名获取购物车列表
     *
     * @param username
     * @return
     */
    @GetMapping("findCartList")
    public List<Cart> findCartList(String username) {
        return cartService.findCartListFromRedis(username);
    }

    /**
     * 添加商品到购物车
     *
     * @param itemId
     * @param num
     * @return
     */
    @GetMapping("addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        String username = "testUser";
        try {
            //获取购物车列表
            List<Cart> cartList = this.findCartList(username);
            //添加商品到购物车
            cartList = cartService.addGoodsToCart(cartList, itemId, num);
            //购物车保存到Redis
            cartService.saveCartListToRedis(username, cartList);

            return new Result(true, StatusCode.OK, "添加到购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "添加到购物车失败");
        }
    }
}
