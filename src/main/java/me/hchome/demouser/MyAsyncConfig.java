package me.hchome.demouser;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
public class MyAsyncConfig {

    @Bean(destroyMethod = "shutdown",name="asyncExecutor")
    public AsyncTaskExecutor asyncTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int numCore = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(2*numCore);
        executor.setMaxPoolSize(2*numCore+2);
        executor.setQueueCapacity(200);
        return executor;
    }

    @Bean(name="securityExecutor")
    public DelegatingSecurityContextAsyncTaskExecutor securityContextAsyncTaskExecutor(@Qualifier("asyncExecutor") AsyncTaskExecutor executor) {
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
