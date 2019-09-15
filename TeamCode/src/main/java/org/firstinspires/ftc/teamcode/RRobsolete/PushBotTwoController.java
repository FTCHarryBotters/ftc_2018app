package org.firstinspires.ftc.teamcode.RRobsolete;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name = "PushBotTwoControllers", group = "Sample")
@Disabled
public class PushBotTwoController extends LinearOpMode
{
    //Declare Motors
    private DcMotor motorleft;
    private DcMotor motorright;
    private DcMotor motorarm;

    //Declare Servos
    private Servo RightArmServo;
    private Servo LeftArmServo;


    @Override
    public void runOpMode() throws InterruptedException
    {

        motorleft = hardwareMap.dcMotor.get("rightdrive");
        motorright = hardwareMap.dcMotor.get("leftdrive");
        motorarm = hardwareMap.dcMotor.get("armmotor");

        motorright.setDirection(DcMotor.Direction.REVERSE);

        LeftArmServo = hardwareMap.servo.get("LeftArmServo");
        RightArmServo = hardwareMap.servo.get("RightArmServo");

        waitForStart();

        while(opModeIsActive())
        {

            motorleft.setPower(-gamepad1.left_stick_y/2);
            motorright.setPower(-gamepad1.right_stick_y/2);





            if (gamepad2.left_bumper) {
                motorarm.setPower(-.25);
            }else{
                    if (gamepad2.right_bumper) {
                        motorarm.setPower(.25);
                    }else{motorarm.setPower(0);

                 }

                 }




            if (gamepad2.b) {
                LeftArmServo.setPosition(.52);
                RightArmServo.setPosition(.39);
            }else{
                     if (gamepad2.a) {
                         LeftArmServo.setPosition(.8);
                         RightArmServo.setPosition(.17);
                     }
                 }


            idle();
        }
    }
}
