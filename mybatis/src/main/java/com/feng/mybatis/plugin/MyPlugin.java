package com.feng.mybatis.plugin;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author fengsy
 * @date 7/26/21
 * @Description
 */
// @Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class,
// ResultHandler.class}),
// @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
// @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
@Intercepts({
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MyPlugin implements Interceptor {
    private Properties properties;
    /*
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        long startTime = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler)target;
        try {
            return invocation.proceed();
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            System.out.printf("执行 SQL：[ %s ] 执行耗时[ %d ms] \n", sql, costTime);
        }
    }*/

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement statement = (MappedStatement)invocation.getArgs()[0];
        long startTime = System.currentTimeMillis();

        try {
            return invocation.proceed();
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            Object param = invocation.getArgs()[1];
            BoundSql boundSql = statement.getBoundSql(param);
            String sql = boundSql.getSql();
            System.out.printf("执行方法：%s  SQL：[ %s ] 参数：[%s]执行耗时[ %d ms] \n", statement.getId(), sql, param, costTime);
        }

    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
        System.out.println("插件配置的信息：" + properties);
    }
}
