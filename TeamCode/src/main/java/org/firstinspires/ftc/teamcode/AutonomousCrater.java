package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.List;
import java.util.Locale;

@Autonomous(name = "AutonomousCrater", group = "Sample")
public class AutonomousCrater extends LinearOpMode {

    private YellowVision yellowVision = new YellowVision();
    int i = 0;
    int j = 0;
    public static double power = .3;
    boolean isGold = false;

    //declare motors
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchLeftM;
    private DcMotor latchRightM;
    private DcMotor linearForwardM;
    private DcMotor linearUpDownM;

    //declare servos
    private Servo latchLeftS;
    private Servo latchRightS;
    private Servo samplingS;
    private Servo markerS;
    private Servo collectorS;
    private Servo collectorUpDownS;
    private Servo mineralDropperS;

    @Override
    public void runOpMode() throws InterruptedException {

        //set motors to th configuration
        driveFLM       = hardwareMap.dcMotor.get("driveFLM");
        driveFRM       = hardwareMap.dcMotor.get("driveFRM");
        driveBLM       = hardwareMap.dcMotor.get("driveBLM");
        driveBRM       = hardwareMap.dcMotor.get("driveBRM");
        latchLeftM     = hardwareMap.dcMotor.get("latchLeftM");
        latchRightM    = hardwareMap.dcMotor.get("latchRightM");
        linearForwardM = hardwareMap.dcMotor.get("linearForwardM");
        linearUpDownM  = hardwareMap.dcMotor.get("linearUpDownM");

        //reversing necessary motors
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        latchLeftM.setDirection(DcMotor.Direction.FORWARD);
        latchRightM.setDirection(DcMotor.Direction.REVERSE);
        linearForwardM.setDirection(DcMotor.Direction.FORWARD);
        linearUpDownM.setDirection(DcMotor.Direction.REVERSE);

        //set servos to th configuration
        latchLeftS       = hardwareMap.servo.get("latchLeftS");
        latchRightS      = hardwareMap.servo.get("latchRightS");
        samplingS        = hardwareMap.servo.get("samplingS");
        markerS          = hardwareMap.servo.get("markerS");
        collectorS       = hardwareMap.servo.get("collectorS");
        collectorUpDownS = hardwareMap.servo.get("collectorUpDownS");
        mineralDropperS  = hardwareMap.servo.get("mineralDropperS");

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        yellowVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        yellowVision.setShowCountours(true);

        // start the vision system
        yellowVision.enable();

        waitForStart();

        driveBackward(0.1, 50);
        moveRight(0.3, 200);
        driveForward(0.4, 400);
        spinRight(0.4, 920);
        driveBackward(0.4, 150);
        moveLeft(0.3, 150);
        Thread.sleep(250);

        SamplingSection();

        spinRight(0.4, 1300);
        moveRight(0.2, 600);

        yellowVision.disable();
    }
    //theses methods move the robots without using encoders.
    //they were made because calling methods from teleop2 did not work and I
    //could just ctrl C V the entire thing rather  than changing minor parts here and there
    //you probably shouldn't use them, they need time, so just use the ones that use encoders
    public void driveForward(double power)
    {
        //this function is to move forward.
        //all motors move forward
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);
    }
    public void driveBackward(double power)
    {
        //this function is to move backward
        //all motors move back
        driveForward(-power);
    }
    public void spinLeft(double power)
    {
        //spins the robot left
        //the left side moves backward &
        //the right side motors move forward
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
    }
    public void spinRight(double power)
    {
        //spins the robot right
        //the right side moves backward &
        //the left side motors move forward
        spinLeft(-power);
    }
    public void moveLeft(double power)
    {
        //slides the robot left
        //the front left and back right motors move backward
        //while the front right and back left motors move forward
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(-power);
    }
    public void moveRight(double power)
    {
        //slides the robot right
        //the front right and back left motors move backward
        //while the front left and back right motors move forward
        moveLeft(-power);
    }
    public void stopMoving()
    {
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);
    }

    //the following methods deal with moving the robot around with encoders.
    //these are the ones you will most likely use in your code.
    public void driveForward(double power, int ticks) throws InterruptedException
    {
        //Reset Encoders
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        driveFLM.setTargetPosition(ticks);
        driveFRM.setTargetPosition(ticks);
        driveBLM.setTargetPosition(ticks);
        driveBRM.setTargetPosition(ticks);

        //set ot RUN_TO_POSITION mode
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //driveForward(power);
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() /*&& driveBLM.isBusy() && driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void driveBackward(double power, int ticks) throws InterruptedException
    {
        //Reset Encoders
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        driveFLM.setTargetPosition(-ticks);
        driveFRM.setTargetPosition(-ticks);
        driveBLM.setTargetPosition(-ticks);
        driveBRM.setTargetPosition(-ticks);

        //set ot RUN_TO_POSITION mode
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //driveForward(power);
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() /*&& driveBLM.isBusy() && driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void spinLeft(double power, int ticks)
    {
        //Reset Encoders
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        driveFLM.setTargetPosition(-ticks);
        driveFRM.setTargetPosition(ticks);
        driveBLM.setTargetPosition(-ticks);
        driveBRM.setTargetPosition(ticks);

        //set ot RUN_TO_POSITION mode
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //spinLeft(power);
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() /*&& driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void spinRight(double power, int ticks) throws InterruptedException
    {
        //Reset Encoders
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        driveFLM.setTargetPosition(ticks);
        driveFRM.setTargetPosition(-ticks);
        driveBLM.setTargetPosition(ticks);
        driveBRM.setTargetPosition(-ticks);

        //set ot RUN_TO_POSITION mode
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //driveForward(power);
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() /*&& driveBLM.isBusy() && driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void moveLeft(double power, int ticks) throws InterruptedException
    {
        //Reset Encoders
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        driveFLM.setTargetPosition(-ticks);
        driveFRM.setTargetPosition(ticks);
        driveBLM.setTargetPosition(ticks);
        driveBRM.setTargetPosition(-ticks);

        //set ot RUN_TO_POSITION mode
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //moveLeft(power);
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() /*&& driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void moveRight(double power, int ticks) throws InterruptedException
    {
        //Reset Encoders
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        driveFLM.setTargetPosition(ticks);
        driveFRM.setTargetPosition(-ticks);
        driveBLM.setTargetPosition(-ticks);
        driveBRM.setTargetPosition(ticks);

        //set ot RUN_TO_POSITION mode
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //driveForward(power);
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() /*&& driveBLM.isBusy() && driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //the following methods deal with the sampling
    public void EnderCVContoursTest()
    {
        List<MatOfPoint> contours = yellowVision.getContours();
        i = 0;
        while (i < contours.size())
        {
            i++;
        }
        telemetry.addData("number of Countours i:", i);
        telemetry.update();
    }
    public void detectingAndSamplingGold(int ticks) throws InterruptedException
    {
        samplingS.setPosition(0);
        EnderCVContoursTest();
        if (i != 0)
        {
            isGold = true;
            samplingS.setPosition(.65);
            Thread.sleep(100);
            driveBackward(power, 400);
            samplingS.setPosition(0);
            driveBackward(power, ticks-400);
        }
    }
    public void SamplingSection() throws InterruptedException
    {
        while (!isGold && j < 2)
        {
            j++;
            detectingAndSamplingGold(1400);

            if (!isGold)
            {
                samplingS.setPosition(0);
                driveForward(power, 650);
                detectingAndSamplingGold(2000);

                if (!isGold)
                {
                    samplingS.setPosition(0);
                    driveBackward(power, 1300);
                    detectingAndSamplingGold(800);

                    if (!isGold)
                    {
                        driveForward(power, 650);
                    }
                }
            }
        }
    }
}