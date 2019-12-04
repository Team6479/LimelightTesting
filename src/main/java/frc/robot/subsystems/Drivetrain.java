/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * A basic arcade drive
 * @author Leo Wilson
 */
public class Drivetrain extends Subsystem {
  private WPI_TalonSRX left0;
  private WPI_TalonSRX left1;
  private WPI_TalonSRX right0;
  private WPI_TalonSRX right1;
  private SpeedControllerGroup leftMotors;
  private SpeedControllerGroup rightMotors;
  private DifferentialDrive drivetrain;

  public Drivetrain() {
    left0 = new WPI_TalonSRX(3);
    left1 = new WPI_TalonSRX(4);
    right0 = new WPI_TalonSRX(1);
    right1 = new WPI_TalonSRX(2);
    leftMotors = new SpeedControllerGroup(left0, left1);
    rightMotors = new SpeedControllerGroup(right0, right1);
    drivetrain = new DifferentialDrive(leftMotors, rightMotors);
  }

  public void arcadeDrive(double speed, double rotation) {
    drivetrain.arcadeDrive(speed, rotation);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
