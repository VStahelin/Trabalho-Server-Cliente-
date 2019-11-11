import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Collecting Client Data
        int port = Integer.parseInt(JOptionPane.showInputDialog(null,"Which port do you want to connect to?"));
        float grades = Float.parseFloat(JOptionPane.showInputDialog(null,"Enter your grades"));

        try {
            //Establishing streams inlets and outlets 
			Socket socket = new Socket("localhost", port);
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            //Adding the message and sending for processing
			output.writeObject(grades); 
			output.flush();

            //Receiving postprocessing data
            System.out.println(input.readObject());

            //Closing streams connections
            socket.close();
			input.close(); 
            output.close(); 

		} catch (IOException error) {
			System.out.println("Error Please Go to SAIAC");
        }
		
	}
}