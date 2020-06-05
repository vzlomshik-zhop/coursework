package app;

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
            RequestProcessor processor = new RequestProcessor(s, db);
            processor.start();
            new MessageChecker(s, db, processor).start();
        }
        ss.close();
    }
}
