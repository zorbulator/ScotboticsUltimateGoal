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

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.lang.UnsupportedOperationException;



public class ScotBot
{
    private PIDController TurnPID = new PIDController(1, 1, 1); 
    private PIDController MovementPID = new PIDController(1, 1, 1);
 
    public static final double MID_SERVO = 0.5;
    public static final double TURN_SCALAR = 1; // Further testing is needed
    public static final double Y_SCALAR = 1; // Further testing is needed
    public static final double ALTERNATE_BALENCE = .5; // Further testing is needed

    public static final int CORRECTION_NONE = 0;
    public static final int CORRECTION_TURN = 1;
    public static final int CORRECTION_BALANCE = 2;
    public static final int CORRECTION_MOVEMENT = 4;
    public static final int CORRECTION_ALL = 7;

    public int correctionFlags = 0;

    public double x, y, rotation;

    public DcMotor fl, fr, bl, br;

    public DcMotor odoSide, odoLeft, odoRight; // references to normal motors where odometry encoders are connected

    private double odoSidePos = 0, odoLeftPos = 0, odoRightPos = 0; // positions of odometry encoders, only used for convenience
    private double oldOdoSidePos = 0, oldOdoLeftPos = 0, oldOdoRightPos = 0; // store old positions to find delta
    private double rotationDelta = 0;

    private static final double RADIANS_PER_COUNT_DIFFERENCE = 10.0; // radians for every count of difference between the odo wheels found with calibration
    private static final double ODO_SIDE_OFFSET = 10.0; // the offset of the horizontal odo wheel, found with calibration

    private int odoLeftMultiplier = 1, odoRightMultiplier = 1, odoSideMultiplier = 1; // multipliers for encoder position

    HardwareMap hwMap;

    public ScotBot(HardwareMap hwMap) {
        this.hwMap = hwMap;

        fl = hwMap.get(DcMotor.class, "fl");
        fr = hwMap.get(DcMotor.class, "fr");
        bl = hwMap.get(DcMotor.class, "bl");
        br = hwMap.get(DcMotor.class, "br");

        fl.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void DriveTurn(double speed, double turn) {
        double leftPower  = Range.clip(speed + turn, 0.0, 1.0);
        double rightPower = Range.clip(speed - turn, 0.0, 1.0);

        fl.setPower(leftPower);
        bl.setPower(leftPower);
        fr.setPower(rightPower);
        br.setPower(rightPower);
    }

    public void MecanumCorrectionDrive(double _x, double _y, double turn)
    {
        if ((correctionFlags & CORRECTION_TURN) == correctionFlags)
        {
            turn *= TURN_SCALAR;
            turn += TurnPID.getCorrection(rotation, turn);
        }
        if ((correctionFlags & CORRECTION_TURN) == correctionFlags)
        {
            _y *= Y_SCALAR;
            _y += MovementPID.getCorrection((odoLeft.getCurrentPosition() * odoLeftMultiplier+odoRight.getCurrentPosition() * odoRightMultiplier)/2, _y);
        }
        MecanumDrive(_x, _y, turn, (((correctionFlags & CORRECTION_BALANCE) == correctionFlags) ? .5 : ALTERNATE_BALENCE));
    }

    public void MecanumDrive(double _x, double _y, double turn, double balence)
    {
        double maxBalence = Math.max(1-balence, balence);
        fl.setPower((-_y-_x+turn) *    balence /maxBalence);
        fr.setPower((-_y+_x-turn) *    balence /maxBalence);
        bl.setPower((-_y+_x+turn) * (1-balence)/maxBalence);
        br.setPower((-_y-_x-turn) * (1-balence)/maxBalence);
    }

    public void updateOdometry() {
        odoLeftPos  = odoLeft.getCurrentPosition()  * odoLeftMultiplier;
        odoRightPos = odoRight.getCurrentPosition() * odoRightMultiplier; // get positions of wheels with multiplier

        double leftDelta = odoLeftPos - oldOdoLeftPos;
        double rightDelta = odoRightPos - oldOdoRightPos; // find change in position

        rotationDelta = (leftDelta - rightDelta) / RADIANS_PER_COUNT_DIFFERENCE; // get difference between wheels and use constant to find radians
        rotation += rotationDelta;

        odoSidePos = odoSide.getCurrentPosition() * odoSideMultiplier;
        double rawSideDelta = odoSidePos - oldOdoSidePos;
        double deltaX = rawSideDelta - (rotationDelta*ODO_SIDE_OFFSET); // correct for offset of wheel using constant

        double deltaY = (leftDelta + rightDelta) / 2; // average both sides to find forward movement

        x += (deltaY*Math.sin(rotation)) + (deltaX*Math.cos(rotation));
        y += (deltaY*Math.cos(rotation)) - (deltaX*Math.sin(rotation)); // change global position based on current orientation

        oldOdoLeftPos  = odoLeftPos;
        oldOdoRightPos = odoRightPos;
        oldOdoSidePos  = odoSidePos;
    }
}
