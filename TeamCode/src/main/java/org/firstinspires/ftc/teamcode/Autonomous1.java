package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Autonomous1", group = "Sample")
public class Autonomous1 extends LinearOpMode
{
    private DcMotor latchLeftM;
    private DcMotor latchRightM;
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;

    private Servo latchLeftS;
    private Servo latchRightS;

    @Override
    public void runOpMode() throws InterruptedException
    {

        //declare motors
        latchLeftM = hardwareMap.dcMotor.get("latchLeftM");
        latchRightM = hardwareMap.dcMotor.get("latchRightM");
        driveFLM = hardwareMap.dcMotor.get("driveFLM");
        driveFRM = hardwareMap.dcMotor.get("driveFRM");
        driveBLM = hardwareMap.dcMotor.get("driveBLM");
        driveBRM = hardwareMap.dcMotor.get("driveBRM");

        //reversing necessary motors
        latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);

        //declare servos
        latchLeftS = hardwareMap.servo.get("latchLeftS");
        latchRightS = hardwareMap.servo.get("latchRightS");

        waitForStart();

        //change time and power when we figure out the exact quantities
        Delatch();

        //insert EnderCV identification here

        //aaaaaaaaaa i hate programming
        //unhook thing from latch
        driveBRM.setPower(.25);
        //change values eventually
        Thread.sleep(500);
        //change values eventually
        driveForward(.25, 400);
        //driveBLM.setPower(.25);
        //change values eventually
        //Thread.sleep(500);
        //change values eventually
        //driveForward(.25, 500);
        //change values eventually
        //spinLeft(.25, 1000);

        //insert gold sampling here

        //change values eventually
        //driveForward(.25, 2000);
        //change values eventually
        //spinLeft(.25, 500);
        //change values eventually
        //driveForward(.25, 2000);

        //insert token dropping here

        //change values eventually
        //driveForward(-0.5, 4500);



    }
    public void driveForward(double power, long time) throws InterruptedException
    {
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);
        Thread.sleep(time);
    }
    public void latchMotor(double power, long time) throws InterruptedException
    {
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        Thread.sleep(time);
    }
    public void spinLeft(double power, long time) throws InterruptedException
    {
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
        Thread.sleep(time);
    }
    public void spinRight(double power, long time) throws InterruptedException
    {
        spinLeft(-power, time);
    }
    public void Delatch() throws InterruptedException
    {
        latchLeftM.setPower(-.75);
        latchRightM.setPower(-.75);
        latchLeftS.setPosition(0);
        latchRightS.setPosition(1);
        Thread.sleep(500);
        latchMotor(.25, 4000);
    }
}
