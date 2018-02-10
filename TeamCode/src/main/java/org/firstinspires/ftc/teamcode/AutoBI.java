/*
 * AutoBI.java
 * Author: Rio
 * Date: 2018/02/10
 *
 * Autonomous mode on blue team, inside
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="Auto Blue Inside", group="Blue")
public class AutoBI extends AutoOpMode {

    @Override
    public void runOpMode() {
        startRot = -Math.PI;
        side = "blue";

        super.runOpMode();
    }
}
