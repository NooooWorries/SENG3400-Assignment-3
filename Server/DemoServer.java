/*
    Name: Zixin Cheng
	Student number : 3218124
	Course: SENG3400
*/

import DemoApp.*; 
import org.omg.CosNaming.*; 
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*; 
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import java.util.Random;

public class DemoServer
{
	public static void main(String[] args)
	{
    	try
    	{
    		ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
				
     		DemoImpl demoImpl = new DemoImpl();
      	
      	    // get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(demoImpl);
			Demo href = DemoHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			String name = "Demo";
			NameComponent[] path = ncRef.to_name(name);
			ncRef.rebind(path, href);

			orb.run();
    	}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

class DemoImpl extends DemoPOA
{
	// Get a random char (a - z, A - Z)
	public char getChar()
	{
		// Generate a random character and return back the result
		int random = (int)(Math.random() * 52);
		char result = (random < 26) ? 'A' : 'a';
		
		// Random delay between 1000 and 3000 ms
		Random r = new Random();
		int randomLag = r.nextInt(3000 - 1000 + 1) +1000;
		try
		{
			Thread.sleep(randomLag);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return (char) (result + random % 26);
	}
	
	public void getCharAsync(DemoCallback cb) 
	{
		char r = getChar();
		cb.callback(r);
	}

}
