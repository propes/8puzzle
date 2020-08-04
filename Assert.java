import edu.princeton.cs.algs4.StdOut;

public class Assert {
    private static int successCount;
    private static int assertCount;

    public static void equal(int expected, int actual, String testName) {
        if (expected != actual) {
            StdOut.println(
                    String.format("Test failed: %s. Expected: %d, Actual: %d.",
                                  testName,
                                  expected,
                                  actual));
        }
        else {
            successCount++;
        }

        assertCount++;
    }

    public static void equal(String expected, String actual, String testName) {
        if (!expected.equals(actual)) {
            StdOut.println(
                    String.format("Test failed: %s. Expected: %s, Actual: %s.",
                                  testName,
                                  expected,
                                  actual));
        }
        else {
            successCount++;
        }

        assertCount++;
    }

    public static void equal(double expected, double actual, String testName) {
        if (expected != actual) {
            StdOut.println(
                    String.format("Test failed: %s. Expected: %f, Actual: %f.",
                                  testName,
                                  expected,
                                  actual));
        }
        else {
            successCount++;
        }

        assertCount++;
    }

    public static void isTrue(boolean actual, String testName) {
        if (!actual) {
            StdOut.println(String.format("Test failed: %s. Expected true, Actual false.", testName)
            );
        }
        else {
            successCount++;
        }

        assertCount++;
    }

    public static void isFalse(boolean actual, String testName) {
        if (actual) {
            StdOut.println(String.format("Test failed: %s. Expected false, Actual true.", testName)
            );
        }
        else {
            successCount++;
        }

        assertCount++;
    }

    public static int assertCount() {
        return assertCount;
    }

    public static int successCount() {
        return successCount;
    }

    public static boolean allTestsPassed() {
        return assertCount == successCount;
    }
}
