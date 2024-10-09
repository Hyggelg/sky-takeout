package com.sky.service;

import com.sky.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {

    /**
     * @description:    统计指定区间营业额的数据
     * @author: liangguang
     * @date: 2024/9/16 0016 15:34
     * @param: [begin, end]
     * @return: com.sky.vo.TurnoverReportVO
     **/
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);


    /**
     * @description:    统计指定区间用户数据
     * @author: liangguang
     * @date: 2024/9/22 0022 16:10
     * @param: [begin, end]
     * @return: com.sky.vo.UserReportVO
     **/
    UserReportVO getuserStatistics(LocalDate begin, LocalDate end);

    /**
     * @description:    订单统计数据     有效订单数、总订单数、订单完成率
     * @author: liangguang
     * @date: 2024/9/22 0022 18:03
     * @param: [begin, end]
     * @return: java.lang.Object
     **/
    OrderReportVO getordersStatistics(LocalDate begin, LocalDate end);

    /**
     * @description:
     * @author: liangguang 查询销量排名top10
     * @date: 2024/9/22 0022 21:29
     * @param: [begin, end]
     * @return: com.sky.vo.SalesTop10ReportVO
     **/
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /*导出最近30天运营数据报表*/
    void exportBusinessData(HttpServletResponse response);
}
