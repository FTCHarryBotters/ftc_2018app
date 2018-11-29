package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;

import java.util.List;

import static java.lang.Math.abs;

@Autonomous(name = "AutonomousDepot1", group = "Sample")
public class AutonomousDepot1 extends LinearOpMode{

    private YellowVision yellowVision = new YellowVision();
    int i = 0;
    int j = 0;
    public static double power = .3;
    boolean isGold = false;

    //declare
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchLeftM;
    private DcMotor latchRightM;

    private Servo samplingS;
    private Servo latchLeftS;
    private Servo latchRightS;
    private Servo markerS;

    Thread  delatchServoThread;

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

        delatchServoThread = new DelatchServoThread();

        latchLeftS  = hardwareMap.servo.get("latchLeftS");
        latchRightS = hardwareMap.servo.get("latchRightS");
        samplingS   = hardwareMap.servo.get("samplingS");
        markerS     = hardwareMap.servo.get("markerS");

        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        yellowVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        yellowVision.setShowCountours(true);
        yellowVision.enable();


        waitForStart();
        //what runs

            deLatchRobot();
            Thread.sleep(2000);

            driveBackwardE(0.1, 50);
            moveRightE(0.3, 200);
            driveForwardE(0.4, 350);
            spinRightE(0.4, 900);
            driveBackwardE(0.4, 150);
            moveLeftE(0.3, 100);
            Thread.sleep(250);

            SamplingSection();

            spinRightE(0.4, 1320);
            moveRightE(0.2, 400);
            driveBackwardE(0.4, 1600);
            moveLeftE(0.4, 200);
            spinRightE(0.4, 450);
            driveBackwardE(0.4, 450);

            markerS.setPosition(0.75);
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

        delatchServoThread.start();
        latchArm(1, 200);

        telemetry.addData("Position2", latchLeftM.getCurrentPosition());
        telemetry.update();

        telemetry.addData("Position3", latchLeftM.getCurrentPosition());
        telemetry.update();

        latchArmE(-0.5, 1500);
        latchLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        latchRightM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addData("Position4", latchLeftM.getCurrentPosition());
        telemetry.update();
    }
    private class DelatchServoThread  extends Thread
    {
        public DelatchServoThread (){

        }
        @Override
        public void run()
        {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {e.printStackTrace();}

            latchLeftS.setPosition(1);
            latchRightS.setPosition(0);
            this.interrupt();
        }
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
            isGold = true;
            samplingS.setPosition(.65);
            driveForwardE(power, 100);
            Thread.sleep(100);
            driveBackwardE(power, 500);
            samplingS.setPosition(0);
            driveBackwardE(power, ticks-500);
        }else{i = 0;}
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
                driveForwardE(power, 650);
                detectingAndSamplingGold(2000);

                if (!isGold)
                {
                    samplingS.setPosition(0);
                    driveBackwardE(power, 1300);
                    detectingAndSamplingGold(800);

                    if (!isGold)
                    {
                        driveForwardE(power, 650);
                    }
                }
            }
        }
    }
}
