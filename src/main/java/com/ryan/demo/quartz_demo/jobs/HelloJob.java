package com.ryan.demo.quartz_demo.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author kq
 * 2024-09-21 11:14
 **/
public class HelloJob extends QuartzJobBean {

    public HelloJob() {
        System.out.println("new HelloJob");
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        System.out.println("name：" + jobDataMap.get("name") + ", age：" + jobDataMap.get("age"));
        // 内部的修改不会被保存
        jobDataMap.put("name", "ryan2");
        System.out.println("hello quartz，执行时间为：" + context.getFireTime());
    }

}
