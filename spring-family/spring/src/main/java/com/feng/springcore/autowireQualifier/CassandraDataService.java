package com.feng.springcore.autowireQualifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */
@Repository
@Slf4j
public class CassandraDataService implements DataService {

    @Override
    public void deleteStudent(int id) {
        log.info("delete student info maintained by cassandra");
    }
}
