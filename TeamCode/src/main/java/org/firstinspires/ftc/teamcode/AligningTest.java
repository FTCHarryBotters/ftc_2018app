package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "GenericAutonomous", group = "Sample")
public class AligningTest extends LinearOpMode{
    //declare

    private double DistFore;
    private double DistBack;
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;

    private DistanceSensor DistanceForeS;
    private OpticalDistanceSensor DistanceBackS;

    @Override
    public void runOpMode() throws InterruptedException {
        //init
        driveFLM   = hardwareMap.dcMotor.get("driveFLM");
        driveFRM   = hardwareMap.dcMotor.get("driveFRM");
        driveBLM   = hardwareMap.dcMotor.get("driveBLM");
        driveBRM   = hardwareMap.dcMotor.get("driveBRM");

        DistanceBackS = hardwareMap.get(OpticalDistanceSensor.class, "MRDistSensor");
        DistanceForeS = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

        waitForStart();
        //what runs
        DistFore = DistanceForeS.getDistance(DistanceUnit.CM);
        DistBack = (((Math.pow(DistanceBackS.getRawLightDetected(), -0.5))*56)+0.75);
        while (DistFore>10 && DistBack>10) {
            DistFore = DistanceForeS.getDistance(DistanceUnit.CM);
            DistBack = (((Math.pow(DistanceBackS.getRawLightDetected(), -0.5))*56)+0.75);
            if (DistFore>10) {
                driveFLM.setPower(-0.3);
                driveFRM.setPower(+0.3);
            }
            if (DistBack>10) {
                driveBLM.setPower(+0.5);
                driveBRM.setPower(-0.5);
            }
        }
    }
    //methods

}
