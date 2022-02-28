package top.wangjinhui.rpc.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Danny
 * @description 创建 ThreadPoll(线程池)的工具类,来自 JavaGuide
 * @CreateTime 2022/2/28 14:50
 */
public class ThreadPollFactory {
    /**
     * 线程池参数
     */
    private static final int CORE_POLL_SIZE = 10;
    private static final int MAX_POLL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private final static Logger logger = LoggerFactory.getLogger(ThreadPollFactory.class);

    private static Map<String, ExecutorService> threadPoolsMap = new ConcurrentHashMap<>();

    public ThreadPollFactory() {
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix) {
        return createDefaultThreadPool(threadNamePrefix, false);
    }

    private static ExecutorService createDefaultThreadPool(String threadNamePrefix, Boolean dameon) {
        ExecutorService pool = threadPoolsMap.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadNamePrefix, dameon));
        if (pool.isShutdown() || pool.isTerminated()) {
            threadPoolsMap.remove(threadNamePrefix);
            pool = createThreadPool(threadNamePrefix, dameon);
            threadPoolsMap.put(threadNamePrefix, pool);
        }
        return pool;
    }

    public static void shutDownAll() {
        logger.info("关闭所有线程...");
        threadPoolsMap.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            logger.info("关闭线程池 [{}] [{}]", entry.getKey(), executorService.isTerminated());
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.info("关闭线程失败！");
                executorService.shutdown();
            }
        });
    }

    private static ExecutorService createThreadPool(String threadNamePrefix, Boolean dameon) {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, dameon);
        return new ThreadPoolExecutor(CORE_POLL_SIZE, MAX_POLL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     * 创建 ThreadFactory , 如果 threadNamePrefix 不为空则自行创建 ThreadFactory， 否则使用 defaultThreadFactory
     * @param threadNamePrefix 作为创建线程名字的前缀
     * @param dameon           指定是否为 Daemon Thread（守护线程）
     * @return ThreadFactory
     */
    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean dameon) {
        if (threadNamePrefix != null) {
            if (dameon != null) {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(dameon).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }
}






