package com.feng.jdk.concurrency.simulation.carbuilder;

import static net.mindview.util.Print.print;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class DriveTrainRobot extends Robot {
    public DriveTrainRobot(RobotPool pool) {
        super(pool);
    }

    protected void performService() {
        print(this + " installing DriveTrain");
        assembler.car().addDriveTrain();
    }
}
