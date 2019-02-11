package org.firstinspires.ftc.teamcode.obsolete;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name = "OmniWheelBot2", group = "Sample")
@Disabled
public class OmniWheelBot2 extends LinearOpMode
{
    //Declare Motors
    private DcMotor motorfl;
    private DcMotor motorfr;
    private DcMotor motorbl;
    private DcMotor motorbr;


    @Override
    public void runOpMode() throws InterruptedException
    {

        //motorright = hardwareMap.dcMotor.get("leftdrive");
        motorfl = hardwareMap.dcMotor.get("motorfl");
        motorfr = hardwareMap.dcMotor.get("motorfr");
        motorbl = hardwareMap.dcMotor.get("motorbl");
        motorbr = hardwareMap.dcMotor.get("motorbr");

        //reverse some motors
        motorfr.setDirection(DcMotor.Direction.REVERSE);
        motorbr.setDirection(DcMotor.Direction.REVERSE);



        waitForStart();

        while(opModeIsActive())
        {

            if(gamepad1.right_bumper)
            {
                motorfl.setPower(-gamepad1.left_stick_y);
                motorbr.setPower(-gamepad1.left_stick_y);
                motorfr.setPower(-gamepad1.right_stick_y);
                motorbl.setPower(-gamepad1.right_stick_y);
            }
            else {
                if(gamepad1.left_bumper){
                    motorfl.setPower(-gamepad1.left_stick_y);
                    motorbr.setPower(gamepad1.left_stick_y);
                    motorfr.setPower(gamepad1.left_stick_y);
                    motorbl.setPower(-gamepad1.left_stick_y);
                }
                else{
                    motorfl.setPower(0);
                    motorbr.setPower(0);
                    motorfr.setPower(0);
                    motorbl.setPower(0);
                }
            }









            idle();
        }
    }
}
