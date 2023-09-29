package org.firstinspires.ftc.teamcode.OpenCV;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;


public class OlehDetector extends OpenCvPipeline {
    private Mat workingMatrix = new Mat ();
    public String position;
    public OlehDetector(){
        position="Left";
    }
    @Override
    public final Mat processFrame (Mat input){
        input.copyTo(workingMatrix);

        if (workingMatrix.empty()){
            return input;
        }
        Imgproc.cvtColor(workingMatrix,workingMatrix, Imgproc.COLOR_RGB2YCrCb);

        Mat matLeft = workingMatrix.submat(150, 250, 30, 230 );
        Mat matCenter = workingMatrix.submat(150, 250, 250, 450 );
        Mat matRight = workingMatrix.submat(150, 250, 470, 670 );

        double leftTotal = Core.sumElems(matLeft).val[2];
        double centerTotal = Core.sumElems(matCenter).val[2];
        double rightTotal = Core.sumElems(matRight).val[2];

        if(leftTotal>centerTotal){
            if(leftTotal>rightTotal){
                position="Left";

            } else{
                position="Right";

            }
        } else{
            if (centerTotal > rightTotal){
                position="Center";

            } else {
                position="Right";

            }
        }
        return workingMatrix;

    }
}
