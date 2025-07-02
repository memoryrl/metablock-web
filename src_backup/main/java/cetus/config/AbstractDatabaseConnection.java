package cetus.config;

import cetus.log.MyBatisLogInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import jodd.util.ArraysUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
public abstract class AbstractDatabaseConnection {
    
    private final String cetusMapperPattern;
    private final String myBatisConfig;
    private final CetusConfig.Datasource datasource;
    private final String datasourceName;
    
    protected AbstractDatabaseConnection(CetusConfig config, String datasourceName) {
        this.myBatisConfig = config.getMybatisConfig();
        this.datasource = config.getDatasource().get(datasourceName);
        this.cetusMapperPattern = "classpath*:cetus/**/*-" + this.datasource.getType() + "-sqlmap.xml";
        this.datasourceName = datasourceName;
    }
    
    protected DataSource getDataSource() {
        log.info("Set datasource: {} ({})", datasourceName, datasource.getType());
        
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(datasource.getUrl());
        dataSource.setUsername(datasource.getUsername());
        dataSource.setPassword(datasource.getPassword());
        dataSource.setDriverClassName(datasource.getDriverClassName());
        dataSource.setMaximumPoolSize(datasource.getMaxPoolSize());
        // dataSource.setLeakDetectionThreshold(5000);
        
        return dataSource;
    }
    
    protected PlatformTransactionManager getTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    /**
     * DataSource가 추가될때 CETUS_MAPPER_PATTERN이 중복으로 등록될 수가 있음
     * 이를 위해 boolean isDefault를 추가해서 처리함
     * 향후 신규 DataSource 추가시에는 isDefault를 false로 호출하도록 한다.
     */
    protected SqlSessionFactory getSqlSessionFactory(DataSource dataSource,
                                                     MyBatisLogInterceptor interceptor,
                                                     boolean isDefault) throws Exception {
        
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource configLocation = resolver.getResource(myBatisConfig);
        Resource[] cetusMapperLocations = resolver.getResources(cetusMapperPattern);
        Resource[] projectMapperLocations = resolver.getResources(datasource.getMapperPattern());
        
        Resource[] mapperLocations;
        if ( isDefault ) {
            mapperLocations = ArraysUtil.join(cetusMapperLocations, projectMapperLocations);
        } else {
            mapperLocations = projectMapperLocations;
        }
        
        bean.setVfs(SpringBootVFS.class);
        bean.setMapperLocations(mapperLocations);
        bean.setConfigLocation(configLocation);
        bean.setDataSource(dataSource);
        bean.setPlugins(new Interceptor[] { interceptor });
        
        return bean.getObject();
    }
    
    protected DataSource getLazyConnectionDataSourceProxy(DataSource dataSource) {
        return new ClosableLazyConnectionDataSourceProxy(dataSource);
    }
    
    protected abstract DataSource dataSource();
    
    protected abstract PlatformTransactionManager transactionManager(DataSource dataSource) throws Exception;
    
    protected abstract SqlSessionFactory sqlSessionFactory(DataSource dataSource,
                                                           MyBatisLogInterceptor interceptor) throws Exception;
    
    /**
     * 스프링은 트랜잭션 시작시 컨넥션의 실제 사용여부와 무관하게 커넥션을 확보한다.
     * 이로인해 트랜잭션 시작 후 커넥션과 무관한 다른 작업으로 많은 시간이 지체되면
     * 그 시간 동안 해당 트랜잭션의 커넥션은 사용불가 상태가 되어,
     * 데이터소스에 커넥션 풀이 부족해지는 사태를 유발할 수도 있다.
     * 혹은 혹은 캐시를 사용하여 커넥션이 전혀 필요없는 상태에서는 아예 커넥션을 맺지 않고 지나갈 수도 있다.
     * LazyConnectionDataSourceProxy를 사용하면 트랜잭션이 시작 되더라도 실제로 커넥션이 필요한 경우에만
     * 데이터소스에서 커넥션을 반환한다.
     */
    public static class ClosableLazyConnectionDataSourceProxy extends LazyConnectionDataSourceProxy implements AutoCloseable {
        public ClosableLazyConnectionDataSourceProxy(DataSource dataSource) {
            super(dataSource);
        }
        
        @Override
        public void close() throws Exception {
            final DataSource ds = getTargetDataSource();
            if ( ds instanceof AutoCloseable ) {
                ((AutoCloseable) ds).close();
            }
        }
    }
}
