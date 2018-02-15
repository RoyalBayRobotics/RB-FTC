/*
 * AutoRI.java
 * Author: Rio
 * Date: 2018/02/10
 *
 * Autonomous mode on red team, inside
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="Auto Red Inside", group="Red")
public class AutoRI extends AutoOpMode {

    @Override
    public void runOpMode() {
        startRot = 90;
        side = "red";

        super.runOpMode();
    }
}
