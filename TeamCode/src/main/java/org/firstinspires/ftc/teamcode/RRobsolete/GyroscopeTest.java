package org.firstinspires.ftc.teamcode.RRobsolete;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

@Autonomous(name = "GyroscopeTest", group = "Sample")
@Disabled
public class GyroscopeTest extends LinearOpMode{
    //declaration
    private DcMotor driveFLM;
    private DcMotor driveFRM;
    private DcMotor driveBLM;
    private DcMotor driveBRM;

    private BNO055IMU GyroS;
    private Orientation angles;
    private Acceleration gravity;

    private float correctedAngle;
    private float yawcorrection;
    private boolean isGyro = false;

    public void runOpMode() throws InterruptedException, NullPointerException {
        //configuration
        driveFLM = hardwareMap.dcMotor.get("driveFLM");
        driveFRM = hardwareMap.dcMotor.get("driveFRM");
        driveBLM = hardwareMap.dcMotor.get("driveBLM");
        driveBRM = hardwareMap.dcMotor.get("driveBRM");
        driveFLM.setDirection(DcMotor.Direction.FORWARD);
        driveFRM.setDirection(DcMotor.Direction.REVERSE);
        driveBLM.setDirection(DcMotor.Direction.FORWARD);
        driveBRM.setDirection(DcMotor.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        GyroS = hardwareMap.get(BNO055IMU.class, "GyroS");
        GyroS.initialize(parameters);

        composeTelemetry();

        angles = GyroS.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity = GyroS.getGravity();

        waitForStart();

            GyroS.startAccelerationIntegration(new Position(), new Velocity(), 1000);

            telemetry.addData("beginning Angle", angles.firstAngle);
            telemetry.update();
            Thread.sleep(1500);
            spinLeftG(0.2, 90);
            Thread.sleep(2000);
            resetGyro();
            telemetry.addData("beginning Angle", angles.firstAngle);
            telemetry.update();
            spinLeftG(0.15, 90);

    }
    //methods
    private void resetGyro()
    {
        yawcorrection = angles.firstAngle;
        correctedAngle = angles.firstAngle - yawcorrection;
        if(correctedAngle <= -180){
            correctedAngle = correctedAngle + 360;
        }
        if (correctedAngle > 180){
            correctedAngle = correctedAngle - 360;
        }
    }
    private void getCorrectedAngle()
    {
        correctedAngle = angles.firstAngle - yawcorrection;
        if(correctedAngle <= -180){
            correctedAngle = correctedAngle + 360;
        }
        if (correctedAngle > 180){
            correctedAngle = correctedAngle - 360;
        }
    }
    private void stopmotors(){
        driveFLM.setPower(0);
        driveFRM.setPower(0);
        driveBLM.setPower(0);
        driveBRM.setPower(0);
    }
    private void setZPB()
    {
        driveFLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveFRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBLM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveBRM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void spinLeft(double power)
    {
        //spins the robot left
        //the left side moves backward &
        //the right side motors move forward
        driveFLM.setPower(-power);
        driveFRM.setPower(power);
        driveBLM.setPower(-power);
        driveBRM.setPower(power);
    }
    public void spinRight(double power)
    {
        //spins the robot right
        //the right side moves backward &
        //the left side motors move forward
        spinLeft(-power);
    }
    private void spinLeftG(double power, double yaw) throws NullPointerException, InterruptedException
    {
        isGyro = false;
        telemetry.update();
        while (!isGyro) {
            telemetry.update();
            getCorrectedAngle();
            if (correctedAngle < yaw*10/11) {
                spinLeft(power);
                isGyro = false;
            }else{
                isGyro = true;
            }
        }
        stopmotors();
        telemetry.addData("angle", angles.firstAngle);
        telemetry.update();
    }
    private void spinRightG(double power, int yaw)throws NullPointerException
    {
        isGyro = false;
        telemetry.update();
        while (!isGyro) {
            telemetry.update();
            getCorrectedAngle();
            if (correctedAngle > -yaw) {
                spinLeft(power);
                isGyro = false;
            }else{
                isGyro = true;
            }
        }
        stopmotors();
        telemetry.addData("angle", angles.firstAngle);
        telemetry.update();
    }
    private void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles = GyroS.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity = GyroS.getGravity();
        }
        });

        /*
        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return GyroS.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return GyroS.getCalibrationStatus().toString();
                    }
                });
                */

        telemetry.addLine()
                .addData("YAW", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                });
                /*.addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });
                */

        /*telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
                */
    }
    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
