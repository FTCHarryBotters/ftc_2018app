package org.firstinspires.ftc.teamcode.RRobsolete;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.abs;

@Autonomous(name = "DeLatchTest", group = "Sample")
@Disabled
public class DeLatchTest extends LinearOpMode{
    //declare
    private DcMotor latchLeftM;
    private DcMotor latchRightM;
    private Servo latchLeftS;
    private Servo latchRightS;

    Thread  delatchServoThread;

    @Override
    public void runOpMode() throws InterruptedException {
        //configure
        latchLeftM     = hardwareMap.dcMotor.get("latchLeftM");
        latchRightM    = hardwareMap.dcMotor.get("latchRightM");

        latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        latchRightM.setDirection(DcMotor.Direction.FORWARD);

        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        delatchServoThread = new DelatchServoThread();

        latchLeftS       = hardwareMap.servo.get("latchLeftS");
        latchRightS      = hardwareMap.servo.get("latchRightS");
        waitForStart();
        //what runs

        deLatchRobot();
        Thread.sleep(2000);

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
        latchLeftM.setPower(0);
        latchRightM.setPower(0);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void latchArmE(double power, int ticks) throws InterruptedException
    {
        //reset encoders

        latchLeftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        //wait
        while (abs(latchLeftM.getCurrentPosition()) < abs(ticks) && abs(latchRightM.getCurrentPosition()) < abs(ticks))
        {

        }
        //stop motors. why o why.
        // the latchE works *fundamentally* different to the driveE.
        // doing the same thing didn't work, and now I pay the price: inconsistent code
        latchLeftM.setPower(0);
        latchRightM.setPower(0);
        latchLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latchRightM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void deLatchRobot() throws InterruptedException
    {
        latchLeftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Position1", latchLeftM.getCurrentPosition());
        telemetry.update();

        delatchServoThread.start();
        latchArm(1, 200);

        telemetry.addData("Position2", latchLeftM.getCurrentPosition());
        telemetry.update();

        telemetry.addData("Position3", latchLeftM.getCurrentPosition());
        telemetry.update();

        latchArmE(-0.5, 1500);

        telemetry.addData("Position4", latchLeftM.getCurrentPosition());
        telemetry.update();
    }
    private class DelatchServoThread  extends Thread
    {
        public DelatchServoThread (){

        }
        @Override
        public void run()
        {
            latchLeftS.setPosition(1);
            latchRightS.setPosition(0);
            this.interrupt();
        }
    }
}
