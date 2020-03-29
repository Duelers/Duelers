package shared;

/**
 * Here's the place to put random helper functions. All functions should be pure (i.e. no
 * "side-effects") and static.
 */
public class UtilityFunctions {

  public static String capitaliseString(String s) {
    // "hello world" -> "Hello world"
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }
}
