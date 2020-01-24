/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TurretConstants;
import frc.robot.util.Angle;
import frc.robot.util.Util;

public class Turret extends SubsystemBase {
  private final double ENCODER_UNITS = 4095; // Range should be 0 - 4095 (aka. 4096 units)
  private final double UNITS_PER_DEGREE = ENCODER_UNITS / 360;

  private TalonSRX motor = new TalonSRX(TurretConstants.motor);
  private double lowerLimit;
  private double upperLimit;
  private boolean correction = false;
  private double goal = 0;

  /**
   * Creates a new Turret.
   */
  public Turret(double lowerLimit, double upperLimit) {
    this.lowerLimit = lowerLimit;
    this.upperLimit = upperLimit;

    // Restore each talonSRX to factory defaults prior to configuration
    motor.configFactoryDefault();

    // Set neutral mode to Brake
    motor.setNeutralMode(NeutralMode.Brake);

    // Add Mag Encoders
    motor.configSelectedFeedbackSensor(FeedbackDevice.PulseWidthEncodedPosition, 0, 0);

    // Set Inverted and Sensor Phase
    motor.setInverted(false);
    motor.setSensorPhase(false);

    // Set PID Values
    motor.config_kP(0, 10.5);
    motor.config_kI(0, .0000125);
    motor.config_kD(0, 50);
    // motor.config_kF(0, .05);

    ShuffleboardTab debug = Shuffleboard.getTab("Debug");
    debug.addNumber("Turret Encoder (Units)", motor::getSelectedSensorPosition);
    debug.addNumber("Turret Encoder (Angle)", this::getCurrentAngle);
    debug.addNumber("Turret Error", motor::getClosedLoopError);
    debug.addNumber("Turret Goal", () -> goal);

    // this.setDefaultCommand(new TestTurret(this));
  }

  /**
   * 
   * @param angle        Angle for Turret to turn to.
   * @param correctAngle Whether to attempt to correct the angle or not. See
   *                     {@link Turret#correctAngle}.
   */
  public void setPosition(double angle, boolean correctAngle) {
    if (correctAngle) {
      double angleNew = correctAngle(angle);
      if (angleNew != angle) {
        correction = true;
      }
      angle = angleNew;
    } else if (angle > upperLimit) {
      DriverStation.reportWarning(
          String.format("Attempting to set position exceeding upper limit: %f", angle), false);
      angle = upperLimit;
    } else if (angle < lowerLimit) {
      DriverStation.reportWarning(
          String.format("Attempting to set position exceeding lower limit: %f", angle), false);
      angle = lowerLimit;
    }

    goal = angle;
    motor.set(ControlMode.Position, angle * UNITS_PER_DEGREE);
  }

  /**
   * 
   * @param angle Angle for Turret to turn to.
   */
  public void setPosition(double angle) {
    setPosition(angle, false);
  }

  public void setPercentOutput(double speed) {
    if (Util.inRange(getCurrentAngle(), upperLimit, 25) || Util.inRange(getCurrentAngle(), lowerLimit, 25)) {
      
      motor.set(ControlMode.PercentOutput, 1 - (2 / (1 + Math.pow(Math.E, 0.25 * speed))));
    }
    motor.set(ControlMode.PercentOutput, speed);
  }

  public void stop() {
    motor.set(ControlMode.PercentOutput, 0);
  }

  public boolean isCorrected() {
    if (!correction) {
      return true;
    }
    return Util.inRange(getCurrentAngle(), goal, 5);
  }

  /**
   * Method which attempts to correct the angle of the turret. This is done by attempting to use the
   * positive/negative inverse angle if possible.
   * 
   * @param angle The angle to correct.
   * @return The angle after corrections.
   */
  public double correctAngle(double angle) {
    // Values should remain between +/- 360
    angle = angle % 360;

    if (angle > upperLimit || angle < lowerLimit) {
      double inverse = angle >= 0 ? -360 + angle : 360 + angle;

      if (inverse >= lowerLimit && inverse <= upperLimit) {
        System.out.println(inverse);
        return inverse;
      } else {
        // If we cant use the inverse we return the closest limit
        return Angle.getShortestDistance(inverse, upperLimit) < Angle.getShortestDistance(inverse,
            upperLimit) ? upperLimit : lowerLimit;
      }
    } else {
      return angle;
    }
  }

  public double getUpperLimit() {
    return upperLimit;
  }

  public double getLowerLimit() {
    return lowerLimit;
  }

  public double getCurrentAngle() {
    return motor.getSelectedSensorPosition() / UNITS_PER_DEGREE;
  }

  public int getPIDError() {
    return motor.getClosedLoopError();
  }


  @Override
  public void periodic() {
    // This method will be called oncgbe per scheduler run
  }
}
