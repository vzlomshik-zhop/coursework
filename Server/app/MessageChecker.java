package app;

import java.io.DataOutputStream;
import java.net.Socket;

public class MessageChecker extends Thread {
    private Socket s;
    private DataBase db;
    private Msg m;
    private RequestProcessor processor;

    public MessageChecker(Socket s, DataBase db, RequestProcessor processor) {
        this.processor = processor;
        this.db = db;
        this.s = s;
    }

    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            while (processor.isAlive()) {
                if ((m = db.checkMessage(processor.getClientName())) != null) {
                    dos.writeUTF("newmessage::" + m.getSender() + "::" + m.getMessage() + "\n");
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
        }
    }
}
