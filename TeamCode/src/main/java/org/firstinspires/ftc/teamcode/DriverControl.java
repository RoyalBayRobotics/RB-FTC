package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

/**
 * Created by Rio
 */

@TeleOp(name = "Driver Control")
public class DriverControl extends OpMode {

    private Hardware hardware;
    private HashMap<Character, Boolean> debounce = new HashMap<>();

    private boolean clawClosed;

    @Override
    public void init() {
        hardware = new Hardware(hardwareMap); // Call the constructor here

        debounce.put('a', false);
        debounce.put('b', false);
        debounce.put('x', false);
        debounce.put('y', false);

        clawClosed = false;

        telemetry.addData("Status", "Initalized");
    }

    @Override
    public void loop() {

        // Wheels
        hardware.leftWheel.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
        hardware.rightWheel.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);

        // Sweepers
        if(gamepad1.right_bumper) {
            for(Servo sweeper : hardware.sweepers) {
                sweeper.setPosition(Servo.MAX_POSITION);
            }
        } else {
            for(Servo sweeper : hardware.sweepers) {
                sweeper.setPosition(Servo.MIN_POSITION);
            }
        }

        // Claw
        if(gamepad1.a) {
            if(!debounce.get('a')) {
                if(!clawClosed) {
                    hardware.claw.setPosition(Hardware.CLAW_CLOSE);
                    clawClosed = true;
                } else {
                    hardware.claw.setPosition(Hardware.CLAW_OPEN);
                    clawClosed = false;
                }
            }
            debounce.put('a', true);
        } else {
            debounce.put('a', false);
        }


        // Base arm
        for(DcMotor armMotor : hardware.baseArm) {
            armMotor.setPower(gamepad1.right_stick_y / 10);
        }

        // Extended arm
        for(Servo extMotor : hardware.extArm) {
            extMotor.setPosition(extMotor.getPosition() +
                    gamepad1.right_stick_x / 10);
        }
    }
}
