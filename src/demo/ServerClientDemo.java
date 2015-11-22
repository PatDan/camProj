package demo;

import client.ClientMonitor;
import server.ServerMonitor;

public class ServerClientDemo {

	public static void main(String[] args) {
		System.out.println("New server");
		ServerMonitor sm = new ServerMonitor();
		System.out.println("New client");
		ClientMonitor cm = new ClientMonitor();

	}

}
