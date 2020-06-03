import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class Window extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = -517906046295582614L;
    private ClientApi client;
    private String user;
    private JPanel chats;
    private JTextArea text;
    private LoginForm form;

    public Window() {
        super("chatik");
        this.user = null;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setVisible(false);
        setResizable(false);
        getContentPane().add(createUI());
    }

    public JSplitPane createUI() {
        chats = new JPanel();
        chats.setLayout(new BoxLayout(chats, BoxLayout.Y_AXIS));

        text = new JTextArea();
        text.append("");
        text.setDisabledTextColor(new Color(0, 0, 0));
        text.setEditable(false);
        JTextArea inputfield = new JTextArea();
        inputfield.setWrapStyleWord(true);
        inputfield.setAutoscrolls(true);
        inputfield.setLineWrap(true);
        inputfield.setPreferredSize(new Dimension(500, 76));
        JPanel lowermenu = new JPanel();
        lowermenu.setLayout(new BoxLayout(lowermenu, BoxLayout.X_AXIS));
        JButton sendbutton = new JButton("SEND");
        sendbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (user != null) {
                    sendForm("send::" + user + "::" + inputfield.getText());
                    inputfield.setText("");
                }
            }
        });
        lowermenu.add(inputfield);
        lowermenu.add(sendbutton);
        inputfield.setWrapStyleWord(true);
        inputfield.setAutoscrolls(true);
        inputfield.setLineWrap(true);
        inputfield.setPreferredSize(new Dimension(500, 76));
        JScrollPane scrollpane = new JScrollPane(chats);
        JScrollPane textpane = new JScrollPane(text);
        JSplitPane chatbox = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textpane, lowermenu);
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollpane, chatbox);
        splitpane.setEnabled(false);
        splitpane.setDividerSize(0);
        splitpane.setDividerLocation(200);
        chatbox.setEnabled(false);
        chatbox.setDividerSize(0);
        chatbox.setDividerLocation(500);
        return splitpane;
    }

    public void init() {
        try {
            this.client = new ClientApi("localhost", 3128);
            client.start(this);
            form = new LoginForm(this);
        } catch (Exception e) {
            System.out.println("Unable to connect to the server");
        }
    }

    public void setUser(String user) {
        this.user = user;
        showMessagesFromUser(user);
    }

    public String getUser() {
        return user;
    }

    public void addText(String text) {
        if (!text.equals("")) {
            this.text.append(text + "\n");
        }
    }

    public void getMessages(String user) {
        this.client.getMessagesFromServer(user);
    }


    public void showMessagesFromUser(String user) {
        text.setText("");
        Vector<String> tmsg = client.getMessages().getMessagesFromTheUser(user);
        Iterator<String> iter = tmsg.iterator();
        while (iter.hasNext()) {
            addText(iter.next());
        }
    }

    public void createButtons() {
        Iterator<String> iter = client.getMessages().getUsers().iterator();
        while (iter.hasNext()) {
            chats.add(new UserButton(iter.next(), this));
        }
        setVisible(true);
    }

    public void addButton(String user) {
        chats.add(new UserButton(user, this));
        revalidate();
    }

    public void sendForm(String request) {
        client.send(request);
    }

    public LoginForm getLoginForm() {
        return this.form;
    }
}
