//package bgu.spl.mics;
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Before;
//import org.junit.After;
//import org.junit.Test;
//
//import bgu.spl.mics.Future;
//
//import java.util.concurrent.TimeUnit;
//
//
//public class FutureTest {
//
//    private static Future future;
//
//
//    @Before
//    public void setUp() throws Exception {
//        future = new Future<>();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        future = null;
//    }
//
//
//    @Test
//    public void get() {
//        assertFalse(future.isDone());
//        future.resolve("");
//        future.get();
//        assertTrue(future.isDone());
//    }
//
//    @Test
//    public void resolve() {
//        Integer x = 3;
//        future.resolve(x);
//        assertEquals(future.get(), x);
//        assertThrows(Exception.class, ()-> future.isDone());
//    }
//
//
//
//    @Test
//    public void isDone() {
//        String str = "someResult";
//        assertFalse(future.isDone());
//        future.resolve(str);
//        assertTrue(future.isDone());
//    }
//
//    @Test
//    public void testGet() throws InterruptedException {
//        assertFalse(future.isDone());
//        future.get(100,TimeUnit.MILLISECONDS);
//        assertFalse(future.isDone());
//        future.resolve("foo");
//        assertEquals(future.get(100,TimeUnit.MILLISECONDS),"foo");
//    }
//}