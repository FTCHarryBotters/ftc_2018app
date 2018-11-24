package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "DeLatchTest", group = "Sample")
public class DeLatchTest extends LinearOpMode{
    //declare
    private DcMotor latchLeftM;
    private DcMotor latchRightM;
    private Servo latchLeftS;
    private Servo latchRightS;

    @Override
    public void runOpMode() throws InterruptedException {
        //configure
        latchLeftM     = hardwareMap.dcMotor.get("latchLeftM");
        latchRightM    = hardwareMap.dcMotor.get("latchRightM");
        latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        latchRightM.setDirection(DcMotor.Direction.FORWARD);
        latchLeftS       = hardwareMap.servo.get("latchLeftS");
        latchRightS      = hardwareMap.servo.get("latchRightS");
        waitForStart();
        //what runs
        deLatchRobot();
    }
    //methods
    //methods for the latch arm
    private void latchArm(double power, long time) throws InterruptedException
    {
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        Thread.sleep(time);
    }
    private void deLatchRobot() throws InterruptedException
    {
        latchArm(1, 500);
        latchLeftS.setPosition(1);
        latchRightS.setPosition(0);
        latchArm(-0.25, 1300);
        latchArm(0, 10);
        latchLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latchRightM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
