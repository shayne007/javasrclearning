package com.feng.transaction;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */
@Configuration
public class JdbcConfig {

	@Value("${jdbc.driver}")
	private String driver;

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "dataSource")
	public DataSource createDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager createTransactionManager(DataSource dataSource) {
		DataSourceTransactionManager manager = new DataSourceTransactionManager();
		manager.setDataSource(dataSource);

		// Configure transaction manager properties
		manager.setDefaultTimeout(30); // 30 seconds default timeout
		manager.setGlobalRollbackOnParticipationFailure(false);
		return manager;
	}

	@Bean
	public TransactionTemplate transactionTemplate(
			PlatformTransactionManager transactionManager) {
		TransactionTemplate template = new TransactionTemplate(transactionManager);

		// Set default transaction attributes
		template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		template.setTimeout(60);
		template.setReadOnly(false);

		return template;
	}

	@Bean
	public TransactionInterceptor transactionInterceptor(
			PlatformTransactionManager transactionManager) {

		Properties attributes = new Properties();
		attributes.setProperty("find*", "PROPAGATION_SUPPORTS,readOnly");
		attributes.setProperty("get*", "PROPAGATION_SUPPORTS,readOnly");
		attributes.setProperty("create*", "PROPAGATION_REQUIRED");
		attributes.setProperty("update*", "PROPAGATION_REQUIRED");
		attributes.setProperty("delete*", "PROPAGATION_REQUIRED");
		attributes.setProperty("*", "PROPAGATION_REQUIRED");

		TransactionInterceptor interceptor = new TransactionInterceptor();
		interceptor.setTransactionManager(transactionManager);
		interceptor.setTransactionAttributes(attributes);

		return interceptor;
	}
}
