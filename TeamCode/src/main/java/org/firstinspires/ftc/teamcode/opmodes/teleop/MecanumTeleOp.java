package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Constants.DriveConstants;
import org.firstinspires.ftc.teamcode.Robot;
@TeleOp(name = "Mecanum TeleOp", group = "Drive")
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);

        boolean fieldCentric = true;
        double driverForwardRad = 0;

        double holdTargetRad = 0;
        boolean holding = false;
        ElapsedTime sinceTurn = new ElapsedTime();

        while (opModeInInit()) {
            robot.localizer.update();
            telemetry.addLine("初始化中不要動機器人，等 Pinpoint READY 再按 START");
            robot.localizer.addTelemetry(telemetry);
            telemetry.update();
        }

        sinceTurn.reset();

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
            double turnInput = gamepad1.right_stick_x;
            double currentRad = robot.localizer.getHeadingRad();
            double turn;

            if (Math.abs(turnInput) > DriveConstants.turnDeadband) {
                turn = turnInput;
                holdTargetRad = currentRad;
                holding = false;
                sinceTurn.reset();
            } else if (sinceTurn.seconds() < DriveConstants.headingHoldDelaySec) {
                turn = 0;
                holdTargetRad = currentRad;
                holding = false;
            } else {
                double errorDeg = Math.toDegrees(AngleUnit.normalizeRadians(holdTargetRad - currentRad));
                turn = Range.clip(-errorDeg * DriveConstants.headingKpPerDeg,
                        -DriveConstants.headingHoldMaxTurn, DriveConstants.headingHoldMaxTurn);
                holding = true;
            }

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
            telemetry.addData("Heading Hold", holding ? "鎖定中" : "駕駛控制");
            telemetry.addData("目標 / 目前 (deg)", "%.1f / %.1f",
                    Math.toDegrees(holdTargetRad), Math.toDegrees(currentRad));
            robot.addTelemetry(telemetry);
            telemetry.update();
        }
    }
}
