package com.example.urlshortenerservice.service;

import org.junit.Assert;
import org.junit.Test;

public class Base62EncoderTest {

    @Test
    public void checkEncoding1(){
        Assert.assertEquals("015ftgG",Base62Encoder.encode(1000000000) );
    }

    @Test
    public void checkEncoding2(){
        Assert.assertEquals("01LObWw",Base62Encoder.encode(1232300030) );
    }

    @Test
    public void checkThatEncodingSameNumberTwiceReturnSameValue(){
        String result1 = Base62Encoder.encode(1000000000) ;
        String result2 = Base62Encoder.encode(1000000000) ;
        Assert.assertEquals(result1, result2);
    }

    @Test
    public void checkDecoding1(){
        Assert.assertEquals(1000000000,Base62Encoder.decode("015ftgG"));
    }

    @Test
    public void checkDecoding2(){
        Assert.assertEquals(1232300030,Base62Encoder.decode("01LObWw"));
    }

    @Test
    public void checkThatDecodingSameNumberTwiceReturnSameValue(){
        Long result1 = Base62Encoder.decode("015ftgG") ;
        Long result2 = Base62Encoder.decode("015ftgG") ;
        Assert.assertEquals(result1, result2);
    }
}
