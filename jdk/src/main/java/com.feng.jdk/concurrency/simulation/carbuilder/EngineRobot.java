package com.feng.jdk.concurrency.simulation.carbuilder;

import static net.mindview.util.Print.print;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class EngineRobot extends Robot {
    public EngineRobot(RobotPool pool) {
        super(pool);
    }

    protected void performService() {
        print(this + " installing engine");
        assembler.car().addEngine();
    }
}
