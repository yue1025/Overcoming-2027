package org.firstinspires.ftc.teamcode.subsystems.drive;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants.LocalizerConstants;
import org.firstinspires.ftc.teamcode.subsystems.Subsystem;

public class Localizer implements Subsystem {
    private final GoBildaPinpointDriver pinpoint;
    private Pose2D pose = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.RADIANS, 0);

    public Localizer(HardwareMap hw) {
        pinpoint = hw.get(GoBildaPinpointDriver.class, LocalizerConstants.pinpointName);
        pinpoint.setOffsets(LocalizerConstants.xOffsetMm, LocalizerConstants.yOffsetMm, DistanceUnit.MM);
        pinpoint.setEncoderResolution(LocalizerConstants.podType);
        pinpoint.setEncoderDirections(LocalizerConstants.xDirection, LocalizerConstants.yDirection);
        pinpoint.setYawScalar(LocalizerConstants.yawScalar);
        pinpoint.resetPosAndIMU();
    }
    @Override
    public void update() {
        pinpoint.update();
        pose = pinpoint.getPosition();
    }

    public Pose2D getPose() {
        return pose;
    }

    public double getXInch() {
        return pose.getX(DistanceUnit.INCH);
    }

    public double getYInch() {
        return pose.getY(DistanceUnit.INCH);
    }

    public double getHeadingRad() {
        return pose.getHeading(AngleUnit.RADIANS);
    }

    public void setPose(Pose2D newPose) {
        pinpoint.setPosition(newPose);
        pose = newPose;
    }

    public void resetPose() {
        setPose(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.RADIANS, 0));
    }

    public double getSpeedInchPerSec() {
        return Math.hypot(pinpoint.getVelX(DistanceUnit.INCH), pinpoint.getVelY(DistanceUnit.INCH));
    }

    public boolean isReady() {
        return pinpoint.getDeviceStatus() == GoBildaPinpointDriver.DeviceStatus.READY;
    }

    public int getRawEncoderX() {
        return pinpoint.getEncoderX();
    }

    public int getRawEncoderY() {
        return pinpoint.getEncoderY();
    }

    @Override
    public void addTelemetry(Telemetry t) {
        t.addData("Pinpoint", pinpoint.getDeviceStatus());
        t.addData("X / Y (in)", "%.2f / %.2f", getXInch(), getYInch());
        t.addData("Heading (deg)", "%.1f", Math.toDegrees(getHeadingRad()));
    }
}