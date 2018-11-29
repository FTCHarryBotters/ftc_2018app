package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "KKMineralDropTest", group = "KK")
public class KKMineralDropTest extends LinearOpMode{
    //declare


    public Servo mineralDropperS;
    public DcMotor linearUpDownM;

    //declare sensor(s)
    public DigitalChannel mineralDropTouchSensor;


//    Thread  delatchServoThread;

    @Override
    public void runOpMode() throws InterruptedException {
        //configure
        mineralDropperS  = hardwareMap.servo.get("mineralDropperS");

        //set sensor(s) to the configuration
        // get a reference to our digitalTouch object.
        mineralDropTouchSensor = hardwareMap.digitalChannel.get("linearUpDownS");

        // set the digital channel to input.
        mineralDropTouchSensor.setMode(DigitalChannel.Mode.INPUT);


        linearUpDownM  = hardwareMap.dcMotor.get("linearUpDownM");
        linearUpDownM.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        dropMinerals();
        bringDownMineralDropper(.5);




        while (opModeIsActive()) {

            // send the info back to driver station using telemetry function.
            // if the digital channel returns true it's HIGH and the button is unpressed.
            if (mineralDropTouchSensor.getState() == true) {
                telemetry.addData("Digital Touch", "Is Not Pressed");
            } else {
                telemetry.addData("Digital Touch", "Is Pressed");
            }

            telemetry.update();

        }



            telemetry.addData("Touch: ", mineralDropTouchSensor.getState());
        telemetry.update();

        telemetry.addData("Position: ", mineralDropperS.getPosition());
        telemetry.update();
        Thread.sleep(250);


        /*
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
        */

    }

    public void bringDownMineralDropper(double power){
        while(mineralDropTouchSensor.getState() == true){
            linearUpDownM.setPower(power * -1);
        }
        linearUpDownM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearUpDownM.setPower(0);
    }


    public void dropMinerals() throws InterruptedException{

        mineralDropperS.setPosition(0.80);

        telemetry.addData("Direction: ", mineralDropperS.getDirection());
        telemetry.update();

        telemetry.addData("Position: ", mineralDropperS.getPosition());
        telemetry.update();
        Thread.sleep(2000);


        mineralDropperS.setPosition(0);

        telemetry.addData("Direction: ", mineralDropperS.getDirection());
        telemetry.update();

        telemetry.addData("Position: ", mineralDropperS.getPosition());
        telemetry.update();
        Thread.sleep(250);
    }


    //methods
    //methods for the latch arm
    /*
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
        delatchServoThread.start();
        latchArm(1, 200);
        latchArmE(0.5, -4000);
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
    */
}




