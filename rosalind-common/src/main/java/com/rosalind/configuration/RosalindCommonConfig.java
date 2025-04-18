package com.rosalind.configuration;

import com.rosalind.configuration.database.pg.PostgresqlConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
@Import({
  PostgresqlConfig.class,
  I18nConfig.class,
})
@ComponentScan(basePackages = {"com.rosalind"})
public class RosalindCommonConfig implements AsyncConfigurer {
  private static final int CORE_POOL_SIZE = 5;
  private static final int MAX_POOL_SIZE = 20;
  private static final int QUEUE_CAPACITY = 1000;

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(CORE_POOL_SIZE);
    taskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
    taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
    taskExecutor.setThreadNamePrefix("default-async-");
    taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
    taskExecutor.initialize();
    return taskExecutor;
  }
}
