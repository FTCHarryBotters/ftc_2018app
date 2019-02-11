package org.firstinspires.ftc.teamcode.obsolete;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name = "TeleOp1", group = "Sample")
@Disabled
public class TeleOp1 extends LinearOpMode
{
    //declare motors
    //private DcMotor latchLeftM;
    //private DcMotor latchRightM;
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    //private DcMotor armUpDownM;
    //private DcMotor armExtenderM;


    //declare servos
    //private Servo latchLeftS;
    //private Servo latchRightS;
    //private Servo collectorS;
    //private Servo samplingS;
    //private Servo collectorUpDOwnS;


    @Override
    public void runOpMode() throws InterruptedException
    {

        //set motors to the configuration
        //latchLeftM = hardwareMap.dcMotor.get("latchLeftM");
        //latchRightM = hardwareMap.dcMotor.get("latchRightM");
        driveFLM = hardwareMap.dcMotor.get("driveFLM");
        driveFRM = hardwareMap.dcMotor.get("driveFRM");
        driveBLM = hardwareMap.dcMotor.get("driveBLM");
        driveBRM = hardwareMap.dcMotor.get("driveBRM");
        //armUpDownM = hardwareMap.dcMotor.get("armUpDownM");
        //armExtenderM = hardwareMap.dcMotor.get("armExtenderM");



        //reversing necessary motors
        //latchRightM.setDirection(DcMotor.Direction.REVERSE);
        driveFLM.setDirection(DcMotor.Direction.REVERSE);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.REVERSE);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);
        //driveBLM.setDirection(DcMotor.Direction.REVERSE);

        //set servos to the configuration
        //latchLeftS = hardwareMap.servo.get("latchLeftS");
        //latchRightS = hardwareMap.servo.get("latchRightS");
        //collectorS = hardwareMap.servo.get("collectorS");
        //samplingS = hardwareMap.servo.get("samplingS");
        //collectorUpDOwnS = hardwareMap.servo.get("collectorUpDownS");

        //latchLeftS.setPosition(0);
        //latchRightS.setPosition(1);

        //declare drivespeed variable. this is to change the speed that the robot moves
        double drivespeed;
        drivespeed = 2;

        //declare latchspeed variable. this is to change the speed that the latch arm moves
        double latchspeed;
        latchspeed = 4;

        waitForStart();

        while(opModeIsActive())
        {

            //lets KV change the speed of the robot
            if (gamepad1.x){
                drivespeed = 1;
            }else{
                if (gamepad1.y){
                    drivespeed = 2;
                }
            }


            //lets Michael change the latchspeed variable
            if (gamepad2.dpad_up){
                latchspeed = 1;
            }else{
                if (gamepad2.dpad_down){
                    latchspeed = 4;
                }
            }


            //lets KV drive the robot
            //Forward,Backward
            driveFLM.setPower(gamepad1.left_trigger/drivespeed);
            driveFRM.setPower(-gamepad1.left_trigger/drivespeed);
            driveBLM.setPower(gamepad1.left_trigger/drivespeed);
            driveBRM.setPower(-gamepad1.left_trigger/drivespeed);

            driveFLM.setPower(-gamepad1.right_trigger/drivespeed);
            driveFRM.setPower(gamepad1.right_trigger/drivespeed);
            driveBLM.setPower(-gamepad1.right_trigger/drivespeed);
            driveBRM.setPower(gamepad1.right_trigger/drivespeed);
            //Spin
            driveFLM.setPower(-gamepad1.right_stick_x/drivespeed);
            driveBRM.setPower(-gamepad1.right_stick_x/drivespeed);
            driveFRM.setPower(-gamepad1.right_stick_x/drivespeed);
            driveBLM.setPower(-gamepad1.right_stick_x/drivespeed);
            //Slide
            driveFLM.setPower(-gamepad1.left_stick_x/drivespeed);
            driveBRM.setPower(gamepad1.left_stick_x/drivespeed);
            driveFRM.setPower(-gamepad1.left_stick_x/drivespeed);
            driveBLM.setPower(gamepad1.left_stick_x/drivespeed);


            /*lets KV move the arm up and down
            if(gamepad1.right_bumper)
            {
                armUpDownM.setPower(0.5);
            }else{
                    if(gamepad1.left_bumper)
                    {
                        armUpDownM.setPower(-0.5);
                    }else{
                        armUpDownM.setPower(0);

                    }
                 }


            //lets Kv move the arm In and OUt.
            //power 1 is in, power -1 is out
            if (gamepad1.left_trigger > .1)
            {
                armExtenderM.setPower(0.50);
            }else{
                if (gamepad1.right_trigger > .1)
                {
                    armExtenderM.setPower(-0.50);
                }else{
                    armExtenderM.setPower(0);
                }
            }

            //Michael moves
            //the latch thing
            latchLeftM.setPower(-gamepad2.left_stick_y/latchspeed);
            latchRightM.setPower(-gamepad2.left_stick_y/latchspeed);


            //Michael moves the Latch servos
            if (gamepad2.left_trigger > .1)
            {
                latchLeftS.setPosition(0);
                latchRightS.setPosition(1);
            }else{
                    if (gamepad2.right_trigger > .1)
                    {
                        latchLeftS.setPosition(1);
                        latchRightS.setPosition(0);
                    }
                 }


            //moves the servo that makes the flaps move
            //set position 0 makes the flaps take in
            //set position 1 make flaps eject blocks
            //set position 0.50 makes flaps stop moving
            if(gamepad2.y)
            {
                collectorS.setPosition(0);
            }else{
                  if(gamepad2.a)
                  {
                    collectorS.setPosition(1);
                  }else{
                        if(gamepad2.b)
                        {
                            collectorS.setPosition(.50);
                        }
                    }
                }*/


            /*if Michael moves the right stick down,
            it moves the controller down by setting position to 0
            if he moves it up, it moves up by setting position to 1
            if he doesn't move it, it outputs 0.50 and doesn't move.*/
            //collectorUpDOwnS.setPosition(gamepad2.right_stick_y/2 +0.5);

            idle();
        }
    }
}
