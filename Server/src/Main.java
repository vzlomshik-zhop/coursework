import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

class Main {
  public static void main(String argv[]) throws Exception {
    new Server(3128).start();
  }
}

class Server {
  int port;
  String serverName;
  String buffer;
  DataBase db;
  
  public Server(int port) {
    this.port = port;
    this.buffer = "";
  }
  
  public void start() throws Exception {
    ServerSocket ss = new ServerSocket(port);
    db = new DataBase();
    while (true) {
      Socket s = ss.accept();    
      MyThread mt = new MyThread(s, s.getInetAddress().toString(), db);
      mt.start();
      new MyThread1(s, db, mt).start();
    }
  }
}

class Msg {
  String message;
  String sender;
  String receiver;
  
  public Msg(String message, String sender, String receiver) {
	  this.message = message;
	  this.sender = sender;
	  this.receiver = receiver;
  }
}

class DataBase {
  Vector <Msg> vm;
  Vector <String> online;
  
  public DataBase() {
    vm = new Vector <Msg>(5);
    online = new Vector <String>(5);
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
  
  public boolean checkRight(String l, String p, boolean tf) {
	try {
	  boolean f = false;
    Scanner sc = new Scanner(new FileInputStream("Accounts.txt"));
    while (sc.hasNext() && f == false) {
	  String t[] = sc.nextLine().split("::");
    if (t[0].equals(l))
	    if (tf)
	      return true;
	    else {
	      if (t[1].equals(p))
	        return true;
		  }
	  }
	  sc.close();
	  return f;
    } catch (Exception e) {

	  }
	return false;
  }
  
  public boolean addToFile(String l, String p) {
	  try {
	    FileOutputStream fos = new FileOutputStream("Accounts.txt", true);
      DataOutputStream dos = new DataOutputStream(fos);
	    if (!checkRight(l, p, true)) {
	      dos.write((l + "::" + p + "\n").getBytes());
	      dos.close();
        fos.close();
        return true;
      }
      return false;
    } catch(Exception e) {
	  
	  }
	  return false;
  }
  
  public Msg checkMessage(String cn) {
	  if (!vm.isEmpty()) {
			for (Msg tmp: vm) {
			  if (tmp.receiver.equals(cn)) {
					vm.removeElement(tmp);
					return tmp;
				}
			}
		}
		return null;
  }
  
  public void addMessage(String mesg, String cn, String receiver) {
    vm.add(new Msg(mesg, cn, receiver));
  }
}

class MyThread extends Thread {
  Socket s;
  String clientName;
  DataBase db;
  DataInputStream dis;
  DataOutputStream dos;
  
  public MyThread(Socket s, String sn, DataBase db) {
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
        if (!s.equals("exit"))
          dos.writeUTF(s + "\n");
      } while (!s.equals("exit"));
      dos.close();
      dis.close();
      this.s.close();
    } catch (Exception e) {
	    parseLine("exit");
	  }
  }
  
  public String parseLine(String s) {
    String sr[] = s.trim().split("::");
    if (sr[0].equals("help")) {
	    return ("Use \"send [receiver] [message]\" to send a message to somebody" + "\r\n" + "Use \"sign up [login] [password]\" to register" + "\r\n" + "Use \"log in [login] [password]\" to login" + "\r\n" + "Use \"exit\" to exit" + "\r\n" + "Use \"online\" to get list of online users" + "\r\n");
	  } else {
	    if (sr[0].equals("sign up") && clientName.equals("")) {
	      if (db.addToFile(sr[1], sr[2])) {
	        clientName = sr[1];
	        db.addToOnlineList(clientName);
	        return ("Signing up was successful!" + "\r\n");
	      } else {
	        return "This login is already claimed, choose another!";
	      }
	    } else {
	      if (sr[0].equals("sign up") && !clientName.equals(""))
	        return("You're already authorized!" + "\r\n");
	    }
	    if (sr[0].equals("log in") && clientName.equals("")) {
	      if (db.checkRight(sr[1], sr[2], false)) {
		    clientName = sr[1];
		    db.addToOnlineList(clientName);
		    return ("Loginning in was successful!" + "\r\n");
		  } else {
		    return("Invalid login or password, try again!" + "\r\n");
		  }
	    } else {
	      if (sr[0].equals("log in") && !clientName.equals(""))
	        return ("You're already authorized!" + "\r\n");
	    }
	    if (sr[0].equals("exit")) {
				db.removeFromOnline(clientName);
	      return "exit";
      }
      if (sr[0].equals("send")) {
	      if (!clientName.equals("")) {
				  if (!sr[1].equals("") && !sr[2].equals("")) {
				    db.addMessage(sr[2], clientName, sr[1]);
				    return ("Sended!" + "\r\n");
					} else
				    return ("Not enough parametrs to use the command!" + "\r\n");
				}
				return ("You haven't logged in yet!" + "\r\n");
	    }
	  }
	  if (sr[0].equals("online")) {
			String sussr = "Online: ";
			Vector <String> ussr = db.getOnline();
			for (String tmp: ussr) {
				if (!tmp.equals(ussr.lastElement()))
			    sussr = sussr + tmp + ", ";
			  else
			    sussr = sussr + tmp;
			}
			if (sussr.equals("Online: "))
			  return ("There are no users online" + "\r\n");
			else
			  return sussr + "\r\n";
		}
	  return ("Unknown command!" + "\r\n");
  } 
}

class MyThread1 extends Thread {
  Socket s;
  DataBase db;
  Msg m;
  String t = "";
  MyThread mt;
  
  public MyThread1(Socket s, DataBase db, MyThread mt) {
	  this.mt = mt;
	  this.db = db;
    this.s = s;
  }
  
  public void run() {
	  try {
	    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
	    while (mt.isAlive()) {
				if ((m = db.checkMessage(mt.clientName)) != null)
				  dos.writeUTF(m.sender + ": " + m.message + "\n");
        Thread.sleep(500);         
	    }
    } catch(Exception e) {
	  }
  }
}
