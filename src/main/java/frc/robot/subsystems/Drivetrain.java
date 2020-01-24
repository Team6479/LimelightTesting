/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team6479.lib.subsystems.TankDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * A basic arcade drive
 * @author Leo Wilson
 */
public class Drivetrain extends SubsystemBase implements TankDrive {
  private WPI_TalonSRX left0;
  private WPI_TalonSRX left1;
  private WPI_TalonSRX right0;
  private WPI_TalonSRX right1;
  private SpeedControllerGroup leftMotors;
  private SpeedControllerGroup rightMotors;
  private DifferentialDrive drivetrain;

  public Drivetrain() {
    left0 = new WPI_TalonSRX(0);
    left1 = new WPI_TalonSRX(1);
    right0 = new WPI_TalonSRX(2);
    right1 = new WPI_TalonSRX(3);

    left0.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

    leftMotors = new SpeedControllerGroup(left0, left1);
    rightMotors = new SpeedControllerGroup(right0, right1);
    drivetrain = new DifferentialDrive(leftMotors, rightMotors);
  }

  public void arcadeDrive(double speed, double rotation) {
    drivetrain.arcadeDrive(speed, rotation);
  }
  
  @Override
  public void tankDrive(double leftSpeed, double rightSpeed) {
    drivetrain.tankDrive(leftSpeed, rightSpeed);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("DT Left Encoder", left0.getSelectedSensorPosition());
  }

  @Override
  public void stop() {
    drivetrain.tankDrive(0, 0);
  }

}
