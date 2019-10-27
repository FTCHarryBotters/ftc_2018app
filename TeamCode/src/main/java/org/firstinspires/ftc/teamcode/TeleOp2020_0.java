package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp2020_0", group = "Sample")
public class TeleOp2020_0 extends LinearOpMode{

    //declare motors
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor armM;
    private DcMotor stoneLeftM;
    private DcMotor stoneRghtM;

    //declare swervos
    private Servo stoneS;
    private Servo armLeftS;
    private Servo armRghtS;
    private Servo stoneLeftS;
    private Servo stoneRghtS;
    private Servo waffleLeftS;
    private Servo waffleRghtS;

    //we need to find the position of the servos that suck the motors in two places.
    //this way we only change it once
    double LeftServo = 0.5;
    double RghtServo = 0.5;

    double drivespeed = 0.5;

    @Override
    public void
    runOpMode() throws InterruptedException {

        //configure motors
        driveFLM   = hardwareMap.dcMotor.get("driveFLM");
        driveFRM   = hardwareMap.dcMotor.get("driveFRM");
        driveBLM   = hardwareMap.dcMotor.get("driveBLM");
        driveBRM   = hardwareMap.dcMotor.get("driveBRM");
        armM       = hardwareMap.dcMotor.get("armM");
        stoneLeftM = hardwareMap.dcMotor.get("stoneLeftM");
        stoneRghtM = hardwareMap.dcMotor.get("stoneRightM");

        //configure swervos
        stoneS      = hardwareMap.servo.get("stoneS");
        armLeftS    = hardwareMap.servo.get("armLeftS");
        armRghtS    = hardwareMap.servo.get("armRightS");
        stoneLeftS  = hardwareMap.servo.get("stoneLeftS");
        stoneRghtS  = hardwareMap.servo.get("stoneRightS");
        waffleLeftS = hardwareMap.servo.get("waffleLeftS");
        waffleRghtS = hardwareMap.servo.get("waffleRightS");

        //set motor directions
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        armM.setDirection(DcMotor.Direction.FORWARD);
        stoneLeftM.setDirection(DcMotor.Direction.REVERSE);
        stoneRghtM.setDirection(DcMotor.Direction.FORWARD);

        //set all motors to not use encoders
        driveFLM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        stoneLeftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        stoneRghtM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        //move succ motors out at start of teleop
        stoneLeftS.setPosition(LeftServo);
        stoneRghtS.setPosition(RghtServo);

        while(opModeIsActive()) {

            if (gamepad1.dpad_down) {
                drivespeed=0.5;
            }else {
                if (gamepad1.dpad_up) {
                    drivespeed=1;
                }
            }

            //lets KV move the robot
            //left stick moves the robot without turning, it is driving and strafing
            //right stick x for spinning
            driveFLM.setPower((-gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x)*drivespeed);
            driveFRM.setPower((-gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x)*drivespeed);
            driveBLM.setPower((-gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x)*drivespeed);
            driveBRM.setPower((-gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x)*drivespeed);

            //KV can start and stop the succ motors
            if (gamepad1.a) {
                stoneLeftM.setPower(1);
                stoneRghtM.setPower(1);
            }else {
                if (gamepad1.b) {
                    stoneLeftM.setPower(0);
                    stoneRghtM.setPower(0);
                }else {
                    if (gamepad1.y) {
                        stoneLeftM.setPower(-1);
                        stoneRghtM.setPower(-1);
                    }
                }
            }

            //KV can move the servos holding the succ motors in and out
            if (gamepad1.right_bumper) {
                stoneLeftS.setPosition(LeftServo);
                stoneRghtS.setPosition(RghtServo);
            }else {
                if (gamepad1.left_bumper) {
                    stoneLeftS.setPosition(1);
                    stoneRghtS.setPosition(0);
                }
            }

            armM.setPower(-gamepad2.left_stick_y);
            armM.setPower(gamepad2.right_stick_y*-.5);

            //Mike uses the triggers to move just the servos at the end of the arm.
            //he uses this to help place stone and to keep it horizontal.
            if (gamepad2.right_trigger>0.1) {
                armLeftS.setPosition(0.65);
                armRghtS.setPosition(0.35);
            }else {
                if (gamepad2.left_trigger>0.1) {
                    armLeftS.setPosition(0.35);
                    armRghtS.setPosition(0.65);
                }else {
                    armLeftS.setPosition(0.5);
                    armRghtS.setPosition(0.5);
                }
            }

            //Mike grabs or lets go of the stone at the end of the arm
            if (gamepad2.b) {
                stoneS.setPosition(0);
            }else {
                if (gamepad2.a) {
                    stoneS.setPosition(1);
                }
            }

            //used in endgame to move the foundation/waffle iron.
            if (gamepad2.dpad_down) {
                waffleLeftS.setPosition(1);
                waffleRghtS.setPosition(0);
            }else {
                if (gamepad2.dpad_up) {
                    waffleLeftS.setPosition(0.5);
                    waffleRghtS.setPosition(0.5);
                }
            }
        }
    }
}
