/*
 * AutoOpMode.java
 * Author: Rio
 * Date: 2018/01/25
 *
 * A base class for all autonomous classes
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

class AutoOpMode extends LinearOpMode {

    //protected double toBox = 60.96;
    protected double toBox = 61;
    protected double startRot = 180;
    protected String side = "red";

    @Override
    public void runOpMode() {
        Hardware hw = new Hardware(hardwareMap, this);
        VuMarkIdentifier vMarkId = new VuMarkIdentifier(hardwareMap, this);

        hw.moveClaw(Hardware.CLAW_OPEN);

        waitForStart();

        hw.moveClaw(Hardware.CLAW_CLOSE);
        sleep(1000);
        hw.setArmPosition(.3f, 1);

        RelicRecoveryVuMark vm = vMarkId.findVuMark();
        double tgtDist = getDistanceToColumn(vm);
        int wallSide = side == "blue" ? 1 : -1;

        hw.turnAngle(startRot, .5f);

        while(opModeIsActive()) {
            double distance = hw.rangeSensor.getDistance(DistanceUnit.CM);
            telemetry.addData("Target", tgtDist + ": " + distance + " a: " + opModeIsActive());
            if(distance < tgtDist) {
                hw.drive(Math.copySign(.3, wallSide), 0, 0);
                telemetry.addData("Target", "Farther: " + distance);
            } else if(distance > tgtDist) {
                hw.drive(.3 * Math.copySign(.3, -wallSide), 0, 0);
                telemetry.addData("Target", "Closer: " + distance);
            } else {
                telemetry.addData("Target", "Here: " + distance);
                break;
            }
            telemetry.update();
        }

        telemetry.update();

        hw.driveDistance(10, 1);

        hw.moveClaw(Hardware.CLAW_RELEASE);
    }

    private double getDistanceToColumn(RelicRecoveryVuMark vm) {
        if(side == "blue") {
            switch(vm) {
                case LEFT:
                    return toBox;
                case CENTER:
                    return toBox + 19;//.3802;
                case RIGHT:
                    //return toBox + 38.7604;
                    return toBox + 39;
            }
        } else {
            switch(vm) {
                case RIGHT:
                    return toBox;
                case CENTER:
                    return toBox + 19;//.3802;
                case LEFT:
                    //return toBox + 38.7604;
                    return toBox + 39;
            }
        }
        return toBox + 19;//.3802;
    }
}
