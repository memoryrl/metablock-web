package cetus.log;

import cetus.config.CetusConfig;
import cetus.config.CetusConfig.Logging;
import cetus.support.Reflector;
import cetus.util.StringUtil;
import cetus.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
@Intercepts({ @Signature(type=StatementHandler.class, method="query", args={ Statement.class, ResultHandler.class }), @Signature(type=StatementHandler.class, method="update", args={ Statement.class }) })
public class MyBatisLogInterceptor implements Interceptor {
    
    private Logging logging;
    
    @Autowired
    public void setLogging(CetusConfig configs) {
        this.logging = configs.getLogging();
    }
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        
        HttpServletRequest request = WebUtil.getCurrentRequest();
        
        if ( request == null ) {
            return invocation.proceed();
        }
        
        if ( logging.isSql() ) {
            StatementHandler handler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = handler.getBoundSql();
            // 쿼리
            String sql = boundSql.getSql().replaceAll("\\s*(?=(\\r\\n|\\r|\\n))+", "");
            
            // 맵핑 파라미터
            Object param = handler.getParameterHandler().getParameterObject();
            if ( param == null ) {
                setSql(request, StringUtils.replace(sql, "?", "''"));
            } else {
                if ( param instanceof String ) {
                    setSql(request, StringUtils.replace(sql, "?", stringValue(param)));
                } else if ( param instanceof Number ) {
                    setSql(request, StringUtils.replace(sql, "?", pureValue(param)));
                } else {
                    
                    List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
                    Map<?, ?> map = null;
                    
                    if ( param instanceof Map ) {
                        map = (Map<?, ?>) param;
                    }
                    
                    for ( ParameterMapping mapping : paramMapping ) {
                        String key = mapping.getProperty();
                        Object value = null;
                        if ( map == null ) {
                            value = Reflector.getValue(param, Reflector.getGetter(param, key));
                        } else {
                            value = map.get(key);
                        }
                        
                        if ( value == null ) {
                            sql = StringUtil.replaceFirst(sql, "?", "#{" + key + "}");
                        } else if ( value instanceof String ) {
                            sql = StringUtil.replaceFirst(sql, "?", stringValue(value));
                        } else {
                            sql = StringUtil.replaceFirst(sql, "?", pureValue(value));
                        }
                    }
                    
                    setSql(request, sql);
                }
            }
        }
        
        try {
            Object result = null;
            if ( logging.isElapsed() ) {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                result = invocation.proceed();
                stopWatch.stop();
                
                setSqlTime(request, stopWatch.getTotalTimeSeconds());
            } else {
                result = invocation.proceed();
            }
            
            setSqlResult(request, result);
            
            return result;
        }
        catch (Throwable t) {
            setSqlError(request, t);
            throw t;
        }
    }
    
    private void setSql(HttpServletRequest request, String sql) {
        StringBuilder sqls = getSqlBuilder(request);
        sqls.append("\n        " + sql);
        request.setAttribute(LoggingInterceptor.LOG_KEY_SQL, sqls);
    }
    
    private void setSqlResult(HttpServletRequest request, Object result) {
        
        StringBuilder sqls = getSqlBuilder(request);
        if ( result instanceof List<?> ) {
            sqls.append("\n\t\t==> RESULT : " + ((List<?>) result).size() + "건 획득");
        } else if ( result instanceof Integer ) {
            sqls.append("\n\t\t==> RESULT : " + ((Integer) result).intValue() + "건 반영");
        }
        sqls.append("\n\t-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        request.setAttribute(LoggingInterceptor.LOG_KEY_SQL, sqls);
    }
    
    private void setSqlError(HttpServletRequest request, Throwable t) {
        
        StringBuilder sqls = getSqlBuilder(request);
        sqls.append("\n\t\t==> ERROR : " + t.getCause());
        sqls.append("\n\t-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        request.setAttribute(LoggingInterceptor.LOG_KEY_SQL, sqls);
    }
    
    private void setSqlTime(HttpServletRequest request, double d) {
        Double sec = (Double) request.getAttribute(LoggingInterceptor.LOG_KEY_SQL_TIME);
        
        if ( sec != null ) {
            sec += d;
        } else {
            sec = d;
        }
        
        request.setAttribute(LoggingInterceptor.LOG_KEY_SQL_TIME, sec);
    }
    
    private StringBuilder getSqlBuilder(HttpServletRequest request) {
        StringBuilder sqls = (StringBuilder) request.getAttribute(LoggingInterceptor.LOG_KEY_SQL);
        
        if ( sqls == null ) {
            sqls = new StringBuilder();
        }
        return sqls;
    }
    
    private String stringValue(Object o) {
        return "'" + parseValue(o) + "'";
    }
    
    private String pureValue(Object o) {
        return parseValue(o);
    }
    
    private String parseValue(Object src) {
        if ( src == null ) {
            return "*";
        }
        
        String _src = StringUtil.toSafeString(src);
        if ( _src.length() > 50 ) {
            _src = _src.substring(0, 50) + "..";
        }
        return _src;
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    @Override
    public void setProperties(Properties properties) {
        log.info("SqlLogInterceptor properties => {}", properties);
    }
    
}