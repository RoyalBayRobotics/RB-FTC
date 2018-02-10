/*
 * Hardware.java
 * Author: Rio
 * Date: 2018/01/22
 */

package org.firstinspires.ftc.teamcode;

import java.util.HashMap;
import java.util.Map;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;

class Hardware {

    public static final float CLAW_OPEN = 1;
    public static final float CLAW_CLOSE = 0;
    public static final float CLAW_RELEASE = .4f;

    private static final double WHEEL_DISTANCE = 36.5;
    private static final double WHEEL_SIZE = 10.2 * Math.PI;
    private static final int MOTOR_COUNTS = 1440;
    private static final int COUNT_TO_TOP = MOTOR_COUNTS * 5;
    private LinearOpMode op;

    ModernRoboticsI2cRangeSensor rangeSensor;
    DcMotor riser;
    Servo[] claws = new Servo[2];
    Map<String, DcMotor> wheels = new HashMap<>();

    Hardware(HardwareMap hardwares, LinearOpMode op) {

        this.op = op;

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

        claws[0].setDirection(Servo.Direction.FORWARD);
        claws[1].setDirection(Servo.Direction.REVERSE);

        for(DcMotor motor : wheels.values()) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        wheels.get("fl").setDirection(DcMotorSimple.Direction.REVERSE);
        wheels.get("fr").setDirection(DcMotorSimple.Direction.FORWARD);
        wheels.get("bl").setDirection(DcMotorSimple.Direction.REVERSE);
        wheels.get("br").setDirection(DcMotorSimple.Direction.FORWARD);
    }

    void drive(double x, double y, double turn) {
        wheels.get("fl").setPower(-y + x + turn);
        wheels.get("fr").setPower(-y - x - turn);
        wheels.get("bl").setPower(-y - x + turn);
        wheels.get("br").setPower(-y + x - turn);
    }

    void turnAngle(double rad, float speed) {
        double perimeter = WHEEL_DISTANCE * Math.PI;

        int leftTarget = (int) (Math.PI * 2 / rad * perimeter * MOTOR_COUNTS / WHEEL_SIZE);
        int rightTarget = (int) (Math.PI * 2 / -rad * perimeter * MOTOR_COUNTS / WHEEL_SIZE);

        for(Map.Entry<String, DcMotor> e : wheels.entrySet()) {
            DcMotor motor = e.getValue();
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            switch(e.getKey()) {
                case "fl":
                case "bl":
                    motor.setTargetPosition(leftTarget);
                    break;
                case "fr":
                case "br":
                    motor.setTargetPosition(rightTarget);
                    break;
            }
        }

        runMotors(speed, wheels.values().toArray(new DcMotor[wheels.size()]));
    }

    void driveDistance(double dist, float speed) {
        for(DcMotor motor : wheels.values()) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition((int) (dist * MOTOR_COUNTS / WHEEL_SIZE));
        }

        runMotors(speed, wheels.values().toArray(new DcMotor[wheels.size()]));
    }

    void setArmPosition(float pos, float speed) {
        pos = Math.max(Math.min(pos, 1), 0);
        riser.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        riser.setTargetPosition((int) (pos * COUNT_TO_TOP));
        runMotors(speed, riser);
    }

    float getArmPosition() {
        return riser.getCurrentPosition() / (float) COUNT_TO_TOP;
    }

    void moveArm(float speed) {
        riser.setPower(speed);
    }

    void moveClaw(float pos) {
        claws[0].setPosition(pos);
        claws[1].setPosition(pos);
    }

    private void runMotors(float speed, DcMotor... motors) {
        for(DcMotor motor : motors) {
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
        } while(running && (op == null || op.opModeIsActive()));

        for(DcMotor motor : motors) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
