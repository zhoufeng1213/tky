package com.xxxx.cc.util;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadTask {
    private static ThreadTask instance;
    private final int netThreadCount = 5;
    private final int dbThreadCount = 3;
    private final int otherThreadCount = 10;
    private ThreadPoolExecutor dbThreadPool;
    private ThreadPoolExecutor netThreadPool;
    private ThreadPoolExecutor otherThreadPool;
    private PriorityBlockingQueue dbThreadQueue;
    private PriorityBlockingQueue netThreadQueue;
    private PriorityBlockingQueue otherThreadQueue;
    private Comparator<PrioriTask> taskCompare;

    private ThreadTask() {
        final long keepAliveTime = 60L;
        this.taskCompare = new TaskCompare();
        this.dbThreadQueue = new PriorityBlockingQueue(3, this.taskCompare);
        this.netThreadQueue = new PriorityBlockingQueue(5, this.taskCompare);
        this.otherThreadQueue = new PriorityBlockingQueue(3, this.taskCompare);
        this.dbThreadPool = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, this.dbThreadQueue);
        this.netThreadPool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, this.netThreadQueue);
        this.otherThreadPool = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, this.otherThreadQueue);
    }

    public static ThreadTask getInstance() {
        if (ThreadTask.instance == null) {
            ThreadTask.instance = new ThreadTask();
        }
        return ThreadTask.instance;
    }

    public void executorDBThread(final Runnable task, final int priority) {
        if (!this.dbThreadPool.isShutdown()) {
            this.dbThreadPool.execute(new PrioriTask(priority, task));
        }
    }

    public void executorNetThread(final Runnable task, final int priority) {
        if (!this.netThreadPool.isShutdown()) {
            this.netThreadPool.execute(new PrioriTask(priority, task));
        }
    }

    public void executorOtherThread(final Runnable task, final int priority) {
        if (!this.otherThreadPool.isShutdown()) {
            this.otherThreadPool.execute(new PrioriTask(priority, task));
        }
    }

    public void shutDownAll() {
        this.netThreadPool.shutdownNow();
        this.dbThreadPool.shutdownNow();
        this.otherThreadPool.shutdownNow();
        ThreadTask.instance = null;
    }


    public class PrioriTask implements Runnable {
        private int priori;
        private Runnable task;

        public PrioriTask(final int priority, final Runnable runnable) {
            this.priori = priority;
            this.task = runnable;
        }

        public int getPriori() {
            return this.priori;
        }

        public void setPriori(final int priori) {
            this.priori = priori;
        }

        public Runnable getTask() {
            return this.task;
        }

        public void setTask(final Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            if (this.task != null) {
                this.task.run();
            }
        }
    }

    public class TaskCompare implements Comparator<PrioriTask> {
        @Override
        public int compare(final PrioriTask lhs, final PrioriTask rhs) {
            return rhs.getPriori() - lhs.getPriori();
        }
    }

    public static class ThreadPeriod {
        public static final int PERIOD_LOW = 1;
        public static final int PERIOD_MIDDLE = 5;
        public static final int PERIOD_HIGHT = 10;
    }
}
