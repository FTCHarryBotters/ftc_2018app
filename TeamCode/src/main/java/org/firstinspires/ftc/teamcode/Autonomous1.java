package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

@Autonomous(name = "Autonomous1", group = "Sample")
public class Autonomous1 extends LinearOpMode
{
    private YellowVision yellowVision;
    private TeleOp2 TeleOp2;
    int i = 0;

    //declare motors

    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;
    private DcMotor latchLeftM;
    private DcMotor latchRightM;
    /*private DcMotor armUpDownM;
    private DcMotor collectorM;
    */

    //declare servos
    private Servo latchLeftS;
    private Servo latchRightS;
    private Servo samplingS;
    /*private Servo collectorUpDOwnS;
    private Servo armExtenderS;
    */

    @Override
    public void runOpMode() throws InterruptedException
    {

        //declare motors
        driveFLM = hardwareMap.dcMotor.get("driveFLM");
        driveFRM = hardwareMap.dcMotor.get("driveFRM");
        driveBLM = hardwareMap.dcMotor.get("driveBLM");
        driveBRM = hardwareMap.dcMotor.get("driveBRM");
        latchLeftM = hardwareMap.dcMotor.get("latchLeftM");
        latchRightM = hardwareMap.dcMotor.get("latchRightM");
        //armUpDownM = hardwareMap.dcMotor.get("armUpDownM");
        //collectorM = hardwareMap.dcMotor.get("collectorM");


        yellowVision = new YellowVision();
        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        yellowVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        yellowVision.setShowCountours(true);

        // start the vision system
        yellowVision.enable();

        //reversing necessary motors
        latchLeftM.setDirection(DcMotor.Direction.REVERSE);
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.REVERSE);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);


        //declare servos
        latchLeftS = hardwareMap.servo.get("latchLeftS");
        latchRightS = hardwareMap.servo.get("latchRightS");
        samplingS = hardwareMap.servo.get("samplingS");
        //collectorUpDOwnS = hardwareMap.servo.get("collectorUpDOwnS");
        //armExtenderS = hardwareMap.servo.get("armExtenderS");

        waitForStart();

            //works great!
            Delatch();

            /*List<MatOfPoint> contours = yellowVision.getContours();
            int i = 0;
            while (i < contours.size())
            {
                i++;
            }
            telemetry.addData("number of Countours i:", i);
            telemetry.update();
            Thread.sleep(100000);*/
            //aaaaaaaaaaaaaaaaaa i hate programming

            moveRight(0.50, 250);
            driveForward(0.50, 500);
            spinRight(0.50, 1000);
            moveLeft(0.50, 100);
            samplingS.setPosition(1);
            EnderCVContoursTest();
            if(i >= 1)
            {
                moveRight(0.50, 500);
            }
            samplingS.setPosition(0);
            driveForward(0.50, 1000);





            yellowVision.disable();

    }
    public void driveForward(double power, long time) throws InterruptedException
    {
        TeleOp2.DriveForward(power);
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
        TeleOp2.SpinLeft(power);
        Thread.sleep(time);
    }
    public void spinRight(double power, long time) throws InterruptedException
    {
        TeleOp2.SpinRight(power);
        Thread.sleep(time);
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
    public void moveLeft(double power, long time) throws InterruptedException
    {
        TeleOp2.MoveLeft(power);
        Thread.sleep(time);
    }
    public void moveRight(double power, long time) throws InterruptedException
    {
        TeleOp2.MoveRight(power);
        Thread.sleep(time);
    }
    public void EnderCVContoursTest()
    {
        List<MatOfPoint> contours = yellowVision.getContours();
        i = 0;
        while (i < contours.size())
        {
            i++;
        }
        telemetry.addData("number of Countours i:", i);
        telemetry.update();
    }
}
