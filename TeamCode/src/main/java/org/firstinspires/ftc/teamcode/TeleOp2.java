package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp2", group = "Sample")
public class TeleOp2 extends LinearOpMode
{
    //declare motors
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;

    //Declare drive speed statics
    private static double FULLDRIVESPEED = 1;
    private static double HALFDRIVESPEED = 0.5;

    //declare servos
    //private Servo markerS;

    @Override
    public void runOpMode() throws InterruptedException {

        //set motors to the configuration
        driveFLM = hardwareMap.dcMotor.get("driveFLM");
        driveFRM = hardwareMap.dcMotor.get("driveFRM");
        driveBLM = hardwareMap.dcMotor.get("driveBLM");
        driveBRM = hardwareMap.dcMotor.get("driveBRM");

        //reversing necessary motors
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);

        //set servos to the configuration
        //markerS = hardwareMap.servo.get("markerS");

        //declare drivespeed variable. this is to change the speed that the robot moves
        double drivespeed;
        drivespeed = HALFDRIVESPEED;

        //markerS.setPosition(0.25);

        waitForStart();

        while(opModeIsActive())
        {
            //lets KV change the speed of the robot
            if (gamepad1.x){
                drivespeed = FULLDRIVESPEED;
            }else{
                if (gamepad1.y){
                    drivespeed = HALFDRIVESPEED;
                }
            }

            /*if(gamepad1.x)
            {
                markerS.setPosition(0.50);
            }*/

            driveFLM.setPower(-gamepad1.left_stick_y*drivespeed);
            driveFRM.setPower(-gamepad1.right_stick_y*drivespeed);
            driveBLM.setPower(-gamepad1.left_stick_y*drivespeed);
            driveBRM.setPower(-gamepad1.right_stick_y*drivespeed);

            moveLeft(gamepad1.left_trigger*drivespeed);
            moveRight(gamepad1.right_trigger*drivespeed);

            idle();
        }
    }
    public void driveForward(double power)
    {
        //this function is to move forward.
        //all motors move forward
        telemetry.addData("f", HALFDRIVESPEED);
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);
    }
    public void driveBackward(double power)
    {
        //this function is to move backward
        //all motors move back
        driveForward(-power);
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
        spinLeft(-power);
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
        moveLeft(-power);
    }
    public void stopMoving()
    {
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);
    }
    public void initializeMotors(DcMotor driveFLM, DcMotor driveFRM, DcMotor driveBLM, DcMotor driveBRM)
    {
        this.driveFLM = driveFLM;
        this.driveFRM = driveFRM;
        this.driveBLM = driveBLM;
        this.driveBRM = driveBRM;
    }
}
