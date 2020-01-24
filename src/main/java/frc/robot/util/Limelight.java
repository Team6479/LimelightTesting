package frc.robot.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.subsystems.Limelight.Pipeline;

/**
 * Class to simplify interacting with the Limelight
 *
 * @author Thomas Quillan
 * @author Leo Wilson
 */
public final class Limelight {
  private static NetworkTable limelightTable =
      NetworkTableInstance.getDefault().getTable("limelight");

  /**
   * Don't let anyone instantiate this class.
   */
  private Limelight() {
  }

  /**
   * Whether the limelight has any valid targets
   *
   * @return True if the Limelight has a valid target
   */
  public static boolean hasTarget() {
    return limelightTable.getEntry("tv").getDouble(0) == 1;
  }

  /**
   * Retrieves the Horizontal Offset From Crosshair To Target from the Limelight
   *
   * @return Horizontal Offset From Crosshair To Target (LL1: -27 degrees to 27 degrees | LL2: -29.8
   *         to 29.8 degrees)
   */
  public static double getXOffset() {
    return limelightTable.getEntry("tx").getDouble(0);
  }

  /**
   * Retrieves the Vertical Offset From Crosshair To Target from the Limelight
   *
   * @return Vertical Offset From Crosshair To Target (LL1: -20.5 degrees to 20.5 degrees | LL2:
   *         -24.85 to 24.85 degrees)
   */
  public static double getYOffset() {
    return limelightTable.getEntry("ty").getDouble(0);
  }

  /**
   * Retrieves the Target Area from the Limelight
   *
   * @return Target Area (0% of image to 100% of image)
   */
  public static double getArea() {
    return limelightTable.getEntry("ta").getDouble(0);
  }

  /**
   * Retrieves the Skew from the Limelight
   *
   * @return Skew or rotation (-90 degrees to 0 degrees)
   */
  public static double getSkew() {
    return limelightTable.getEntry("ts").getDouble(0);
  }

  /**
   * Sets limelight’s LED state
   *
   * <ul>
   *   <li><b>States</b>:
   *   <ul>
   *     <li><b>0</b> use the LED Mode set in the current pipeline
   *     <li><b>1</b> force off
   *     <li><b>2</b> force blink
   *     <li><b>3</b> force on
   *   </ul>
   * </ul>
   *
   * @param state integer value of state to set
   */
  public static void setLEDState(int state) {
    limelightTable.getEntry("ledMode").setDouble(state);
  }

  /**
   * Sets limelight’s operation mode
   *
   * <ul>
   *   <li><b>Modes</b>:
   *   <ul>
   *     <li><b>0</b> Vision processor
   *     <li><b>1</b> Driver Camera (Increases exposure, disables vision processing)
   *   </ul>
   * </ul>
   *
   * @param mode integer value of mode to set
   */
  public static void setCamMode(int mode) {
    limelightTable.getEntry("camMode").setDouble(mode);
  }

  /**
   * Sets limelight’s current pipeline.
   *
   * @param p the {@link Pipeline} to be set. Should be a value of 0-9
   */
  public static void setPipeline(int pipeline) {
    limelightTable.getEntry("pipeline").setDouble(pipeline);
  }

  /**
   * Sets limelight’s operation mode
   *
   * <ul>
   *   <li><b>Modes</b>:
   *   <ul>
   *     <li><b>0</b> Standard - Side-by-side streams if a webcam is attached to Limelight
   *     <li><b>1</b> PiP Main - The secondary camera stream is placed in the lower-right corner of the
   *     primary camera stream
   *     <li><b>2</b> PiP Secondary - The primary camera stream is placed in the lower-right corner of
   *     the secondary camera stream
   *   </ul>
   * </ul>
   *
   * @param mode integer value of mode to set
   */
  public static void setStreamMode(int mode) {
    limelightTable.getEntry("stream").setDouble(mode);
  }

  /**
   * Sets limelight’s operation mode
   *
   * <ul>
   *   <li><b>States</b>:
   *   <ul>
   *     <li><b>0</b> Stop taking snapshots
   *     <li><b>1</b> Take two snapshots per second
   *   </ul>
   * </ul>
   *
   * @param state integer value of state to set
   */
  public static void setSnapshotState(int state) {
    limelightTable.getEntry("snapshot").setDouble(state);
  }
}
