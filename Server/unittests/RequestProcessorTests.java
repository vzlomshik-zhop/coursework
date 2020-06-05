package unittests;

import static org.junit.Assert.assertEquals;

import app.DataBase;
import app.RequestProcessor;

import org.junit.Test;

public class RequestProcessorTests {
    @Test
    public void checkRespond() {
        DataBase db = new DataBase();
        RequestProcessor requestProcessor = new RequestProcessor(null, db);
        assertEquals(requestProcessor.parseLine("send::qwerty::123"), "You haven't logged in yet!");
        assertEquals(requestProcessor.parseLine("login::qwerty::123"), "Invalid login or password, try again!");
        assertEquals(requestProcessor.parseLine("getmessagesfor::123"), "proceedcopying::123");
        assertEquals(requestProcessor.parseLine("exit"), "exit");
        assertEquals(requestProcessor.parseLine("abc"), "Unknown command!");
    }
}