package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "moveLatchDown", group = "Sample")
public class moveLatchDown extends LinearOpMode {

    private DcMotor latchM;

    @Override
    public void runOpMode() throws InterruptedException {

        latchM = hardwareMap.dcMotor.get("latchM");

        latchM.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        MoveLatchDown();
    }
    public void MoveLatchDown() {
        latchM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latchM.setTargetPosition(-26300);
        latchM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        latchM.setPower(1);
        while (latchM.isBusy()) {}
        latchM.setPower(0);
        latchM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latchM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
