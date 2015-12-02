package client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import server.ServerMonitor;

public class Main {
	String input;
public static void main(String[] args) {
		
		Runnable r = new Runnable() {
			public void run() {
				JFrame inputPanel = new JFrame();
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
				}
				
				
				
				ServerMonitor sm = new ServerMonitor();
				ServerMonitor sm2 = new ServerMonitor();
				ClientMonitor am = new ClientMonitor();

			}
		};
		SwingUtilities.invokeLater(r);
	}
}
