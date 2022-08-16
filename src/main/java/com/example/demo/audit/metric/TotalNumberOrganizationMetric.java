package com.example.demo.audit.metric;

import com.example.demo.service.OrganizationService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Метрика количества сотрудничающих организаций.
 */
public class TotalNumberOrganizationMetric implements MeterBinder {

    @Autowired
    private OrganizationService organizationService;

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder("total_number_organization", organizationService::getTotalOrganization)
                .description("общее количество сотрудничающих организаций")
                .register(meterRegistry);
    }
}
