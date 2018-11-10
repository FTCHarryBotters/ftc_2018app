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

@Autonomous(name = "Autonomous1", group = "Sample")
public class Autonomous1 extends LinearOpMode {
    private YellowVision yellowVision;
    //private TeleOp2 teleOp2;
    int i = 0;
    int j = 0;

    boolean isGold = false;

    //declare motors

    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    //private DcMotor latchLeftM;
    //private DcMotor latchRightM;
    /*private DcMotor armUpDownM;
    private DcMotor collectorM;
    */

    //declare servos
    //private Servo latchLeftS;
    //private Servo latchRightS;
    private Servo samplingS;
    /*private Servo collectorUpDOwnS;
    private Servo armExtenderS;
    */

    @Override
    public void runOpMode() throws InterruptedException
    {

        //declare motors
        driveFLM = hardwareMap.dcMotor.get("driveFLM");
        driveFRM = hardwareMap.dcMotor.get("driveFRM");
        driveBLM = hardwareMap.dcMotor.get("driveBLM");
        driveBRM = hardwareMap.dcMotor.get("driveBRM");
        //latchLeftM = hardwareMap.dcMotor.get("latchLeftM");
        //latchRightM = hardwareMap.dcMotor.get("latchRightM");
        //armUpDownM = hardwareMap.dcMotor.get("armUpDownM");
        //collectorM = hardwareMap.dcMotor.get("collectorM");

        //teleOp2 = new TeleOp2();
        //teleOp2.initialize();
        yellowVision = new YellowVision();
        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        yellowVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        yellowVision.setShowCountours(true);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //reversing necessary motors
        //latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);

        //declare servos
        //latchLeftS = hardwareMap.servo.get("latchLeftS");
        //latchRightS = hardwareMap.servo.get("latchRightS");
        samplingS = hardwareMap.servo.get("samplingS");
        //collectorUpDOwnS = hardwareMap.servo.get("collectorUpDOwnS");
        //armExtenderS = hardwareMap.servo.get("armExtenderS");

        // start the vision system
        yellowVision.enable();

        waitForStart();

        //works great!
        //Delatch();



            /*moveRight(0.50, 560);
            driveForward(0.50, 1120);
            spinRight(0.50, 2240);
            moveLeft(0.50, 224);*/
        SamplingSection();


        yellowVision.disable();

    }

    //the following methods deal with moving the robot around
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

        //teleOp2.DriveForward(power);
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //teleOp2.DriveForward(0);
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
        driveForward(-power, ticks);
    }

    public void spinLeft(double power, int ticks)
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

        //teleOp2.SpinLeft(power);
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //teleOp2.SpinLeft(0);
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
        spinLeft(-power, ticks);
    }

    public void moveLeft(double power, int ticks) throws InterruptedException
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

        //teleOp2.MoveLeft(power);
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(-power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //teleOp2.MoveLeft(0);
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
        moveLeft(-power, ticks);
    }



    /*the following methods deal with the latch arm
    public void latchMotor(double power, long time) throws InterruptedException
    {
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        Thread.sleep(time);
    }
    public void Delatch() throws InterruptedException
    {
        latchLeftM.setPower(-.75);
        latchRightM.setPower(-.75);
        latchLeftS.setPosition(0);
        latchRightS.setPosition(1);
        Thread.sleep(500);
        latchMotor(.25, 100);
    }*/



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
        samplingS.setPosition(1);
        EnderCVContoursTest();
        telemetry.addData("isGold1", isGold);
        if (i != 0)
        {
            isGold = true;
            telemetry.addData("isGold2", isGold);
            driveBackward(0.025, 2000);
            samplingS.setPosition(0);
            driveForward(0.025, 2000);
            driveForward(-0.025, ticks);
        }
    }

    public void SamplingSection() throws InterruptedException
    {
        while (!isGold && j < 2)
        {
            j++;
            detectingAndSamplingGold(6000);

            if (!isGold)
            {
                samplingS.setPosition(0);
                driveForward(0.025, 3000);
                detectingAndSamplingGold(9000);

                if (!isGold)
                {
                    samplingS.setPosition(0);
                    driveForward(-0.025, 6000);
                    detectingAndSamplingGold(3000);

                    if (!isGold)
                    {
                        driveForward(-0.025, 3000);
                    }
                }
            }
        }
    }
}
