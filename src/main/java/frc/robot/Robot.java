package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {


  private WPI_TalonSRX m_Left0 = new WPI_TalonSRX(3);
  private WPI_TalonSRX m_Left1 = new WPI_TalonSRX(4);
  private WPI_TalonSRX m_Right0 = new WPI_TalonSRX(1);
  private WPI_TalonSRX m_Right1 = new WPI_TalonSRX(2);
  private SpeedControllerGroup m_LeftMotors = new SpeedControllerGroup(m_Left0, m_Left1);
  private SpeedControllerGroup m_RightMotors = new SpeedControllerGroup(m_Right0, m_Right1);
  private DifferentialDrive m_Drive = new DifferentialDrive(m_LeftMotors, m_RightMotors);

  // private XboxController m_Controller = new XboxController(0);

  private boolean m_LimelightHasValidTarget = false;
  private double m_LimelightDriveCommand = 0.0;
  private double m_LimelightSteerCommand = 0.0;

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
    // m_autoSelected = m_chooser.getSelected();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Update_Limelight_Tracking();

    if (m_LimelightHasValidTarget) {
      m_Drive.arcadeDrive(m_LimelightDriveCommand, m_LimelightSteerCommand);
    } else {
      m_Drive.arcadeDrive(0, 0);
    }
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

  /**
   * This function implements a simple method of generating driving and steering commands based on
   * the tracking data from a limelight camera.
   */
  public void Update_Limelight_Tracking() {
    // These numbers must be tuned for your Robot! Be careful!
    final double STEER_K = 0.04; // how hard to turn toward the target
    final double DRIVE_K = 0.26; // how hard to drive fwd toward the target
    final double DESIRED_TARGET_AREA = 13.0; // Area of the target when the robot reaches the wall
    final double ACCELERATED_STEER_AREA = 17;
    final double ACCELERATED_STEER_K = 0.02;
    final double MAX_DRIVE = 0.4; // Simple speed limit so we don't drive too fast


    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    // double ty = Netwo+rkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    
    

    if (tv < 1.0) {
      m_LimelightHasValidTarget = false;
      m_LimelightDriveCommand = 0.0;
      m_LimelightSteerCommand = 0.0;
      return;
    }

    m_LimelightHasValidTarget = true;

    // Start with proportional steering
    double steer_cmd = 0;
    if (tx > ACCELERATED_STEER_AREA) {
      steer_cmd = tx * (STEER_K + ACCELERATED_STEER_K);
    } else {
      steer_cmd = tx * STEER_K;
    }
    m_LimelightSteerCommand = steer_cmd;

    // try to drive forward until the target area reaches our desired area
    double drive_cmd = (DESIRED_TARGET_AREA - ta) * DRIVE_K;


    // don't let the robot drive too fast into the goal
    if (drive_cmd > MAX_DRIVE) {
      drive_cmd = MAX_DRIVE;
    } else if (drive_cmd < -MAX_DRIVE) {
      drive_cmd = -MAX_DRIVE;
    }
    m_LimelightDriveCommand = drive_cmd;
  }
}
