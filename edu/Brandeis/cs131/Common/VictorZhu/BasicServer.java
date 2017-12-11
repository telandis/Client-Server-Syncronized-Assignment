package edu.Brandeis.cs131.Common.VictorZhu;

import java.util.ArrayList;

import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Server;

public class BasicServer extends Server {
	//numShared and numBasic track how many of each are connected to the server
	int numShared = 0;
	int numBasic = 0;
	ArrayList<Client> clientList = new ArrayList<Client>();//ArrayList to hold all connected clients
	
	public BasicServer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized boolean connectInner(Client client) {
		// TODO Auto-generated method stub
		if(client instanceof BasicClient) {
			if(this.numShared > 0 || this.numBasic > 0) {
				return false;
			} else {
				this.clientList.add(client);
				numBasic++;
				return true;
			}//if a basic client tries to connect, checks to see if there are any other clients connected before connecting
		} else {//if a shared client tries to connect
			if(this.numBasic == 0) {
				if(this.numShared == 0) {
					this.clientList.add(client);
					this.numShared++;
					return true;//if there are no other clients connected, connect this client to server
				} else if(this.numShared == 1 && !this.clientList.get(0).getIndustry().equals(client.getIndustry())) {
					this.clientList.add(client);
					this.numShared++;
					return true;//if there is only one shared client of a different industry connected, connect to server
				}
			}
			return false;//otherwise do not connect
		}
	}

	@Override
	public synchronized void disconnectInner(Client client) {
		// TODO Auto-generated method stub
		this.clientList.remove(client);//removes client from list of clients connected
		if(client instanceof BasicClient) {//decrements numBasic or numShared depending on which disconnects
			this.numBasic--;
		} else {
			this.numShared--;
		}
	}
}
