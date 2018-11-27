package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.abs;

@Autonomous(name = "AutoTestDelatch", group = "Sample")
public class AutoTestDelatch extends LinearOpMode{
    //declare
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchLeftM;
    private DcMotor latchRightM;
    private Servo latchLeftS;
    private Servo latchRightS;

    Thread  delatchServoThread;

    @Override
    public void runOpMode() throws InterruptedException {
        //configure
        driveFLM       = hardwareMap.dcMotor.get("driveFLM");
        driveFRM       = hardwareMap.dcMotor.get("driveFRM");
        driveBLM       = hardwareMap.dcMotor.get("driveBLM");
        driveBRM       = hardwareMap.dcMotor.get("driveBRM");
        latchLeftM     = hardwareMap.dcMotor.get("latchLeftM");
        latchRightM    = hardwareMap.dcMotor.get("latchRightM");

        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        latchRightM.setDirection(DcMotor.Direction.FORWARD);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchLeftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        latchRightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        delatchServoThread = new DelatchServoThread();

        latchLeftS       = hardwareMap.servo.get("latchLeftS");
        latchRightS      = hardwareMap.servo.get("latchRightS");
        waitForStart();
        //what runs

        deLatchRobot();
        Thread.sleep(2000);

        moveRightE(0.3, 200);
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
    public void moveRightE(double power, int ticks) throws InterruptedException
    {
        //Reset Encoders
        driveFLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        driveFLM.setTargetPosition(ticks);
        driveFRM.setTargetPosition(-ticks);
        driveBLM.setTargetPosition(-ticks);
        driveBRM.setTargetPosition(ticks);

        //set ot RUN_TO_POSITION mode
        driveFLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveFRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBLM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveBRM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //moveRight
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);

        //wait until target position
        while (driveFLM.isBusy() && driveFRM.isBusy() && driveBLM.isBusy() && driveBRM.isBusy())
        {

        }

        //stopMoving();
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);

        driveFLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
