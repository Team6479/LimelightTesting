package frc.robot.util;

public final class Util {
    private Util() {}

    public static double getRange(double x, double y) {
      return Math.abs(Math.abs(x) - Math.abs(y));
    }

    public static boolean inRange(double x, double y, double range) {
        return getRange(x, y) <= range;
    }
}
