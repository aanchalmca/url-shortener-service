package com.example.urlshortenerservice.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Base62EncoderTest {
    private Base62Encoder base62Encoder;
    @Before
    public void setUp() {
        base62Encoder = new Base62Encoder();
    }
    @Test
    public void WhenLongIdIsPassed_Base62CodeIsReurned_1() {
        Assert.assertEquals("015ftgG",base62Encoder.encode(1000000000L) );
    }

    @Test
    public void WhenLongIdIsPassed_Base62CodeIsReurned_2() {
        Assert.assertEquals("01LObWw",base62Encoder.encode(1232300030L) );
    }

    @Test
    public void checkThatEncodingSameNumberTwiceReturnSameValue() {
        String result1 = base62Encoder.encode(1000000000) ;
        String result2 = base62Encoder.encode(1000000000) ;
        Assert.assertEquals(result1, result2);
    }

    @Test
    public void WhenBase62CodeisPassed_ThenDecodeToLongIdValue_1() {
        Assert.assertEquals(1000000000L,base62Encoder.decode("015ftgG"));
    }

    @Test
    public void WhenBase62CodeisPassed_ThenDecodeToLongIdValue_2() {
        Assert.assertEquals(1232300030L,base62Encoder.decode("01LObWw"));
    }

    @Test
    public void checkThatDecodingSameNumberTwiceReturnSameValue() {
        Long result1 = base62Encoder.decode("015ftgG") ;
        Long result2 = base62Encoder.decode("015ftgG") ;
        Assert.assertEquals(result1, result2);
    }
}
