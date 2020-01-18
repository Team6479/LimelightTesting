package frc.robot.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.subsystems.Limelight.Pipeline;

public class Limelight {
    private static NetworkTable limelightTable =
            NetworkTableInstance.getDefault().getTable("limelight");

    public static boolean hasTarget() {
        return limelightTable.getEntry("tv").getDouble(0) == 1;
    }

    public static void setPipeline(Pipeline p) {
        limelightTable.getEntry("pipeline").setDouble(p.value);
    }

    /**
     * Between -29.8 and 29.8, in degrees
     */
    public static double getXOffset() {
        return limelightTable.getEntry("tx").getDouble(0);
    }

    /**
     * Between -29.8 and 29.8, in degrees
     */
    public static double getZOffset() {
        return limelightTable.getEntry("tz").getDouble(0);
    }

    /**
     * Between 0% and 100% of the screen
     */
    public static double getArea() {
        return limelightTable.getEntry("ta").getDouble(0);
    }
}
