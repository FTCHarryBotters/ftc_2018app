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

    private YellowVision yellowVision = new YellowVision();
    int i = 0;
    int j = 0;
    public static double power = .2;
    boolean isGold = false;

    //declare motors
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    //private DcMotor latchLeftM;
    //private DcMotor latchRightM;

    //declare servos
    //private Servo latchLeftS;
    //private Servo latchRightS;
    private Servo samplingS;

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

        // start the vision system
        yellowVision.enable();

        waitForStart();

            //works great!
            //Delatch();

            //this line moves right
            moveWithEncoders(0.1, 1, 1000);
            moveWithEncoders(0.1, 2, 1000);
            moveWithEncoders(0.1, 3, 1000);
            moveWithEncoders(0.1, 4, 1000);
            moveWithEncoders(0.1, 5, 1000);
            moveWithEncoders(0.1, 6, 1000);
            
            //SamplingSection();

            yellowVision.disable();
    }
    public void move(double power, int mode)
    {
        int FL = 0;
        int FR = 0;
        int BL = 0;
        int BR = 0;

        if(mode == 1){
            FL = 1; FR = 1; BL = 1; BR = 1;
        }else{
            if(mode == 2) {
                FL = -1; FR = -1; BL = -1; BR = -1;
            }else{
                if(mode == 3) {
                    FL = -1; FR = 1; BL = -1; BR = 1;
                }else{
                    if(mode == 4) {
                        FL = 1; FR = -1; BL = 1; BR = -1;
                    }else{
                        if(mode == 5) {
                            FL = -1; FR = 1; BL = 1; BR = -1;
                        }else{
                            if(mode == 6) {
                                FL = 1; FR = -1; BL = -1; BR = 1;
                            }
                        }
                    }
                }
            }
        }
        // 1, 1, 1, 1 goes forward
        //-1,-1,-1,-1 goes backward
        //-1, 1,-1, 1 spins left
        // 1,-1, 1,-1 spins right
        //-1, 1, 1,-1 moves left
        // 1,-1,-1, 1 moves right
        driveFLM.setPower(power*FL);
        driveFRM.setPower(power*FR);
        driveBLM.setPower(power*BL);
        driveBRM.setPower(power*BR);
    }
    public void moveWithEncoders(double power, int mode, int ticks)
    {
        int FLE = 0;
        int FRE = 0;
        int BLE = 0;
        int BRE = 0;

        if(mode == 1){
            FLE = 1; FRE = 1; BLE = 1; BRE = 1;
        }else{
            if(mode == 2) {
                FLE = -1; FRE = -1; BLE = -1; BRE = -1;
            }else{
                if(mode == 3) {
                    FLE = -1; FRE = 1; BLE = -1; BRE = 1;
                }else{
                    if(mode == 4) {
                        FLE = 1; FRE = -1; BLE = 1; BRE = -1;
                    }else{
                        if(mode == 5) {
                            FLE = -1; FRE = 1; BLE = 1; BRE = -1;
                        }else{
                            if(mode == 6) {
                                FLE = 1; FRE = -1; BLE = -1; BRE = 1;
                            }
                        }
                    }
                }
            }
        }

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

        //drive based on mode
        driveFLM.setPower(power*FLE);
        driveFRM.setPower(power*FRE);
        driveBLM.setPower(power*BLE);
        driveBRM.setPower(power*BRE);


        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() /*&& driveBLM.isBusy() && driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        driveFLM.setPower(power*FLE);
        driveFRM.setPower(power*FRE);
        driveBLM.setPower(power*BLE);
        driveBRM.setPower(power*BRE);

        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //theses methods move the robots without using encoders.
    // they were made because calling methods from teleop2 did not work and
    // i could just ctrl C V the entire thing rather  than changing minor parts here and there
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
        move(power,1);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() /*&& driveBLM.isBusy() && driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        move(0,0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveBackward(double power, int ticks) throws InterruptedException
    {
        driveForward(power, -ticks);
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

        //spinLeft(power);
        /*driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
        */
        move(power,3);
        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() /*&& driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        move(0,0);
        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void setMode(DcMotor.RunMode runMode){

        driveFLM.setMode(runMode);
        driveFRM.setMode(runMode);
        driveBLM.setMode(runMode);
        driveBRM.setMode(runMode);
    }

    private void encoderSetMode(int ticks){

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

    }

    public void spinRight(double power, int ticks) throws InterruptedException
    {
        spinLeft(power, -ticks);
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

        //moveLeft(power);
        /*driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(-power);
        */
        move(power,5);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() /*&& driveBRM.isBusy()*/)
        {

        }

        //stopMoving();
        move(0,0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveRight(double power, int ticks) throws InterruptedException
    {
        moveLeft(power, -ticks);
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
        samplingS.setPosition(0);
        EnderCVContoursTest();
        if (i != 0)
        {
            isGold = true;
            samplingS.setPosition(.65);
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
                driveForward(power, 600);
                detectingAndSamplingGold(2000);

                if (!isGold)
                {
                    samplingS.setPosition(0);
                    driveBackward(power, 1300);
                    detectingAndSamplingGold(800);

                    if (!isGold)
                    {
                        driveForward(power, 600);
                    }
                }
            }
        }
    }
}