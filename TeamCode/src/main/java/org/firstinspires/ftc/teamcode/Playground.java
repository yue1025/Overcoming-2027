package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

/*
 * 不需要馬達的練習程式：只用 Control Hub 內建的 IMU、LED、電池電壓和遙控器。
 *
 * 操作：
 *   A / B / X  -> Control Hub LED 變綠 / 紅 / 藍
 *   Y          -> 遙控器震動（PS4/PS5 手把才有）
 *   Back       -> 把目前方向設成 Yaw 0 度
 */
@TeleOp(name = "Playground")
public class Playground extends LinearOpMode {
    @Override
    public void runOpMode() {
        // 內建 IMU，Configuration 掃描時會自動命名為 "imu"
        IMU imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));

        LynxModule hub = hardwareMap.getAll(LynxModule.class).get(0);
        VoltageSensor battery = hardwareMap.voltageSensor.iterator().next();

        telemetry.addLine("準備好了，按 Start");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) hub.setConstant(Color.GREEN);
            if (gamepad1.b) hub.setConstant(Color.RED);
            if (gamepad1.x) hub.setConstant(Color.BLUE);

            if (gamepad1.y && !gamepad1.isRumbling()) gamepad1.rumble(300);

            if (gamepad1.back) imu.resetYaw();

            YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();
            telemetry.addData("Yaw 旋轉", "%.1f°", angles.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch 前後傾", "%.1f°", angles.getPitch(AngleUnit.DEGREES));
            telemetry.addData("Roll 左右傾", "%.1f°", angles.getRoll(AngleUnit.DEGREES));
            telemetry.addData("左搖桿", "x=%.2f  y=%.2f", gamepad1.left_stick_x, gamepad1.left_stick_y);
            telemetry.addData("右板機", "%.2f", gamepad1.right_trigger);
            telemetry.addData("電池", "%.2f V", battery.getVoltage());
            telemetry.update();
        }
    }
}
