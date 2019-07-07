package Serv;
import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
	public static Vector<ClientHandler> ar = new Vector<>(); 
	static int i = 0; 
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
	   // server is listening on port 1234
	   ServerSocket ss=null;
	   Socket s=null;
       try {
    	   ss = new ServerSocket(4444);
       }
       catch(IOException e) {
    	   System.out.println("Oops "+e.getMessage());   
       }   
       while (true)  
        { 
          try{
        	   s = ss.accept();
           }
           catch(IOException e)
           {
        	   System.out.println("error Oops:"+e.getMessage());
           }
            System.out.println("New client request received : " + s); 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
            //each client has its own input and output stream  
            System.out.println("Creating a new handler for this client..."); 
            //get the name of client
            String name;
            name = dis.readUTF();
            ClientHandler mtch = new ClientHandler(s,name, dis, dos); 
  
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active client list"); 
            // add this client to active clients list 
            ar.add(mtch); 
            System.out.println("added user to active:"+name);
            // start the thread. 
            t.start(); 
            i++; 
        }
      }
    } 


