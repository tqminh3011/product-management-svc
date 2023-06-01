//package com.smg.backend.productmanagement.config;
//
//import com.smg.backend.productmanagement.config.props.CustomR2dbcProperties;
//import io.r2dbc.pool.PoolingConnectionFactoryProvider;
//import io.r2dbc.spi.ConnectionFactories;
//import io.r2dbc.spi.ConnectionFactory;
//import io.r2dbc.spi.ConnectionFactoryOptions;
//import io.r2dbc.spi.Option;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
//import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
//import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
//import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
//import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
//import org.springframework.data.r2dbc.dialect.MySqlDialect;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
//import org.springframework.r2dbc.connection.R2dbcTransactionManager;
//import org.springframework.r2dbc.core.DatabaseClient;
//import org.springframework.transaction.ReactiveTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.reactive.TransactionalOperator;
//
//import java.time.Duration;
//
//@Configuration
//@EnableR2dbcAuditing
//@PropertySource({"classpath:application-db.yml"})
//@EnableR2dbcRepositories(entityOperationsRef = "entityTemplate",
//        basePackages = "com.smg.backend.productmanagement.repository")
//@EnableTransactionManagement
//public class DBConfig extends AbstractR2dbcConfiguration {
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.r2dbc.product-management-db")
//    public CustomR2dbcProperties tradingR2dbcProperties() {
//        return new CustomR2dbcProperties();
//    }
//
//    @NotNull
//    @Bean("connectionFactory")
//    @Primary
//    public ConnectionFactory connectionFactory() {
//
//        CustomR2dbcProperties properties = tradingR2dbcProperties();
//
//        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
//                .option(ConnectionFactoryOptions.DRIVER, "pool")
//                .option(ConnectionFactoryOptions.PROTOCOL, "mysql")
//                .option(ConnectionFactoryOptions.HOST, properties.getHost())
//                .option(ConnectionFactoryOptions.PORT, 3306)
//                .option(ConnectionFactoryOptions.USER, properties.getUsername())
//                .option(ConnectionFactoryOptions.PASSWORD, properties.getPassword())
//                .option(ConnectionFactoryOptions.DATABASE, properties.getDatabase())
//                .option(ConnectionFactoryOptions.SSL, true)
////                .option(Option.valueOf("tlsVersion"), "TLSv1.2")
//                .option(PoolingConnectionFactoryProvider.INITIAL_SIZE, properties.getPool().getInitialSize())
//                .option(PoolingConnectionFactoryProvider.MAX_SIZE, properties.getPool().getMaxSize())
//                .option(PoolingConnectionFactoryProvider.MIN_IDLE, properties.getPool().getInitialSize())
//                .option(PoolingConnectionFactoryProvider.MAX_IDLE_TIME, properties.getPool().getMaxIdleTime())
//                .option(Option.valueOf("queryTimeout"), Duration.ofMillis(properties.getQueryTimeoutInMs()))
//                .option(PoolingConnectionFactoryProvider.VALIDATION_QUERY, properties.getPool().getValidationQuery())
//                .option(PoolingConnectionFactoryProvider.POOL_NAME, "r2dbc-pool")
//                .build());
//    }
//
//    @Bean
//    @Primary
//    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
//        return new R2dbcTransactionManager(connectionFactory);
//    }
//
//    @Bean
//    @Primary
//    TransactionalOperator transactionOperations(ReactiveTransactionManager reactiveTransactionManager) {
//        return TransactionalOperator.create(reactiveTransactionManager);
//    }
//
//    @Bean
//    public R2dbcEntityOperations entityTemplate(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
//        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(MySqlDialect.INSTANCE);
//        DatabaseClient databaseClient = DatabaseClient.builder()
//                .connectionFactory(connectionFactory)
//                .bindMarkers(MySqlDialect.INSTANCE.getBindMarkersFactory())
//                .build();
//
//        return new R2dbcEntityTemplate(databaseClient, strategy);
//    }
//}
