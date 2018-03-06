package chatRoom;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.io.BufferedReader;
import java.util.HashSet;

public class ChatServer {
	
	//Assign port number which is used by all client to get into chat room
	private static final int PORT = 9090;
	
	 //Use hash file to keep track of name, to avoid duplicate name
	
	private static HashSet<String> names = new HashSet<String>();
	
	//similarly use hash file for writer making easy to broadcast
	
	//private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	private static HashSet<DataOutputStream> writers = 
			new HashSet<DataOutputStream>();			
	public static void main(String [] args) throws Exception{
		
		System.out.println("Server for chat is running...");
		ServerSocket listner = new ServerSocket(PORT);
		try{
			while (true){
				
					new ChatToMany(listner.accept()).start();
					System.out.println("Check point ///");
			    }
			}finally{
				listner.close();
		}
		
	}
	private static class ChatToMany extends Thread{
		private Socket socket;
		private DataInputStream in ;
		private DataOutputStream out ;
		//private BufferedReader in;
		//private PrintWriter out;
		private String name;//name of client
		
		//constructor to make socket for client
		public ChatToMany(Socket socket){
			this.socket = socket;
			System.out.println("Socket is created for client");
		}
		
		public void run(){
			try {
				
				//in = new BufferedReader(new InputStreamReader(
				//		socket.getInputStream()));
				in =new DataInputStream(socket.getInputStream());		
				//out = new PrintWriter(socket.getOutputStream(),true);
				out =new DataOutputStream(socket.getOutputStream()); 
				while (true){
					System.out.println("Chacke poit 222");
					out.writeUTF("SUBMITNAME");
					name = in.readUTF();
					//check name with database
					if (name ==null){
						return;
					}
					synchronized(names){                      //doubt
						if(!names.contains(name)){
							names.add(name);
							break;
						}
					}
				}
				out.writeUTF("NAMEACCEPTED");
				writers.add(out);
				//now broadcast message to all client received from a client
				while(true){
					String input = in.readUTF();
					if (input == null){
						return;
					}
					for (DataOutputStream writer : writers){
						writer.writeUTF("MESSAGE"+ name+":"+input);
					}
				}
				
			}catch(IOException e){
				System.out.println(e);
				
			}finally{
				//if some client went down then remove that client from hashset
				if (name !=null){
					names.remove(name);
				}
				if (out!=null){
					writers.remove(out);
				}
				try{
					socket.close();
				}catch(IOException e){
					
				}
			}
			
		}
		
	}
	

}
