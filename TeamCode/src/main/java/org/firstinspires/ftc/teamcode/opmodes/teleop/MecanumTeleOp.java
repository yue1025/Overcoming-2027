package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot;
@TeleOp(name = "Mecanum TeleOp", group = "Drive")
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);

        boolean fieldCentric = true;
        double driverForwardRad = 0;

        while (opModeInInit()) {
            robot.localizer.update();
            telemetry.addLine("初始化中不要動機器人，等 Pinpoint READY 再按 START");
            robot.localizer.addTelemetry(telemetry);
            telemetry.update();
        }

        while (opModeIsActive()) {
            robot.readSensors();

            if (gamepad1.dpadUpWasPressed()) {
                driverForwardRad = robot.localizer.getHeadingRad();
            }

            if (gamepad1.dpadDownWasPressed()) {
                fieldCentric = !fieldCentric;
            }

            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn  = gamepad1.right_stick_x;

            robot.drive.setSpeedMultiplier(
                    (gamepad1.right_bumper ? Constants.DriveConstants.slowMultiplier : 1.0));

            if (fieldCentric) {
                double heading = robot.localizer.getHeadingRad() - driverForwardRad;
                robot.drive.driveFieldCentric(x, y, turn, heading);
            } else {
                robot.drive.drive(y, x, turn);
            }

            robot.writeOutputs();

            telemetry.addData("模式",
                    fieldCentric ? "Field-centric（dpad↓ 切換）" : "Robot-centric（dpad↓ 切換）");
            telemetry.addLine("dpad↑ = 把車頭現在的方向設成前方");
            robot.addTelemetry(telemetry);
            telemetry.update();
        }
    }
}
