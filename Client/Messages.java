import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;

public class Messages {
    private HashMap<String, Vector<String>> msg;
    private Vector<String> users;
    private int msgAmount;

    public Messages() {
        this.msg = new HashMap<String, Vector<String>>();
        users = new Vector<String>();
    }

    public void addMessage(String userName, String msg) {
        Vector<String> tmsg = getMessagesFromTheUser(userName);
        if (tmsg == null) {
            tmsg = new Vector<String>(1);
            users.add(userName);
        }
        tmsg.add(msg);
        this.msg.put(userName, tmsg);
        msgAmount++;
    }

    public boolean isPresent(String user) {
        Iterator<String> iter = users.iterator();
        boolean f = false;
        while (iter.hasNext() && !f) {
            f = (iter.next().equals(user));
        }
        return f;
    }

    public Vector<String> getMessagesFromTheUser(String userName) {
        return this.msg.get(userName);
    }

    public Vector<String> getUsers() {
        return this.users;
    }
}
