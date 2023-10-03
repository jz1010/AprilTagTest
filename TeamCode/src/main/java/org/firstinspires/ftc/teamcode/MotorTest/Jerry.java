package org.firstinspires.ftc.teamcode.MotorTest;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.OpenCV.OpenCVTestOleh;

@TeleOp
public class Jerry extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {


        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");


        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // Remember, Y stick value is reversed



            double frontLeftPower = y;

            frontLeftMotor.setPower(frontLeftPower);

        }


    }
}




