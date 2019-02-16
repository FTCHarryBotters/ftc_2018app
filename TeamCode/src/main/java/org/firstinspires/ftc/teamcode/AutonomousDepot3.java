package org.firstinspires.ftc.teamcode;

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

import java.util.List;

@Autonomous(name = "AutonomousDepot3", group = "Sample")
public class AutonomousDepot3 extends LinearOpMode {

    //used for vision system and sampling
    private YellowVision yellowVision = new YellowVision();
    private boolean isGold = false;
    private int contourNumber = 0;
    //contourNumber(i) checks if there is a gold, because a contour means gold
    private int samplingHowMany = 0;
    //(j) how many times we've done the run though of sampling
    //because we want to do it twice but not keep going if it never senses
    private int robotWhere = 1;
    //robotwhere(k) tracks where the robot is during sampling
    //and thus where the gold was because we do different things with each position
    //1 is center, 2 is right, and 0 is left

    //declare motors
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

        //configure seervos
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

        //sets to using encoder
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //set up the vision code
        yellowVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        yellowVision.setShowCountours(true);
        yellowVision.enable();

        waitForStart();
        if (true!=false&&false!=true&&true&&!false&&!!!!!!!false) {

            //moving into position for the sampling to start
            //i would very much like it if the sections here and in AutoCrater3 were the same
            //delatch();                         //drop down from latch
            moveLeftE(0.9, 100);    //out of latch
            driveForwardE(0.9, 600);//away from lander
            phoneS.setPosition(0.4);           //moves phone to see
            spinRghtE(0.9, 900);    //spin so phone toward minerals
            driveBackwardE(0.9, 75);//lines up with center mineral

            //goes to each of the minerals and checks which one is gold
            //and saves it so the robot can move it in different ways
            sampling();

            //the following three if statements check where the robot is
            //and does different things for each position
            //we found this to be the best thing to do
            //1=center, 2=right, 0=left
            if (robotWhere==1) {

                //moves mineral and gets in position for marker dropping
                phoneS.setPosition(0.9);        //bring phone up
                moveLeftE(0.9, 2000);//move mineral into depot
                moveRghtE(0.9, 500); //move away from depot
                spinRghtE(0.9, 1600);//spin so marker faces depot

                //drops marker
                markerS.setPosition(0.75);//drops marker
                Thread.sleep(1000);  //waits for marker to slide
                markerS.setPosition(0);   //bring it back up

                //moves away from depot and to crater
                moveRghtE(0.9, 100);     //move toward depot so we can go to crater w/o hitting left mineral
                driveForwardE(0.9, 500); //move forward towards crater (still diagonal)
                spinLeftE(0.9, 325);     //spin to align with wall, facing crater
                moveRghtE(0.9, 250);     //move toward wall
                driveForwardE(1.0, 2800);//drive full speed towards crater

                //move collector into crater
                collectorUpDownLeftS.setPosition(0);//moves down left side
                collectorUpDownRghtS.setPosition(1);//moves down right side
                Thread.sleep(800);             //waits for them to move before program stops
            }
            if (robotWhere==2) {

                //moves mineral
                phoneS.setPosition(0.2);       //move phone down
                Thread.sleep(100);        //wait for phone to move
                moveLeftE(0.4, 700);//move mineral
                phoneS.setPosition(0.9);       //move phone back up
                moveRghtE(0.4, 550);//move back to previous position

                //moves to the crater to get ready for marker dropping
                driveBackwardE(0.6, 2100);                //drives to wall. around mineral
                spinLeftE(0.9, 450);                      //align with wall, facing towards depot
                double Dist=distanceFLS.getDistance(DistanceUnit.CM);//saves distance measurement
                while (Dist>24) {                                    //checks if we've reached dist from wall
                    Dist = distanceFLS.getDistance(DistanceUnit.CM);   //re-saves distance measurement
                    if (Dist>24) {                                   //checks if we've reached distance
                        driveFLM.setPower(-0.5);                     //moves motors
                        driveFRM.setPower(+0.5);                     //moves motors
                        driveBLM.setPower(+0.5);                     //moves motors
                        driveBRM.setPower(-0.5);                     //moves motors
                    }
                }
                driveForwardE(0.9, 1500);                 //move to depot
                spinLeftE(0.9, 900);                      //face marker to depot

                //drops marker
                markerS.setPosition(0.75);//drops marker
                Thread.sleep(1000);  //waits for marker to slide
                markerS.setPosition(0);   //bring it back up

                //moves away from depot and to crater
                spinLeftE(0.9, 850);   //face crater
                driveForwardE(1, 2222);//go to crater

                //move collector into crater
                collectorUpDownLeftS.setPosition(0);//moves down left side
                collectorUpDownRghtS.setPosition(1);//moves down right side
                Thread.sleep(800);             //waits for them to move before program stops
            }
            if (robotWhere==0) {

                phoneS.setPosition(0.9);
                spinRghtE(0.9, 900);      //moves the robot so its back faces the mineral
                driveBackwardE(0.9, 1000);//uses the back of robot to move mineral
                spinRghtE(0.9, 420);      //spin so robot is in line with wall
                driveBackwardE(0.9, 500); //drive backward towards depot
                spinRghtE(0.9, 900);      //spin so marker is toward depot

                //drops marker
                markerS.setPosition(0.75);//drops marker
                Thread.sleep(1000);  //waits for marker to slide
                markerS.setPosition(0);   //bring it back up

                spinLeftE(0.9, 800);    //face crater
                moveRghtE(0.9, 200);    //moves toward wall so we don't hit enemy silver
                driveForwardE(01, 2222);//drives to crater

                //move collector into crater
                collectorUpDownLeftS.setPosition(0);//moves down left side
                collectorUpDownRghtS.setPosition(1);//moves down right side
                Thread.sleep(800);             //waits for them to move before program stops
            }
        yellowVision.disable();
        }
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
    public void goldMover() throws InterruptedException {
        phoneS.setPosition(0.4);//moves phone to checking position
        Thread.sleep(100); //wait for phone to move
        EnderCVContoursTest();  //saves contour number
        if (contourNumber!=0) { //checks if it is gold
            isGold=true;        //saves that we found gold
        }
    }
    public void sampling() throws InterruptedException {
        while (!isGold&&samplingHowMany<2) {               //checks if we've found gold and how many times we've tried
            samplingHowMany++;                             //increases how many times we've tried
            robotWhere=1;                                  //saves where the robot is
            goldMover();                                   //check mineral and may move it
            if (!isGold) {                                 //checks if we've seen gold
                driveForwardE(0.6, 600);        //goes to next mineral
                robotWhere=2;                              //saves where the robot is
                goldMover();                               //check mineral and may move it
                if (!isGold) {                             //checks if we've seen gold
                    driveBackwardE(0.6, 1250);  //goes to next mineral
                    robotWhere=0;                          //saves where the robot is
                    goldMover();                           //check mineral and may move it
                    if (!isGold) {                         //checks if we've seen gold
                        driveForwardE(0.6, 650);//goes to next mineral
                    }
                }
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
    //i apologize for the curse code above
    //it looks bad but we don't ever need to change so it's fine
    //the old code is in Auton2, but it is just a cosmetic change, tbh
}
