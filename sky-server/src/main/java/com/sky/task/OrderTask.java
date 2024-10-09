package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:    定时任务处理，定时处理订单状态
 * @author: liangguang
 * @date: 2024/9/16 0016 11:25
 * @param:
 * @return:
 **/

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * @description:    处理超时订单
     * @author: liangguang
     * @date: 2024/9/16 0016 11:28
     * @param: []
     * @return: void
     **/
//    @Scheduled(cron = "0 * * * *")//每分钟触发一次
//    @Scheduled(cron = "0/5 * * * * ?")//每隔五秒触发一次
    public void processTimeoutOrder(){
        log.info("定时处理超时订单:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLt(Orders.PENDING_PAYMENT, time);

        if (ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时,自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * @description:    定时处理一直派送中的订单
     * @author: liangguang
     * @date: 2024/9/16 0016 11:41
     * @param: []
     * @return: void
     **/
//    @Scheduled(cron = "0 0 * * * ?")//每天凌晨一点触发
//    @Scheduled(cron = "0/6 * * * * ?")//每隔五秒触发一次
    public void processDeliveryOrder(){
        log.info("处理一直派送中的订单:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLt(Orders.DELIVERY_IN_PROGRESS, time);
        if (ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
