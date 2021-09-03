package com.feng.mybatis.sqlsession;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

/**
 * @author fengsy
 * @date 7/25/21
 * @Description
 */
public class MyDataSourceFactory extends UnpooledDataSourceFactory {
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
        PooledDataSource ds = new PooledDataSource((String)properties.get("driver"), (String)properties.get("url"),
            (String)properties.get("username"), (String)properties.get("password"));
        ds.setDefaultTransactionIsolationLevel(TransactionIsolationLevel.REPEATABLE_READ.getLevel());
        return ds;
    }
}
