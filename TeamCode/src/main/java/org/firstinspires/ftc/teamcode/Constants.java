package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Constants {
    public static final class FieldConstants {
        public static final String[] startNames = {
                "牆中央",
                "牆靠觀眾",
        };
        public static final Pose2D[] redStartPoses = {
                new Pose2D(DistanceUnit.INCH, 0, -63, AngleUnit.DEGREES, 90),
                new Pose2D(DistanceUnit.INCH, 48, -63, AngleUnit.DEGREES, 90),
        };
    }

    public static final class DriveConstants {
        public static final String frontLeft  = "fl";
        public static final String backLeft    = "bl";
        public static final String frontRight  = "fr";
        public static final String backRight  = "br";
        public static final double strafeCorrection = 1.1;
        public static final double slowMultiplier = 0.4;

        public static final double turnDeadband = 0.05;
        public static final double headingHoldDelaySec = 0.25;
        public static final double headingKpPerDeg = 0.02;
        public static final double headingHoldMaxTurn = 0.5;
    }

    public static final class LocalizerConstants {
        public static final String pinpointName = "pinpoint";
        public static final double xOffsetMm = -61.5;
        public static final double yOffsetMm = 57;
        public static final GoBildaPinpointDriver.GoBildaOdometryPods podType =
                GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        public static final GoBildaPinpointDriver.EncoderDirection xDirection =
                GoBildaPinpointDriver.EncoderDirection.REVERSED;
        public static final GoBildaPinpointDriver.EncoderDirection yDirection =
                GoBildaPinpointDriver.EncoderDirection.FORWARD;
        public static final double yawScalar = 1.0022;
    }
}