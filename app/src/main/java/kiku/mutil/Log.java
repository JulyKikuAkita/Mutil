package kiku.mutil;

import java.lang.reflect.Method;

/**
 * Logging class for Selenium tests. Given a test name, it will include the test name in
 * the prefix for any debug/warn/error log call.
 *
 */
public class Log {

    private static final ThreadLocal<String> sThreadTestName = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "";
        }
    };

    public static void setTestName(String testName) {
        sThreadTestName.set(testName);
    }

    public static String getTestName() {
        String testName = sThreadTestName.get();
        if ("".equals(testName)) {
            testName = getCurrentClassName();
        }
        return testName;
    }

    public static String getCurrentClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Skip 0 - it's always getStackTrace().
        for (int i = 1; i < stackTrace.length; i++) {
            try {
                Class<?> testClass = Class.forName(stackTrace[i].getClassName());
                Method method = testClass.getMethod(stackTrace[i].getMethodName());
                if (testClass.getCanonicalName().contains("MemCache")) {
                    return String.format("%s.%s", testClass.getCanonicalName(), stackTrace[i].getMethodName());
                }
            } catch (Exception e) {}
        }
        return "";
    }

    public static void unsetTestName() {
        sThreadTestName.remove();
    }

    public static void d(String fmt, Object... args) {
        String prefix = String.format("d(%s): ", getTestName());
        String output = prefix+String.format(fmt, args);
        System.out.println(output);
    }

    public static void w(String fmt, Object... args) {
        String prefix = String.format("w(%s): ", getTestName());
        String output = prefix+String.format(fmt, args);
        System.err.println(output);
    }

    public static void e(String fmt, Object... args) {
        String prefix = String.format("e(%s): ", getTestName());
        String output = prefix+String.format(fmt, args);
        System.err.println(output);
    }

    public static void printHorizontalLine() {
        System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
                + "+++++++++++++++++++++++++++++++++++++++++++");
    }
}
