package frc.robot;

import com.team6479.lib.commands.TeleopTankDrive;
import com.team6479.lib.controllers.CBJoystick;
import com.team6479.lib.controllers.CBXboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.TeleopTurretControl;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Turret;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private final Drivetrain drivetrain = new Drivetrain();
  private final Turret turret = new Turret(-270, 270);

  private final CBXboxController xbox = new CBXboxController(0);
  private final CBJoystick joystick = new CBJoystick(1);

  public Robot() {
    Shuffleboard.getTab("Debug").addNumber("Joystick", joystick::getTwist);

    drivetrain.setDefaultCommand(new TeleopTankDrive(drivetrain,
      () -> -xbox.getY(Hand.kLeft), () -> xbox.getX(Hand.kRight)));

    // turret.setDefaultCommand(new TestTurret(turret));
    turret.setDefaultCommand(new TeleopTurretControl(turret,
        () -> Math.abs(joystick.getTwist()) >= .1 ? joystick.getTwist() : 0,
        joystick.getButton(1)));
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow and SmartDashboard
   * integrated updating.
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void disabledInit() {
    // We don't want any lingering corrections after disabling
    turret.clearCorrection();
  }
}
