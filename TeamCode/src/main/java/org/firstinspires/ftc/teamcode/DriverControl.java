package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Rio
 */

public class DriverControl extends OpMode {

    private Hardware hardware;

    private boolean[] debounce = new boolean[4]; // a, b, x, y

    @Override
    public void init() {
        hardware = new Hardware(hardwareMap); // Call the constructor here

        debounce['a'] = false;
        debounce['b'] = false;
        debounce['x'] = false;
        debounce['y'] = false;
    }

    @Override
    public void loop() {

        // Wheels
        hardware.leftWheel.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
        hardware.rightWheel.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);

        // Sweepers
        if(gamepad1.left_bumper)
            hardware.leftSweeper.setPosition(hardware.leftSweeper.getPosition() + .1);
        else if(gamepad1.left_trigger > .8)
            hardware.leftSweeper.setPosition(hardware.leftSweeper.getPosition() - .1);
        if(gamepad1.right_bumper)
            hardware.rightSweeper.setPosition(hardware.rightSweeper.getPosition() + .1);
        else if(gamepad1.right_trigger > .8)
            hardware.rightSweeper.setPosition(hardware.rightSweeper.getPosition() - .1);

        // Claw
        if(gamepad1.a) {
            if(debounce['a']) {
                if(hardware.claw.getPosition() != Hardware.CLAW_CLOSE) {
                    hardware.claw.setPosition(Hardware.CLAW_CLOSE);
                } else {
                    hardware.claw.setPosition(Hardware.CLAW_OPEN);
                }
            }
            debounce['a'] = true;
        } else {
            debounce['a'] = false;
        }
    }
}
