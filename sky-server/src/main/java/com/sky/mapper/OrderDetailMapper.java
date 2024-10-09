package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * @description:    批量插入订单明细数据
     * @author: liangguang
     * @date: 2024/9/11 0011 14:14
     * @param: [orderDetailList]
     * @return: void
     **/
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * @description:    查询订单明细
     * @author: liangguang
     * @date: 2024/9/11 0011 21:57
     * @param: [ordersId]
     * @return: java.util.List<com.sky.entity.OrderDetail>
     **/
    List<OrderDetail> getByOrderId(Long ordersId);
}
