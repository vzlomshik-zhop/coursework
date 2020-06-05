import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

public class DataBase {
    private Vector<Msg> vm;
    private Vector<String> online;

    public DataBase() {
        vm = new Vector<Msg>(5);
        online = new Vector<String>(5);
    }

    public void addToOnlineList(String cn) {
        online.add(cn);
    }

    public Vector<String> getOnline() {
        return online;
    }

    public void removeFromOnline(String cn) {
        online.removeElement(cn);
    }

    public boolean isOnline(String userName) {
        Iterator<String> iter = online.iterator();
        boolean f = false;
        while (iter.hasNext() && !f) {
            f = (iter.next().equals(userName));
        }
        return f;
    }

    public boolean checkRight(String l, String p, boolean tf) {
        try {
            boolean f = false;
            Scanner sc = new Scanner(new FileInputStream(".//Accounts.txt"));
            while (sc.hasNext() && !f) {
                String[] t = sc.nextLine().split("::");
                if (t[0].equals(l)) {
                    if (tf) {
                        return true;
                    } else {
                        if (t[1].equals(p)) {
                            return true;
                        }
                    }
                }
            }
            sc.close();
            return f;
        } catch (Exception e) {}
        return false;
    }

    public boolean addToFile(String l, String p) {
        try {
            FileOutputStream fos = new FileOutputStream(".//Accounts.txt", true);
            DataOutputStream dos = new DataOutputStream(fos);
            if (!checkRight(l, p, true)) {
                dos.write((l + "::" + p + "\n").getBytes());
                dos.close();
                fos.close();
                return true;
            }
            dos.close();
            fos.close();
        } catch (Exception e) {}
        return false;
    }

    public void addMessageToFile(String sender, String receiver, String msg) {
        try {
            File fsender = new File(".//" + sender + ".txt");
            fsender.createNewFile();
            File freceiver = new File(".//" + receiver + ".txt");
            freceiver.createNewFile();
            FileOutputStream fos = new FileOutputStream(freceiver, true);
            FileOutputStream fos1 = new FileOutputStream(fsender, true);
            DataOutputStream dos = new DataOutputStream(fos);
            DataOutputStream dos1 = new DataOutputStream(fos1);
            dos.write((sender + "::" + sender + ": " + msg + "\n").getBytes());
            dos1.write((receiver + "::" + "You: " + msg + "\n").getBytes());
            dos.close();
            fos.close();
            dos1.close();
            fos1.close();
        } catch(Exception e) {}
    }

    public Msg checkMessage(String cn) {
        if (!vm.isEmpty()) {
            for (Msg tmp: vm) {
                if (tmp.getReceiver().equals(cn)) {
                    vm.removeElement(tmp);
                    return tmp;
                }
            }
        }
        return null;
    }

    public Vector<String> getMessagesFor(String user) {
        try {
            Vector<String> t = getUsernames(user);
            File fuser = new File(".//" + user + ".txt");
            if (!fuser.exists()) {
                return t;
            }
            Scanner sc = new Scanner(new FileInputStream(fuser));
            while (sc.hasNext()) {
                t.add(sc.nextLine());
            }
            sc.close();
            return t;
        } catch(Exception e) {}
        return null;
    }

    public Vector<String> getUsernames(String exception) {
        try {
            Vector<String> users = new Vector<String>();
            Scanner sc = new Scanner(new FileInputStream(".//Accounts.txt"));
            while (sc.hasNext()) {
                String t = sc.nextLine().split("::")[0];
                if (!t.equals(exception)) {
                    users.add(t);
                }
            }
            sc.close();
            return users;
        } catch(Exception e) {}
        return null;
    }

    public void addMessage(String mesg, String cn, String receiver) {
        addMessageToFile(cn, receiver, mesg);
        if (isOnline(receiver)) {
            vm.add(new Msg(cn + ": " + mesg, cn, receiver));
        }
        vm.add(new Msg("You: " + mesg, receiver, cn));
    }

    public void sendRegistrationSignal(String cn) {
        Iterator<String> iter = getUsernames(cn).iterator();
        while (iter.hasNext()) {
            String user = iter.next();
            if (isOnline(user)) {
                vm.add(new Msg("", cn, user));
            }
        }
    }
}
