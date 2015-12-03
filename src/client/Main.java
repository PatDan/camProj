package client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import server.ServerMonitor;

public class Main {
	
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
				int[] input = null;
				JFrame inputPanel = new JFrame();
				while(incorrectEntry){
				try{
				input = createInputDialog(inputPanel);
		
				}catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(inputPanel, "Please Enter Numbers only");
					incorrectEntry = true;
				}
				if(!(input == null)){
					incorrectEntry = false;
				}
				}
				
				ServerMonitor sm = new ServerMonitor();
				ServerMonitor sm2 = new ServerMonitor();
				ClientMonitor am = new ClientMonitor();

			}

			private int[] createInputDialog(JFrame inputPanel) {
				
				JTextField field1 = new JTextField();
				JTextField field2 = new JTextField();
				JTextField field3 = new JTextField();
				JTextField field4 = new JTextField();
				Object[] message = {
				    "Enter Number for First Camera", field1,
				    "Enter Port number for first Camera:", field2,
				    "Enter Number for Second Camera:", field3,
				    "Enter Port Number for second Camera:", field4,
				};
				int option = JOptionPane.showConfirmDialog(inputPanel, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
				    int FirstCameraNbr = Integer.parseInt(field1.getText());
				    int FirstPortNbr = Integer.parseInt(field2.getText());
				    int SecondCameraNbr = Integer.parseInt(field3.getText());
				    int SecondPortNbr = Integer.parseInt(field4.getText());
				    int[] returnVector = {FirstCameraNbr, FirstPortNbr, SecondCameraNbr, SecondPortNbr};
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