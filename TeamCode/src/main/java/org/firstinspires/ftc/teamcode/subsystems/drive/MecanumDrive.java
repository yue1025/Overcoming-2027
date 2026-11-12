package org.firstinspires.ftc.teamcode.subsystems.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Subsystem;

public class MecanumDrive implements Subsystem {
    private final DcMotorEx frontLeft, backLeft, frontRight, backRight;
    private double flPower, blPower, frPower, brPower;
    private double speedMultiplier = 1.0;
    public MecanumDrive(HardwareMap hw) {
        frontLeft = hw.get(DcMotorEx.class, Constants.DriveConstants.frontLeft);
        backLeft = hw.get(DcMotorEx.class, Constants.DriveConstants.backLeft);
        frontRight = hw.get(DcMotorEx.class, Constants.DriveConstants.frontRight);
        backRight = hw.get(DcMotorEx.class, Constants.DriveConstants.backRight);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        for (DcMotorEx m : new DcMotorEx[]{frontLeft, backLeft, frontRight, backRight}) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void drive(double forward, double strafe, double turn) {
        strafe *= Constants.DriveConstants.strafeCorrection;
        double fl = forward + strafe + turn;
        double bl = forward - strafe + turn;
        double fr = forward - strafe - turn;
        double br = forward + strafe - turn;

        double max = Math.max(1.0, Math.max(Math.max(Math.abs(fl), Math.abs(bl)),
                Math.max(Math.abs(fr), Math.abs(br))));

        flPower = fl / max * speedMultiplier;
        blPower = bl / max * speedMultiplier;
        frPower = fr / max * speedMultiplier;
        brPower = br / max * speedMultiplier;
    }

    public void driveFieldCentric(double x, double y, double turn, double headingRad) {
        double strafe  = x * Math.cos(-headingRad) - y * Math.sin(-headingRad);
        double forward = x * Math.sin(-headingRad) + y * Math.cos(-headingRad);
        drive(forward, strafe, turn);
    }

    public void setSpeedMultiplier(double m) {
        speedMultiplier = m;
    }

    @Override
    public void update() {
        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(blPower);
        backRight.setPower(brPower);
    }

    @Override
    public void addTelemetry(Telemetry t) {
        t.addData("FL / FR", "%.2f / %.2f", flPower, frPower);
        t.addData("BL / BR", "%.2f / %.2f", blPower, brPower);
    }
}
