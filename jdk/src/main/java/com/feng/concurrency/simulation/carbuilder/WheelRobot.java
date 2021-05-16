package com.feng.concurrency.simulation.carbuilder;

import static net.mindview.util.Print.print;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class WheelRobot extends Robot {
    public WheelRobot(RobotPool pool) {
        super(pool);
    }

    protected void performService() {
        print(this + " installing Wheels");
        assembler.car().addWheels();
    }
}
