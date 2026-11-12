package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

public class Constants {
    public static final class DriveConstants {
        public static final String frontLeft  = "fl";
        public static final String backLeft    = "bl";
        public static final String frontRight  = "fr";
        public static final String backRight  = "br";
        public static final double strafeCorrection = 1.1;
        public static final double slowMultiplier = 0.4;
    }

    public static final class LocalizerConstants {
        public static final String pinpointName = "pinpoint";
        // X pod（量前後）：偏左為正
        public static final double xOffsetMm = -61.5;
        // Y pod（量左右）：偏前為正
        public static final double yOffsetMm = 57;
        public static final GoBildaPinpointDriver.GoBildaOdometryPods podType =
                GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        public static final GoBildaPinpointDriver.EncoderDirection xDirection =
                GoBildaPinpointDriver.EncoderDirection.REVERSED;
        public static final GoBildaPinpointDriver.EncoderDirection yDirection =
                GoBildaPinpointDriver.EncoderDirection.FORWARD;
        // 正反轉各 10 圈都多量約 7.9°，實測 3607.9 / 3600 ≈ 1.0022
        public static final double yawScalar = 1.0022;
    }
}