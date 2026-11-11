package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface Subsystem {
    void update();
    void addTelemetry(Telemetry telemetry);
}