package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Test Color", group = "Sample")
@Disabled
public class ColorTest extends LinearOpMode
{
    //Declare Motors
    private DcMotor motorleft;
    private DcMotor motorright;
    private DcMotor motorarm;

    //Declare Servos
    private Servo RightArmServo;
    private Servo LeftArmServo;

    //Declare Color sensor
    public ColorSensor ColorSensor;


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

        ColorSensor = hardwareMap.get(ColorSensor.class, "ColorSensor");

        float hsvValues[] = {0F, 0F, 0F};

        final double SCALE_FACTOR = 225;


        //set servos to open positionduring initialization
        LeftArmServo.setPosition(.52);
        RightArmServo.setPosition(.39);

        waitForStart();

        Color.RGBToHSV( (int) (ColorSensor.red() * SCALE_FACTOR),
                (int) (ColorSensor.green() * SCALE_FACTOR),
                (int) (ColorSensor.blue() * SCALE_FACTOR),
                hsvValues);

        DriveForward(1, 250);
        STOP();
        Thread.sleep(3000);
        Armup();

        if (hsvValues[0] > 90 && hsvValues[0] < 150 && hsvValues[1] > .6)
        {
            Thread.sleep(5000);
            DriveForward(1, 1000);
        }else{DriveForward(-1, 1000);}






    }
    public void DriveForward(double power, long time) throws InterruptedException
    {
        motorleft.setPower(power);
        motorright.setPower(power);
        Thread.sleep(time);
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
        DriveForward(0,1);
        motorarm.setPower(0);
        Thread.sleep(1000);
    }
}
