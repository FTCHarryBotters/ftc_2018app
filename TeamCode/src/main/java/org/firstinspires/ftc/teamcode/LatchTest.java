package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


@TeleOp(name = "LatchTest", group = "Sample")
@Disabled
public class LatchTest extends LinearOpMode
{
    //Declare Motors
    private DcMotor LatchMotorLeft;
    private DcMotor LatchMotorRight;


    //Declare Servos
    private Servo clawServoLeft;
    private Servo clawServoRight;


    @Override
    public void runOpMode() throws InterruptedException
    {

        LatchMotorLeft = hardwareMap.dcMotor.get("left");
        LatchMotorRight = hardwareMap.dcMotor.get("right");

        LatchMotorRight.setDirection(DcMotor.Direction.REVERSE);

        clawServoLeft = hardwareMap.servo.get("clawServoLeft");
        clawServoRight = hardwareMap.servo.get("clawServoRight");

        waitForStart();

        while(opModeIsActive())
        {

            LatchMotorRight.setPower(-gamepad1.right_stick_y);
            LatchMotorLeft.setPower(-gamepad1.right_stick_y);

            if (gamepad1.b) {
                clawServoLeft.setPosition(1);
                clawServoRight.setPosition(0);
            }else{
                if (gamepad1.a){
                    clawServoLeft.setPosition(.6);
                    clawServoRight.setPosition(.4);
                }
            }







            idle();
        }
    }
}

