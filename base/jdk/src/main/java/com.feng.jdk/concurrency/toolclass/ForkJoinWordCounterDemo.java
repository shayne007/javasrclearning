package com.feng.jdk.concurrency.toolclass;


import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @Description Fork/Join：单机版的MapReduce
 * 分而治之的思想；
 * @Author fengsy
 * @Date 11/8/21
 */
public class ForkJoinTimeSliceDemo {

    public static void main(String[] args) {
        //创建分治任务线程池
        ForkJoinPool pool = new ForkJoinPool(4);
        //创建分治任务

        TimeSliceTask task = new TimeSliceTask(new Date(), new Date(System.currentTimeMillis() + 600_000));
        //启动分治任务
        Map<String, Boolean> result = pool.invoke(task);
        //输出结果
        result.forEach((k, v) -> System.out.println("key: " + k + ", value: " + v));
    }

    //递归任务
    static class TimeSliceTask extends RecursiveTask<Map<String, Boolean>> {
        final Date start;
        final Date end;

        TimeSliceTask(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Boolean> compute() {

            if (DateUtils.addMinutes(start, 1).after(end)) {
                //处理最终不可分的子任务
                return calculate(start);
            } else {
                Date middleTime = new Date(start.getTime() + ((end.getTime() - start.getTime()) / 2));
                //创建子任务
                TimeSliceTask left = new TimeSliceTask(start, middleTime);
                TimeSliceTask right = new TimeSliceTask(middleTime, end);
                left.fork();
                //等待子任务结果，并合并结果
                return merge(right.compute(), left.join());

            }
        }

        private Map<String, Boolean> calculate(Date start) {
            Map<String, Boolean> mapper = new HashMap<>();
            mapper.put(org.apache.http.client.utils.DateUtils.formatDate(start, "yyyyMMddHHmm "), true);
            return mapper;
        }

        private Map<String, Boolean> merge(Map<String, Boolean> m1, Map<String, Boolean> m2) {
            Map<String, Boolean> mapper = new HashMap<>();
            mapper.putAll(m1);
            mapper.putAll(m2);
            return mapper;
        }
    }
}
