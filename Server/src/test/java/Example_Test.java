package test.java;

import org.junit.Assert;
import org.junit.Test;

/**
 * So this file is a very quick practical guide to unit-testing.
 * 
 * Here are a few conventions I would like us to follow: 1) All tests belong in the src/test.java
 * folder. 2) All test classes should end with "_Test". 3) All test classes should start with the
 * name of class that they are testing. 4) Try to name tests such that when they fail, it is obvious
 * what went wrong. 5) Try to get into the habit of running 'mvm clean package test'.
 **/

public class Example_Test {

  @Test // All individual tests require @Test in order for the test runner to pick it up.
  public void TwoPlusTwoIsFour() { // all tests should be set to Public and return nothing.
    // Notice that the test name makes it clear what we are testing.

    int expected = 4;
    int actual = 2 + 2;

    // If we all get in the habit of using the 'expected' and 'actual' terminology it
    // should be easy to write tests that are easily understandable.

    // The test...
    Assert.assertEquals("Optional error message", expected, actual);
  }

  @Test
  public void SevenIsLessThanThree() {

    boolean actual = 7 < 3;
    boolean expected = false;

    Assert.assertEquals("Optional error message", expected, actual);

    // Although the above will work, when testing booleans its often better to get rid of 'expected'
    // and call assertTrue/assertFalse instead. Like so:

    Assert.assertFalse("Optional error message", actual);
  }
}
