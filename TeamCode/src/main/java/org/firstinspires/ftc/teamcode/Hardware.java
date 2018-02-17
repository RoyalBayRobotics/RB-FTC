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
    public static final float CLAW_CLOSE = .1f;
    public static final float CLAW_RELEASE = .3f;

    private static final double WHEEL_DISTANCE = 36.5;
    private static final double WHEEL_SIZE = 10.2 * Math.PI;
    private static final int WHEEL_MOTOR_COUNTS = 1120; // NeveRest motor
    private static final int RISER_MOTOR_COUNTS = 1440; // TETRIX motor
    private static final int COUNTS_TO_TOP = Math.round(RISER_MOTOR_COUNTS * 14.5f);
    private LinearOpMode op;

    ModernRoboticsI2cRangeSensor rangeSensor;
    DcMotor lift;
    Servo[] claws = new Servo[2];
    Map<String, DcMotor> wheels = new HashMap<>();
    DigitalChannel button;

    Hardware(HardwareMap hardwares, LinearOpMode op) {

        this.op = op;

        // Get all hardwares
        rangeSensor = hardwares.get(ModernRoboticsI2cRangeSensor.class, "range_sensor");

        lift = hardwares.get(DcMotor.class, "lift");

        claws[0] = hardwares.get(Servo.class, "claw_l");
        claws[1] = hardwares.get(Servo.class, "claw_r");

        // front left, front right, back left, back right
        wheels.put("fl", hardwares.get(DcMotor.class, "wheel_fl"));
        wheels.put("fr", hardwares.get(DcMotor.class, "wheel_fr"));
        wheels.put("bl", hardwares.get(DcMotor.class, "wheel_bl"));
        wheels.put("br", hardwares.get(DcMotor.class, "wheel_br"));

        button = hardwares.get(DigitalChannel.class, "button");

        // Init them
        lift.setDirection(DcMotorSimple.Direction.FORWARD);
        claws[0].setDirection(Servo.Direction.FORWARD);
        claws[1].setDirection(Servo.Direction.REVERSE);

        for(DcMotor motor : wheels.values()) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        wheels.get("fl").setDirection(DcMotorSimple.Direction.REVERSE);
        wheels.get("fr").setDirection(DcMotorSimple.Direction.FORWARD);
        wheels.get("bl").setDirection(DcMotorSimple.Direction.REVERSE);
        wheels.get("br").setDirection(DcMotorSimple.Direction.FORWARD);

        button.setMode(DigitalChannel.Mode.INPUT);

        // Move the arm to the bottom
        while(!button.getState()) { // Button is not pressed
            lift.setPower(-.5f);
        }

        lift.setPower(0);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void drive(double x, double y, double turn) {
        wheels.get("fl").setPower(-y + x + turn);
        wheels.get("fr").setPower(-y - x - turn);
        wheels.get("bl").setPower(-y - x + turn);
        wheels.get("br").setPower(-y + x - turn);
    }

    void turnAngle(double deg, float speed) {
        double perimeter = WHEEL_DISTANCE * Math.PI;

        int leftTarget = (int) Math.round(deg / 360 * perimeter * WHEEL_MOTOR_COUNTS / WHEEL_SIZE);
        int rightTarget = (int) Math.round(-deg / 360 * perimeter * WHEEL_MOTOR_COUNTS / WHEEL_SIZE);

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
            motor.setTargetPosition((int) Math.round(dist * WHEEL_MOTOR_COUNTS / WHEEL_SIZE));
        }

        runMotors(speed, wheels.values().toArray(new DcMotor[wheels.size()]));
    }

    void setArmPosition(float pos, float speed) {
        pos = Math.max(Math.min(pos, 1), 0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setTargetPosition(Math.round(pos * COUNTS_TO_TOP));
        runMotors(speed, lift);
    }

    float getArmPosition() {
        return lift.getCurrentPosition() / (float) COUNTS_TO_TOP;
    }

    void moveArm(float speed) {
        if(button.getState()) { // Button is pressed
            if(lift.getPower() < 0)
                lift.setPower(0);
            if(speed < 0)
                return;
        }

        if(lift.getCurrentPosition() > COUNTS_TO_TOP) { // Arm is too high
            if(lift.getPower() > 0)
                lift.setPower(0);
            if(speed > 0)
                return;
        }

        lift.setPower(speed);
    }

    void moveClaw(float pos) {
        claws[0].setPosition(pos);
        claws[1].setPosition(pos);
    }

    private void runMotors(float speed, DcMotor... motors) {
        for(DcMotor motor : motors) {
            motor.setPower(Math.abs(speed));
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
