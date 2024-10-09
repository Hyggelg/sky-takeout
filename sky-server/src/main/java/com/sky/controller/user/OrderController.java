package com.sky.controller.user;

import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "C端-订单相关接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @description:    用户下单
     * @author: liangguang
     * @date: 2024/9/11 0011 13:30
     * @param: [ordersSubmitDTO]
     * @return: com.sky.result.Result<com.sky.vo.OrderSubmitVO>
     **/
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单:{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOreder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * @description:    订单支付
     * @author: liangguang
     * @date: 2024/9/11 0011 15:20
     * @param: [ordersPaymentDTO]
     * @return: com.sky.result.Result<OrderPaymentVO>
     **/
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }

    /**
     * @description:    查询历史订单
     * @author: liangguang
     * @date: 2024/9/11 0011 21:26
     * @param: [pageNum, pageSize, status]
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     **/
    @GetMapping("/historyOrders")
    @ApiOperation("查询历史订单")
    public Result<PageResult> getHistoryOrders(int page, int pageSize, Integer status) {
        PageResult pageResult = orderService.getHistoryOrders(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * @description:    查询订单详情
     * @author: liangguang
     * @date: 2024/9/11 0011 22:38
     * @param: [orderId]
     * @return: com.sky.result.Result<com.sky.vo.OrderVO>
     **/
    @GetMapping("/orderDetail/{id}")
    @ApiOperation(value = "查询订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long id){
        log.info("根据订单id查询订单详情:{}",id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * @description:    取消订单
     * @author: liangguang
     * @date: 2024/9/11 0011 23:07
     * @param: [id]
     * @return: com.sky.result.Result
     **/
    @PutMapping("/cancel/{id}")
    @ApiOperation(value = "取消订单")
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        orderService.cancel(id);
        return Result.success();
    }

    /**
     * @description:    再来一单
     * @author: liangguang
     * @date: 2024/9/11 0011 23:27
     * @param: [id]
     * @return: com.sky.result.Result
     **/
    @PostMapping("/repetitions/{id}")
    @ApiOperation(value ="再来一单")
    public Result repetition(@PathVariable("id") Long id){
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * @description:    用户催单
     * @author: liangguang
     * @date: 2024/9/16 0016 14:28
     * @param: [id]
     * @return: com.sky.result.Result
     **/
    @GetMapping("/reminder/{id}")
    @ApiOperation(value = "用户催单")
    public Result reminder(@PathVariable("id") Long id){
        orderService.reminder(id);
        return Result.success();
    }
}
