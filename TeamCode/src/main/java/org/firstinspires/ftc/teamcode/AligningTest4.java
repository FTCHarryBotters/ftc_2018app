package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.opencv.core.MatOfPoint;

import java.util.List;

@Autonomous(name = "AligningTest", group = "Sample")
@Disabled
public class AligningTest4 extends LinearOpMode {

    //used for vision program and sampling
    private YellowVision yellowVision = new YellowVision();
    private int contourNumber = 0;
    private int samplingHowMany = 0;
    private boolean isGold = false;

    //declare
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchM;
    private DcMotor upDownM;
    private DcMotor inOutLeftM;
    private DcMotor inOutRghtM;

    //declare swervos
    private Servo collectorS;
    private Servo collectorUpDownLeftS;
    private Servo collectorUpDownRghtS;
    private Servo mineralDropperS;
    private Servo phoneS;
    private Servo markerS;

    //declare sensor
    private DistanceSensor distanceFLS;
    private DistanceSensor distanceBLS;
    private DistanceSensor distanceFS;
    private DistanceSensor distanceBS;
    @Override
    public void runOpMode() throws InterruptedException {

        //configure motors
        driveFLM   = hardwareMap.dcMotor.get("driveFLM");
        driveFRM   = hardwareMap.dcMotor.get("driveFRM");
        driveBLM   = hardwareMap.dcMotor.get("driveBLM");
        driveBRM   = hardwareMap.dcMotor.get("driveBRM");
        latchM     = hardwareMap.dcMotor.get("latchM");
        upDownM    = hardwareMap.dcMotor.get("upDownM");
        inOutLeftM = hardwareMap.dcMotor.get("inOutLeftM");
        inOutRghtM = hardwareMap.dcMotor.get("inOutRightM");

        //set motors's direction
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        latchM.setDirection(DcMotor.Direction.FORWARD);
        upDownM.setDirection(DcMotor.Direction.FORWARD);
        inOutLeftM.setDirection(DcMotor.Direction.REVERSE);
        inOutRghtM.setDirection(DcMotor.Direction.REVERSE);

        //configure sensors
        collectorS           = hardwareMap.servo.get("collectorS");
        collectorUpDownLeftS = hardwareMap.servo.get("collectorUpDownLeftS");
        collectorUpDownRghtS = hardwareMap.servo.get("collectorUpDownRightS");
        mineralDropperS      = hardwareMap.servo.get("mineralDropperS");
        markerS              = hardwareMap.servo.get("markerS");
        phoneS               = hardwareMap.servo.get("phoneS");

        //configure sensor
        distanceFLS = hardwareMap.get(DistanceSensor.class, "distanceFLS");
        distanceBLS = hardwareMap.get(DistanceSensor.class, "distanceBLS");
        distanceFS = hardwareMap.get(DistanceSensor.class, "distanceFS");
        distanceBS = hardwareMap.get(DistanceSensor.class, "distanceBS");

        //set some motors to use encoders
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //set up vision code
        yellowVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        yellowVision.setShowCountours(true);
        yellowVision.enable();

        waitForStart();

        double DistFL=distanceFLS.getDistance(DistanceUnit.CM);//saves distance measurement
        double DistBL=distanceBLS.getDistance(DistanceUnit.CM);//saves distance measurement
        while (DistFL>24 || DistBL>24) {                       //checks if we've reached dist from wall
            DistFL = distanceFLS.getDistance(DistanceUnit.CM); //re-saves distance measurement
            DistBL = distanceBLS.getDistance(DistanceUnit.CM); //re-saves distance measurement
            telemetry.addData("FLS", DistFL);
            telemetry.addData("BLS", DistBL);
            telemetry.update();
            if (DistFL>24) {                                   //checks if we've reached distance
                driveFLM.setPower(-0.2);                       //moves motors
                driveFRM.setPower(+0.2);                       //moves motors
            } else {
                driveFLM.setPower(0);
                driveFRM.setPower(0);
            }
            if (DistBL > 24) {                                   //checks if we've reached distance
                driveBLM.setPower(+0.2);                       //moves motors
                driveBRM.setPower(-0.2);                       //moves motors
            } else {
                driveBLM.setPower(0);
                driveBRM.setPower(0);
            }
        }

        yellowVision.disable();//stops vision program

    }
    public void delatch() {
        latchM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);      //resets encoder values to zero
        latchM.setTargetPosition(26300);                             //set how many ticks are needed to move
        latchM.setMode(DcMotor.RunMode.RUN_TO_POSITION);             //lets it move
        latchM.setPower(1);                                          //makes it move
        while (latchM.isBusy()) {}                                   //wait until it is done moving
        latchM.setPower(0);                                          //stop moving
        latchM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//stop moving part 2 robot boogaloo
        latchM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);           //goes back to normal mode
    }
    public void EnderCVContoursTest() {
        List<MatOfPoint>contours=yellowVision.getContours();     //gets the contours from seeing program
        contourNumber=contours.size();                           //saves contour number
        telemetry.addData("contourNumber", contourNumber);//tells us the contour number
        telemetry.update();
    }
    public void goldMover(int distance) throws InterruptedException {
        Thread.sleep(100);                 //idk
        EnderCVContoursTest();                  //saves number of contours
        if (contourNumber != 0) {               //checks if it is gold
            isGold=true;                        //saves info that we found gold
            phoneS.setPosition(0.2);            //moves phone down push mineral
            Thread.sleep(100);             //waits for phone to move
            moveLeftE(0.4, 500);     //moves mineral
            moveLeftE(0.4, 500);     //moves mineral
            phoneS.setPosition(0.90);           //pull phone back to vertikal
            moveRghtE(0.7, 350);     //moves back to previous position
            driveBackwardE(0.6, distance);//drives back to set position for rest of program
        }
    }
    public void sampling() throws InterruptedException {   //moves to each mineral, checks it, moves it if needed
        while (!isGold && samplingHowMany < 2) {           //checks if we've found gold and how many times we've tried
            samplingHowMany++;                             //increases how many times we've tried
            goldMover(1400);                       //check mineral and may move it
            if (!isGold) {                                 //checks if we've seen gold
                driveForwardE(0.7, 600);        //goes to next mineral
                goldMover(2000);                    //check mineral and may move it
                if (!isGold) {                             //checks if we've seen gold
                    driveBackwardE(0.7, 1250);  //drives to next mineral
                    goldMover(800);                //check mineral and may move it
                    if (!isGold) {                         //checks if we've found gold
                        driveForwardE(0.7, 650);//drives back to center mineral
                    }
                }
            }
        }
        phoneS.setPosition(0.9);                           //move phone back up
    }
    public void driveForwardE(double power, int ticks) throws InterruptedException {
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFLM.setTargetPosition(ticks);driveFRM.setTargetPosition(ticks);driveBLM.setTargetPosition(ticks);driveBRM.setTargetPosition(ticks);
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFLM.setPower(power);driveFRM.setPower(power);driveBLM.setPower(power);driveBRM.setPower(power);
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy()) {}
        driveFLM.setPower(0);driveFRM.setPower(0);driveBLM.setPower(0);driveBRM.setPower(0);
        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void driveBackwardE(double power, int ticks) throws InterruptedException {
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFLM.setTargetPosition(-ticks);driveFRM.setTargetPosition(-ticks);driveBLM.setTargetPosition(-ticks);driveBRM.setTargetPosition(-ticks);
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFLM.setPower(power);driveFRM.setPower(power);driveBLM.setPower(power);driveBRM.setPower(power);
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy()) {}
        driveFLM.setPower(0);driveFRM.setPower(0);driveBLM.setPower(0);driveBRM.setPower(0);
        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void spinLeftE(double power, int ticks) {
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFLM.setTargetPosition(-ticks);driveFRM.setTargetPosition(ticks);driveBLM.setTargetPosition(-ticks);driveBRM.setTargetPosition(ticks);
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFLM.setPower(power);driveFRM.setPower(power);driveBLM.setPower(power);driveBRM.setPower(power);
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy()) {}
        driveFLM.setPower(0);driveFRM.setPower(0);driveBLM.setPower(0);driveBRM.setPower(0);
        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void spinRghtE(double power, int ticks) throws InterruptedException {
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFLM.setTargetPosition(ticks);driveFRM.setTargetPosition(-ticks);driveBLM.setTargetPosition(ticks);driveBRM.setTargetPosition(-ticks);
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFLM.setPower(power);driveFRM.setPower(power);driveBLM.setPower(power);driveBRM.setPower(power);
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy()) {}
        driveFLM.setPower(0);driveFRM.setPower(0);driveBLM.setPower(0);driveBRM.setPower(0);
        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void moveLeftE(double power, int ticks) throws InterruptedException {
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFLM.setTargetPosition(-ticks);driveFRM.setTargetPosition(ticks);driveBLM.setTargetPosition(ticks);driveBRM.setTargetPosition(-ticks);
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFLM.setPower(power);driveFRM.setPower(power);driveBLM.setPower(power);driveBRM.setPower(power);
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy()) {}
        driveFLM.setPower(0);driveFRM.setPower(0);driveBLM.setPower(0);driveBRM.setPower(0);
        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void moveRghtE(double power, int ticks) throws InterruptedException {
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFLM.setTargetPosition(ticks);driveFRM.setTargetPosition(-ticks);driveBLM.setTargetPosition(-ticks);driveBRM.setTargetPosition(ticks);
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFLM.setPower(power);driveFRM.setPower(power);driveBLM.setPower(power);driveBRM.setPower(power);
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy()) {}
        driveFLM.setPower(0);driveFRM.setPower(0);driveBLM.setPower(0);driveBRM.setPower(0);
        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
