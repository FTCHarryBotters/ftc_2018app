package org.firstinspires.ftc.teamcode.RRobsolete;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;

import java.util.List;

import static java.lang.Math.abs;

@Autonomous(name = "AutonomousDepot2", group = "Sample")
@Disabled
public class AutonomousDepot2 extends LinearOpMode{

    private YellowVision yellowVision = new YellowVision();
    int i = 0;
    int j = 0;
    int k = 1;
    public static double SAMPLINGPOWER = .3;
    boolean isGold = false;

    //declare
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchLeftM;
    private DcMotor latchRightM;

    private Servo latchLeftS;
    private Servo latchRightS;
    private Servo samplingS;
    private Servo markerS;
    private Servo collectorUpDownS;

    @Override
    public void runOpMode() throws InterruptedException {
        //configure
        driveFLM       = hardwareMap.dcMotor.get("driveFLM");
        driveFRM       = hardwareMap.dcMotor.get("driveFRM");
        driveBLM       = hardwareMap.dcMotor.get("driveBLM");
        driveBRM       = hardwareMap.dcMotor.get("driveBRM");
        latchLeftM     = hardwareMap.dcMotor.get("latchLeftM");
        latchRightM    = hardwareMap.dcMotor.get("latchRightM");

        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        latchRightM.setDirection(DcMotor.Direction.FORWARD);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //delatchServoThread = new DelatchServoThread();

        latchLeftS       = hardwareMap.servo.get("latchLeftS");
        latchRightS      = hardwareMap.servo.get("latchRightS");
        samplingS        = hardwareMap.servo.get("samplingS");
        markerS          = hardwareMap.servo.get("markerS");
        collectorUpDownS = hardwareMap.servo.get("collectorUpDownS");


        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        yellowVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        yellowVision.setShowCountours(true);
        yellowVision.enable();

        waitForStart();
        //what runs

            deLatchRobot();
            Thread.sleep(1500);

            //moving from hook onto the lander
            //to the phone in front of the central mineral
            driveBackwardE(0.1, 50);
            driveForwardE(0.1, 15);
            moveRightE(0.6, 300);
            driveForwardE(0.7, 350);
            moveLeftE(0.6, 50);
            spinRightE(0.4, 900);
            driveBackwardE(0.7, 250);
            moveLeftE(0.7, 150);
            Thread.sleep(250);

            SamplingSection();

            spinRightE(0.6, 1300);
            moveRightE(0.7, 450);
            driveBackwardE(0.7, 1450);
            moveLeftE(0.7, 200);
            spinRightE(0.5, 900);

            markerS.setPosition(0.6);
            Thread.sleep(1000);

            spinLeftE(0.5, 900);
            moveRightE(0.7, 200);
            driveForwardE(0.8, 2600);
            collectorUpDownS.setPosition(1);
            Thread.sleep(1000);

        yellowVision.disable();
    }
    //methods
    //methods for the latch arm
    private void latchArm(double power, long time) throws InterruptedException
    {
        latchLeftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        Thread.sleep(time);
        latchLeftM.setPower(0);
        latchRightM.setPower(0);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void latchArmE(double power, int ticks) throws InterruptedException
    {
        //reset encoders

        latchLeftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        //wait
        while (abs(latchLeftM.getCurrentPosition()) < abs(ticks) && abs(latchRightM.getCurrentPosition()) < abs(ticks))
        {
            telemetry.addData("power", latchLeftM.getPower());
            telemetry.update();
        }
        //stop motors. why o why.
        // the latchE works *fundamentally* different to the driveE.
        // doing the same thing didn't work, and now I pay the price: inconsistent code
        latchLeftM.setPower(0);
        latchRightM.setPower(0);
        latchLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latchRightM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void deLatchRobot() throws InterruptedException
    {
        latchLeftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Position1", latchLeftM.getCurrentPosition());
        telemetry.update();

        //delatchServoThread.start();
        latchArm(1, Constants.UPTIME);
        latchLeftS.setPosition(1);
        latchRightS.setPosition(0);

        telemetry.addData("Position2", latchLeftM.getCurrentPosition());
        telemetry.update();

        telemetry.addData("Position3", latchLeftM.getCurrentPosition());
        telemetry.update();
        // replace the value from constants like Constants.TICkSDOWN

        latchArmE(-0.5,  Constants.TICKSDOWN);
        latchLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        latchRightM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addData("Position4", latchLeftM.getCurrentPosition());
        telemetry.update();
    }
    public void driveForwardE(double power, int ticks) throws InterruptedException
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

        //driveForward
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void driveBackwardE(double power, int ticks) throws InterruptedException
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

        //driveBackward
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void spinLeftE(double power, int ticks)
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

        //spinLeft
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void spinRightE(double power, int ticks) throws InterruptedException
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

        //spinRight
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void moveLeftE(double power, int ticks) throws InterruptedException
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

        //moveLeft
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void moveRightE(double power, int ticks) throws InterruptedException
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

        //moveRight
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
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
            if (k != 0) {
                isGold = true;
                driveForwardE(SAMPLINGPOWER, 100);
                samplingS.setPosition(.65);
                Thread.sleep(100);
                driveBackwardE(SAMPLINGPOWER, 400);
                samplingS.setPosition(0);
                driveBackwardE(SAMPLINGPOWER, ticks-400);
            }else{
                isGold = true;
                driveBackwardE(SAMPLINGPOWER, 400);
                samplingS.setPosition(0.65);
                Thread.sleep(100);
                driveForwardE(SAMPLINGPOWER, 400);
                samplingS.setPosition(0);
                driveBackwardE(SAMPLINGPOWER, ticks);
            }
        }else{i = 0;}
    }
    public void SamplingSection() throws InterruptedException
    {
        while (!isGold && j < 2)
        {
            j++;
            k = 1;
            detectingAndSamplingGold(1400);

            if (!isGold)
            {
                samplingS.setPosition(0);
                driveForwardE(SAMPLINGPOWER, 650);
                k = 2;
                detectingAndSamplingGold(2000);

                if (!isGold)
                {
                    samplingS.setPosition(0);
                    driveBackwardE(SAMPLINGPOWER, 1300);
                    k = 0;
                    detectingAndSamplingGold(800);

                    if (!isGold)
                    {
                        driveForwardE(SAMPLINGPOWER, 650);
                    }
                }
            }
        }
    }
}
