package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class MyTask {

    /**
     * @description:    定时任务处理  每隔五秒触发一次  cron表达式(https://cron.qqe2.com/)
     * @author: liangguang
     * @date: 2024/9/15 0015 23:38
     * @param: []
     * @return: void
     **/
//    @Scheduled(cron = "0/5 * * * * ?")//每隔五秒触发一次
   public void execute(){
       log.info("定时任务开始执行:{}",new Date());
   }


}