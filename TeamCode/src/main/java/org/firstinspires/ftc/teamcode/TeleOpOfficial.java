package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorDigitalTouch;

@TeleOp(name = "TeleOpOfficial", group = "Sample")
public class TeleOpOfficial extends LinearOpMode
{
    //declare motors
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchLeftM;
    private DcMotor latchRightM;
    private DcMotor linearForwardM;
    private DcMotor linearUpDownM;

    //declare servos
    private Servo latchLeftS;
    private Servo latchRightS;
    private Servo samplingS;
    private Servo markerS;
    private Servo collectorS;
    private Servo collectorUpDownS;
    private Servo mineralDropperS;

    //declare sensor(s)
    private DigitalChannel linearUpDownN;

    //set touch sensor boolean
    private boolean isTouchSensor;

    //Declare latch speed statics
    private static double LATCHSPEEDFAST = 1;
    private static double LATCHSPEEDSLOW = 0.41;

    //Thread mineralDropping;

    @Override
    public void runOpMode() throws InterruptedException {

        //set motors to th configuration
        driveFLM       = hardwareMap.dcMotor.get("driveFLM");
        driveFRM       = hardwareMap.dcMotor.get("driveFRM");
        driveBLM       = hardwareMap.dcMotor.get("driveBLM");
        driveBRM       = hardwareMap.dcMotor.get("driveBRM");
        latchLeftM     = hardwareMap.dcMotor.get("latchLeftM");
        latchRightM    = hardwareMap.dcMotor.get("latchRightM");
        linearForwardM = hardwareMap.dcMotor.get("linearForwardM");
        linearUpDownM  = hardwareMap.dcMotor.get("linearUpDownM");

        //reversing necessary motors
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        latchLeftM.setDirection(DcMotor.Direction.FORWARD);
        latchRightM.setDirection(DcMotor.Direction.REVERSE);
        linearForwardM.setDirection(DcMotor.Direction.FORWARD);
        linearUpDownM.setDirection(DcMotor.Direction.REVERSE);

        //NOT using encoders
        driveFLM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //set servos to th configuration
        latchLeftS       = hardwareMap.servo.get("latchLeftS");
        latchRightS      = hardwareMap.servo.get("latchRightS");
        samplingS        = hardwareMap.servo.get("samplingS");
        markerS          = hardwareMap.servo.get("markerS");
        collectorS       = hardwareMap.servo.get("collectorS");
        collectorUpDownS = hardwareMap.servo.get("collectorUpDownS");
        mineralDropperS  = hardwareMap.servo.get("mineralDropperS");

        //set sensor(s) to the configuration
        linearUpDownN = hardwareMap.digitalChannel.get("linearUpDownS");
        linearUpDownN.setMode(DigitalChannel.Mode.INPUT);

        //declare latchspeed variable. this is to change th speed of th latch arm
        double latchspeed = LATCHSPEEDSLOW;

        //mineralDropping = new mineralDropping();

        waitForStart();

        while(opModeIsActive()) {

            //make sure the marker dropper and the sampling arm
            //don't move when the robot shakes.
            //since neither KV nor Michael can control them,
            //if they get in they way of other things,
            //they can't move them back
            markerS.setPosition(1);
            samplingS.setPosition(0);

            //this boolean saves information
            //on whether or not the touch sensor is pressed.
            //if the touch sensor is pressed, the getState returns false and vice versa.
            //this is reversed, so the boolean reverses the value with ^=
            isTouchSensor ^= linearUpDownN.getState();

            //lets Mike change the speed of the Latch arm.
            //He can use 1/6 speed to move the arm into the latch
            //and he can use full speed to actually lift the robot
            if (gamepad2.dpad_up){
                latchspeed = LATCHSPEEDFAST;
            }else{
                if (gamepad2.dpad_down){
                    latchspeed = LATCHSPEEDSLOW;
                }
            }

            //these six lines let KV move the Robot.
            //the left side of the robot, FLM and BLM are controlled w/ the left stick
            //the right side of the robot, FRM and BRM are controlled w/ the right stick
            //he can use the triggers to move left or right.
            driveFLM.setPower(-gamepad1.left_stick_y);
            driveFRM.setPower(-gamepad1.right_stick_y);
            driveBLM.setPower(-gamepad1.left_stick_y);
            driveBRM.setPower(-gamepad1.right_stick_y);
            moveLeft(gamepad1.left_trigger);
            moveRight(gamepad1.right_trigger);

            //if KV presses RB, the slide goes forward
            //if he presses LB, the slide goes back
            //otherwise, nothing happens
            if (gamepad1.right_bumper) {
                linearForwardM.setPower(0.5);
            }else{
                if (gamepad1.left_bumper) {
                    linearForwardM.setPower(-0.5);
                }else{
                    linearForwardM.setPower(0);
                }
            }

            //if KV pressed dpad up, the shoe moves to drop the minerals
            //if he presses down, it moves back
            if (gamepad1.dpad_up) {
                //mineralDropperS.setPosition(mineralDropperS.getPosition()+0.01);
                mineralDropperS.setPosition(0.8);
            }else{
                if (gamepad1.dpad_down) {
                    //mineralDropperS.setPosition(mineralDropperS.getPosition()-0.01);
                    mineralDropperS.setPosition(0);
                }
            }

            //if KV presses A and B, the latch servos move
            //B delatches, and A latches
            if (gamepad1.b) {
                latchLeftS.setPosition(1);
                latchRightS.setPosition(0);
            }else{
                if (gamepad1.a) {
                    latchLeftS.setPosition(0);
                    latchRightS.setPosition(1);
                }
            }
            //if KV presses x, then the parallel thread starts
            //the parallel thread moves the mineral dropper back, waits for the minerals to drop,
            //moves the mineraldropper back, and moves the slide down.
            /**********/
            /*if (gamepad1.x){
                mineralDropping.start();
            }
            */

            //the most annoying code in this program
            //if M presses RB, the the up down slide will go up
            //if he presses LB, then it checks if the touch sensor is pressed.
            //if it is pressed, then nothing happens.
            //if it isn't pressed, then it goes down
            //if neither RB nor LB is pressed, nothing happens
            if (gamepad2.right_bumper) {
                linearUpDownM.setPower(0.5);
            }
            if (gamepad2.left_bumper) {
                if (isTouchSensor){
                    linearUpDownM.setPower(0);
                    linearUpDownM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                }else{
                    linearUpDownM.setPower(-0.5);
                }
            }
            if (!gamepad2.right_bumper && !gamepad2.left_bumper) {
                linearUpDownM.setPower(0);
                linearUpDownM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            //the latch motors are powered by M's left stick
            //if he goes down, the latch moves toward the robot
            //if he goes up, it moves away from the robot
            //he pushes down to make the robot lift in the endgame
            latchLeftM.setPower(-gamepad2.left_stick_y*latchspeed);
            latchRightM.setPower(-gamepad2.left_stick_y*latchspeed);

            //the collector mechanism goes up
            //if M moves his right stick up, and vice versa
            collectorUpDownS.setPosition((gamepad2.right_stick_y+1)*0.5);

            //A, B, and Y move the collector flaps
            //A sucks in minerals;
            //Y pushes out minerals, if we get 3 or the wrong type
            //and B stops the flaps
            if (gamepad2.y) {
                collectorS.setPosition(1);
            }else {
                if (gamepad2.a) {
                    collectorS.setPosition(0);
                } else {
                    if (gamepad2.b) {
                        collectorS.setPosition(0.5);
                    }
                }
            }

            idle();
        }
    }
    //methods for moving.
    public void driveForward(double power)
    {
        //this function is to move forward.
        //all motors move forward
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);
    }
    public void driveBackward(double power)
    {
        //this function is to move backward
        //all motors move back
        driveFLM.setPower(-power);
        driveFRM.setPower(-power);
        driveBLM.setPower(-power);
        driveBRM.setPower(-power);
    }
    public void spinLeft(double power)
    {
        //spins the robot left
        //the left side moves backward &
        //the right side motors move forward
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
    }
    public void spinRight(double power)
    {
        //spins the robot right
        //the right side moves backward &
        //the left side motors move forward
        driveFLM.setPower(power);
        driveFRM.setPower(-power);
        driveBLM.setPower(power);
        driveBRM.setPower(-power);
    }
    public void moveLeft(double power)
    {
        //slides the robot left
        //the front left and back right motors move backward
        //while the front right and back left motors move forward
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(-power);
    }
    public void moveRight(double power)
    {
        //slides the robot right
        //the front right and back left motors move backward
        //while the front left and back right motors move forward
        driveFLM.setPower(power);
        driveFRM.setPower(-power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
    }
    public void stopMoving()
    {
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);
    }
    /*
    public class mineralDropping extends Thread
    {
        public mineralDropping () {

        }
        public void bringDownMineralDropper(double power){
            while(linearUpDownN.getState() == true){
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
        @Override
        public void run() {
            try {
                dropMinerals();
                bringDownMineralDropper(0.5);

            } catch (InterruptedException e) {
            } catch (Exception e) {
            }
        }
    }
    */
}
