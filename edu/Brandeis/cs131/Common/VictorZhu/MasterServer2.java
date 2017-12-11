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

public class MasterServer2 extends Server {

    private final Map<Integer, List<Client>> mapQueues = new HashMap<Integer, List<Client>>();
    private final Map<Integer, Server> mapServers = new HashMap<Integer, Server>();
    private final Lock lock = new ReentrantLock();
    private final Condition isFront = lock.newCondition();


    public MasterServer2(String name, Collection<Server> servers, Log log) {
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
    }

    @Override
    public boolean connectInner(Client client) {
        // TODO Auto-generated method stub
    	lock.lock();
    	if(this.mapQueues.get(this.getKey(client)).isEmpty() && this.mapServers.get(this.getKey(client)).connectInner(client)) {
    		lock.unlock();
    	} else if(this.mapQueues.get(this.getKey(client)).isEmpty()) {
    		try {
    			this.mapQueues.get(this.getKey(client)).add(client);
    			while(!this.mapServers.get(this.getKey(client)).connectInner(client)) {
    				isFront.await();
    			}
    		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				this.mapQueues.get(this.getKey(client)).remove(0);
    			lock.unlock();
    		}
    	} else if(!this.mapQueues.get(this.getKey(client)).isEmpty()) {
    		try {
    			this.mapQueues.get(this.getKey(client)).add(client);
    			while(this.mapQueues.get(this.getKey(client)).indexOf(client) != 0) {
    				isFront.await();
    			}
    			while(!this.mapServers.get(this.getKey(client)).connectInner(client)) {
    				isFront.await();
    			}
    		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				this.mapQueues.get(this.getKey(client)).remove(0);
    			lock.unlock();
    		}
    	}
        return true;
    }

    @Override
    public void disconnectInner(Client client) {
        // TODO Auto-generated method stub
    	lock.lock();
    	try {
    		this.mapServers.get(this.getKey(client)).disconnectInner(client);
        	isFront.signalAll();     	
    	}  finally {
    		lock.unlock();
    	}  	
    }

	//returns a number from 0- mapServers.size -1
    // MUST be used when calling get() on mapServers or mapQueues
    private int getKey(Client client) {
        return client.getSpeed() % mapServers.size();
    }
}
