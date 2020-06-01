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

class Messages {
	HashMap<String, Vector<String>> msg;
	int msgAmount;

	public Messages(int msgAmount) {
		this.msg = new HashMap<String, Vector<String>>(msgAmount);
		this.msgAmount = msgAmount;
	}

	public void addMessage(String userName, String msg) {
		Vector<String> tmsg = getMessagesFromTheUser(userName);
		if (tmsg == null)
			tmsg = new Vector<String>(1);
		tmsg.add(msg);
		this.msg.put(userName, tmsg);
		msgAmount++;
	}

	public Vector<String> getMessagesFromTheUser(String userName) {
		return this.msg.get(userName);
	}
}

class MyFrame extends JFrame {
	ClientApi client;
	String currentUser;
	String user;
	Messages msg;

	public MyFrame() {
		super("chatik");
		createUI();
		this.currentUser = null;
		this.user = null;
		this.msg = null;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1280, 720);
	}

	public void createUI() {
		JPanel chats = new JPanel();
		chats.setLayout(new BoxLayout(chats, BoxLayout.Y_AXIS));
		JScrollPane scrollpane = new JScrollPane(chats);
		JTextArea text = new JTextArea();
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
											if (currentUser != null) {
												client.createSendingForm(currentUser, inputfield.getText());
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
		getContentPane().add(splitpane);
	}

	public void init() {
		try {
			this.client = new ClientApi("localhost", 3128);
			client.start();
			getMessages();
		} catch(Exception e) {
			System.out.println("Unable to connect to the server");
			setVisible(true);
			//System.exit(0);
		}
	}
	
	public void getMessages() {
		this.client.getMessagesFromServer(currentUser); 
	}
}

class ClientApi {
	int port;
	String ip;
	Socket cs;
	DataOutputStream dos;
	DataInputStream dis;

	public ClientApi(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void start() throws Exception {
		this.cs = new Socket(ip, port);
		//Scanner sc = new Scanner(System.in);
		//String c = "";
		//String s[];
		this.dos = new DataOutputStream(cs.getOutputStream());
		this.dis = new DataInputStream(cs.getInputStream());
		MyThread mt = new MyThread(dis);
		mt.start();
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

	public void createSendingForm(String currentUser, String text) {
		try {
			dos.writeUTF("send::" + currentUser + "::" + text);
		} catch (Exception e) {
		}
	}

	public void getMessagesFromServer(String userName) {
		try {
			this.dos.writeUTF("getMessagesFor::" + userName + "\n");
		} catch (Exception e) {
			System.out.println("An error has occured while sending a request");
		}
	}

	public void processAnswer(String ans) {
		//TBD process answer from the server and add to the msg object
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
