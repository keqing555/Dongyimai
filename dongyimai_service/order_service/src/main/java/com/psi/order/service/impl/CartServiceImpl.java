package com.psi.order.service.impl;

import com.psi.order.entity.Cart;
import com.psi.order.pojo.OrderItem;
import com.psi.order.service.CartService;
import com.psi.sellergoods.feign.ItemFeign;
import com.psi.sellergoods.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ItemFeign itemFeign;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public List<Cart> addGoodsToCart(List<Cart> cartList, Long itemId, Integer num) {
        //根据sukId查询sku信息
        Item item = itemFeign.findById(itemId).getData();
        if (item == null) {
            throw new RuntimeException("该商品不存在");
        }
        if (!item.getStatus().equals("1")) {
            throw new RuntimeException("该商品无效");
        }
        //获取商家id
        String sellerId = item.getSellerId();
        //根据商家id判断购物车列表中是否存在该商家的购物车
        Cart cart = this.findCartBySellerId(cartList, sellerId);

        if (cart == null) {
            //购物车列表不存在该商家的购物车对象
            //新建购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());

            OrderItem orderItem = this.createOrderItem(item, num);

            List<OrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(orderItem);

            cart.setOrderItemList(orderItemList);

            //把购物车对象添加到购物车列表
            cartList.add(cart);
        } else {
            //购物车列表存在该商家的购物车
            //判断该购物车列表中是否存在该商品
            OrderItem orderItem = this.findOrderItemByItemId(cart.getOrderItemList(), itemId);
            if (orderItem == null) {
                //不存在该商品，新建订单明细
                orderItem = this.createOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);
            } else {
                //存在该商品，在购物车明细里添加数量，修改总金额
                orderItem.setNum(orderItem.getNum() + num);
                //计算总金额，转为BigDecimal类型
                BigDecimal totalFee = new BigDecimal(orderItem.getNum() * orderItem.getPrice().doubleValue());
                orderItem.setTotalFee(totalFee);

                //如果操作后数量小于等于0，移除该商品
                if (orderItem.getNum() <= 0) {
                    cart.getOrderItemList().remove(orderItem);
                }
                //如果购物车明细小于等于0，移除购物车
                if (cart.getOrderItemList().size() <= 0) {
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }

    /**
     * 从redis里查询购物车
     *
     * @param username
     * @return
     */
    @Override
    public List<Cart> findCartListFromRedis(String username) {
        //从redis里获取指定用户名的购物车数据
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        System.out.println("从Redis里获取购物车成功：" + username);
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    /**
     * 把购物车保存到redis
     *
     * @param username
     * @param cartList
     */
    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        redisTemplate.boundHashOps("cartList").put(username, cartList);
        System.out.println("购物车保存到Redis成功：" + username);
    }

    /**
     * 根据商家id查询购物车列表中的该商家的购物车对象
     *
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart findCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }


    /**
     * 创建订单明细
     *
     * @param item
     * @param num
     * @return
     */
    private OrderItem createOrderItem(Item item, Integer num) {
        if (num <= 0) {
            throw new RuntimeException("数量非法");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * num));

        return orderItem;
    }

    /**
     * 根据商品明细id查询订单明细
     *
     * @param orderItemList
     * @param itemId
     * @return
     */
    private OrderItem findOrderItemByItemId(List<OrderItem> orderItemList, Long itemId) {
        for (OrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId.longValue()) {
                return orderItem;
            }
        }
        return null;
    }
}
