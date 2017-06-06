package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Rio
 */

@TeleOp(name = "Vic Motor Test")
public class VicMotor extends OpMode {

    // Use servo pwm to control them
    private Servo lMotor;
    private Servo rMotor;

    @Override
    public void init() {
        lMotor = hardwareMap.servo.get("left");
        rMotor = hardwareMap.servo.get("right");
    }

    @Override
    public void loop() {
        lMotor.setPosition((gamepad1.left_stick_y + gamepad1.left_stick_x) * .5 + .5);
        rMotor.setPosition((gamepad1.left_stick_y - gamepad1.left_stick_x) * .5 + .5);
    }
}
