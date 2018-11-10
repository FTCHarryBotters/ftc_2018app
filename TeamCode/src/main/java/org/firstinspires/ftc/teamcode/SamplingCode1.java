package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "SamplingCode1", group = "Sample")
@Disabled
public class SamplingCode1 extends LinearOpMode
{
    //Declare Motors
    private DcMotor drivemotorleft;
    private DcMotor drivemotorright;

    //Declare Servos
    private Servo RightArmServo;
    private Servo LeftArmServo;

    //Declare Color sensor
    public ColorSensor ColorSensor;


    @Override
    public void runOpMode() throws InterruptedException
    {
        //Match Motors in Config
        drivemotorleft = hardwareMap.dcMotor.get("left_drive");
        drivemotorright = hardwareMap.dcMotor.get("right_drive");

        //reverse right motor
        drivemotorright.setDirection(DcMotor.Direction.REVERSE);

        //match servos in config
        LeftArmServo = hardwareMap.servo.get("LeftArmServo");
        RightArmServo = hardwareMap.servo.get("RightArmServo");

        ColorSensor = hardwareMap.get(ColorSensor.class, "ColorSensor");

        float hsvValues[] = {0F, 0F, 0F};

        final double SCALE_FACTOR = 225;



        waitForStart();

        Color.RGBToHSV(
                (int) (ColorSensor.red() * SCALE_FACTOR),
                (int) (ColorSensor.green() * SCALE_FACTOR),
                (int) (ColorSensor.blue() * SCALE_FACTOR),
                hsvValues);







    }
    public void Drive(double power, long time) throws InterruptedException
    {
        drivemotorleft.setPower(power);
        drivemotorright.setPower(power);
        Thread.sleep(time);
    }
    public void Turn(double power, long time) throws InterruptedException
    {
        drivemotorleft.setPower(power);
        drivemotorright.setPower(-power);
        Thread.sleep(time);
    }

}
