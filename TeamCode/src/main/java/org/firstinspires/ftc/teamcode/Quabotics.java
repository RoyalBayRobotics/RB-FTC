package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Quabotics")
public class Quabotics extends OpMode {

    private Quasimodo quasimodo;

    @Override
    public void init() {

        quasimodo = new Quasimodo(hardwareMap);

        telemetry.addData("Report", "Quasimodo is prepared");
    }

    @Override
    public void loop() {

        // Running by left joystick
        quasimodo.run(gamepad1.left_stick_y + gamepad1.left_stick_x,
               gamepad1.left_stick_y - gamepad1.left_stick_x);

        // Control the claw
        if(gamepad1.right_bumper) {
            quasimodo.openClaw();
        } else if(gamepad1.right_trigger > .8) {
            quasimodo.pushClaw();
        } else {
            quasimodo.closeClaw();
        }

        // The height of the tube
        if(gamepad1.dpad_up) {
            quasimodo.raiseTube();
        } else if(gamepad1.dpad_down) {
            quasimodo.dropTube();
        }
    }
}
