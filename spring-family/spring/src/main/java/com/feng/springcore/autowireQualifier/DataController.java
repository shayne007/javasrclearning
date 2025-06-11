package com.feng.springcore.autowireQualifier;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * component bean name match rules:
 * <p>
 * 1.优先找@Primary注解的；<br>
 * 2.匹配变量名称：精确匹配component声明的名称，未声明时模糊匹配类名； <br>
 * 3.Qualifier注解匹配:精确匹配component声明的名称，未声明时首字母小写匹配或多个大写字母匹配；<br>
 * 内部类匹配：dataController.InnerClassDataService
 *
 * @author fengsy
 * @date 7/8/21
 * @Description
 */
@RestController
@Slf4j
@Validated
public class DataController {
    //     @Qualifier("SQLiteDataService")
    // @Qualifier("OracleDataService")
    @Autowired
    // @Qualifier("dataController.InnerClassDataService")
            // DataService oracleDataService;
            DataService innerClassDataService;

    @RequestMapping(path = "data/{id}", method = RequestMethod.DELETE)
    public void deleteStudent(@PathVariable("id") @Range(min = 1, max = 100) int id) {
        innerClassDataService.deleteStudent(id);
    }

    @Repository("innerClassDataService")
    public static class InnerClassDataService implements DataService {

        @Override
        public void deleteStudent(int id) {
            log.info("inner class data service delete...");
        }
    }
}
