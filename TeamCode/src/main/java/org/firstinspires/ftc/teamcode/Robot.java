package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.drive.Localizer;
import org.firstinspires.ftc.teamcode.subsystems.drive.MecanumDrive;

import java.util.List;

/** 這個是對應 FRC 的 RobotContainer：建立所有子系統，TeleOp 和 Auto 共用。 */
public class Robot {
    public final Localizer localizer;
    public final MecanumDrive drive;
    private final List<LynxModule> hubs;
    private final Subsystem[] subsystems;

    public Robot(HardwareMap hw) {
        hubs = hw.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        localizer = new Localizer(hw);
        drive = new MecanumDrive(hw);

        subsystems = new Subsystem[]{localizer, drive};
    }

    public void readSensors() {
        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }
        localizer.update();
        if (!localizer.hasPendingPose()) {
            RobotState.savePose(localizer.getPose());
        }
    }

    public void writeOutputs() {
        drive.update();
    }

    public void addTelemetry(Telemetry t) {
        for (Subsystem s : subsystems) {
            s.addTelemetry(t);
        }
    }
}
