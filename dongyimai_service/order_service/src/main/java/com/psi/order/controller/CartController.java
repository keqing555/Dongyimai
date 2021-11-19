package com.psi.order.controller;

import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.order.entity.Cart;
import com.psi.order.service.CartService;
import com.psi.utils.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 根据用户名获取购物车列表
     *
     * @return
     */
    @GetMapping("findCartList")
    public List<Cart> findCartList() {
        return cartService.findCartListFromRedis(this.getUsername());
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

        try {
            //获取购物车列表
            List<Cart> cartList = this.findCartList();
            //添加商品到购物车
            cartList = cartService.addGoodsToCart(cartList, itemId, num);
            //购物车保存到Redis
            cartService.saveCartListToRedis(this.getUsername(), cartList);

            return new Result(true, StatusCode.OK, "添加到购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "添加到购物车失败");
        }
    }

    /***
     * 获取TokenDecode从公钥中读取的用户信息
     * @return
     */
    private String getUsername() {
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        return userInfo.get("user_name");
    }
}
