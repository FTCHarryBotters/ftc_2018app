package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * this is a generic autonomous program.
 * it is for copy pasting if we need to test a specific thing
 * w/o screwing up another program that already works
 */

@Autonomous(name = "GenericAutonomous", group = "Sample")
@Disabled
public class GenericAutonomous extends LinearOpMode{
    //declare

    @Override
    public void runOpMode() throws InterruptedException {
        //init

        waitForStart();
        //what runs

    }
    //methods

}
