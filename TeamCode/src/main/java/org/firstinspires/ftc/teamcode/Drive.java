package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="DRIVE", group="Pushbot")
public class Drive extends LinearOpMode {

    /* Declare OpMode members. */
    // HardwarePushbot robot           = new HardwarePushbot();   // Use a Pushbot's hardware
    // double          clawOffset      = 0;                       // Servo mid position
    // final double    CLAW_SPEED      = 0.02 ;                   // sets rate to move servo

    ScotBot robot;

    @Override
    public void runOpMode() {

    //     double left;
    //     double right;
    //     double drive;
    //     double turn;
    //     double max;

    //     robot.init(hardwareMap);
        robot = new ScotBot(hardwareMap);

    //     telemetry.addData("Say", "Hello Driver");    //
    //     telemetry.update();

    //     waitForStart();

    //     while (opModeIsActive()) {

    //         // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
    //         // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
    //         // This way it's also easy to just drive straight, or just turn.
    //         drive = -gamepad1.left_stick_y;
    //         turn  =  gamepad1.right_stick_x;

    //         // Combine drive and turn for blended motion.
    //         left  = drive + turn;
    //         right = drive - turn;

    //         max = Math.max(Math.abs(left), Math.abs(right));
    //         if (max > 1.0)
    //         {
    //             left /= max;
    //             right /= max;
    //         }

    //         robot.leftDrive.setPower(left);
    //         robot.rightDrive.setPower(right);

    //         if (gamepad1.right_bumper)
    //             clawOffset += CLAW_SPEED;
    //         else if (gamepad1.left_bumper)
    //             clawOffset -= CLAW_SPEED;

    //         clawOffset = Range.clip(clawOffset, -0.5, 0.5);
    //         robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset);
    //         robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset);

    //         if (gamepad1.y)
    //             robot.leftArm.setPower(robot.ARM_UP_POWER);
    //         else if (gamepad1.a)
    //             robot.leftArm.setPower(robot.ARM_DOWN_POWER);
    //         else
    //             robot.leftArm.setPower(0.0);

    //         robot.DriveTurn(1.0, 0.0);

    //         telemetry.addData("claw",  "Offset = %.2f", clawOffset);
    //         telemetry.addData("left",  "%.2f", left);
    //         telemetry.addData("right", "%.2f", right);
    //         telemetry.update();

    //         sleep(50);
    //     }
    }
}
