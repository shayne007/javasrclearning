package com.feng.mybatis.sqlsession;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;

/**
 * @author fengsy
 * @date 7/25/21
 * @Description
 */
public class OrderDataSourceFactory implements DataSourceFactory {
    private Properties properties;

    @Override
    public void setProperties(Properties props) {
        properties = props;
    }

    @Override
    public DataSource getDataSource() {
        properties = new Properties();
        properties.put("driver", "com.mysql.cj.jdbc.Driver");
        properties.put("url", "jdbc:mysql://localhost:3306/test");
        properties.put("username", "root");
        properties.put("password", "123456");
        DataSource ds = new PooledDataSource((String)properties.get("driver"), (String)properties.get("url"),
            (String)properties.get("username"), (String)properties.get("password"));
        return ds;
    }
}
