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

        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
        latchLeftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        Thread.sleep(time);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void latchArmE(double power, int ticks)
    {
        //reset encoders
        latchLeftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //set target position
        latchLeftM.setTargetPosition(ticks);
        latchRightM.setTargetPosition(ticks);
        //run to position mode
        latchLeftM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        latchRightM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //start motors
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        //wait
        while (latchLeftM.isBusy() && latchRightM.isBusy()){
            telemetry.addData("leftbusy", latchLeftM.isBusy());
            telemetry.addData("leftbusy", latchRightM.isBusy());
            telemetry.update();
        }
        //stop motors
        latchLeftM.setPower(0);
        latchRightM.setPower(0);
        //reset mode
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void deLatchRobot() throws InterruptedException
    {
        latchArmE(1, 10);
        latchLeftS.setPosition(1);
        latchRightS.setPosition(0);
        //latchArmE(0.5, -4000);
    }
}
