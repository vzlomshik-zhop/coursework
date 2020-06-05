package app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

public class RequestProcessor extends Thread {
    private Socket s;
    private String clientName;
    private DataBase db;
    private DataInputStream dis;
    private DataOutputStream dos;

    public RequestProcessor(Socket s, DataBase db) {
        this.db = db;
        this.s = s;
        this.clientName = "";
    }

    public void run() {
        try {
            this.dis = new DataInputStream(s.getInputStream());
            this.dos = new DataOutputStream(s.getOutputStream());
            String s;
            do {
                s = parseLine(dis.readUTF());
                if (!s.equals("exit") && !s.contains("proceedcopying")) {
                    dos.writeUTF(s + "\n");
                }
                if (s.contains("proceedcopying")) {
                    Vector<String> tmsg = db.getMessagesFor(s.trim().split("::")[1]);
                    Iterator<String> iter = tmsg.iterator();
                    while (iter.hasNext()) {
                        String[] line = iter.next().trim().split("::");
                        if (line.length == 1) {
                            dos.writeUTF("messagefrom::" + line[0] + "::" + "\n");
                        } else {
                            dos.writeUTF("messagefrom::" + line[0] + "::" + line[1] + "\n");
                        }
                    }
                    dos.writeUTF("messagescopied" + "\n");
                }
            } while (!s.equals("exit"));
            dos.close();
            dis.close();
            this.s.close();
        } catch (Exception e) {
            parseLine("exit");
        }
    }

    public String parseLine(String s) {
        String[] sr = s.trim().split("::");
        if (sr[0].equals("help")) {
            return ("Use \"send [receiver] [message]\" to send a message to somebody" + "\r\n" + "Use \"sign up [login] [password]\" to register" + "\r\n" + "Use \"log in [login] [password]\" to login" + "\r\n" + "Use \"exit\" to exit" + "\r\n" + "Use \"online\" to get list of online users" + "\r\n");
        } else {
            if (sr[0].equals("signup") && clientName.equals("")) {
                if (db.addToFile(sr[1], sr[2])) {
                    clientName = sr[1];
                    db.addToOnlineList(clientName);
                    db.sendRegistrationSignal(clientName);
                    return ("successfullyentered");
                } else {
                    return "This login is already claimed, choose another!";
                }
            } else {
                if (sr[0].equals("signup") && !clientName.equals("")) {
                    return ("You're already authorized!");
                }
            }
            if (sr[0].equals("login") && clientName.equals("")) {
                if (db.checkRight(sr[1], sr[2], false)) {
                    clientName = sr[1];
                    db.addToOnlineList(clientName);
                    return  ("successfullyentered");
                } else {
                    return ("Invalid login or password, try again!");
                }
            } else {
                if (sr[0].equals("login") && !clientName.equals("")) {
                    return ("You're already authorized!");
                }
            }
            if (sr[0].equals("exit")) {
                db.removeFromOnline(clientName);
                return "exit";
            }
            if (sr[0].equals("getmessagesfor")) {
                return ("proceedcopying::" + sr[1]);
            }
            if (sr[0].equals("send")) {
                if (!clientName.equals("")) {
                    if (!sr[1].equals("") && !sr[2].equals("")) {
                        db.addMessage(sr[2], clientName, sr[1]);
                        return ("Sended!");
                    } else {
                        return ("Not enough parametrs to use the command!");
                    }
                }
                return ("You haven't logged in yet!");
            }
        }
        if (sr[0].equals("online")) {
            String sussr = "Online: ";
            Vector<String> ussr = db.getOnline();
            for (String tmp: ussr) {
                if (!tmp.equals(ussr.lastElement())) {
                    sussr = sussr + tmp + ", ";
                } else {
                    sussr = sussr + tmp;
                }
            }
            if (sussr.equals("Online: ")) {
                return ("There are no users online");
            } else {
                return sussr;
            }
        }
        return ("Unknown command!");
    }

    public String getClientName() {
        return this.clientName;
    }
}
