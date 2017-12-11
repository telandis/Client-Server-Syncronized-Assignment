PART 1

I set MyClient to be the basic version of both the basic and shared client classes, with MyClient using a random number for speed. Basic client and shared client both were very simple and nearly identical except for their toString methods, which would return either basic or shared in its printout. For the basic server, I used two integer fields to track the number of shared and basic clients connected to the server at any time and used an array list to hold whatever servers were connected. Connect inner was a synchronized method that used many conditional statements to check if a server could connect or not. Disconnect inner was a simple method that would disconnect and remove a client connected to the server, and was also a synchronized method.

PART 2:
In this part, the MasterServer that I implemented used lock and condition statements to manage the connecting and disconnecting of servers. Instead of busy waiting, each client waits in a queue until it is its turn to connect to the server, and clients sleep until connected clients disconnect and signal all the other clients to wake up. Connect and disconnect both start with acquiring with the lock, and end with releasing the lock.

Please ignore my MasterServer2.java file, I was using that to hold on to previous version of my code. My MasterServer.java is the one I ended up with, although they both work.