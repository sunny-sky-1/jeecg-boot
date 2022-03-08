package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sunhuiqiang
 */
@Slf4j
public class TimeOutTask2Job implements Job {

    /**
     * 若参数变量名修改 QuartzJobController中也需对应修改
     */
    private String parameter;
    @Autowired
    private IQuartzJobService quartzJobService;
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Autowired
    private Scheduler scheduler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = null;
        List<QuartzJob> jobList = new ArrayList();
        try {
            final List<QuartzJob> list = quartzJobService.list();
            for (QuartzJob quartzJob : list) {
                System.out.println(quartzJob);
                if(quartzJob.getDescription()!=null&& "带参测试 后台将每隔1秒执行输出日志".equals(quartzJob.getDescription())){
                    quartzJobService.resumeJob(quartzJob);
                }
            }
            System.out.println("---不使用mybatis-plus查询定时任务-----");
                jobKeys = scheduler.getJobKeys(matcher);
                System.out.println(jobKeys);
                for (JobKey jobKey : jobKeys) {
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger : triggers) {
                        System.out.println(jobKey.getName());
                        System.out.println(jobKey.getGroup());
                        System.out.println("触发器:" + trigger.getKey());
                        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                        System.out.println(triggerState.name());
                        if (trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            String cronExpression = cronTrigger.getCronExpression();
                            System.out.println(cronExpression);
                        }
                    }
                }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}