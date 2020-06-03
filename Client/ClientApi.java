import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientApi {
    private int port;
    private String ip;
    private Socket cs;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Messages msg;
    private Window frame;

    public ClientApi(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start(Window frame) throws Exception {
        this.frame = frame;
        this.msg = new Messages();
        this.cs = new Socket(ip, port);
        this.dos = new DataOutputStream(cs.getOutputStream());
        this.dis = new DataInputStream(cs.getInputStream());
        new MessageChecker(this, dis).start();
    }

    public void send(String request) {
        try {
            dos.writeUTF(request);
        } catch (Exception e) {}
    }

    public void getMessagesFromServer(String userName) {
        try {
            this.dos.writeUTF("getmessagesfor::" + userName + "\n");
        } catch (Exception e) {
            System.out.println("An error has occured while sending a request");
        }
    }

    public Messages getMessages() {
        return this.msg;
    }

    public void processAnswer(String ans) {
        String[] s = ans.trim().split("::");
        if (s[0].equals("newmessage")) {
            if (!msg.isPresent(s[1])) {
                frame.addButton(s[1]);
            }
            if (s.length == 2) {
                msg.addMessage(s[1], "");
            } else {
                msg.addMessage(s[1], s[2]);
            }
            if (frame.getUser().equals(s[1])) {
                frame.addText(s[2]);
            }
        }
        if (s[0].equals("messagefrom")) {
            if (s.length == 2) {
                msg.addMessage(s[1], "");
            } else {
                msg.addMessage(s[1], s[2]);
            }
        }
        if  (s[0].equals("messagescopied")) {
            frame.createButtons();
        }
        if (s[0].equals("successfullyentered")) {
            frame.getLoginForm().successfullyEntered();
        }
    }
}
