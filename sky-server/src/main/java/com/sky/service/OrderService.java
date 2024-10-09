package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import java.util.List;

public interface OrderService {

    /**
     * @description:    用户下单
     * @author: liangguang
     * @date: 2024/9/11 0011 15:25
     * @param: [ordersSubmitDTO]
     * @return: com.sky.vo.OrderSubmitVO
     **/
    OrderSubmitVO submitOreder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * @description:    订单支付
     * @author: liangguang
     * @date: 2024/9/11 0011 15:26
     * @param: [ordersPaymentDTO]
     * @return: OrderPaymentVO
     **/
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * @description:    支付成功，修改订单状态
     * @author: liangguang
     * @date: 2024/9/11 0011 15:26
     * @param: [outTradeNo]
     * @return: void
     **/
    void paySuccess(String outTradeNo);

    /**
     * @description:    查询历史订单
     * @author: liangguang
     * @date: 2024/9/11 0011 21:21
     * @param: [dishPageQueryDTO]
     * @return: void
     **/
    PageResult getHistoryOrders(int page, int pageSize, Integer status);

    /**
     * @description:    查询订单详情
     * @author: liangguang
     * @date: 2024/9/11 0011 22:40
     * @param: [orderId]
     * @return: void
     **/
    OrderVO details(Long id);

    /**
     * @description:    取消订单
     * @author: liangguang
     * @date: 2024/9/11 0011 23:10
     * @param: [id]
     * @return: void
     **/
    void cancel(Long id) throws Exception;

    /**
     * @description:    再来一单
     * @author: liangguang
     * @date: 2024/9/11 0011 23:27
     * @param: [id]
     * @return: void
     **/
    void repetition(Long id);

    /**
     * @description:    用户催单
     * @author: liangguang
     * @date: 2024/9/16 0016 14:29
     * @param: [id]
     * @return: void
     **/
    void reminder(Long id);

    /**
     * @description:    订单搜索
     * @author: liangguang
     * @date: 2024/9/16 0016 17:04
     * @param: [ordersPageQueryDTO]
     * @return: com.sky.result.PageResult
     **/
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * @description: 各个状态的订单数量统计
     * @author: liangguang
     * @date: 2024/9/16 0016 17:35
     * @param: []
     * @return: com.sky.vo.OrderOverViewVO
     **/
    OrderStatisticsVO statistics();

    /**
     * @description:    接单
     * @author: liangguang
     * @date: 2024/9/16 0016 17:52
     * @param: [ordersConfirmDTO]
     * @return: void
     **/
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * @description:    拒单
     * @author: liangguang
     * @date: 2024/9/16 0016 17:57
     * @param: [ordersRejectionDTO]
     * @return: void
     **/
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * @description:    商家取消订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:10
     * @param: [ordersCancelDTO]
     * @return: void
     **/
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * @description:    派送订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:17
     * @param: [id]
     * @return: void
     **/
    void delivery(Long id);

    /**
     * @description:    完成订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:22
     * @param: [id]
     * @return: void
     **/
    void complete(Long id);
}
