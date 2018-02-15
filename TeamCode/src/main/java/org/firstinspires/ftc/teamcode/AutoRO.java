/*
 * AutoRO.java
 * Author: Rio
 * Date: 2018/01/25
 *
 * Autonomous mode on red team, outside
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="Auto Red Outside", group="Red")
public class AutoRO extends AutoOpMode {

    @Override
    public void runOpMode() {
        startRot = 180;
        side = "red";

        super.runOpMode();
    }
}
