package kware.common.config.aop;

import cetus.bean.AuditBean;
import cetus.user.UserUtil;
import kware.common.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QueryInterceptorAspect {

    @Around("execution(* cetus.dao.SuperDao.*(..)) || execution(* cetus.dao.CetusDao.*(..)) || execution(* kware.apps..*Dao.*(..)))")
    public Object daoMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        Object target = joinPoint.getTarget();

        String methodName = joinPoint.getSignature().getName();

        // {insert, update} method check
        if(methodName.length() != 0 &&
                (methodName.contains("insert") || methodName.contains("update"))
        ) {
            // {authentication} check
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if( UserUtil.isAuthenticated(authentication) ) {
                PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
                Object[] args = joinPoint.getArgs();
                for ( Object arg : args ) {
                    if ( arg instanceof AuditBean) {
                        ((AuditBean) arg).setRegUid(details.getUser().getUid());
                        ((AuditBean) arg).setUpdtUid(details.getUser().getUid());
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
