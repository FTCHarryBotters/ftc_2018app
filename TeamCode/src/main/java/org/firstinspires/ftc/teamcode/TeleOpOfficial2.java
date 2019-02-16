package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOpOfficial2", group = "Sample")
public class TeleOpOfficial2 extends LinearOpMode {

    //for autom
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

        //configure motors
        driveFLM   = hardwareMap.dcMotor.get("driveFLM");
        driveFRM   = hardwareMap.dcMotor.get("driveFRM");
        driveBLM   = hardwareMap.dcMotor.get("driveBLM");
        driveBRM   = hardwareMap.dcMotor.get("driveBRM");
        latchM     = hardwareMap.dcMotor.get("latchM");
        upDownM    = hardwareMap.dcMotor.get("upDownM");
        inOutLeftM = hardwareMap.dcMotor.get("inOutLeftM");
        inOutRghtM = hardwareMap.dcMotor.get("inOutRightM");

        //set motor direction
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        latchM.setDirection(DcMotor.Direction.FORWARD);
        upDownM.setDirection(DcMotor.Direction.FORWARD);
        inOutLeftM.setDirection(DcMotor.Direction.FORWARD);
        inOutRghtM.setDirection(DcMotor.Direction.REVERSE);

        //configire seervos
        collectorS           = hardwareMap.servo.get("collectorS");
        collectorUpDownLeftS = hardwareMap.servo.get("collectorUpDownLeftS");
        collectorUpDownRghtS = hardwareMap.servo.get("collectorUpDownRightS");
        mineralDropperS      = hardwareMap.servo.get("mineralDropperS");
        markerS              = hardwareMap.servo.get("markerS");
        phoneS               = hardwareMap.servo.get("phoneS");

        //gets the latch and wheels not using the encoders, that's for Auton
        latchM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveFLM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveFRM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveBLM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveBRM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //makes the vertikal slide use encoders for Autom
        upDownM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upDownM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        upDownM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upDownM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(opModeIsActive()) {

            telemetry.addData("F", upDownM.getCurrentPosition());
            telemetry.update();

            //makes these two servos not move
            markerS.setPosition(0);
            phoneS.setPosition(0.9);

            //lets KV change how fast the robot moves
            //full speed normally and quart speed for latching
            if (gamepad1.x) {
                drivespeed = 0.25;
            }else {
                if (gamepad1.y) {
                    drivespeed = 1;
                }
            }

            //lets KV move the robot
            //left stick moves the robot without turning, it is driving and strafing
            //right stick x for spinning
            driveFLM.setPower((-gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x)*drivespeed);
            driveFRM.setPower((-gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x)*drivespeed);
            driveBLM.setPower((-gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x)*drivespeed);
            driveBRM.setPower((-gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x)*drivespeed);

            //lets KV use the triggers(2) to move the Latch
            if (gamepad1.right_trigger>0.1) {
                latchM.setPower(1);
            }else {
                if (gamepad1.left_trigger > 0.1) {
                    latchM.setPower(-1);
                }else {
                    latchM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    latchM.setPower(0);
                    latchM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                }
            }

            //KV uses the bumpers(1) to move the collector in and out
            if (gamepad1.right_bumper) {
                inOutLeftM.setPower(1);
                inOutRghtM.setPower(1);
            }else {
                if (gamepad1.left_bumper) {
                    inOutLeftM.setPower(-1);
                    inOutRghtM.setPower(-1);
                }else {
                    inOutLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    inOutRghtM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    inOutLeftM.setPower(0);
                    inOutRghtM.setPower(0);
                    inOutLeftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    inOutRghtM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                }
            }

            //Miike uses the letters to succ in and yeet out minerals
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

            //Miike uses the triggers(2) to move the mineral dropper
            if (gamepad2.right_trigger>0.1) {
                mineralDropperS.setPosition(0);
            }else {
                if (gamepad2.left_trigger>0.1) {
                    mineralDropperS.setPosition(0.75);
                } else {
                    mineralDropperS.setPosition(0.5);
                }
            }

            //Miike uses  the bumpers(1) to move the Vertical Linear Slide
            if (gamepad2.right_bumper) {
                upDownM.setPower(1);
            }else {
                if (gamepad2.left_bumper) {
                    upDownM.setPower(-1);
                }else {
                    upDownM.setPower(0);
                }
            }

            //mike uses the left stick y to move the collector up and down
            collectorUpDownLeftS.setPosition(((+gamepad2.left_stick_y)*0.5)+0.5);
            collectorUpDownRghtS.setPosition(((-gamepad2.left_stick_y)*0.5)+0.5);

            //Miike can use the x button to send the vertikal slide up
            //it first sets a bool to true so we can do other things while sending up
            //it then uses encoders to set it to the right position
            if (gamepad2.x) {
                sendDropperUp = true;
                upDownM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                upDownM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                upDownM.setTargetPosition(1000);
            }
            if (sendDropperUp) {
                if (Math.abs(upDownM.getCurrentPosition())<Math.abs(upDownM.getTargetPosition())) {
                    upDownM.setPower(0.5);
                }else {
                    upDownM.setPower(0);
                    upDownM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    sendDropperUp = false;
                }
            }

            //Miike uses the up button to drop the minerals and then bring the vertikal slide bak down
            //same as other thing
            if (gamepad2.dpad_up) {
                mineralDropperS.setPosition(0);
                Thread.sleep(750);
                mineralDropperS.setPosition(0.5);
                Thread.sleep(750);
                mineralDropperS.setPosition(1);
                Thread.sleep(750);
                mineralDropperS.setPosition(0.5);
                bringDropperDown = true;
                upDownM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                upDownM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                upDownM.setTargetPosition(-2000);
            }
            if (bringDropperDown) {
                if (Math.abs(upDownM.getCurrentPosition())<Math.abs(upDownM.getTargetPosition())) {
                    upDownM.setPower(-0.5);
                }else {
                    upDownM.setPower(0);
                    upDownM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    bringDropperDown = false;
                }
            }

            //used to measure the cycles of our loop
            telemetry.addData("a", testCounter);
            telemetry.update();
            testCounter++;

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
}
