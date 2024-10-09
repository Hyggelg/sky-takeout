package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:    数据统计
 * @author: liangguang
 * @date: 2024/9/16 0016 15:23
 *
 **/
@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * @description:    营业额统计
     * @author: liangguang
     * @date: 2024/9/16 0016 15:31
     * @param: [begin, end]
     * @return: com.sky.result.Result<com.sky.vo.TurnoverReportVO>
     **/
    @GetMapping("/turnoverStatistics")
    @ApiOperation(value = "营业额统计接口")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,//年月日格式
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额数据统计:{},{}", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }

    /**
     * @description:    用户数据统计
     * @author: liangguang
     * @date: 2024/9/22 0022 15:59
     * @param: [begin, end]
     * @return: com.sky.result.Result<com.sky.vo.UserReportVO>
     **/
    @ApiOperation("用户数据统计")
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("在这个时间区间的用户数据统计：{}， {}", begin, end);
        return Result.success(reportService.getuserStatistics(begin, end));
    }

    /**
     * @description:    订单统计数据     有效订单数、总订单数、订单完成率      订单完成率 = 有效订单数 / 总订单数 * 100%
     * @author: liangguang
     * @date: 2024/9/22 0022 18:00
     * @param: [begin, end]
     * @return: com.sky.result.Result<com.sky.vo.OrderReportVO>
     **/
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单数据统计")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计数据:{}, {}", begin, end);
        return Result.success(reportService.getordersStatistics(begin, end));
    }

    /**
     * @description:    查询销量排名top10
     * @author: liangguang
     * @date: 2024/9/22 0022 21:27
     * @param: [begin, end]
     * @return: com.sky.result.Result<com.sky.vo.SalesTop10ReportVO>
     **/
    @ApiOperation("查询销量排名top10")
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("查询销量排名top10:{},{}",begin, end);
        return Result.success(reportService.getSalesTop10(begin, end));
    }

    /**
     * @description:    导出最近30天运营数据报表
     * @author: liangguang
     * @date: 2024/9/22 0022 23:29
     * @param: [response]
     * @return: void
     **/
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    public void export(HttpServletResponse response){
        reportService.exportBusinessData(response);
    }
}
