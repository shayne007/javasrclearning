package com.feng.application.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.joda.money.Money;

/**
 * @author fengsy
 * @date 7/26/21
 * @Description
 */
@MappedTypes(Money.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class GenericTypeHandler<E extends Money> extends BaseTypeHandler<E> {

    private Class<E> type;

    public GenericTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.getAmountMinorLong());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return null;
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }

}
