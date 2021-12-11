package com.feng.jdk.annotation.genextfile;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
@DBTable(name = "MEMBER")
public class Member {
    @SQLString(value = 30, constraints = @Constraints(unique = true))
    String firstName;
    @SQLString(50)
    String lastName;
    @SQLInteger(constraints = @Constraints(allowNull = false))
    Integer age;
    @SQLString(value = 30, constraints = @Constraints(primaryKey = true))
    String handle;
    static int memberCount;

    public String getHandle() {
        return handle;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return handle;
    }

    public Integer getAge() {
        return age;
    }
}
