package com.bertof.pitestremote;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pily on 06/06/17.
 */

public class AuthenticationTester_test {

    @Test
    public void testAuthentication() throws Exception {
        assertEquals(Boolean.TRUE, com.bertof.pitestremote.API_client.AuthenticationTester.testAuthentication("localhost", 4646, "SK9KRrteDpEzLhAmJSwV"));
    }
}
