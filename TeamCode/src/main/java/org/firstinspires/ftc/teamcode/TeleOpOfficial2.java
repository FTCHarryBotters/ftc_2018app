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

@TeleOp(name = "TeleOpOfficial2", group = "Sample")
public class TeleOpOfficial2 extends LinearOpMode
{
    //declare motors
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchM;
    private DcMotor upDownM;
    private DcMotor inOutLeftM;
    private DcMotor inOutRightM;

    //declare swervos
    private Servo collectorS;
    private Servo collectorUpDownLeftS;
    private Servo collectorUpDownRightS;
    private Servo mineralDropperS;

    @Override
    public void runOpMode() throws InterruptedException {

        driveFLM    = hardwareMap.dcMotor.get("driveFLM");
        driveFRM    = hardwareMap.dcMotor.get("driveFRM");
        driveBLM    = hardwareMap.dcMotor.get("driveBLM");
        driveBRM    = hardwareMap.dcMotor.get("driveBRM");
        latchM      = hardwareMap.dcMotor.get("latchM");
        upDownM     = hardwareMap.dcMotor.get("upDownM");
        inOutLeftM  = hardwareMap.dcMotor.get("inOutLeftM");
        inOutRightM = hardwareMap.dcMotor.get("inOutRightM");

        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        latchM.setDirection(DcMotor.Direction.FORWARD);
        //unsure which way since hardware unbuilt
        upDownM.setDirection(DcMotor.Direction.REVERSE);
        inOutLeftM.setDirection(DcMotor.Direction.FORWARD);
        inOutRightM.setDirection(DcMotor.Direction.REVERSE);

        collectorS            = hardwareMap.servo.get("CollectorS");
        collectorUpDownLeftS  = hardwareMap.servo.get("CollectorUpDownLeftS");
        collectorUpDownRightS = hardwareMap.servo.get("CollectorUpDownRightS");
        mineralDropperS       = hardwareMap.servo.get("MineralDropperS");

        waitForStart();

        while(opModeIsActive()) {

            driveFLM.setPower(-gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x);
            driveFRM.setPower(-gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x);
            driveBLM.setPower(-gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x);
            driveBRM.setPower(-gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x);

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
                inOutLeftM.setPower(0.5);
                inOutRightM.setPower(0.5);
            }else {
                if (gamepad1.left_bumper) {
                    inOutLeftM.setPower(-0.5);
                    inOutRightM.setPower(-0.5);
                }else {
                    inOutLeftM.setPower(0);
                    inOutRightM.setPower(0);
                }
            }

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

            if (gamepad2.dpad_up) {
                collectorUpDownLeftS .setPosition(collectorUpDownLeftS .getPosition()-0.1);
                collectorUpDownRightS.setPosition(collectorUpDownRightS.getPosition()+0.1);
            }else {
                if (gamepad2.dpad_down) {
                    collectorUpDownLeftS .setPosition(collectorUpDownLeftS .getPosition()+0.1);
                    collectorUpDownRightS.setPosition(collectorUpDownRightS.getPosition()-0.1);
                }else {
                    collectorUpDownLeftS .setPosition(collectorUpDownLeftS .getPosition());
                    collectorUpDownRightS.setPosition(collectorUpDownRightS.getPosition());
                }
            }

            if (gamepad2.right_trigger>0.5) {
                mineralDropperS.setPosition(0);
            }else {
                if (gamepad2.left_trigger>0.5) {
                    mineralDropperS.setPosition(0.75);
                }
            }

            if (gamepad2.right_bumper) {
                upDownM.setPower(0.5);
            }else {
                if (gamepad2.left_bumper) {
                    upDownM.setPower(-0.5);
                }else {
                    upDownM.setPower(0);
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
}
