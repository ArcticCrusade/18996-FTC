package org.firstinspires.ftc.common.Hardware;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.common.Interfaces.Subsystem;

public class DeliverySystem extends SubsystemBase {
    LiftSubsystem lift;
    GrabberSubsystem grabber;
    FourBarSubsystem fourBar;
    RobotHardware robot;

    public DeliverySystem(RobotHardware robot) {
        this.robot = robot;
        lift = new LiftSubsystem(robot);
        grabber = new GrabberSubsystem(robot);
        fourBar = new FourBarSubsystem(robot);
        //grabber.initialize(new OpMode());
    }

    // DO NOT CHANGE THE ORDER OF THESE COMMANDS - WILL (probably) BREAK IF YOU DO
    public void intake() throws InterruptedException {
        fourBar.setPickUpPosition();
        lift.setLow();
        grabber.grab();
    }

    public void dropHigh() throws InterruptedException {
        fourBar.setHigh();
        lift.setHigh();
        grabber.release();
    }
}
