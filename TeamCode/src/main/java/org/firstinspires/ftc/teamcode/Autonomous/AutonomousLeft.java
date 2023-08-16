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
@Autonomous(name="Left", group="Linear Opmode")
public class AutonomousLeft extends LinearOpMode {
    String tagReading;
    AprilTagAutonomous aprilTag;
    @Override
    public void runOpMode() {
        //yoink all of the motor declarations and their methods
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        aprilTag = new AprilTagAutonomous(this);

        waitForStart();
        if (isStopRequested()) return;

        Pose2d startPose = new Pose2d(-35.6,-60, Math.toRadians(90));
        drive.setPoseEstimate(startPose);
        Trajectory start = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(-35.2,-21.6), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(-30,-8, Math.toRadians(-127)), Math.toRadians(-127))
                .build();
        TrajectorySequence cycle = drive.trajectorySequenceBuilder(start.end())
                .splineToLinearHeading(new Pose2d(-59.2,-12, Math.toRadians(180)), Math.toRadians(180))
                .setReversed(true)
                .lineToSplineHeading(new Pose2d(-30,-8, Math.toRadians(-127)))
                .setReversed(false)
                .build();
        TrajectorySequence park1 = drive.trajectorySequenceBuilder(cycle.end())
                .splineTo(new Vector2d(-35,-35), Math.toRadians(225))
                .splineTo(new Vector2d(-59, -35), Math.toRadians(180))
                .build();
        TrajectorySequence park2 = drive.trajectorySequenceBuilder(cycle.end())
                .splineToSplineHeading(new Pose2d(-35.2,-35, Math.toRadians(-90)), Math.toRadians(-90))
                .build();
        TrajectorySequence park3 = drive.trajectorySequenceBuilder(cycle.end())
                .splineTo(new Vector2d(-35,-35), Math.toRadians(-45))
                .splineTo(new Vector2d(-11, -35), Math.toRadians(0))
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