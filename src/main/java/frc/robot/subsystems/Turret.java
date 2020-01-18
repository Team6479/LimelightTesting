/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TurretConstants;
import frc.robot.commands.AimTurret;

public class Turret extends SubsystemBase {
  private final double ENCODER_UNITS = 4095; // Range should be 0 - 4095 (aka. 4096 units)
  private final double UNITS_PER_DEGREE = ENCODER_UNITS / 360;

  private TalonSRX motor = new TalonSRX(TurretConstants.motor);
  private double lowerLimit;
  private double upperLimit;

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

    // Invert Motor to match Sensor Phase
    motor.setInverted(false);
    motor.setSensorPhase(false);

    // Set PID Values
    motor.config_kP(0, 24);
    // motor.config_kI(0, 0);
    motor.config_kD(0, 240);
    // motor.config_kF(0, .2);


    this.setDefaultCommand(new AimTurret(this));
  }

  /**
   * 
   * @param angle Angle for Turret to turn to.
   */
  public void setPosition(double angle) {
    if (angle > upperLimit) {
      DriverStation.reportWarning(String.format("Attempting to set position exceeding upper limit: %f", angle), false);
      angle = upperLimit;
    } else if (angle < lowerLimit) {
      DriverStation.reportWarning(String.format("Attempting to set position exceeding lower limit: %f", angle), false);
      angle = lowerLimit;
    }

    motor.set(ControlMode.Position, angle * UNITS_PER_DEGREE);
  }

  public void stop() {
    motor.set(ControlMode.PercentOutput, 0);
  }

  public int validateAngle(double angle) {
    int returnCode = 0;

    if (angle > upperLimit) {
      returnCode = 1;
    } else if (angle < lowerLimit) {
      returnCode = -1;
    }

    return returnCode;
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
    SmartDashboard.putNumber("Turret Encoder (Units)", motor.getSelectedSensorPosition());
    SmartDashboard.putNumber("Turret Encoder (Angle)", getCurrentAngle());
    SmartDashboard.putNumber("Turret Error", motor.getClosedLoopError());
  }
}
