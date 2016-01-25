package org.yogurt.protobufftools;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MessageWrapperTest {

    MessageWrapper messageWrapper;

    @Before
    public void setUp() throws Exception {
        messageWrapper = new MessageWrapper();
    }


    @Test
    public void testWrapAndUnwrap() throws Exception {

        byte[] expectedBytes = "messagePayload".getBytes();
        String expectedMesageType = "someMessageType";

        byte[] wrappedMessage = messageWrapper.wrap(expectedMesageType, expectedBytes);
        Message unwrap = messageWrapper.unwrap(wrappedMessage);

        assertArrayEquals(expectedBytes, unwrap.getPayload());
        assertEquals(expectedMesageType, unwrap.getMessageType());
    }
}
