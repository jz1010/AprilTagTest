package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
//hi
@TeleOp
public class AprilTagTestNew_Jerry extends LinearOpMode
{
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    //April Tag ID of interest
    int BLUE_LEFT=1;
    int BLUE_MIDDLE=2;
    int BLUE_RIGHT=3;

    int BLUE_SMALL = 9;
    int BLUE_LARGE = 10;

    int RED_LEFT=4;
    int RED_MIDDLE=5;
    int RED_RIGHT=6;
    int RED_SMALL = 8;
    int RED_LARGE = 7;

    AprilTagDetection tagOfInterest = null;

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addData("Detected tag ID", detection.id);
        telemetry.addData("Translation X (feet)", detection.pose.x * FEET_PER_METER);
        telemetry.addData("Translation Y (feet)", detection.pose.y * FEET_PER_METER);
        telemetry.addData("Translation Z (feet)", detection.pose.z * FEET_PER_METER);

        Orientation rot = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);

        telemetry.addData("Rotation Yaw (degrees)", rot.firstAngle);
        telemetry.addData("Rotation Pitch (degrees)", rot.secondAngle);
        telemetry.addData("Rotation Roll (degrees)", rot.thirdAngle);
        //if else for which tag id its looking at --> add
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested()) {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0) {
                boolean tagFound = false;

                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == BLUE_LEFT || tag.id == BLUE_MIDDLE || tag.id == BLUE_RIGHT || tag.id == BLUE_SMALL || tag.id == BLUE_LARGE || tag.id == RED_LEFT || tag.id == RED_MIDDLE || tag.id == RED_RIGHT || tag.id == RED_SMALL || tag.id == RED_LARGE ) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if (tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        /* Actually do something useful */
        if (tagOfInterest.id == BLUE_LEFT) {
            telemetry.addLine("Blue Alliance Left");
            telemetry.update();
        } else if (tagOfInterest.id == BLUE_MIDDLE) {
            telemetry.addLine("Blue Alliance Middle");
            telemetry.update();
        } else if (tagOfInterest.id == BLUE_RIGHT) {
            telemetry.addLine("Blue Alliance Right");
            telemetry.update();
        } else if (tagOfInterest.id == BLUE_SMALL) {
            telemetry.addLine("Blue Alliance Small");
            telemetry.update();
        } else if (tagOfInterest.id == BLUE_LARGE) {
            telemetry.addLine("Blue Alliance Large");
            telemetry.update();
        } else if (tagOfInterest.id == RED_LEFT) {
            telemetry.addLine("Red Alliance Left");
            telemetry.update();
        } else if (tagOfInterest.id == RED_MIDDLE) {
            telemetry.addLine("Red Alliance Middle");
            telemetry.update();
        } else if (tagOfInterest.id == RED_RIGHT) {
            telemetry.addLine("Red Alliance Right");
        } else if (tagOfInterest.id == RED_SMALL) {
            telemetry.addLine("Red Alliance Small");
            telemetry.update();
        } else if (tagOfInterest.id == RED_LARGE) {
            telemetry.addLine("Red Alliance Right");
            telemetry.update();
        }



        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive()) {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0) {
                boolean tagFound = false;

                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == BLUE_LEFT || tag.id == BLUE_MIDDLE || tag.id == BLUE_RIGHT || tag.id == BLUE_SMALL || tag.id == BLUE_LARGE || tag.id == RED_LEFT || tag.id == RED_MIDDLE || tag.id == RED_RIGHT || tag.id == RED_SMALL || tag.id == RED_LARGE ) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }
            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }
            }

            telemetry.update(); // Update telemetry in each iteration of the loop
            sleep(20);
        }


    }
}
