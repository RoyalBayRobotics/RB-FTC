/*
 * AutoBO.java
 * Author: Rio
 * Date: 2018/02/10
 *
 * Autonomous mode on blue team, outside
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="Auto Blue Outside", group="Blue")
public class AutoBO extends AutoOpMode {

    @Override
    public void runOpMode() {
        startRot = Math.PI * 2;
        side = "blue";

        super.runOpMode();
    }
}

