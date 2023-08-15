package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.common.Software.AprilTagAutonomous;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;


@Config
@Autonomous(name="Right", group="Linear Opmode")
public class AutonomousRight extends LinearOpMode {
    String tagReading;
    AprilTagAutonomous aprilTag;
    @Override
    public void runOpMode() {
        //yoink all of the motor declarations and their methods
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        aprilTag = new AprilTagAutonomous(this);

        waitForStart();
        if (isStopRequested()) return;

        Pose2d startPose = new Pose2d(36,-60.8, Math.toRadians(90));
        drive.setPoseEstimate(startPose);
        Trajectory start = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(36,-30), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(32,-7, Math.toRadians(-43)), Math.toRadians(-43))
                .build();
        TrajectorySequence cycle = drive.trajectorySequenceBuilder(start.end())
                .splineToLinearHeading(new Pose2d(57,-12.4, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(32,-7, Math.toRadians(-43)))
                .setReversed(false)
                .build();
        TrajectorySequence park1 = drive.trajectorySequenceBuilder(cycle.end())
                .splineTo(new Vector2d(36,-20), Math.toRadians(-90))
                .splineTo(new Vector2d(36,-35), Math.toRadians(225))
                .splineTo(new Vector2d(11, -35), Math.toRadians(180))
                .build();
        TrajectorySequence park2 = drive.trajectorySequenceBuilder(cycle.end())
                .splineTo(new Vector2d(36,-20), Math.toRadians(-90))
                .splineTo(new Vector2d(36,-35), Math.toRadians(225))
                .build();
        TrajectorySequence park3 = drive.trajectorySequenceBuilder(cycle.end())
                .splineTo(new Vector2d(36,-20), Math.toRadians(-90))
                .splineTo(new Vector2d(36,-35), Math.toRadians(-45))
                .splineTo(new Vector2d(59, -35), Math.toRadians(0))
                .build();

        aprilTag.findTagIDSimple();
        tagReading = aprilTag.getTagOfInterest();

        drive.followTrajectory(start);
        for (int i = 1; i <= 5; i++) {
            drive.followTrajectorySequence(cycle);
        }
        if (tagReading.equals("LEFT")) { //state 1 - left
            drive.followTrajectorySequence(park1);
        } else if (tagReading.equals("MIDDLE")) { //state 2 - middle
            drive.followTrajectorySequence(park2);
        } else if (tagReading.equals("RIGHT")) { //state 3 - right
            drive.followTrajectorySequence(park3);
        } else {
            drive.followTrajectorySequence(park1);
        }
    }
}