package org.firstinspires.ftc.teamcode.obsolete;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "Test Touch", group = "Sample")
@Disabled
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
    DigitalChannel digitalTouch;


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

        digitalTouch = hardwareMap.get(DigitalChannel.class, "TouchSensor");
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);





        //set servos to open position during initialization
        LeftArmServo.setPosition(.52);
        RightArmServo.setPosition(.39);

        waitForStart();

        while (opModeIsActive()){

        if (digitalTouch.getState() == false) {

            telemetry.addData("Digital Touch", "Is Not Pressed");
            DriveForward(1);
        } else {
            telemetry.addData("Digital Touch", "Is Pressed");
            STOP();
        }

        telemetry.update();

    }


        }
    public void DriveForward(double power) throws InterruptedException
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
        motorright.setPower(0);
        motorleft.setPower(0);
    }
}
