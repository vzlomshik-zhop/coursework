import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private int port;
    private DataBase db;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        ServerSocket ss = new ServerSocket(port);
        db = new DataBase();
        while (!ss.isClosed()) {
            Socket s = ss.accept();
            MyThread mt = new MyThread(s, s.getInetAddress().toString(), db);
            mt.start();
            new MyThread1(s, db, mt).start();
        }
        ss.close();
    }
}
