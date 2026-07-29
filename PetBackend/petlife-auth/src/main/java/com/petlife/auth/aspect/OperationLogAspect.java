package com.petlife.auth.aspect;

import com.petlife.common.annotation.OperationLog;
import com.petlife.auth.service.SysLogService;
import com.petlife.common.entity.SysLog;
import com.petlife.common.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
public class OperationLogAspect {

    private final SysLogService sysLogService;
    private final ObjectMapper objectMapper;

    public OperationLogAspect(SysLogService sysLogService, ObjectMapper objectMapper) {
        this.sysLogService = sysLogService;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(com.petlife.common.annotation.OperationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader("Authorization");
        Long userId = null;
        String userName = "admin";

        if (token != null && token.startsWith("Bearer ")) {
            try {
                userId = JwtUtil.getUserIdFromToken(token.substring(7));
                userName = "管理员-" + userId;
            } catch (Exception ignored) {
            }
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLogAnnotation = method.getAnnotation(OperationLog.class);

        String operation = operationLogAnnotation.operation();
        String module = operationLogAnnotation.module();
        String description = operationLogAnnotation.description();

        if (operation.isEmpty()) {
            String httpMethod = request.getMethod();
            switch (httpMethod) {
                case "POST" -> operation = "新增";
                case "PUT" -> operation = "修改";
                case "DELETE" -> operation = "删除";
                case "GET" -> operation = "查询";
                default -> operation = "操作";
            }
        }

        if (module.isEmpty()) {
            module = joinPoint.getTarget().getClass().getSimpleName().replace("Controller", "");
        }

        String params = "";
        try {
            params = objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (Exception ignored) {
        }

        Object result = null;
        int status = 1;
        String errorMsg = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            status = 0;
            errorMsg = e.getMessage();
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;

            SysLog sysLog = new SysLog();
            sysLog.setUserId(userId);
            sysLog.setUserName(userName);
            sysLog.setOperation(operation);
            sysLog.setModule(module);
            sysLog.setDescription(description);
            sysLog.setUrl(request.getRequestURI());
            sysLog.setMethod(request.getMethod());
            sysLog.setParams(params);
            sysLog.setResult(result != null ? objectMapper.writeValueAsString(result) : "");
            sysLog.setIp(request.getRemoteAddr());
            sysLog.setUserAgent(request.getHeader("User-Agent"));
            sysLog.setExecutionTime(executionTime);
            sysLog.setStatus(status);
            sysLog.setErrorMsg(errorMsg);

            sysLogService.save(sysLog);
        }
    }
}
