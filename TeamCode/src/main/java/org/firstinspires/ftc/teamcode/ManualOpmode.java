/*
 * ManualOpmode.java
 * Author: Rio
 * Date: 2018/02/04
 *
 * Control the robot manually
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@TeleOp(name="Manual")
public class ManualOpmode extends OpMode {

    private Hardware hw;
    private boolean bumperPushed = false;
    private boolean isClawOpen = true;

    public void init() {
        hw = new Hardware(hardwareMap, null);
    }

    public void loop() {
        hw.drive(gamepad1.right_stick_x, gamepad1.left_stick_y, gamepad1.left_stick_x);
        hw.moveArm((gamepad1.right_bumper ? 1f : 0f) - gamepad1.right_trigger);

        if(gamepad1.left_bumper) {
            if(!bumperPushed) {
                isClawOpen = !isClawOpen;
            }
            bumperPushed = true;
        } else {
            bumperPushed = false;
        }

        hw.moveClaw(isClawOpen ?
                Hardware.CLAW_OPEN - gamepad1.left_trigger :
                Hardware.CLAW_CLOSE + gamepad1.left_trigger
                );
    }
}
