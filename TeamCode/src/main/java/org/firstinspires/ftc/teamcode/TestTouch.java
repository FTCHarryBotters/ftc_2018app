package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Autonomous(name = "Test Touch", group = "Sample")
public class TestTouch extends LinearOpMode
{
    //Declare Motors
    private DcMotor motorleft;
    private DcMotor motorright;
    private DcMotor motorarm;

    //Declare Servos
    private Servo RightArmServo;
    private Servo LeftArmServo;

    //Declare Color sensor
    public DigitalChannel TouchSensor;


    @Override
    public void runOpMode() throws InterruptedException
    {
        //Match Motors in Config
        motorleft = hardwareMap.dcMotor.get("left_drive");
        motorright = hardwareMap.dcMotor.get("right_drive");
        motorarm = hardwareMap.dcMotor.get("arm_motor");

        //reverse right motor
        motorright.setDirection(DcMotor.Direction.REVERSE);

        //match servos in config
        LeftArmServo = hardwareMap.servo.get("LeftArmServo");
        RightArmServo = hardwareMap.servo.get("RightArmServo");

        TouchSensor = hardwareMap.get(DigitalChannel.class, "TouchSensor");
        TouchSensor.setMode(DigitalChannel.Mode.INPUT);





        //set servos to open positionduring initialization
        LeftArmServo.setPosition(.52);
        RightArmServo.setPosition(.39);

        waitForStart();

        DriveForward(1);
        if (TouchSensor.getState() == true)
        {
            STOP();
        }











        }
    public void DriveForward(double power)
    {
        motorleft.setPower(power);
        motorright.setPower(power);
    }

    public void closeServos()
    {
        LeftArmServo.setPosition(.01);
        RightArmServo.setPosition(.98);
    }

    public void openServos()
    {
        LeftArmServo.setPosition(.52);
        RightArmServo.setPosition(.39);
    }

    public void Armup() throws InterruptedException
    {
        motorarm.setPower(.25);
        Thread.sleep(1000);
    }

    public void Armdown() throws InterruptedException
    {
        motorarm.setPower(-.25);
        Thread.sleep(1000);
    }

    public void STOP() throws InterruptedException
    {
        DriveForward(0);
        motorarm.setPower(0);
        Thread.sleep(1000);
    }
}
