package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Quasimodo {

    protected final float CLAW_OPEN = 1f;
    protected final float CLAW_CLOSE = .5f;
    protected final float CLAW_PUSH = 0f;

    private Servo wheels[] = new Servo[2]; // Using pwm to control
    private Servo claw;
    private Servo tube;
    private DcMotor[] FlyWheels = new DcMotor[2];

    public Quasimodo(HardwareMap hwMap) {

        wheels[0] = hwMap.servo.get("left_wheel");
        wheels[0].setPosition(.5);

        wheels[1] = hwMap.servo.get("right_wheel");
        wheels[1].setPosition(.5);

        claw = hwMap.servo.get("claw");
        claw.setPosition(CLAW_CLOSE);

        tube = hwMap.servo.get("tube");
        tube.setPosition(Servo.MIN_POSITION);

        FlyWheels[0] = hwMap.dcMotor.get("left_fly");
        FlyWheels[1] = hwMap.dcMotor.get("right_fly");
    }

    public void run(float left, float right) {
        wheels[0].setPosition(left * .5 + .5);
        wheels[1].setPosition(right * .5 + .5);
    }

    public void openClaw() {
        claw.setPosition(CLAW_OPEN);
    }

    public void closeClaw() {
        claw.setPosition(CLAW_CLOSE);
    }

    public void pushClaw() {
        claw.setPosition(CLAW_PUSH);
    }

    public void raiseTube() {
        tube.setPosition(tube.getPosition() + .05);
    }

    public void dropTube() {
        tube.setPosition(tube.getPosition() - .05);
    }
}
