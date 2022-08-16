package com.example.demo.audit.metric;

import com.example.demo.service.UserService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Метрика количества зарегистрированных пользователей.
 */
public class TotalNumberUserMetric implements MeterBinder {

    @Autowired
    private UserService userService;

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder("total_number_users", userService::getTotalCountUser)
                .description("Общее количество пользователей, зарегистрированных в системе")
                .register(meterRegistry);
    }
}
