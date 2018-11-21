package org.firstinspires.ftc.teamcode;

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
    private DigitalChannel linearUpDownS;

    //set touchsensor boolean
    private boolean isTouchSensor = true;

    //Declare drive and latch speed statics
    private static double DRIVESPEEDFULL = 1;
    private static double DRIVESPEED3BY4 = 0.5;
    private static double LATCHSPEEDFULL = 1;
    private static double LATCHSPEEDQRTR = 0.25;

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
        linearUpDownS = hardwareMap.digitalChannel.get("linearUpDownS");
        linearUpDownS.setMode(DigitalChannel.Mode.INPUT);


        //declare latchspeed variable. this is to change th speed of th latch arm
        double latchspeed = LATCHSPEEDQRTR;

        waitForStart();

        while(opModeIsActive()) {

            markerS.setPosition(markerS.getPosition());

            samplingS.setPosition(0);

            isTouchSensor ^= linearUpDownS.getState();

            //lets Mike change the speed of the Latch arm
            if (gamepad2.dpad_up){
                latchspeed = LATCHSPEEDFULL;
            }else{
                if (gamepad2.dpad_down){
                    latchspeed = LATCHSPEEDQRTR;
                }
            }

            driveFLM.setPower(gamepad1.left_stick_y);
            driveFRM.setPower(gamepad1.right_stick_y);
            driveBLM.setPower(gamepad1.left_stick_y);
            driveBRM.setPower(gamepad1.right_stick_y);
            moveLeft(gamepad1.left_trigger);
            moveRight(gamepad1.right_trigger);

            if (gamepad1.right_bumper) {
                linearForwardM.setPower(0.5);
            }else{
                if (gamepad1.left_bumper) {
                    linearForwardM.setPower(-0.5);
                }else{
                    linearForwardM.setPower(0);
                }
            }

            if (gamepad1.dpad_up) {
                linearUpDownM.setPower(0.5);
            }

            if (gamepad1.dpad_down) {
                if (isTouchSensor){
                    linearUpDownM.setPower(0);
                    linearUpDownM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                }else{
                    linearUpDownM.setPower(-0.5);
                }
            }

            if (!gamepad1.dpad_down && !gamepad1.dpad_up) {
                linearUpDownM.setPower(0);
                linearUpDownM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            if (gamepad2.right_trigger > 0.1) {
                latchLeftS.setPosition(1);
                latchRightS.setPosition(0);
            }else{
                if (gamepad2.left_trigger > 0.1) {
                    latchLeftS.setPosition(0);
                    latchRightS.setPosition(1);
                }
            }

            collectorUpDownS.setPosition((gamepad2.right_stick_y+1)*0.5);

            if (gamepad2.a) {
                collectorS.setPosition(1);
            }else{
                if (gamepad2.y) {
                    collectorS.setPosition(0);
                }else{
                    if (gamepad2.b) {
                        collectorS.setPosition(0.5);
                    }
                }
            }

            if (gamepad2.right_bumper) {
                mineralDropperS.setPosition(0.60);
            }else{
                if (gamepad2.left_bumper) {
                    mineralDropperS.setPosition(0.40);
                }else{
                    mineralDropperS.setPosition(0.50);
                }
            }

            idle();
        }
    }
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
}
