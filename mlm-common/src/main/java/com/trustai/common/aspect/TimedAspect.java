package com.trustai.common.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TimedAspect {
    private final MeterRegistry meterRegistry;

    @Around("@annotation(Timed)")
    public Object aroundTimedMethod(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Timed timed = signature.getMethod().getAnnotation(Timed.class);

        // Use annotation value or method name as metric name
        String metricName = timed.value();
        if (metricName.isEmpty()) {
            metricName = signature.getDeclaringTypeName() + "." + signature.getName();
        }

        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            return pjp.proceed();
        } finally {
            sample.stop(meterRegistry.timer(metricName));
            log.info("Method {} executed and timed as {}", signature.getName(), metricName);
        }
    }
}
