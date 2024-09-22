package com.ryan.demo.quartz_demo;

import com.ryan.demo.quartz_demo.jobs.HelloJob;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuartzDemoApplicationTests {

    @Test
    void testJob() {
        try {
            // 通过调度器工厂，创建任务调度器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 通过任务构建器，来创建 Job 实例（Job Detail）
            JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("my_hello_job").build();

            // 获取 JobDataMap 来存储状态信息
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            jobDataMap.put("name", "ryan");
            jobDataMap.put("age", 18);

            // 通过触发器构建器，来创建触发器对象，并配置每隔三秒触发一次
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("1,3,5,7,9 * * * * ?");
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("my_hello_trigger").
                    withSchedule(cronScheduleBuilder).build();

            // 使用 Scheduler 将 Job 和 Trigger 进行关联
            scheduler.scheduleJob(jobDetail, trigger);

            // 启动任务调度器，开始任务调度
            scheduler.start();

            // 主线程休眠，等待任务运行
            Thread.sleep(50000);
        } catch (SchedulerException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
