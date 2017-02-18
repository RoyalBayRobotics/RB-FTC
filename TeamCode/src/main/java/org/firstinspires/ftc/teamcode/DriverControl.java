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
    private HashMap<String, Boolean> debounce = new HashMap<>(); // Might not need this

    private double clawPos;
    private int tick;

    @Override
    public void init() {
        hardware = new Hardware(hardwareMap); // Call the constructor here

        // Store debounce informations
        debounce.put("claw", false);

        clawPos = Hardware.CLAW_OPEN;

        telemetry.addData("Status", "Initalized");
    }

    @Override
    public void loop() {

        // Wheels
        hardware.leftWheel.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
        hardware.rightWheel.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);

        // Sweepers
        for(Servo sweeper : hardware.sweepers) {
            if(gamepad1.right_bumper) {
                sweeper.setPosition(Servo.MIN_POSITION);
            }
        }

        // Claw
        if(gamepad1.left_bumper) {
            if(!debounce.get("claw")) {
                if(clawPos != Hardware.CLAW_OPEN) {
                    clawPos = Hardware.CLAW_OPEN;
                    for(Servo sweeper : hardware.sweepers) {
                        sweeper.setPosition(Servo.MIN_POSITION);
                    }
                } else {
                    hardware.claw.setPosition(Hardware.CLAW_CLOSE);
                    for(Servo sweeper : hardware.sweepers) {
                        sweeper.setPosition(Servo.MAX_POSITION);
                    }
                    clawPos = Hardware.CLAW_CLOSE;
                }
            }
            debounce.put("claw", true);
        } else {
            debounce.put("claw", false);
        }

        hardware.claw.setPosition(clawPos + gamepad1.left_trigger / 4);

        // Base arm
        for(DcMotor armMotor : hardware.baseArm) {
            if(tick % 5 == 0) {
                if(gamepad1.right_stick_y > .5)
                    //armMotor.setPower(.1 + gamepad1.left_trigger / 2);
                    armMotor.setPower(1);
                else if(gamepad1.right_stick_y < -.5)
                    //armMotor.setPower(-.1 - gamepad1.left_trigger / 2);
                    armMotor.setPower(-1);
                else
                    armMotor.setPower(0);
            } else {
                armMotor.setPower(0);
            }
        }

        // Extended arm
        for(Servo extMotor : hardware.extArm) {
            double go = 0;
            if(gamepad1.right_stick_x > .5)
                go = .01;
            else if(gamepad1.right_stick_x < -.5)
                go = -.01;

            extMotor.setPosition(extMotor.getPosition() + go);
        }
        tick++;
    }
}
