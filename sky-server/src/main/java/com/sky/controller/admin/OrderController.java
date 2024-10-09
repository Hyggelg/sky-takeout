package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单管理接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @description:    查询订单详情
     * @author: liangguang
     * @date: 2024/9/16 0016 16:55
     * @param: [id]
     * @return: com.sky.result.Result<com.sky.vo.OrderVO>
     **/
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long id){
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * @description:    订单搜索
     * @author: liangguang
     * @date: 2024/9/16 0016 17:04
     * @param: [ordersPageQueryDTO]
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     **/
    @GetMapping("/conditionSearch")
    @ApiOperation(value = "订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @description:    各个状态的订单数量统计
     * @author: liangguang
     * @date: 2024/9/16 0016 17:33
     * @param: []
     * @return: com.sky.result.Result<com.sky.vo.OrderOverViewVO>
     **/
    @GetMapping("/statistics")
    @ApiOperation(value = "各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO  orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * @description:    接单
     * @author: liangguang
     * @date: 2024/9/16 0016 17:51
     * @param: []
     * @return: com.sky.result.Result
     **/
    @PutMapping("/confirm")
    @ApiOperation(value = "接单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        orderService.confirm(ordersConfirmDTO);
        return Result.success(ordersConfirmDTO);
    }

    /**
     * @description: 拒单
     * @author: liangguang
     * @date: 2024/9/16 0016 17:56
     * @param: [ordersRejectionDTO]
     * @return: com.sky.result.Result
     **/
    @PutMapping("/rejection")
    @ApiOperation(value = "拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejection(ordersRejectionDTO);
        return Result.success(ordersRejectionDTO);
    }

    /**
     * @description:    商家取消订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:06
     * @param: [ordersRejectionDTO]
     * @return: com.sky.result.Result
     **/
    @PutMapping("/cancel")
    @ApiOperation(value = "商家取消订单")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception{
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * @description:    派送订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:15
     * @param: [id]
     * @return: com.sky.result.Result
     **/
    @PutMapping("/delivery/{id}")
    @ApiOperation(value = "派送订单")
    public Result delivery(@PathVariable("id") Long id){
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * @description:    完成订单
     * @author: liangguang
     * @date: 2024/9/16 0016 18:22
     * @param: [id]
     * @return: com.sky.result.Result
     **/
    @PutMapping("/complete/{id}")
    @ApiOperation(value = "完成订单")
    public Result complete(@PathVariable Long id){
        orderService.complete(id);
        return Result.success();
    }
}

