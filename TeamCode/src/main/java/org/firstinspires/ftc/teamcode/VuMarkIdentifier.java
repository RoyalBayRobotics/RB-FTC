/*
 * VumarkIdentifirt.java
 * Date: 2018/01/25
 * Copied and edited from org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuMarkIdentification
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class VuMarkIdentifier {

    private VuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private LinearOpMode op;

    public VuMarkIdentifier(HardwareMap hardwareMap, LinearOpMode op) {
        this.op = op;

        // Camera view
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Init and setup vuforia
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AaJVKDn/////AAAAGSKxCj95A0UsgAnYhm9D9o5zvmyYZxOMti9OsRwgWqmVXi26bQx0vbBgPLtAEMOmnYaEhh/nb7zOkvWnjLR5Bge9I564ljAEZYiQn3y9d4+oGckDw7vSE+vc+h+ktBJCzyRzU3J9k/aoaeImXxeU1k8Akd3FtGOOncjgjOEqvYNcrTtujwCTd18KzSc6/oyQGaHFGbD0SVPHj3+IN+LsZo7O0FnuIqXLK/H2zl7KIYshpklAm3+IAbsVT4xqYCRG0Y/mszh97TDlLxLvVUEWYRlxmfpLz1NxJjjYjK0IzB0YvrFEVBQ+s3u1/75a/ATFH4UdA8LKFe9K4Qs9IffsO12vgrCSo9jkgnizJZytHxwy";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);


        // Load data
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
    }

    // Try to find vu mark in camera view
    // returns CENTER if not found after 20 seconds
    public RelicRecoveryVuMark findVuMark() {

        relicTrackables.activate();
        while (op.getRuntime() < 10) {

            // Find a instance
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                // Found one
                return vuMark;
            }
        }
        return RelicRecoveryVuMark.CENTER;
    }
}
