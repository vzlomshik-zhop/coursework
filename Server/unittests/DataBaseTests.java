package unittests;

import static org.junit.Assert.assertEquals;

import app.DataBase;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import org.junit.Test;

public class DataBaseTests {
    private DataBase db;

    @Test
    public void checkOnlineList() {
        this.db = new DataBase();
        db.addToOnlineList("Test");
        db.addToOnlineList("just a test");
        db.addToOnlineList("qwerty");
        db.addToOnlineList("123");
        Iterator<String> iter = db.getOnline().iterator();
        assertEquals(iter.next(), "Test");
        assertEquals(iter.next(), "just a test");
        assertEquals(iter.next(), "qwerty");
        assertEquals(iter.next(), "123");
    }

    @Test
    public void checkMessages() {
        try {
            File file = new File(".//" + "::TEST::" + ".txt");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.write(("qwerty" + "::" + "qwerty" + ": " + "it's a test" + "\n").getBytes());
            dos.write(("123" + "::" + "123" + ": " + "it's a test" + "\n").getBytes());
            dos.write(("qwerty123" + "::" + "qwerty123" + ": " + "still a test" + "\n").getBytes());
            dos.write(("TEST" + "::" + "TEST" + ": " + "still" + "\n").getBytes());
            dos.close();
            fos.close();
            assertEquals(db.getMessagesFor("qwerty"), "qwerty" + ": " + "it's a test");
            assertEquals(db.getMessagesFor("123"), "123" + ": " + "it's a test");
            assertEquals(db.getMessagesFor("qwerty123"), "qwerty123" + ": " + "still a test");
            assertEquals(db.getMessagesFor("TEST"), "TEST" + ": " + "still");
            file.delete();
        } catch(Exception e) {}
    }
}