package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.lib.Alliance;

public class RobotState {
    public static Alliance alliance = Alliance.RED;
    public static int startIndex = 0;
    public static Pose2D lastPose = null;
    private static long lastPoseTimeMs = 0;

    public static void savePose(Pose2D pose) {
        lastPose = pose;
        lastPoseTimeMs = System.currentTimeMillis();
    }

    public static boolean hasPose() {
        return lastPose != null;
    }

    public static double secondsSinceSave() {
        return (System.currentTimeMillis() - lastPoseTimeMs) / 1000.0;
    }
}
