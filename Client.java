import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import java.io.*; 
import java.net.*; 

public class Client {
	Client mainGUI;
	final static int ServerPort = 4444; 
    JFrame newFrame = new JFrame("Chatsy v0.1");
    JButton sendMessage;
    JTextField messageBox;
    JTextArea chatBox;
    JTextField usernameChooser;
    JTextField recipientChooser;
    JFrame preFrame;
    Socket s=null;
    String username,recipient;
    DataOutputStream dos = null;
	DataInputStream dis = null;
	public static void main(String[] args) {
		  try {
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        Client mainGUI = new Client();
	        mainGUI.preDisplay();
	 }
	  public void preDisplay() {
	        newFrame.setVisible(false);
	        preFrame = new JFrame("Choose your username!(Chatsy v0.1)");
	        usernameChooser = new JTextField();
	        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
	        recipientChooser = new JTextField();
	        JLabel chooseRecipientLabel = new JLabel("Enter recipient name:");
	        JButton enterServer = new JButton("Enter Chat Server");
	        JPanel prePanel = new JPanel(new GridBagLayout());

	        GridBagConstraints preRight = new GridBagConstraints();
	        preRight.anchor = GridBagConstraints.EAST;
	        GridBagConstraints preLeft = new GridBagConstraints();
	        preLeft.anchor = GridBagConstraints.WEST;
	        preRight.weightx = 2.0;
	        preRight.fill = GridBagConstraints.HORIZONTAL;
	        preRight.gridwidth = GridBagConstraints.REMAINDER;

	        prePanel.add(chooseUsernameLabel, preLeft);
	        prePanel.add(usernameChooser, preRight);
	        prePanel.add(chooseRecipientLabel, preLeft);
	        prePanel.add(recipientChooser, preRight);
	        preFrame.add(BorderLayout.CENTER, prePanel);
	        preFrame.add(BorderLayout.SOUTH, enterServer);
	        preFrame.setVisible(true);
	        preFrame.setSize(400, 400);

	        enterServer.addActionListener(new enterServerButtonListener());
	    }
	  public void display() {
	        newFrame.setVisible(true);
	        JPanel southPanel = new JPanel();
	        newFrame.add(BorderLayout.SOUTH, southPanel);
	        southPanel.setBackground(Color.BLUE);
	        southPanel.setLayout(new GridBagLayout());
	        messageBox = new JTextField(40);
	        sendMessage = new JButton("Send Message");
	        chatBox = new JTextArea();
	        chatBox.setEditable(false);
	        newFrame.add(new JScrollPane(chatBox), BorderLayout.CENTER);
             
	        chatBox.setLineWrap(true);

	        GridBagConstraints left = new GridBagConstraints();
	        left.anchor = GridBagConstraints.WEST;
	        GridBagConstraints right = new GridBagConstraints();
	        right.anchor = GridBagConstraints.EAST;
	        right.weightx = 2.0;

	        southPanel.add(messageBox, left);
	        southPanel.add(sendMessage, right);

	        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
	        sendMessage.addActionListener(new sendMessageButtonListener());
	        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        newFrame.setSize(500, 300);
	        Thread readMessage = new Thread(new Runnable()  
	        { 
	          @Override
	          public void run() { 
	                while (true) { 
	                    try { 
	                        // read the message sent to this client 
	                        String msg = dis.readUTF(); 
	                        chatBox.append(msg + "\n");
	                    } catch (IOException e) { 
	                        e.printStackTrace(); 
	                    } 
	                } 
	            } 
	        }); 
	        readMessage.start();
	    }
	  class sendMessageButtonListener implements ActionListener {
		   
	        public void actionPerformed(ActionEvent event) {
	                chatBox.append("< You >:  " + messageBox.getText() + "\n");
	                try {
						dos.writeUTF(recipient+"#"+messageBox.getText());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	    	                   
	                messageBox.setText("");
	            }
	   }

	    class enterServerButtonListener implements ActionListener {
	        public void actionPerformed(ActionEvent event) {
	            username = usernameChooser.getText();
	            recipient = recipientChooser.getText();
	            if (username.length() < 1) {System.out.println("No! WTF "); }
	            else {
	            	
	            preFrame.setVisible(false);
	            InetAddress ip = null;
				try {
					ip = InetAddress.getByName("localhost");
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} 
	  	      // establish the connection 
	  	      Socket s=null;
	  	 
	  	      try{
	  	    	  s = new Socket(ip, ServerPort); 
	  	      }
	  	      catch(IOException e)
	  	      {
	  	    	  System.out.println("Unable to connect to server:"+e.getMessage());
	  	      }
	  	      try {
	  	    	  dis = new DataInputStream(s.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} 
	  	      try {
				dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}    
	  	    //need to register the client in server
		      try{
		    	  dos.writeUTF(username); 
		      }
		      catch (IOException e) { 
	              e.printStackTrace(); 
	        
	            }
		      display();
	            }
	            
	        }
	        }
}
