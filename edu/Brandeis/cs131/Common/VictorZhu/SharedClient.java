package edu.Brandeis.cs131.Common.VictorZhu;

import edu.Brandeis.cs131.Common.Abstract.Industry;

public class SharedClient extends MyClient {
	public SharedClient(String name, Industry industry) {
		super(name, industry);
	}//create shared client with name and industry
	
	public String toString() {//returns string format of object
        return String.format("%s SHARED %s", this.getIndustry(), this.getName());
    }
}
