package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "OmniWheelBotthree", group = "Sample")
public class OmniWheelBot3 extends LinearOpMode
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
        motorfl.setDirection(DcMotor.Direction.REVERSE);
        motorbr.setDirection(DcMotor.Direction.REVERSE);



        waitForStart();

        while(opModeIsActive())
        {

            motorfl.setPower(-gamepad1.left_stick_y/2);
            motorbl.setPower(-gamepad1.left_stick_y/2);
            motorfr.setPower(-gamepad1.right_stick_y/2);
            motorbr.setPower(-gamepad1.right_stick_y/2);




            idle();
        }
    }
}
