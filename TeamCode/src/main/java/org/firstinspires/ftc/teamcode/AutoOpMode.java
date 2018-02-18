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

    protected double toBox = 152 - 18; // distance - 1/2 robot width
    protected double startRot = 180;
    protected String side = "red";

    private Hardware hw;

    @Override
    public void runOpMode() {
        hw = new Hardware(hardwareMap, this);
        /*
        VuMarkIdentifier vMarkId = new VuMarkIdentifier(hardwareMap, this);
*/
        hw.moveClaw(Hardware.CLAW_OPEN);

        telemetry.addData("Status", "Inited");
        telemetry.update();

        waitForStart();

/*
        hw.moveClaw(Hardware.CLAW_CLOSE);

        sleep(500);

        hw.setArmPosition(.1f, 1);

        RelicRecoveryVuMark vm = vMarkId.findVuMark();

        hw.turnAngle(startRot, .5f);

        sleep(1000);

        double tgtDist = getDistanceToColumn(vm);
        toDistance(tgtDist);

        sleep(1000);

        hw.driveDistance(10, 1);

        sleep(500);

        hw.moveClaw(Hardware.CLAW_RELEASE);

        sleep(500);

        hw.driveDistance(-10, 1);
        hw.setArmPosition(0, 1);
        hw.moveClaw(Hardware.CLAW_OPEN);

        sleep(500);

        tgtDist = getDistanceToColumn(RelicRecoveryVuMark.CENTER);
        toDistance(tgtDist);

        sleep(1000);

        hw.driveDistance(10, 1);
        */
        hw.driveDistance(-100, 1);
    }

    private void toDistance(double tgtDist) {
        int wallSide = side == "blue" ? 1 : -1;

        while(opModeIsActive()) {
            double distance = hw.rangeSensor.getDistance(DistanceUnit.CM);
            telemetry.addData("Target", tgtDist);
            if(distance < tgtDist) {
                hw.drive(Math.copySign(.3, wallSide), 0, 0);
                telemetry.addData("Target", "Farther: " + distance);
            } else if(distance > tgtDist) {
                hw.drive(Math.copySign(.3, -wallSide), 0, 0);
                telemetry.addData("Target", "Closer: " + distance);
            } else {
                telemetry.addData("Target", "Here: " + distance);
                hw.drive(0, 0, 0);
                break;
            }
            telemetry.update();
        }
        telemetry.update();
    }

    private double getDistanceToColumn(RelicRecoveryVuMark vm) {
        if(side == "blue") {
            switch(vm) {
                case RIGHT:
                    return toBox + 30;//.48;
                case CENTER:
                    return toBox;
                case LEFT:
                    return toBox - 30;//.48
            }
        } else {
            switch(vm) {
                case RIGHT:
                    return toBox - 30;//.48;
                case CENTER:
                    return toBox;
                case LEFT:
                    return toBox + 30;//.48
            }
        }
        return toBox + 19;//.3802;
    }
}
