package com.bertof.pitestremote;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class ConnectionTester_test {

    /**
     * Test if the server is running
     *
     * @throws Exception java.lang.AssertionError
     */
    @Test
    public void test_connection_to_server() throws Exception {
        assertEquals("Returns true if it finds a server to the address given", true, com.bertof.pitestremote.API_client.ConnectionTester.testConnection(new URL("http://localhost:4646")));
    }
}