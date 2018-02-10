/*
 * ManualOpmode.jaba
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

    public void init() {
        hw = new Hardware(hardwareMap, null);
    }

    public void loop() {
        hw.drive(gamepad1.right_stick_x, gamepad1.left_stick_y, gamepad1.left_stick_x);
        hw.moveArmWith((gamepad1.right_bumper ? 1f : 0f) - gamepad1.right_trigger);
        hw.moveClaw(gamepad1.left_trigger);
    }
}
