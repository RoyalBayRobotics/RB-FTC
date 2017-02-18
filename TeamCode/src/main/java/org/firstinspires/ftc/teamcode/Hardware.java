package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*; // We have DcMotor and Servo

import java.util.*; // List and ArrayList

/**
 * Created by Rio
 */

public class Hardware {

    public static final double CLAW_OPEN = .6;
    public static final double CLAW_CLOSE = .2;

    public DcMotor leftWheel;
    public DcMotor rightWheel;

    public List<Servo> sweepers = new ArrayList<>(); // Doing the same things to them

    public Servo claw;

    public List<DcMotor> baseArm = new ArrayList<>(); // Same as above
    public List<Servo> extArm = new ArrayList<>();

    public Hardware(HardwareMap hwMap) {

        leftWheel  = hwMap.dcMotor.get("left_wheel");
        rightWheel = hwMap.dcMotor.get("right_wheel");
        leftWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        rightWheel.setDirection(DcMotorSimple.Direction.REVERSE);

        sweepers.add(0, hwMap.servo.get("left_sweeper"));
        sweepers.add(1, hwMap.servo.get("right_sweeper"));
        sweepers.get(0).setDirection(Servo.Direction.FORWARD);
        sweepers.get(1).setDirection(Servo.Direction.REVERSE);
        for(Servo sweeper : sweepers) {
            sweeper.setPosition(Servo.MIN_POSITION);
        }

        claw = hwMap.servo.get("claw");
        claw.setDirection(Servo.Direction.REVERSE);
        claw.setPosition(Hardware.CLAW_OPEN);

        baseArm.add(0, hwMap.dcMotor.get("base_arm_left"));
        baseArm.add(1, hwMap.dcMotor.get("base_arm_right"));
        baseArm.get(0).setDirection(DcMotorSimple.Direction.REVERSE);
        baseArm.get(1).setDirection(DcMotorSimple.Direction.FORWARD);
        
        extArm.add(0, hwMap.servo.get("ext_arm_left"));
        extArm.add(1, hwMap.servo.get("ext_arm_right"));
        extArm.get(0).setDirection(Servo.Direction.FORWARD);
        extArm.get(1).setDirection(Servo.Direction.REVERSE);
        for(Servo armServo : extArm) {
            armServo.setPosition(Servo.MIN_POSITION);
        }
    }

}
