package chatRoom;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class ChatClient {
	DataInputStream in ;
	DataOutputStream out ;
	//BufferedReader in;
	//PrintWriter out;
	private JFrame frame = new JFrame("Chat Room");
	private JTextField textField = new JTextField(40);
	private JTextArea messageArea = new JTextArea(8,40);
	
	public ChatClient(){
		
		messageArea.setEnabled(false);
		textField.setEnabled(false);
		frame.getContentPane().add(textField,"North");
		frame.getContentPane().add(new JScrollPane(messageArea),"Center");
		frame.pack();
		
		textField.addActionListener(new ActionListener(){
		
			public void actionPerformed(ActionEvent e) {
			try {
				out.writeUTF(textField.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			textField.setText("");
		}
			
		});
	}
	//Server name/IP 
	private String getServerAdd(){
		return JOptionPane.showInputDialog(frame, "Enetr Server IP address :",
				"Wlecome to Chat ROOM",JOptionPane.PLAIN_MESSAGE);
	}
	//Client name
	private String getName(){
		return JOptionPane.showInputDialog(frame, "Choose Screen Name :",
				"Screen Name Selection",JOptionPane.PLAIN_MESSAGE);
	}
	//Decisions based server's output
	private void run() throws IOException{
		String ServerName = getServerAdd();
		Socket client = new Socket(ServerName,9091);
		//in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		//out = new PrintWriter(client.getOutputStream(),true);
		in = new DataInputStream(client.getInputStream());
		out = new DataOutputStream(client.getOutputStream());
		while (true){
			//System.out.println("Check 1");
			String line = in.readUTF();
			//System.out.println("Check 1"+line);
			if (line.startsWith("SUBMITNAME")){
				out.writeUTF(getName());
			}else if (line.startsWith("NAMEACCEPTED")){
				textField.setEnabled(true);
			}else if (line.startsWith("MESSAGE")){
				messageArea.append(line.substring(7)+"\n");
				
			}
		}
		
		
		
	}
	public static void main(String [] args) throws IOException{
		ChatClient chatClient = new ChatClient();
		chatClient.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatClient.frame.setLocation(400, 400);
		chatClient.frame.setVisible(true);
		chatClient.run();
	}

}
