import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Main {
	public static void main(String argv[]) throws Exception {
		new MyFrame().init();
	}
}

class LoginForm extends JDialog {	
	MyFrame frame;
	String userName;

	public LoginForm(MyFrame frame) {
		super(frame, "Login");
		this.frame = frame;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 200));
		getContentPane().add(createUI());
		setResizable(false);
		setVisible(true);
	}

    public JPanel createUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		JPanel name = new JPanel();
		name.setLayout(new BoxLayout(name, BoxLayout.X_AXIS));
		JLabel nameLabel = new JLabel("Login:         ");
		name.add(nameLabel);
		name.add(Box.createHorizontalStrut(12));
		JTextField login = new JTextField(15);
		name.add(login);
		JPanel password = new JPanel();
		password.setLayout(new BoxLayout(password, BoxLayout.X_AXIS));
		JLabel passwrdLabel = new JLabel("Password:");
		password.add(passwrdLabel);
		password.add(Box.createHorizontalStrut(12));
		JTextField passwd = new JTextField(15);
		password.add(passwd);
		JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		JPanel grid = new JPanel(new GridLayout(1, 2, 5, 0));
		JButton loginButton = new JButton("Log in");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setCurrentUser(login.getText());
				frame.sendForm("login::" + login.getText() + "::" + passwd.getText());
				userName = login.getText();
			}
		});
		JButton signupButton = new JButton("Sign up");
		signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setCurrentUser(login.getText());
				frame.sendForm("signup::" + login.getText() + "::" + passwd.getText());
				userName = login.getText();
			}
		});
		grid.add(loginButton);
		grid.add(signupButton);
		flow.add(grid);
		panel.add(name);
		panel.add(Box.createVerticalStrut(12));
		panel.add(password);
		panel.add(Box.createVerticalStrut(17));
		panel.add(flow);
		return panel;
	}

	public void successfullyEntered() {
		setVisible(false);
		frame.getMessages(userName);
		frame.setVisible(true);
	}
}

class Messages {
	HashMap<String, Vector<String>> msg;
	Vector<String> users;
	int msgAmount;

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

	public Vector<String> getMessagesFromTheUser(String userName) {
		return this.msg.get(userName);
	}

	public Vector<String> getUsers() {
		return this.users;
	}
}

class UserButton extends JButton {
	String userName;
	MyFrame frame;

	public UserButton(String userName, MyFrame frame) {
		super(userName);
		setPreferredSize(new Dimension(196, 100));
		setMaximumSize(new Dimension(196, 100));
		this.userName = userName;
		this.frame = frame;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setUser(userName);
			}
		});
	}
}

class MyFrame extends JFrame {
	ClientApi client;
	String currentUser;
	String user;
	JPanel chats;
	JTextArea text;
	LoginForm form;

	public MyFrame() {
		super("chatik");
		createUI();
		this.currentUser = null;
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
		JScrollPane scrollpane = new JScrollPane(chats);
		text = new JTextArea();
		////////////////
		text.append("");
		text.setDisabledTextColor(new Color(0, 0, 0));
		text.setEditable(false);
		///////////////
		JScrollPane textpane = new JScrollPane(text);
		JPanel lowermenu = new JPanel();
		JTextArea inputfield = new JTextArea();
		////////////////
		inputfield.setWrapStyleWord(true);
		inputfield.setAutoscrolls(true);
		inputfield.setLineWrap(true);
		inputfield.setPreferredSize(new Dimension(500, 76));
		////////////////
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
		////////////////////
		inputfield.setWrapStyleWord(true);
		inputfield.setAutoscrolls(true);
		inputfield.setLineWrap(true);
		inputfield.setPreferredSize(new Dimension(500, 76));
		////////////////////
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
			client.start(currentUser, this);
			form = new LoginForm(this);
		} catch (Exception e) {
			System.out.println("Unable to connect to the server");
		}
	}

	public void setCurrentUser(String userName) {
		this.currentUser = userName;
	}

	public void setUser(String user) {
		this.user = user;
		showMessagesFromUser(user);
	}
	
	public String getUser() {
		return user;
	}

	public void addText(String text) {
		if (!text.equals(""))
			this.text.append(text + "\n");
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

	public void sendForm(String request) {
		client.send(request);
	}

	public LoginForm getLoginForm() {
		return this.form;
	}
}

class ClientApi {
	int port;
	String ip;
	Socket cs;
	DataOutputStream dos;
	DataInputStream dis;
	Messages msg;
	String userName;
	int msgAmount;
	MyFrame frame;

	public ClientApi(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void start(String userName, MyFrame frame) throws Exception {
		this.frame = frame;
		this.msg = new Messages();
		this.cs = new Socket(ip, port);
		this.dos = new DataOutputStream(cs.getOutputStream());
		this.dis = new DataInputStream(cs.getInputStream());
		MyThread mt = new MyThread(this, dis);
		mt.start();
		this.userName = userName;
		//this.frame = frame;
		/*while (!c.equals("exit")) {
		 * c = sc.nextLine();
		 * s = c.trim().split("::");
		 * if (s[0].equals("online") | s[0].equals("help") | s[0].equals("exit"))
		 * dos.writeUTF(c + "\n");
		 * else {
		 * if (s[0].equals("log in") | s[0].equals("sign up")) {
		 * if (s.length == 3)
		 * dos.writeUTF(c + "\n");
		 * else
		 * System.out.println("Parameters don't match!\n");
		 * } else
		 * if (s[0].equals("send")) {
		 * if (s.length >= 3)
		 * dos.writeUTF(c + "\n");
		 * else
		 * System.out.println("Parameters don't match!\n");
		 * } else
		 * dos.writeUTF("null" + "\n");
		 * }
		 * }*/
		//dis.close();
		//dos.close();
		//cs.close();
		//sc.close();
		}

	public void send(String request) {
		try {
			dos.writeUTF(request);
		} catch (Exception e) {
		}
	}

	public void getMessagesFromServer(String userName) {
		try {
			this.dos.writeUTF("getmessagesfor::" + userName + "\n");
		} catch (Exception e) {
			System.out.println("An error has occured while sending a request");
		}
	}

	/*public void getUsersFromServer() {
		try {
			this.dos.writeUTF("getusers" + "\n");
		} catch (Exception e) {
			System.out.println("An error has occured while sending a request");
		}
	}*/

	public Messages getMessages() {
		return this.msg;
	}

	public void processAnswer(String ans) {
		//TBD process answer from the server and add to the msg object
		String s[] = ans.trim().split("::");
		System.out.println(ans);
		if (s[0].equals("newmessage")) {
			if (s.length == 2)
				msg.addMessage(s[1], "");
			else
				msg.addMessage(s[1], s[2]);
			if (frame.getUser().equals(s[1]))
				frame.addText(s[2]);
		}
		if (s[0].equals("messagefrom")) {
			if (s.length == 2)
				msg.addMessage(s[1], "");
			else
				msg.addMessage(s[1], s[2]);
		}
		if  (s[0].equals("messagescopied")) {
			frame.createButtons();
		}
		if (s[0].equals("successfullyentered")) {
			frame.getLoginForm().successfullyEntered();
		}
		/*this.msg = new Messages(5);
		msg.addMessage("qwert", "221118");
		msg.addMessage("qwert1", "22123131238\r\n\r\n");
		msg.addMessage("qwert2", "221231238\r\n\r\n\r");
		msg.addMessage("qwert3", "2212318");
		msg.addMessage("qwert4", "221231231238");
		frame.setMessages(msg);
		frame.createButtons();
		//if (s[0].equals()) {

		//}*/
	}
}

class MyThread extends Thread {
	DataInputStream dis;
	ClientApi client;

	public MyThread(ClientApi client, DataInputStream dis) {
		this.client = client;
		this.dis = dis;
	}

	public void run() {
		while (true) {
			try {
				client.processAnswer(dis.readUTF());
			} catch(Exception e) {
			}
		}
	}
}
