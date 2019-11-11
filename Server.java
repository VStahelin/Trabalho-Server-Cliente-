import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException{

        //Creating Server setup
        int port = Integer.parseInt(JOptionPane.showInputDialog(null,"Which port do you want to start the server?"));
        NetworkManager networkmanager = new NetworkManager();
        networkmanager.assigningSocket(port);
        String TREATMENT = "MATH_GRADES";

		try {
			while(true) {
                //Accepting the client connection
				Socket socket = networkmanager.connectionValidation();
				try {
                    //Handling the customer request
                    String message = networkmanager.streamManager(socket, TREATMENT);
                    System.out.println(message);

				} catch (ClassNotFoundException error) {
					error.printStackTrace();
				} finally {
                    //Closing Connection
                    networkmanager.closingSocket(socket);
                }
			}
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
}

//Controlling class of operations performed during connection
class NetworkManager {

	private ServerSocket serverSocket;

    //Opening the door for clients connections
	public void assigningSocket(int port) throws IOException {
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException error){
            error.printStackTrace();
        } finally {
            System.out.println("Starting server");
        }
	}
    
    //Accepting the client connection
    public Socket connectionValidation() throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("connection made without objections");
        return socket;
    }

    //Stream Manager 
    public String streamManager(Socket socket, String treatment) throws IOException, ClassNotFoundException {
        String result = "Message";
        Math math = new Math();
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());   //data output
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());       //data input
        
        try{
            switch (treatment) {
                case "MATH_GRADES":
                
                Object object = input.readObject();
                String string = object.toString();
                Float grade = Float.parseFloat(string);

                output.writeObject(math.finalGradeVerification(grade));

                output.flush(); //sending data for client
                break;

                default:
                    System.out.println("No treatment selected");
                break;
                
            }

            result = "New process successfully completed";
            
        } catch (IOException error) {
            System.out.println("Stream Error");
            result = "Process terminated with error, Stream error";
			error.printStackTrace();
        }

        //Closing data transfer path
        output.close();
        input.close();  
        return result;
    }
    
    //Closing Client Connection 
	public void closingSocket (Socket socket) throws IOException {
		socket.close();
    }
}

//Class responsible for calculating stream requests
class Math {   

	public String finalGradeVerification(Float grade) throws IOException, ClassNotFoundException  {
        String message;
        grade = 12 - grade;
        if (grade > 10){
            message = "You have already failed";    
        } else if (grade <= 5 ) {
            message = "You have completed this course";
        } else {
            message = "You need to take " + grade + " o complete this course";
        }
        return message;
	}
}
