package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum TeleOp", group = "Drive")
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "fl");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "bl");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "fr");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "br");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{frontLeft, backLeft, frontRight, backRight}) {
            // 另一個選項 FLOAT 會讓馬達自由滑動。
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // 執行模式設成 RUN_WITHOUT_ENCODER：setPower() 的數值直接當作輸出功率的比例，不經過速度閉迴路控制。
            // 名字容易誤會：這個模式下其實還是讀得到編碼器的值。
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        waitForStart();

        while (opModeIsActive()) {
            double y  = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            double fl = y + x + rx;
            double bl = y - x + rx;
            double fr = y - x - rx;
            double br = y + x - rx;
            // 找出四輪中絕對值最大的
            double max = Math.max(Math.max(Math.abs(fl), Math.abs(bl)),
                    Math.max(Math.abs(fr), Math.abs(br)));
            // 只有超過 1 時才縮小
            if (max > 1) {
                fl /= max;
                bl /= max;
                fr /= max;
                br /= max;
            }
            // 減速鍵
            double speed = gamepad1.right_bumper ? 0.4 : 1.0;

            // 把算好的功率乘上速度倍率，送給馬達。
            frontLeft.setPower(fl * speed);
            backLeft.setPower(bl * speed);
            frontRight.setPower(fr * speed);
            backRight.setPower(br * speed);

            telemetry.addData("FL / FR", "%.2f / %.2f", fl * speed, fr * speed);
            telemetry.addData("BL / BR", "%.2f / %.2f", bl * speed, br * speed);
            telemetry.update();
        }
    }
}
