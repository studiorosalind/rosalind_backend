package com.rosalind.configuration.database.pg;

import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
  basePackages = "com.rosalind.*"
)
@RequiredArgsConstructor
public class PostgresqlConfig {

  private final JpaProperties jpaProperties;

  @Bean
  public HikariConfigCustom hikariConfig() {
    return new HikariConfigCustom();
  }

  private DataSource createDataSource(String jdbcUrl) {
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(jdbcUrl);
    hikariDataSource.setDataSourceClassName(hikariConfig().getDataSourceClassName());
    hikariDataSource.setUsername(hikariConfig().getUsername());
    hikariDataSource.setPassword(hikariConfig().getPassword());
    hikariDataSource.setMaximumPoolSize(hikariConfig().getMaximumPoolSize());
    hikariDataSource.setMinimumIdle(hikariConfig().getMinimumIdle());
    return hikariDataSource;
  }

  @Bean
  public DataSource routingDataSource() {
    ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();
    DataSource master = createDataSource(hikariConfig().getJdbcUrl());

    Map<Object, Object> dataSourceMap = Maps.newHashMap();
    dataSourceMap.put("master", master);

    List<HikariConfigCustom.Slave> slaves = hikariConfig().getSlave();
    if (!CollectionUtils.isEmpty(slaves)) {
      slaves.forEach(slave -> dataSourceMap.put(slave.getName(), createDataSource(slave.getJdbcUrl())));
    }

    replicationRoutingDataSource.setTargetDataSources(dataSourceMap);
    replicationRoutingDataSource.setDefaultTargetDataSource(master);
    return replicationRoutingDataSource;
  }

  @Bean
  @DependsOn({"routingDataSource"})
  @Primary
  public DataSource dataSource() {
    return new LazyConnectionDataSourceProxy(routingDataSource());
  }

  @Bean(name = "jdbcTemplate")
  public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Primary
  @Bean(name = "entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource());
    entityManagerFactoryBean.setPackagesToScan("com.rosalind.*");
    entityManagerFactoryBean.setPersistenceUnitName("rosalind");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
    entityManagerFactoryBean.setJpaPropertyMap(getJpaProperties());
    return entityManagerFactoryBean;
  }

  @Primary
  @Bean(name = "transactionManager")
  public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  @Bean(name = "namedParameterJdbcTemplate")
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }


  private Map<String, String> getJpaProperties() {
    Map<String, String> properties = jpaProperties.getProperties();
    log.info("[ROSALIND][JPA] properties - {}", properties);
    return properties;
  }

}
