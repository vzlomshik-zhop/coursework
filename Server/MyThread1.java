import java.net.Socket;
import java.io.DataOutputStream;

public class MyThread1 extends Thread {
    private Socket s;
    private DataBase db;
    private Msg m;
    private MyThread mt;

    public MyThread1(Socket s, DataBase db, MyThread mt) {
        this.mt = mt;
        this.db = db;
        this.s = s;
    }

    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            while (mt.isAlive()) {
                if ((m = db.checkMessage(mt.getClientName())) != null) {
                    dos.writeUTF("newmessage::" + m.getSender() + "::" + m.getMessage() + "\n");
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
        }
    }
}
