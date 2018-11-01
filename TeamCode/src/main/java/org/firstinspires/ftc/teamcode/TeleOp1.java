package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp1", group = "Sample")
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
    //private DcMotor collectorM;

    //declare servos
    //private Servo latchLeftS;
    //private Servo latchRightS;
    private Servo samplingS;
    //private Servo collectorUpDOwnS;
    //private Servo armExtenderS;


    @Override
    public void runOpMode() throws InterruptedException
    {

        //declare motors
        //latchLeftM = hardwareMap.dcMotor.get("latchLeftM");
        //latchRightM = hardwareMap.dcMotor.get("latchRightM");
        driveFLM = hardwareMap.dcMotor.get("driveFLM");
        driveFRM = hardwareMap.dcMotor.get("driveFRM");
        driveBLM = hardwareMap.dcMotor.get("driveBLM");
        driveBRM = hardwareMap.dcMotor.get("driveBRM");
        //armUpDownM = hardwareMap.dcMotor.get("armUpDownM");
        //collectorM = hardwareMap.dcMotor.get("collectorM");


        //reversing necessary motors
        //latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        driveFLM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.REVERSE);

        //declare servos
        //latchLeftS = hardwareMap.servo.get("latchLeftS");
        //latchRightS = hardwareMap.servo.get("latchRightS");
        samplingS = hardwareMap.servo.get("samplingS");
        //collectorUpDOwnS = hardwareMap.servo.get("collectorUpDOwnS");
        //armExtenderS = hardwareMap.servo.get("armExtenderS");

        //latchLeftS.setPosition(0);
        //latchRightS.setPosition(1);

        //declare drivespeed variable. this is to change the speed that the robot moves
        double drivespeed;
        drivespeed = 2;

        //declare latchspeed variable. this is to change the speed that the latch arm moves
        //double latchspeed;
        //latchspeed = 4;

        waitForStart();

        while(opModeIsActive())
        {


            //lets the user change the latchspeed variable
            /*if (gamepad2.x){
                latchspeed = 1.33;
            }else{
                if (gamepad2.y){
                    latchspeed = 4;
                }
            }*/


            //lets the user change the speed of the robot
            if (gamepad1.x){
                drivespeed = 1;
            }else{
                if (gamepad1.y){
                    drivespeed = 2;
                }
            }



            driveFLM.setPower(-gamepad1.left_stick_y/drivespeed);
            driveFRM.setPower(-gamepad1.right_stick_y/drivespeed);
            driveBLM.setPower(-gamepad1.left_stick_y/drivespeed);
            driveBRM.setPower(-gamepad1.right_stick_y/drivespeed);

            //latchLeftM.setPower(-gamepad2.left_stick_y/latchspeed);
            //latchRightM.setPower(-gamepad2.left_stick_y/latchspeed);

            /*if (gamepad2.b) {
                latchLeftS.setPosition(0);
                latchRightS.setPosition(1);
            }else{
                if (gamepad2.a) {
                    latchLeftS.setPosition(1);
                    latchRightS.setPosition(0);
                }
            }

            /*if(gamepad2.dpad_down){
                collectorM.setPower(-1);
            }else{
                if(gamepad2.dpad_up){
                    collectorM.setPower(1);
                }else{
                    if(gamepad2.dpad_left){
                        collectorM.setPower(0);
                    }
                }
            }*/




            idle();
        }
    }
}
