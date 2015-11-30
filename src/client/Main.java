package client;

import javax.swing.SwingUtilities;

import server.ServerMonitor;

public class Main {
	String input;
public static void main(String[] args) {
		
		Runnable r = new Runnable() {
			public void run() {
				ServerMonitor sm = new ServerMonitor();
				ServerMonitor sm2 = new ServerMonitor();
				ClientMonitor am = new ClientMonitor();

			}
		};
		SwingUtilities.invokeLater(r);
	}
}
