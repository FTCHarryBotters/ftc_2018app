package org.firstinspires.ftc.teamcode.RRobsolete;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "TestAuto", group = "Sample")
@Disabled
public class AutonomousTest1 extends LinearOpMode
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




        //set servos to open positionduring initialization
        LeftArmServo.setPosition(.52);
        RightArmServo.setPosition(.39);

        waitForStart();

        DriveForward(1, 1000);
        STOP();
        Thread.sleep(1000);
        closeServos();
        Armup();
        Thread.sleep(1000);
        DriveForward(1, 1000);
        Armdown();
        openServos();







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
        motorarm.setPower(1);
        Thread.sleep(500);
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
