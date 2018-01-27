/*
 * Hardware.java
 * Author: Rio
 * Date: 2018/01/22
 */

package org.firstinspires.ftc.teamcode;

import java.util.HashMap;
import java.util.Map;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;

class Hardware {

    private static final double WHEEL_DISTANCE = 15;
    private static final double WHEEL_SIZE = 8;
    private static final double MOTOR_COUNTS = 1440;

    ModernRoboticsI2cRangeSensor rangeSensor;
    DcMotor riser;
    Servo[] claws = new Servo[2];
    Map<String, DcMotor> wheels = new HashMap<>();

    Hardware(HardwareMap hardwares) {

        // Get all hardwares
        rangeSensor = hardwares.get(ModernRoboticsI2cRangeSensor.class, "range_sensor");

        riser = hardwares.get(DcMotor.class, "riser");

        claws[0] = hardwares.get(Servo.class, "claw_l");
        claws[1] = hardwares.get(Servo.class, "claw_r");

        // front left, front right, back left, back right
        wheels.put("fl", hardwares.get(DcMotor.class, "wheel_fl"));
        wheels.put("fr", hardwares.get(DcMotor.class, "wheel_fr"));
        wheels.put("bl", hardwares.get(DcMotor.class, "wheel_bl"));
        wheels.put("br", hardwares.get(DcMotor.class, "wheel_br"));

        // Init them
        riser.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        riser.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        for(DcMotor motor : wheels.values()) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    void drive(double x, double y, double turn) {
        wheels.get("fl").setPower(y - x + turn);
        wheels.get("fr").setPower(y + x - turn);
        wheels.get("bl").setPower(x + y + turn);
        wheels.get("br").setPower(x - y - turn);
    }

    void turnAngle(double rad, float speed) {
        double perimeter = WHEEL_DISTANCE * Math.PI;

        int leftTarget = (int) (Math.PI * 2 / rad * perimeter * MOTOR_COUNTS * WHEEL_SIZE);
        int rightTarget = (int) (Math.PI * 2 / -rad * perimeter * MOTOR_COUNTS * WHEEL_SIZE);

        wheels.get("fl").setTargetPosition(leftTarget);
        wheels.get("bl").setTargetPosition(leftTarget);
        wheels.get("fr").setTargetPosition(rightTarget);
        wheels.get("br").setTargetPosition(rightTarget);
        for(Map.Entry<String, DcMotor> e : wheels.entrySet()) {
            DcMotor motor = e.getValue();
            switch(e.getKey()) {
                case "fl":
                case "bl":
                    motor.setTargetPosition(motor.getCurrentPosition() + leftTarget);
                    break;
                case "fr":
                case "br":
                    motor.setTargetPosition(motor.getCurrentPosition() + rightTarget);
                    break;
            }
        }

        runMotors(speed, wheels.values().toArray(new DcMotor[wheels.size()]));
    }

    void driveDistance(double dist, float speed) {
        for(DcMotor motor : wheels.values()) {
            motor.setTargetPosition(motor.getCurrentPosition() + (int) (dist * MOTOR_COUNTS * WHEEL_SIZE));
        }

        runMotors(speed, wheels.values().toArray(new DcMotor[wheels.size()]));
    }

    private void runMotors(float speed, DcMotor... motors) {
        for(DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(speed);
        }

        boolean running;
        do {
            running = false;
            for(DcMotor motor : motors) {
                if(motor.isBusy()) {
                    running = true;
                    break;
                }
            }
        } while(running);

        for(DcMotor motor : motors) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
