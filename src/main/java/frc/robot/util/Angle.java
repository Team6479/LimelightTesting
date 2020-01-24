package frc.robot.util;

public final class Angle {
    /**
     * Don't let anyone instantiate this class.
     */
    private Angle() {}

    /**
     * Gets the shortest distance (angular) between the two angles. It will be in range [0, 180].
     * 
     * @param angle1 First angle.
     * @param angle2 Second angle.
     * @return Shortest distance (angular) between the two angles.
     */
    public static double getShortestDistance(double angle1, double angle2) {
        double angle = Math.abs(angle1 - angle2) % 360; // This is either the distance or
                                                        // 360 - distance
        return angle > 180 ? 360 - angle : angle;
    }
}
