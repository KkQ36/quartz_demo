package com.ryan.demo.quartz_demo.controller;

import com.ryan.demo.quartz_demo.jobs.HelloJob;
import jakarta.annotation.Resource;
import org.quartz.*;
import org.springframework.web.bind.annotation.*;

/**
 * @author kq
 * 2024-09-21 20:26
 **/
@RestController
@RequestMapping("quartz")
public class QuartzController {

    @Resource
    private Scheduler scheduler;

    @PostMapping("/add")
    public String saveJob(Long bizId, String cronExpr) {
        JobDetail detail = JobBuilder.newJob(HelloJob.class).withIdentity("my_job_" + bizId).build();
        JobDataMap jobDataMap = detail.getJobDataMap();
        jobDataMap.put("biz_id", bizId);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                .cronSchedule(cronExpr)
                .withMisfireHandlingInstructionDoNothing(); // 错过时，不执行任何操作

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("my_trigger_" + bizId)
                .withSchedule(cronScheduleBuilder)
                .build();
        try {
            scheduler.scheduleJob(detail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    @PutMapping("/pause/{bizId}")
    public String pauseJob(@PathVariable Long bizId) {
        JobKey jobKey = JobKey.jobKey("my_job_" + bizId);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return "OK";
    }

    @PutMapping("/resume/{bizId}")
    public String resumeJob(@PathVariable Long bizId) {
        JobKey jobKey = JobKey.jobKey("my_job_" + bizId);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return "OK";
    }

    @DeleteMapping
    public String deleteJob(Long bizId) {
        JobKey jobKey = JobKey.jobKey("my_job_" + bizId);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return "OK";
    }

    @PutMapping("/run/{bizId}")
    public String runJob(@PathVariable Long bizId) {
        JobKey jobKey = JobKey.jobKey("my_job_" + bizId);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return "OK";
    }

    @PutMapping("/update/{bizId}")
    public String updateJob(@PathVariable Long bizId) {
        TriggerKey triggerKey = TriggerKey.triggerKey("my_trigger_" + bizId);
        try {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0-10 * * * * ? *")
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(cronScheduleBuilder)
                    .build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return "OK";
    }



}
