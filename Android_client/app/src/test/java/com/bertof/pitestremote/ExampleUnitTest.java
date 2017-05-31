package com.bertof.pitestremote;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_connection_to_server() throws Exception {
        assertEquals(com.bertof.pitestremote.SetupConnection.testConnection(new URL("http://localhost:4646")), true);
    }
}