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
}
