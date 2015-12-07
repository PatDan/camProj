package client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import server.ServerMonitor;

public class Client {
	
public static void main(String[] args) {
		
		Runnable r = new Runnable() {
			/**
			 * This is the run method for the main method, it shows a dialog box for the initial inputs
			 * the variable input is a vector which includes 4 values corresponding to the First Camera Number,
			 * the first port number, the second camera number, and lastly the second port number.
			 * Thus input[0] gives you the first camera number and so forth.
			 */
			public void run() {
				boolean incorrectEntry = true;
				String[] input = null;
				int[] ports = new int[2];
				String[] cameras = new String[2];
				JFrame inputPanel = new JFrame();
				while(incorrectEntry){
				try{
				input = createInputDialog(inputPanel);
				ports[0] = Integer.parseInt(input[1]);
				ports[1] = Integer.parseInt(input[3]);
				cameras[0] = input[0];
				cameras[1] = input[2];
		
				}catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(inputPanel, "Please Enter Numbers only");
					incorrectEntry = true;
				}
				if(!(input == null)){
					incorrectEntry = false;
				}
				}
				
//				ServerMonitor sm = new ServerMonitor(input[0], input[1]);
//				ServerMonitor sm2 = new ServerMonitor(input[2], input[3]);
				ClientMonitor am = new ClientMonitor(ports, cameras);

			}

			private String[] createInputDialog(JFrame inputPanel) {
				
				JTextField field1 = new JTextField();
				JTextField field2 = new JTextField();
				JTextField field3 = new JTextField();
				JTextField field4 = new JTextField();
				Object[] message = {
				    "Enter Address for First Camera: ", field1,
				    "Enter Port number for first Camera:", field2,
				    "Enter Address for Second Camera: ", field3,
				    "Enter Port Number for second Camera:", field4,
				};
				int option = JOptionPane.showConfirmDialog(inputPanel, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
				    String FirstCameraNbr = field1.getText();
				    String FirstPortNbr = field2.getText();
				    String SecondCameraNbr = field3.getText();
				    String SecondPortNbr = field4.getText();
				    String[] returnVector = {FirstCameraNbr, FirstPortNbr, SecondCameraNbr, SecondPortNbr};
				    return returnVector;
					
				    
				}else if(option == JOptionPane.CANCEL_OPTION){
					System.exit(0);
				}
				return null;
			}
		};
		SwingUtilities.invokeLater(r);
	}
}
