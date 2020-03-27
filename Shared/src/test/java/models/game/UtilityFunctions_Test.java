package test.java.models.game;

import org.junit.Assert;
import org.junit.Test;
import shared.UtilityFunctions;


public class UtilityFunctions_Test {

    @Test
    public void capitaliseString_returnsHello() {
        String expected = "Hello";
        String actual = UtilityFunctions.capitaliseString("hello");

        Assert.assertEquals(actual, expected);
    }

}
