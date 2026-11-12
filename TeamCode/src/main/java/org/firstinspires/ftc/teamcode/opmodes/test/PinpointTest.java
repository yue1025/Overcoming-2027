package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.drive.Localizer;

@TeleOp(name = "PinpointTest", group = "Test")
public class PinpointTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        Localizer localizer = new Localizer(hardwareMap);

        while (opModeInInit()) {
            localizer.update();
            telemetry.addLine("初始化中不要動機器人，等 READY 再按 START");
            localizer.addTelemetry(telemetry);
            telemetry.update();
        }

        while (opModeIsActive()) {
            if (gamepad1.aWasPressed()) {
                localizer.resetPose();
            }

            localizer.update();

            telemetry.addLine("A = 座標歸零");
            localizer.addTelemetry(telemetry);
            telemetry.addData("Raw encoder X / Y", "%d / %d",
                    localizer.getRawEncoderX(), localizer.getRawEncoderY());
            telemetry.addData("Speed (in/s)", "%.1f", localizer.getSpeedInchPerSec());
            telemetry.update();
        }
    }
}
