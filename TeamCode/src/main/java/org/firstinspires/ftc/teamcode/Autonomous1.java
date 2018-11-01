package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

@Autonomous(name = "Autonomous1", group = "Sample")
public class Autonomous1 extends LinearOpMode
{
    private YellowVision yellowVision;

    //declare motors

    private DcMotor latchLeftM;
    private DcMotor latchRightM;
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor armUpDownM;
    private DcMotor collectorM;

    //declare servos
    private Servo latchLeftS;
    private Servo latchRightS;
    private Servo samplingS;
    private Servo collectorUpDOwnS;
    private Servo armExtenderS;

    @Override
    public void runOpMode() throws InterruptedException
    {

        //declare motors
        //latchLeftM = hardwareMap.dcMotor.get("latchLeftM");
        //latchRightM = hardwareMap.dcMotor.get("latchRightM");
        /*driveFLM = hardwareMap.dcMotor.get("driveFLM");
        driveFRM = hardwareMap.dcMotor.get("driveFRM");
        driveBLM = hardwareMap.dcMotor.get("driveBLM");
        driveBRM = hardwareMap.dcMotor.get("driveBRM");*/
        //armUpDownM = hardwareMap.dcMotor.get("armUpDownM");
        //collectorM = hardwareMap.dcMotor.get("collectorM");
        yellowVision = new YellowVision();
        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        yellowVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        yellowVision.setShowCountours(true);

        // start the vision system
        yellowVision.enable();

        //reversing necessary motors
        //latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        //driveFLM.setDirection(DcMotor.Direction.REVERSE);
        //driveBLM.setDirection(DcMotor.Direction.REVERSE);

        //declare servos
        //latchLeftS = hardwareMap.servo.get("latchLeftS");
        //latchRightS = hardwareMap.servo.get("latchRightS");
        //samplingS = hardwareMap.servo.get("samplingarm");
        //collectorUpDOwnS = hardwareMap.servo.get("collectorUpDOwnS");
        //armExtenderS = hardwareMap.servo.get("armExtenderS");

        waitForStart();

            //works great!
            //Delatch();

            //samplingS.setPosition(1);
            List<MatOfPoint> contours = yellowVision.getContours();

            int i = 0;
            while (i < contours.size())
            {
                i++;
            }
            telemetry.addData("number of Countours i:", i);
            telemetry.update();
            Thread.sleep(5000);



            //aaaaaaaaaa i hate programming
            //unhook thing from latch
            //latchMotor(0,1500);

            //spinLeft(.25, 1000);
            //spinLeft(0,100);
            //change values eventually
            //driveBLM.setPower(0.5);
            //Thread.sleep(1000);
            //change values eventually
            //driveForward(.5, 500);
            //spinLeft(.5, 1000);

            //insert gold sampling here

            //change values eventually
            //driveForward(.25, 3000);
            //change values eventually
            //spinLeft(.25, 500);
            //change values eventually
            //driveForward(.25, 2000);

            //insert token dropping here

            //change values eventually
            //driveForward(-0.5, 4500);
            yellowVision.disable();




    }
    public void driveForward(double power, long time) throws InterruptedException
    {
        driveFLM.setPower(power);
        driveFRM.setPower(power);
        driveBLM.setPower(power);
        driveBRM.setPower(power);
        Thread.sleep(time);
    }

    public void latchMotor(double power, long time) throws InterruptedException
    {
        latchLeftM.setPower(power);
        latchRightM.setPower(power);
        Thread.sleep(time);
    }
    public void spinLeft(double power, long time) throws InterruptedException
    {
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
        Thread.sleep(time);
    }
    public void spinRight(double power, long time) throws InterruptedException
    {
        spinLeft(-power, time);
    }
    public void Delatch() throws InterruptedException
    {
        latchLeftM.setPower(-.75);
        latchRightM.setPower(-.75);
        latchLeftS.setPosition(0);
        latchRightS.setPosition(1);
        Thread.sleep(500);
        latchMotor(.25, 100);

    }

}
