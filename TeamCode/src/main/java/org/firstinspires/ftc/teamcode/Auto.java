/*
 * Auto.java
 * Author: Rio
 * Date: 2018/01/25
 *
 * A base class for all autonomous classes
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

class Auto extends LinearOpMode {

    //protected double toBox = 60.96;
    protected double toBox = 61;
    protected double startRot = Math.PI * 2;
    protected String side = "red";

    @Override
    public void runOpMode() {
        Hardware hw = new Hardware(hardwareMap);
        VuMarkIdentifier vMarkId = new VuMarkIdentifier(hardwareMap);

        waitForStart();

        RelicRecoveryVuMark vm = vMarkId.findVuMark();
        double tgtDist = getDistanceToColumn(vm);

        hw.turnAngle(startRot, 1);

        while(true) {
            double distance = hw.rangeSensor.getDistance(DistanceUnit.CM);
            if(distance < tgtDist) {
                hw.drive(-1, 0, 0);
                telemetry.addData("Target", "Farther");
            } else if(distance > tgtDist) {
                hw.drive(1, 0, 0);
                telemetry.addData("Target", "Closer");
            } else {
                break;
            }
            telemetry.update();
        }

        telemetry.addData("Target", "Here");
        telemetry.update();

        hw.driveDistance(10, 1);
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
