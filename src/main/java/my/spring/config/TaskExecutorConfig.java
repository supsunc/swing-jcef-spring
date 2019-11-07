package my.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class TaskExecutorConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Set up the ExecutorService.
        executor.initialize();

        // 线程池核心线程数，核心线程会一直存活，即使没有任务需要处理。
        // 当线程数小于核心线程数时，即使现有的线程空闲，线程池也会优先创建新线程来处理任务，而不是直接交给现有的线程处理。
        // 核心线程在allowCoreThreadTimeout被设置为true时会超时退出，默认情况下不会退出。
        // 默认是 1

        // CPU 核心数 Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() + 1);

        // 当线程数大于或等于核心线程，且任务队列已满时，线程池会创建新的线程，直到线程数量达到maxPoolSize。
        // 如果线程数已等于maxPoolSize，且任务队列已满，则已超出线程池的处理能力，线程池会拒绝处理任务而抛出异常。
        // 默认时是 Integer.MAX_VALUE
        // executor.setMaxPoolSize(10);

        // 任务队列容量。从maxPoolSize的描述上可以看出，任务队列的容量会影响到线程的变化，因此任务队列的长度也需要恰当的设置。
        // 默认时是 Integer.MAX_VALUE
        executor.setQueueCapacity(1000);

        /*  keepAliveTime: 当线程空闲时间达到keepAliveTime，该线程会退出，直到线程数量等于corePoolSize。
         *  默认时是 60
         *  executor.setKeepAliveSeconds(10);
         */

        // allowCoreThreadTimeout: 是否允许核心线程空闲退出，默认值为false。
        // 如果allowCoreThreadTimeout设置为true，则所有线程均会退出直到线程数量为0。
        // executor.setAllowCoreThreadTimeOut(true);

        return executor;
    }
}
