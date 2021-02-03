package com.fsy.javasrc.basic.string;

import java.util.Date;

/**
 * @author fengsy
 * @date 1/30/21
 * @Description
 */
 class ImmutableClass {
    private Date d;
    public ImmutableClass(Date d){
        this.d = d;
    }
    public void printState(){
        System.out.println(d);
    }


}
public class TestImmutable{

}