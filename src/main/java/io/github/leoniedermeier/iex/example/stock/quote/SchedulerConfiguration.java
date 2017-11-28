package io.github.leoniedermeier.iex.example.stock.quote;

import java.util.concurrent.ExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class SchedulerConfiguration {

    @Bean
    Scheduler scheduler() {
        final ThreadPoolExecutorFactoryBean bean = new ThreadPoolExecutorFactoryBean();
        bean.setCorePoolSize(3);
        bean.setThreadNamePrefix("scheduler-");
        bean.afterPropertiesSet();
        final ExecutorService executorService = bean.getObject();

        // Gibt den SecurityContext mit
        final DelegatingSecurityContextExecutorService securityContextExecutorService = new DelegatingSecurityContextExecutorService(
                executorService);
        return Schedulers.fromExecutorService(securityContextExecutorService);
    }
}
