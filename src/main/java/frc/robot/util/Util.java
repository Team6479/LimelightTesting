package frc.robot.util;

public final class Util {
  private Util() {}

  public static boolean inRange(double x, double y, double range) {
    return Math.abs(Math.abs(x) - Math.abs(y)) <= range;
  }
}