package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public enum Alliance {
    RED, BLUE;

    public Pose2D fromRed(Pose2D redPose) {
        if (this == RED) {
            return redPose;
        }
        return new Pose2D(DistanceUnit.INCH,
                -redPose.getX(DistanceUnit.INCH),
                -redPose.getY(DistanceUnit.INCH),
                AngleUnit.RADIANS,
                AngleUnit.normalizeRadians(redPose.getHeading(AngleUnit.RADIANS) + Math.PI));
    }

    public double driverForwardRad() {
        return this == RED ? Math.PI / 2 : -Math.PI / 2;
    }
}
