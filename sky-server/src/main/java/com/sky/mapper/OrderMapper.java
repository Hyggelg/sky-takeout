package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * @description:     插入订单数据
     * @author: liangguang
     * @date: 2024/9/11 0011 15:36
     * @param: [orders]
     * @return: void
     **/
    void insert(Orders orders);

    /**
     * @description:     根据订单号查询订单
     * @author: liangguang
     * @date: 2024/9/11 0011 15:36
     * @param: [orderNumber]
     * @return: com.sky.entity.Orders
     **/
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * @description:    修改订单信息
     * @author: liangguang
     * @date: 2024/9/11 0011 15:36
     * @param: [orders]
     * @return: void
     **/
    void update(Orders orders);

    /**
     * @description:    历史订单分页查询
     * @author: liangguang
     * @date: 2024/9/11 0011 21:56
     * @param: [ordersPageQueryDTO]
     * @return: com.github.pagehelper.Page<com.sky.entity.Orders>
     **/
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * @description:    根据id查询订单
     * @author: liangguang
     * @date: 2024/9/11 0011 22:43
     * @param: [orderId]
     * @return: void
     **/
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * @description:    根据订单的状态统计订单的数量
     * @author: liangguang
     * @date: 2024/9/16 0016 11:34
     * @param: [status]
     * @return: java.lang.Integer
     **/
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * @description:    根据订单状态和下单时间查询订单
     * @author: liangguang
     * @date: 2024/9/16 0016 11:34
     * @param: [status, orderTime]
     * @return: java.util.List<com.sky.entity.Orders>
     **/
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLt(Integer status, LocalDateTime orderTime);

    /**
     * @description:    根据动态条件统计营业额
     * @author: liangguang
     * @date: 2024/9/16 0016 16:04
     * @param: [map]
     * @return: java.lang.Double
     **/
    Double sumByMap(Map map);

    /**
     * @description:    根据动态条件统计订单数量
     * @author: liangguang
     * @date: 2024/9/22 0022 18:10
     * @param: [map]
     * @return: java.lang.Integer
     **/
    Integer countByMap(Map map);


    /**
     * @description:    查询销量排名top10
     * @author: liangguang
     * @date: 2024/9/22 0022 21:54
     * @param: [beginTime, endTime]
     * @return: void
     **/
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
