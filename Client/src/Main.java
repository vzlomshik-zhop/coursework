import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

class Main {
  public static void main(String argv[]) throws Exception {
    new Client("localhost", 3128).start();
  }
}

class Client {
  int port;
  String ip;
  
  public Client(String ip, int port) {
	this.ip = ip;
    this.port = port;
  }
  
  public void start() throws Exception {
    Socket cs = new Socket(ip, port);
    Scanner sc = new Scanner(System.in);
    String c = "";
    String s[];
    DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
    DataInputStream dis = new DataInputStream(cs.getInputStream());
    MyThread mt = new MyThread(dis);
    mt.start();
    while (!c.equals("exit")) {
      c = sc.nextLine();
      s = c.trim().split("::");
      if (s[0].equals("online") | s[0].equals("help") | s[0].equals("exit"))
        dos.writeUTF(c + "\n");
      else {
        if (s[0].equals("log in") | s[0].equals("sign up")) {
		  if (s.length == 3)
            dos.writeUTF(c + "\n");
          else
            System.out.println("Parameters don't match!\n");
		} else 
	      if (s[0].equals("send")) {
			if (s.length >= 3)
              dos.writeUTF(c + "\n");
            else 
              System.out.println("Parameters don't match!\n");
		  } else
            dos.writeUTF("null" + "\n");
		}
    }
    dis.close();
    dos.close();
    cs.close();
    sc.close();
  }
}

class MyThread extends Thread{
  DataInputStream dis;
  Client c;
  
  public MyThread(DataInputStream dis) {
    this.dis = dis;
  }
  
  public void run() {
    while (true) {
	  try {
	    System.out.println(dis.readUTF());
	  } catch(Exception e) {
	  }
	}
  }
}
