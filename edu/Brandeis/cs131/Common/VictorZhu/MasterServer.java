package edu.Brandeis.cs131.Common.VictorZhu;

import java.util.HashMap;
import java.util.LinkedList;

import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Log.Log;
import edu.Brandeis.cs131.Common.Abstract.Server;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MasterServer extends Server {

    private final Map<Integer, List<Client>> mapQueues = new HashMap<Integer, List<Client>>();
    private final Map<Integer, Server> mapServers = new HashMap<Integer, Server>();
    private final Lock lock = new ReentrantLock();//create lock and condition variables
    private final Map<Integer, Condition> mapConditions = new HashMap<Integer, Condition>();//
    //private final Condition isFront = lock.newCondition();


    public MasterServer(String name, Collection<Server> servers, Log log) {
        super(name, log);
        Iterator<Server> iter = servers.iterator();
        while (iter.hasNext()) {
            this.addServer(iter.next());
        }
    }

    public void addServer(Server server) {
        int location = mapQueues.size();
        this.mapServers.put(location, server);
        this.mapQueues.put(location, new LinkedList<Client>());
        this.mapConditions.put(location, lock.newCondition());//
    }

    @Override
    public boolean connectInner(Client client) {
        // TODO Auto-generated method stub
    	lock.lock();//acquires the lock, otherwise waits until lock is available to acquire
    	try {
			this.mapQueues.get(this.getKey(client)).add(client);//add client to queue
			while(this.mapQueues.get(this.getKey(client)).indexOf(client) != 0) {
				this.mapConditions.get(this.getKey(client)).await();//
				//isFront.await();//waits until the client is at the front of the queue
			}
			while(!this.mapServers.get(this.getKey(client)).connectInner(client)) {
				this.mapConditions.get(this.getKey(client)).await();//
				//isFront.await();//waits until the server is available to be connected to
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.mapQueues.get(this.getKey(client)).remove(0);//removes client from the front of the queue
			lock.unlock();//releases the lock
		}
        return true;
    }

    @Override
    public void disconnectInner(Client client) {
        // TODO Auto-generated method stub
    	lock.lock();//acquires the lock, otherwise waits unti lock is available
    	try {
    		this.mapServers.get(this.getKey(client)).disconnectInner(client);//disconnects from server
    		this.mapConditions.get(this.getKey(client)).signalAll();//
    		//isFront.signalAll();//signals all the other threads to wake up and check to see if they can connect
    	}  finally {
    		lock.unlock();//releases the lock
    	}  	
    }

	//returns a number from 0- mapServers.size -1
    // MUST be used when calling get() on mapServers or mapQueues
    private int getKey(Client client) {
        return client.getSpeed() % mapServers.size();
    }
}
