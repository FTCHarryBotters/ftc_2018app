package org.firstinspires.ftc.teamcode;

import android.graphics.Paint;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.opencv.core.MatOfPoint;

import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

@Autonomous(name = "AutonomousCrater3", group = "Sample")
public class AutonomousCrater3 extends LinearOpMode {

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

    private long startingMilliseconds=0l;

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
        if (!!!!!!!!!!!!!!!!true) {

            startingMilliseconds = new Date().getTime();

            //move from latch to center mineral
            delatch();                         //drop from latch
            collectorUpDownLeftS.setPosition(0.5);
            collectorUpDownRghtS.setPosition(0.5);
            moveLeftE(0.9, 100);    //out of latch
            driveForwardE(0.9, 650);//away from lander
            phoneS.setPosition(0.4);           //moves phone to see
            spinRghtE(0.9, 900);    //spin so phone toward minerals
            driveBackwardE(0.9, 75);//lines up with center mineral

            //goes to each mineral and turn, checks if it's gold, and moves it if it is
            sampling();

            //moves robot to depot, lines up marker to be dropped
            spinLeftE(0.9, 450);      //lines up with wall
            alignLeft(0.3, 17);  //aligns with wall using dist sensor
            Thread.sleep(50);               //waits for bot to stop moving
            driveBackwardE(0.8, 1600);//drives to depot
            moveRghtE(0.9, 200);      //move away from wall
            spinRghtE(0.9, 900);      //spins so marker is towards depot

            //drops marker
            markerS.setPosition(0.75);//drops marker
            Thread.sleep(1000);  //waits for marker to slide
            markerS.setPosition(0);   //bring it back up

            //go to crater
            spinLeftE(0.9, 900);   //spin to face crater
            alignLeft(0.3, 17);
            collectorUpDownLeftS.setPosition(1);//moves down left side
            collectorUpDownRghtS.setPosition(0);//moves down right side
            driveForwardE(1, 2100);//go to crater

        yellowVision.disable();//stops vision program
        }
    }
    public void delatch() {
        latchM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);      //resets encoder values to zero
        latchM.setTargetPosition(26300);                             //set how many ticks are needed to move
        latchM.setMode(DcMotor.RunMode.RUN_TO_POSITION);             //lets it move
        latchM.setPower(1);
        long diff =0l;
        while (latchM.isBusy() &&  diff <12000) {                    //wait until it is done moving
            diff = new Date().getTime()-startingMilliseconds;
            telemetry.addData("time", diff);
            telemetry.update();
        }
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
            moveLeftE(0.8, 500);     //moves mineral
            phoneS.setPosition(0.90);           //pull phone back to vertikal
            moveRghtE(0.8, 400);     //moves back to previous position
            driveBackwardE(0.9, distance);//drives back to set position for rest of program
        }
    }
    public void sampling() throws InterruptedException {   //moves to each mineral, checks it, moves it if needed
        while (!isGold && samplingHowMany < 2) {           //checks if we've found gold and how many times we've tried
            samplingHowMany++;                             //increases how many times we've tried
            goldMover(1400);                       //check mineral and may move it
            if (!isGold) {                                 //checks if we've seen gold
                driveForwardE(0.7, 650);        //goes to next mineral
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
    public void alignLeft(double power, int DistanceCM) {
        double DistFL=distanceFLS.getDistance(DistanceUnit.CM);//saves distance measurement
        double DistBL=distanceBLS.getDistance(DistanceUnit.CM);//saves distance measurement
        while (DistFL>DistanceCM || DistBL>DistanceCM) {           //checks if we've reached dist from wall
            DistFL = distanceFLS.getDistance(DistanceUnit.CM); //re-saves distance measurement
            DistBL = distanceBLS.getDistance(DistanceUnit.CM); //re-saves distance measurement
            telemetry.addData("FLS", DistFL);
            telemetry.addData("BLS", DistBL);
            telemetry.update();
            if (DistFL>DistanceCM) {                               //checks if we've reached distance
                driveFLM.setPower(-power);                       //moves motors
                driveFRM.setPower(+power);                       //moves motors
            } else {
                driveFLM.setPower(0);
                driveFRM.setPower(0);
            }
            if (DistBL>DistanceCM) {                               //checks if we've reached distance
                driveBLM.setPower(+power);                       //moves motors
                driveBRM.setPower(-power);                       //moves motors
            } else {
                driveBLM.setPower(0);
                driveBRM.setPower(0);
            }
        }
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
//Life Before Death
//Strength Before Weakness
//Journey Before Destination