package com.feng.mybatis.sqlsession;

import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.feng.application.domain.OrderItem;
import com.feng.application.mapper.CustomerMapper;
import com.feng.application.mapper.OrderItemMapper;

/**
 * @author fengsy
 * @date 7/25/21
 * @Description
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        DataSource dataSource = new MyDataSourceFactory().getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(CustomerMapper.class);
        InputStream inputStream = Resources.getResourceAsStream("mapper/OrderItemMapper.xml");
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration,
            "mapper/OrderItemMapper.xml", configuration.getSqlFragments());
        xmlMapperBuilder.parse();

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            // Order order2 = session.selectOne("com.feng.mybatis.sqlsession.ProductMapper.find", 1L);
            // System.out.println(order2);
            OrderItemMapper mapper = session.getMapper(OrderItemMapper.class);
            OrderItem orderItem = mapper.find(1);
            System.out.println(orderItem);
        }
    }
}
