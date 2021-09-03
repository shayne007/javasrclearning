package com.feng.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author fengsy
 * @date 7/24/21
 * @Description
 */
public class DaoUtils {
    private static SqlSessionFactory factory;
    static {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            System.err.println("read mybatis-config.xml fail");
            e.printStackTrace();
            System.exit(1);
        }
        factory = new SqlSessionFactoryBuilder().build(inputStream, "development");
    }

    public static <R> R execute(Function<SqlSession, R> function) {
        // 创建SqlSession
        SqlSession session = factory.openSession();
        try {
            R apply = function.apply(session);
            // 提交事务
            session.commit();
            return apply;
        } catch (Throwable t) {
            // 出现异常的时候，回滚事务
            session.rollback();
            System.out.println("execute error");
            throw t;
        } finally {
            // 关闭SqlSession
            session.close();
        }
    }
}
