package edu.Brandeis.cs131.Common.VictorZhu;

import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Industry;

public abstract class MyClient extends Client {
	
    public MyClient(String name, Industry industry) {
    	//Initializes a client with a random speed value
        super(name, industry, (int) (Math.random() * 10), 3);
    }
}
