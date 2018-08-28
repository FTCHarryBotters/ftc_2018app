package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "OmniWheelBot", group = "Sample")
public class OmniwheelBot extends LinearOpMode
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
        motorbl.setDirection(DcMotor.Direction.REVERSE);



        waitForStart();

        while(opModeIsActive())
        {
            //movingrobot
            motorfl.setPower(gamepad1.left_stick_y);
            motorfl.setPower(-gamepad1.left_stick_x);
            motorfr.setPower(gamepad1.left_stick_y);
            motorfr.setPower(gamepad1.left_stick_x);
            motorbl.setPower(gamepad1.left_stick_y);
            motorbl.setPower(gamepad1.left_stick_x);
            motorbr.setPower(gamepad1.left_stick_y);
            motorbr.setPower(-gamepad1.left_stick_x);

            //spins. probably
            motorfl.setPower(gamepad1.right_stick_x);
            motorfr.setPower(-gamepad1.right_stick_x);
            motorbl.setPower(-gamepad1.right_stick_x);
            motorfr.setPower(gamepad1.right_stick_x);









            idle();
        }
    }
}
