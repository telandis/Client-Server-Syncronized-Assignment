package edu.Brandeis.cs131.Common.VictorZhu;

import edu.Brandeis.cs131.Common.Abstract.Industry;

public class BasicClient extends MyClient {
	//Creates a BasicClient object with input name and industry
	public BasicClient(String name, Industry industry) {
		super(name, industry);
	}
	//Returns a string format of this object
    public String toString() {
        return String.format("%s BASIC %s", this.getIndustry(), this.getName());
    }
}
