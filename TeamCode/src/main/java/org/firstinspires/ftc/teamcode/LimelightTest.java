package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import java.util.List;

@TeleOp(name = "Limelight Test", group = "Test")
public class LimelightTest extends LinearOpMode {
    static final double TURN_KP = 0.02;
    static final double AIM_TOLERANCE_DEG = 1.0;

    @Override
    public void runOpMode() {
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();

        telemetry.setMsTransmissionInterval(11);
        telemetry.addLine("Limelight 已啟動，按 START");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                double tx = result.getTx();   // 正 = 目標在右
                double ty = result.getTy();   // 正 = 目標在上
                double ta = result.getTa();   // 佔畫面 %，越近越大

                telemetry.addData("tx (deg)", "%.2f", tx);
                telemetry.addData("ty (deg)", "%.2f", ty);
                telemetry.addData("ta (%)", "%.2f", ta);
                telemetry.addData("staleness (ms)", result.getStaleness());

                List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();
                telemetry.addData("tag 數量", tags.size());
                for (LLResultTypes.FiducialResult tag : tags) {
                    telemetry.addData("  Tag " + tag.getFiducialId(),
                            "x=%.1f°  y=%.1f°", tag.getTargetXDegrees(), tag.getTargetYDegrees());
                }

                // 算對準需要的轉向（只顯示，不送馬達）
                double turn = Range.clip(tx * TURN_KP, -0.5, 0.5);
                telemetry.addData("建議 turn", "%.3f", turn);
                telemetry.addData("已對準?", Math.abs(tx) < AIM_TOLERANCE_DEG ? "是" : "否");
            } else {
                telemetry.addLine("沒有看到目標");
            }

            telemetry.update();
        }

        limelight.stop();
    }
}