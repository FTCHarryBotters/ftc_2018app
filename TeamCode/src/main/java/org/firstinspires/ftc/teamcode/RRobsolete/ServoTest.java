package org.firstinspires.ftc.teamcode.RRobsolete;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name = "TestServo", group = "Sample")
@Disabled
public class ServoTest extends LinearOpMode
{


    //Declare Servos
    private Servo RightArmServo;
    private Servo LeftArmServo;


    @Override
    public void runOpMode() throws InterruptedException
    {


        LeftArmServo = hardwareMap.servo.get("LeftArmServo");
        RightArmServo = hardwareMap.servo.get("RightArmServo");

        waitForStart();

        while(opModeIsActive())
        {






            if (gamepad1.b) {
                LeftArmServo.setPosition(.52);
                RightArmServo.setPosition(.39);
            }else{
                     if (gamepad1.a) {
                         LeftArmServo.setPosition(.8);
                         RightArmServo.setPosition(.17);
                     }
                 }


            idle();
        }
    }
}
