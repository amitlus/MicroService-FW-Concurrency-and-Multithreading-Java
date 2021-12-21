package bgu.spl.mics;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import bgu.spl.mics.Future;

import java.util.concurrent.TimeUnit;


public class FutureTest {

    private static Future future;


    @Before
    public void setUp() throws Exception {
        future = new Future();
    }

    @After
    public void tearDown() throws Exception {
        future = null;
    }


    @Test
    public void get() {
    }

    @Test
    public void resolve() {
        Integer x = 3;
        future.resolve(x);
        assertEquals(future.get(), x);
        assertThrows(Exception.class, ()-> future.isDone());
    }



    @Test
    public void isDone() {
        future.resolve(3);
        assertTrue(future.isDone());
    }

    @Test
    public void testGet() {
        assertEquals(null, future.get(1, TimeUnit.SECONDS));
        future.resolve(3);
        Integer x = 3;
        assertEquals(x, future.get(1, TimeUnit.SECONDS));
    }
}