package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * @description:    动态条件查询
     * @author: liangguang
     * @date: 2024/9/4 0004 22:24
     * @param: [shoppingCart]
     * @return: java.util.List<com.sky.entity.ShoppingCart>
     **/
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * @description:    商品存在购物车中，修改
     * @author: liangguang
     * @date: 2024/9/4 0004 22:40
     * @param: [shoppingCart]
     * @return: void
     **/
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * @description:    插入购物车数据
     * @author: liangguang
     * @date: 2024/9/4 0004 22:39
     * @param: [shoppingCart]
     * @return: void
     **/
    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)" +
            "values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * @description:    清空购物车
     * @author: liangguang
     * @date: 2024/9/4 0004 23:09
     * @param: [userId]
     * @return: void
     **/
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * @description:    删除购物车中一个商品
     * @author: liangguang
     * @date: 2024/9/5 0005 13:45
     * @param: [id]
     * @return: void
     **/
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
