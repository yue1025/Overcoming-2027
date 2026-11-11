package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.drive.MecanumDrive;
@TeleOp(name = "Mecanum TeleOp", group = "Drive")
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            drive.setSpeedMultiplier(
                    gamepad1.right_bumper ? Constants.DriveConstants.slowMultiplier : 1.0);
            drive.drive(-gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x);
            drive.update();
            drive.addTelemetry(telemetry);
            telemetry.update();
        }
    }
}
