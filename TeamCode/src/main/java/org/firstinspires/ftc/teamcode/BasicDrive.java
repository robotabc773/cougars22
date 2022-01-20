/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Basic Drive")
//@Disabled
public class BasicDrive extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private OurBot robot = new OurBot();
    private boolean tankDrive = false;
    private double basePower = 0.8;
    private double lastPowerChangeTime;
    private double armTargetPosition = 0;
    private boolean armHolding = true;

    private final double FAST_DRIVE_POWER = 0.8;
    private final double SLOW_DRIVE_POWER = 0.5;
    private final double ARM_POWER = 0.4;
    private final double ARM_HOLD_POWER = 1;
    private final double INTAKE_POWER = 0.8;
    private final double DUCK_SPINNER_POWER = 0.3;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Seb", "Is Cool");
        telemetry.update();

        robot.init(hardwareMap);


       // armTargetPosition = 0;
       // robot.arm.setTargetPosition(0);
       // robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        lastPowerChangeTime = runtime.time();
        public static final robot.arm.ZeroPowerBehavior BRAKE

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {

            // Setup a variable for each drive wheel
            double leftPower;
            double rightPower;
            double armPower;
            double intakePower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            if (tankDrive)
            {
                leftPower =  gamepad1.left_stick_y;
                rightPower = gamepad1.right_stick_y;
            }
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            else
            {
                double drive = gamepad1.left_stick_y;
                double turn = -gamepad1.right_stick_x;
                leftPower = Range.clip(drive + turn, -1.0, 1.0);
                rightPower = Range.clip(drive - turn, -1.0, 1.0);
            }

            leftPower *= basePower;
            rightPower *= basePower;

            /*
            if (gamepad2.left_stick_y != 0)
            {
                armPower = ARM_POWER * gamepad2.left_stick_y * -1;
                robot.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armHolding = false;
            }
            else
            {
                armPower = ARM_HOLD_POWER;
                if (!armHolding)
                {
                    robot.arm.setTargetPosition(robot.arm.getCurrentPosition());
                    robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armHolding = true;
                }
            }
            */
            armPower = ARM_POWER * gamepad2.left_stick_y;
            intakePower = INTAKE_POWER * gamepad2.right_stick_y;

            // Send calculated power to wheels
            robot.leftFront.setPower(leftPower);
            robot.leftBack.setPower(leftPower);
            robot.rightFront.setPower(-rightPower);
            robot.rightBack.setPower(-rightPower);
            robot.arm.setPower(armPower);
            robot.intake.setPower(intakePower);
            robot.duckSpinner.setPower(DUCK_SPINNER_POWER * (gamepad1.left_trigger - gamepad1.right_trigger));

            // Switch modes
            if (gamepad1.dpad_up)
            {
                tankDrive = false;
            }
            else if (gamepad1.dpad_down)
            {
                tankDrive = true;
            }

            // Adjust Power
            if (gamepad1.dpad_left)
            {
                basePower = SLOW_DRIVE_POWER;
            }
            else if (gamepad1.dpad_right)
            {
                basePower = FAST_DRIVE_POWER;
            }
            /*
            if (runtime.time() > lastPowerChangeTime + 0.5)
            {
                if (gamepad1.dpad_left)
                {
                    basePower = Math.max(0, basePower - 0.1);
                    lastPowerChangeTime = runtime.time();
                }
                else if (gamepad1.dpad_right)
                {
                    basePower = Math.min(1, basePower + 0.1);
                    lastPowerChangeTime = runtime.time();
                }
            }
            */

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Power", basePower);
            telemetry.addData("Mode", tankDrive ? "Tank Drive" : "POV Drive");
            telemetry.addData("Arm Position", armTargetPosition);
            telemetry.update();

            // Sleep to make the loop have more consistent timing
            // Possibly have to remove if things are updating too slow
            sleep(5);
            idle();
        }
    }
}
