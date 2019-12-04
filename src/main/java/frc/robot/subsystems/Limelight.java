/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Calculates values based on input from a Limelight
 * @author Leo Wilson
 */
public class Limelight extends Subsystem {
  /**
   * Available Limelight pipelines (0-9)
   * They start with P to make Java happy
   */
  public enum Pipeline {
    P0(0), P1(1), P2(2), P3(3), P4(4), P5(5), P6(6), P7(7), P8(8), P9(9);
    public int value;
    private Pipeline(int value) {
      this.value = value;
    }
  };

  NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

  public boolean hasTarget() {
    return limelightTable.getEntry("tv").getDouble(0) == 1;
  }

  public void setPipeline(Pipeline p) {
    limelightTable.getEntry("pipeline").setDouble(p.value);
  }

  /**
   * Between -29.8 and 29.8, in degrees
   */
  public double getXOffset() {
    return limelightTable.getEntry("tx").getDouble(0);
  }

  /**
   * Between 0% and 100% of the screen
   */
  public double getArea() {
    return limelightTable.getEntry("ta").getDouble(0);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
