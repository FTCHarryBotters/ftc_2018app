package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TOO2.2", group = "Sample")
public class TOO2butInOutMotorReversed extends LinearOpMode {

    boolean sendDropperUp = false;
    boolean bringDropperDown = false;
    int testCounter = 0;


    //declare motors
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchM;
    private DcMotor upDownM;
    private DcMotor inOutLeftM;
    private DcMotor inOutRghtM;

    double drivespeed = 1;

    //declare swervos
    private Servo collectorS;
    private Servo collectorUpDownLeftS;
    private Servo collectorUpDownRghtS;
    private Servo mineralDropperS;
    private Servo markerS;
    private Servo phoneS;

    @Override
    public void runOpMode() throws InterruptedException {

        driveFLM   = hardwareMap.dcMotor.get("driveFLM");
        driveFRM   = hardwareMap.dcMotor.get("driveFRM");
        driveBLM   = hardwareMap.dcMotor.get("driveBLM");
        driveBRM   = hardwareMap.dcMotor.get("driveBRM");
        latchM     = hardwareMap.dcMotor.get("latchM");
        upDownM    = hardwareMap.dcMotor.get("upDownM");
        inOutLeftM = hardwareMap.dcMotor.get("inOutLeftM");
        inOutRghtM = hardwareMap.dcMotor.get("inOutRightM");

        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        latchM.setDirection(DcMotor.Direction.FORWARD);
        upDownM.setDirection(DcMotor.Direction.FORWARD);
        inOutLeftM.setDirection(DcMotor.Direction.REVERSE);
        inOutRghtM.setDirection(DcMotor.Direction.FORWARD);

        collectorS           = hardwareMap.servo.get("collectorS");
        collectorUpDownLeftS = hardwareMap.servo.get("collectorUpDownLeftS");
        collectorUpDownRghtS = hardwareMap.servo.get("collectorUpDownRightS");
        mineralDropperS      = hardwareMap.servo.get("mineralDropperS");
        markerS              = hardwareMap.servo.get("markerS");
        phoneS               = hardwareMap.servo.get("phoneS");

        phoneS.setPosition(0.9);

        waitForStart();

        latchM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(opModeIsActive()) {

            markerS.setPosition(0);
            phoneS.setPosition(0.9);

            if (gamepad1.x) {
                drivespeed = 0.25;
            }else {
                if (gamepad1.y) {
                    drivespeed = 1;
                }
            }

            driveFLM.setPower((-gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x)*drivespeed);
            driveFRM.setPower((-gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x)*drivespeed);
            driveBLM.setPower((-gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x)*drivespeed);
            driveBRM.setPower((-gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x)*drivespeed);

            if (gamepad1.right_trigger>0.1) {
                latchM.setPower(1);
            }else {
                if (gamepad1.left_trigger > 0.1) {
                    latchM.setPower(-1);
                }else {
                    latchM.setPower(0);
                }
            }

            if (gamepad1.right_bumper) {
                inOutLeftM.setPower(0.4);
                inOutRghtM.setPower(0.4);
            }else {
                if (gamepad1.left_bumper) {
                    inOutLeftM.setPower(-0.4);
                    inOutRghtM.setPower(-0.4);
                }else {
                    inOutLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    inOutRghtM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    inOutLeftM.setPower(0);
                    inOutRghtM.setPower(0);
                    inOutLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    inOutRghtM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                }
            }

            if (gamepad2.a) {
                collectorS.setPosition(1);
            }else {
                if (gamepad2.y) {
                    collectorS.setPosition(0);
                }else {
                    if (gamepad2.b) {
                        collectorS.setPosition(0.5);
                    }
                }
            }

            if (gamepad2.right_trigger>0.1) {
                mineralDropperS.setPosition(0);
            }else {
                if (gamepad2.left_trigger>0.1) {
                    mineralDropperS.setPosition(0.75);
                } else {
                    mineralDropperS.setPosition(0.5);
                }
            }

            if (gamepad2.right_bumper) {
                upDownM.setPower(1);
            }else {
                if (gamepad2.left_bumper) {
                    upDownM.setPower(-1);
                }else {
                    upDownM.setPower(0);
                }
            }

            collectorUpDownLeftS.setPosition(((-gamepad2.left_stick_y)*0.5)+0.5);
            collectorUpDownRghtS.setPosition(((+gamepad2.left_stick_y)*0.5)+0.5);

            if (gamepad2.x) {
                sendDropperUp = true;
                testCounter = 0;
            }
            if (sendDropperUp) {
                if (testCounter <= 350) {
                    upDownM.setPower(0.5);
                }else {
                    upDownM.setPower(0);
                    sendDropperUp = false;
                }
                testCounter++;
            }

            if (gamepad2.dpad_up) {
                mineralDropperS.setPosition(0);
                Thread.sleep(750);
                mineralDropperS.setPosition(0.5);
                Thread.sleep(750);
                mineralDropperS.setPosition(1);
                Thread.sleep(750);
                mineralDropperS.setPosition(0.5);
                testCounter = 0;
                bringDropperDown = true;
            }

            if (bringDropperDown) {
                if (testCounter <= 350) {
                    upDownM.setPower(-0.5);
                }else {
                    upDownM.setPower(0);
                    bringDropperDown = false;
                }
                testCounter++;
            }

            telemetry.addData("a", latchM.getCurrentPosition());
            telemetry.update();

            idle();
        }
    }
    //methods for moving.
    public void driveForward(double power) {
        //this function is to move forward.
        //all motors move forward
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);
    }
    public void driveBackward(double power) {
        //this function is to move backward
        //all motors move back
        driveFLM.setPower(-power);
        driveFRM.setPower(-power);
        driveBLM.setPower(-power);
        driveBRM.setPower(-power);
    }
    public void spinLeft(double power) {
        //spins the robot left
        //the left side moves backward &
        //the right side motors move forward
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
    }
    public void spinRght(double power) {
        //spins the robot right
        //the right side moves backward &
        //the left side motors move forward
        driveFLM.setPower(power);
        driveFRM.setPower(-power);
        driveBLM.setPower(power);
        driveBRM.setPower(-power);
    }
    public void moveLeft(double power) {
        //slides the robot left
        //the front left and back right motors move backward
        //while the front right and back left motors move forward
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(-power);
    }
    public void moveRght(double power) {
        //slides the robot right
        //the front right and back left motors move backward
        //while the front left and back right motors move forward
        driveFLM.setPower(power);
        driveFRM.setPower(-power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
    }
    public void stopMoving() {
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);
    }
    public void dropMinerals()throws InterruptedException{
        upDownM.setPower(0.5);
        Thread.sleep(1000);
        upDownM.setPower(0);
    }
}
