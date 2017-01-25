package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

/**
 * Created by Rio
 */

@TeleOp(name = "Driver Control")
public class DriverControl extends OpMode {

    private Hardware hardware;

    private HashMap<Character, Boolean> debounce = new HashMap<>();

    @Override
    public void init() {
        hardware = new Hardware(hardwareMap); // Call the constructor here

        debounce.put('a', false);
        debounce.put('b', false);
        debounce.put('x', false);
        debounce.put('y', false);

        telemetry.addData("Status", "Initalized");
    }

    @Override
    public void loop() {

        // Wheels
        hardware.leftWheel.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
        hardware.rightWheel.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);

        // Sweepers
        if(gamepad1.left_bumper)
            hardware.leftSweeper.setPosition(Servo.MAX_POSITION);
        else if(gamepad1.left_trigger > .8)
            hardware.leftSweeper.setPosition(Servo.MIN_POSITION);
        if(gamepad1.right_bumper)
            hardware.rightSweeper.setPosition(Servo.MAX_POSITION);
        else if(gamepad1.right_trigger > .8)
            hardware.rightSweeper.setPosition(Servo.MIN_POSITION);
        
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
        

        // Base arm
        for(DcMotor armMotor : hardware.baseArm) {
            armMotor.setPower(gamepad1.right_stick_y);
        }
    }
}
