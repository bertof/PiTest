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

    /** Test if the server is running
     * @throws Exception java.lang.AssertionError
     */
    @Test
    public void test_connection_to_server() throws Exception {
        assertEquals(com.bertof.pitestremote.API_client.ConnectionTester.testConnection(new URL("http://localhost:4646")), true);
    }
}