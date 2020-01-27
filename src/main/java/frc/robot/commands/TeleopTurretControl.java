/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Turret;
import frc.robot.util.Limelight;

public class TeleopTurretControl extends CommandBase {
  private final Turret turret;
  private final DoubleSupplier manualAdjustValue;
  private final Trigger overrideTrigger;

  private boolean visionDelayState = false;

  /**
   * Creates a new AimTurret.
   */
  public TeleopTurretControl(Turret turret, DoubleSupplier manualAdjustValue, Trigger overrideTrigger) {
    this.turret = turret;
    addRequirements(this.turret);

    this.manualAdjustValue = manualAdjustValue;
    this.overrideTrigger = overrideTrigger;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret.addClearCorrectionHook(overrideTrigger::get);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean isCorrected = turret.isCorrected();
    boolean hasTarget = Limelight.hasTarget();
    boolean isOverridden = overrideTrigger.get();

    // We want target searching to begin before correction completes
    // if (!isCorrected && Util.inRange(goal, currentAngle, goal*.8) && visionDelayState) {
    if (!isCorrected && !hasTarget && visionDelayState) {
      visionDelayState = false;
    }

    if (hasTarget && (isCorrected || !visionDelayState) && !isOverridden) {
      if (!isCorrected) {
        turret.clearCorrection();
      }
      turret.setPosition(turret.getCurrentAngle() + Limelight.getXOffset(), true);
      if (turret.isCorrected()) {
        visionDelayState = true;
      }
    } else if (isCorrected || isOverridden) {
      turret.setPercentOutput(manualAdjustValue.getAsDouble());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
