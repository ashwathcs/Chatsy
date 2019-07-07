package Serv;
import java.util.*;
import java.io.*;
import java.net.*;
public class ClientHandler implements Runnable {
	 Scanner scn = new Scanner(System.in); 
	 private String name; 
	 final DataInputStream dis; 
	 final DataOutputStream dos; 
	 Socket s; 
	 boolean isloggedin;
	 public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) { 
		  this.dis = dis; 
		  this.dos = dos; 
		  this.name = name; 
		  this.s = s; 
		  this.isloggedin=true; 
	 }
	 @Override
	    public void run() { 
	  
	        String received; 
	        while (true)  
	        { 
	            try
	            { 
	                received = dis.readUTF(); 
	                  
	                System.out.println(received); 
	                  
	                if(received.equals("logout")){ 
	                    this.isloggedin=false; 
	                    this.s.close(); 
	                    System.out.println("closing connection to "+name);
	                    break; 
	                } 
	                  
	                // break the string into message and recipient part 
	                StringTokenizer st = new StringTokenizer(received, "#"); 
	                String recipient = st.nextToken(); 
	                String MsgToSend = st.nextToken(); 
	                
	                // search for the recipient in the connected devices list. 
	                // ar is the vector storing client of active users 
	                for (ClientHandler mc : Server.ar)  
	                { 
	                    // if the recipient is found, write on its 
	                    // output stream 
	                	System.out.println("searching......name :"+mc.name+"recipient: "+recipient);
	                	
	                    if (mc.name.equals(recipient) && mc.isloggedin==true)  
	                    { 
	                        mc.dos.writeUTF(this.name+" : "+MsgToSend); 
	                        break; 
	                    } 
	                   
	                } 
	                System.out.println("No recipient found !!!!!");
	            } catch (IOException e) { 
	                  
	                e.printStackTrace(); 
	            } 
	              
	        } 
	        try
	        { 
	            // closing resources 
	            this.dis.close(); 
	            this.dos.close(); 
	              
	        }catch(IOException e){ 
	            e.printStackTrace(); 
	        } 
	    } 
}
