package rea;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Test @{link {@link ReaException}} constructors.
 */
class ReaExceptionTest {

    /**
     * Test empty constructor
     */
    @Test
    void testConstructor_empty() {
        assertInstanceOf( Exception.class , new ReaException() ) ;


    }

    /**
     * Test constructor with message.
     */
    @Test
    void testConstructor_string() {
        String message = "message";

        ReaException reaException = new ReaException(message);

        assertEquals(message, reaException.getMessage());
    }

    /**
     * Test constructor with message and throwable.
     */
    @Test
    void testConstructor_string_throwable() {
        String message = "message";
        Throwable throwable = new Throwable();

        ReaException reaException = new ReaException(message,throwable);

        assertEquals(message, reaException.getMessage());
        assertEquals(throwable, reaException.getCause());
    }

    /**
     * Test constructor with throwable.
     */
    @Test
    void testConstructor_throwable() {
        Throwable throwable = new Throwable();

        ReaException reaException = new ReaException(throwable);

        assertEquals(throwable, reaException.getCause());
    }

    /**
     * Test constructor with message and throwable and boolean flags.
     */
    @Test
    void testConstructor_string_throwable_boolean_boolean() {
        String message = "message";
        Throwable throwable = new Throwable();
        boolean enableSuppression = true;
        boolean writableStackTrace = true;

        ReaException reaException = new ReaException(message,throwable,enableSuppression,writableStackTrace);

        assertEquals(message, reaException.getMessage());
        assertEquals(throwable, reaException.getCause());
    }

}