package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.Set;

@Slf4j
public class TimeOutTaskJob implements Job {

    /**
     * 若参数变量名修改 QuartzJobController中也需对应修改
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            final Scheduler scheduler = jobExecutionContext.getScheduler();
            Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
            for (JobKey str : jobKeySet) {
                System.out.println("当前运行的job任务"+str);
                System.out.println("当前运行的job任务"+str.getName());
                System.out.println("当前运行的job任务"+str.getGroup());
                System.out.println(this.parameter);
            }
            log.info(" Jeecg-Boot 普通定时任务 SampleJob !  时间:" + DateUtils.getTimestamp());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}