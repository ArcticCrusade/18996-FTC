package org.firstinspires.ftc.teamcode.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.common.Config.DimensionalMovementConfig;
import org.firstinspires.ftc.common.Config.SpeedMovementConfig;

@TeleOp(name="Driving Configuration", group="Linear Opmode")
public class DrivingConfig extends LinearOpMode {
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    private double LF, RF, LR, RR, rightY, rightX, leftY, leftX;
    @Override
    public void runOpMode() {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        SpeedMovementConfig slow = new SpeedMovementConfig();
        SpeedMovementConfig normal = new SpeedMovementConfig();
        SpeedMovementConfig fast = new SpeedMovementConfig();
        SpeedMovementConfig[] speeds = {slow, normal, fast};
        int index = 0;

        while (opModeIsActive()) {
            if (gamepad1.a) {
                index = (index + 1) % 3;
                sleep(100);
            }

            if (gamepad1.b) {
                speeds[index].changeConfig();
                sleep(100);
            }

            if (gamepad1.y) {
                speeds[index].getConfig().changeMode();
                sleep(100);
            }

            if (gamepad1.dpad_up) {
                speeds[index].getConfig().changeVal(0.01);
            }

            if (gamepad1.dpad_down) {
                speeds[index].getConfig().changeVal(-0.01);
            }


            LF = 0; RF = 0; LR = 0; RR = 0;


            // Get joystick values
            rightY = -gamepad1.right_stick_y;
            rightX = gamepad1.right_stick_x;
            leftY = -gamepad1.left_stick_y;
            leftX = gamepad1.left_stick_x;

            DimensionalMovementConfig[] configs = speeds[index].getConfigList();
            double X1 = configs[0].calculateSpeed(rightX);
            double Y1 = configs[1].calculateSpeed(rightY);
            double X2 = configs[2].calculateSpeed(leftX);
            double Y2 = configs[3].calculateSpeed(leftY);

            //calculate motor output from joysticks
            LF = Y1 - X1 + X2;
            RF = Y1 + X1 - X2;
            LR = Y1 + X1 + X2;
            RR = Y1 - X1 - X2;

            //set motor commands
            leftFront.setPower(LF);
            rightFront.setPower(RF);
            leftRear.setPower(LR);
            rightRear.setPower(RR);

            switch (index) {
                case 1: {
                    telemetry.addLine("Currently Modifying Slow Speed");
                    break;
                }
                case 2: {
                    telemetry.addLine("Currently Modifying Normal Speed");
                    break;
                }
                case 3: {
                    telemetry.addLine("Currently Modifying Fast Speed");
                    break;
                }
            }

            telemetry.addLine(speeds[index].getName());
            telemetry.addLine(speeds[index].getConfig().getCurrentlyChanging());
            telemetry.update();
        }
    }
}
