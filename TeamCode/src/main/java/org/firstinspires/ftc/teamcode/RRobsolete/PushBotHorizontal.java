package org.firstinspires.ftc.teamcode.RRobsolete;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name = "Sample TeleOp", group = "Sample")
@Disabled
public class PushBotHorizontal extends LinearOpMode
{
    //Declare Motors
    private DcMotor motorleft;
    private DcMotor motorright;
    private DcMotor motorarm;
    private DcMotor armmotor = motorarm;

    //Declare Servos
    private Servo RightArmServo;
    private Servo LeftArmServo;


    @Override
    public void runOpMode() throws InterruptedException
    {

        motorleft = hardwareMap.dcMotor.get("leftdrive");
        motorright = hardwareMap.dcMotor.get("rightdrive");
        motorarm = hardwareMap.dcMotor.get("armmotor");

        motorright.setDirection(DcMotor.Direction.REVERSE);

        LeftArmServo = hardwareMap.servo.get("LeftArmServo");
        RightArmServo = hardwareMap.servo.get("RightArmServo");

        waitForStart();

        while(opModeIsActive())
        {

            motorleft.setPower(-gamepad1.left_stick_y);
            motorright.setPower(-gamepad1.right_stick_y);



            if (gamepad1.right_bumper) {
                motorarm.setPower(.25);
            }else{
                    if (gamepad1.left_bumper) {
                        motorarm.setPower(-.25);
                    }else{motorarm.setPower(0);

                 }

                 }




            if (gamepad1.b) {
                LeftArmServo.setPosition(.01);
                RightArmServo.setPosition(.98);
            }else{
                     if (gamepad1.a) {
                         LeftArmServo.setPosition(.52);
                         RightArmServo.setPosition(.39);
                     }
                 }


            idle();
        }
    }
}
