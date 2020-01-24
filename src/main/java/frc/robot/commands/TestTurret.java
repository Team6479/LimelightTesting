/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;

public class TestTurret extends CommandBase {
  private final Turret turret;
  private double angle = 0;
  private boolean state = false;

  /**
   * Creates a new TestTurret.
   */
  public TestTurret(Turret turret) {
    this.turret = turret;
    addRequirements(this.turret);
    // Shuffleboard.getTab("Debug").addNumber("Goal", () -> angle);
    angle = turret.getCurrentAngle();
  } 

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (state) {
      angle = -90;
      state = !state;
    } else {
      angle = 270;
      state = !state;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turret.setPosition(angle, true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
