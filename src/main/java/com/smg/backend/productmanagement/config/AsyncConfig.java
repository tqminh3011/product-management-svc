package com.smg.backend.productmanagement.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@Slf4j
public class AsyncConfig {

	public static final String DB_TASK_EXECUTOR = "dbTaskExecutor";

	@Bean(DB_TASK_EXECUTOR)
	public Executor dbTaskExecutor(
			@Value("${app.config.dbThreadPool.corePoolSize:4}") int corePoolSize,
			@Value("${app.config.dbThreadPool.maxPoolSize:4}") int maxPoolSize,
			@Value("${app.config.dbThreadPool.keepAliveSeconds:60}") int keepAliveSeconds
	) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setThreadNamePrefix("db_executor_thread_");
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.initialize();

		return executor;
	}
}