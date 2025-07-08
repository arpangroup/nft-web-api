//package com.trustai.common.audit;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class AuditAspect {
//    private final AuditLogRepository auditLogRepository;
//    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);
//
//    @Around("@annotation(audit)")
//    public Object logAudit(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {
//        String action = audit.action();
//        String methodName = joinPoint.getSignature().toShortString();
//        String username = getCurrentUsername(); // from SecurityContext
//
//        // Extract parameters
//        Object[] methodArgs = joinPoint.getArgs();
//        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
//        Map<String, Object> paramMap = new HashMap<>();
//        for (int i = 0; i < parameterNames.length; i++) {
//            paramMap.put(parameterNames[i], methodArgs[i]);
//        }
//        String parametersJson = new ObjectMapper().writeValueAsString(paramMap);
//
//
//
//
//        AuditLog auditLog = new AuditLog();
//        auditLog.setAction(action);
//        auditLog.setMethod(methodName);
//        auditLog.setUsername(username);
//        auditLog.setParameters(parametersJson);
//        auditLog.setTimestamp(LocalDateTime.now());
//
//        // Example: You could log to DB or send to Kafka here
//        //log.info("Audit START - Action: {}, Method: {}", action, methodName);
//
//        try {
//            Object result = joinPoint.proceed();
//            auditLog.setStatus("SUCCESS");
//            auditLogRepository.save(auditLog);
//            //log.info("Audit SUCCESS - Action: {}", action);
//            return result;
//        } catch (Throwable ex) {
//            log.error("Audit FAILED - Action: {}, Error: {}", action, ex.getMessage());
//            auditLog.setStatus("FAILURE");
//            auditLog.setErrorMessage(ex.getMessage());
//            auditLogRepository.save(auditLog);
//            throw ex;
//        }
//    }
//
//    private String getCurrentUsername() {
//        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "SYSTEM";*/
//        return "";
//    }
//
//}
