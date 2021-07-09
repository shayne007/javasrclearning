package com.feng.springcore.autowireQualifier;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */
@Repository
@Slf4j
public class SQLiteDataService implements DataService {

    @Override
    public void deleteStudent(int id) {
        log.info("delete student info maintained by SQLite");
    }
}
