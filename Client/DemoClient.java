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
import java.util.Scanner;

public class DemoClient
{
	private static int counter = 0;
    public static char value = '*';
	public static boolean isReceived = false;
	public static void main(String[] args)
	{
		try
		{
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Register name service
			String name = "Demo";
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			Demo demoRef = DemoHelper.narrow(ncRef.resolve_str(name));
			
			// Menu, ask user to select one demostrate model
			System.out.println("Please select a demostrate model");
			System.out.println("1. Synchronous");
			System.out.println("2. Deferred Synchronous");
			System.out.println("3. Asynchronous");
			System.out.println("Others. Exit");
			
			// Get input from user
			Scanner scanner = new Scanner(System.in);
			int choice = scanner.nextInt();
			
			// Switch to execute
			switch (choice)
			{
				case 1: // Synchronous
					sync(demoRef);
					break;
				case 2: // Deferred Synchronous
					deferredSync(demoRef, orb);
					break;
				case 3: // Asynchronous
					async(demoRef, orb);
					break;
				default:
					System.exit(0);
					break;
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void sync(Demo demoRef)
	{
		System.out.println("Demonstrating synchronous interaction");
		
		try
		{
			// Loop 5 times - show initial value
			for (int i = 0; i < 5; i ++)
			{
				counter ++;
				System.out.println(counter + ". Value is " + value);
				
				// pause for 1 second to make output readable
				Thread.sleep(1000);
			}
		
			System.out.println("Call to server");
		
			// Call server and loop 5 times
			value = demoRef.getChar();
			for (int i = 0; i < 5; i ++)
			{
				counter ++;
				System.out.println(counter + ". Value is " + value);
				
				// pause for 1 second to make output readable
				Thread.sleep(1000);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// Reset counter and value
		counter = 0;
		value = '*';
	}
	
	public static void deferredSync(Demo demoRef, ORB orb)
	{
		System.out.println("Demonstrating deferred synchronous interaction");
		
		try
		{
			// Register request
			Request request = demoRef._request("getChar");
			request.set_return_type(orb.get_primitive_tc(TCKind.tk_char));
		
			// Loop 5 times - show initial value
			for (int i = 0; i < 5; i ++)
			{
				counter ++;
				System.out.println(counter + ". Value is " + value);
			
				// pause for 1 second to make output readable
				Thread.sleep(1000);
			}
		
			// Call server and countinue loop 5 times
			System.out.println("Call to server");
			request.send_deferred();
		
			for (int i = 0; i < 5; i ++)
			{
				counter ++;
				System.out.println(counter + ". Value is " + value);
				
				// pause for 1 second to make output readable
				Thread.sleep(1000);
			}	
		
			// Get the result from server
			request.get_response();
			value = request.return_value().extract_char();
		
			// Loop 5 times (result may already prepared)
			for (int i = 0; i < 5; i ++)
			{
				counter ++;
				System.out.println(counter + ". Value is " + value);
			
				// pause for 1 second to make output readable
				Thread.sleep(1000);
			}
			
			// Release connection
			demoRef._release();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void async(Demo demoRef, ORB orb)
	{
		System.out.println("Demonstrating asynchronous interaction");
		try
		{
			// Create callback instance
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			DemoClient demoClient = new DemoClient();
			DemoCallbackClient callbackClient = new DemoCallbackClient(demoClient);
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(callbackClient);
			DemoCallback callback = DemoCallbackHelper.narrow(ref);
			
			
			for (int i = 0; i < 5; i ++)
			{
				counter ++;
				System.out.println(counter + ". Value is " + value);
			
				// pause for 1 second to make output readable
				Thread.sleep(1000);
			}
			// Loop untill received response	
			while (isReceived == false)
			{
				// Send request to server
				System.out.println("Call to server");
				demoRef.getCharAsync(callback);
				
				for (int i = 0; i < 5; i ++)
				{
					if (isReceived == true)
					{
						break;
					}
					
					counter ++;
					System.out.println(counter + ". Value is " + value);
							
					// pause for 1 second to make output readable
					Thread.sleep(1000);
				}
				
				// Continue loop till the result loop 5 times
				for (int i = 0; i <5; i ++)
				{
					counter ++;
					System.out.println(counter + ". Value is " + value);
					
					// pause for 1 second to make output readable
					Thread.sleep(1000);
				}
			}
			
			// Close connection after finish
			demoRef._release();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}

class DemoCallbackClient extends DemoCallbackPOA
{
	private DemoClient client;
	
	// Constructor
	public DemoCallbackClient(DemoClient newClient)
	{
		// set new client
		client = newClient;
	}
	
	// Member function
	// Once result received from the middleware
	public void callback(char random)
	{
		client.value = random;	 // Assign result to value
		client.isReceived = true;  // Set received to true
	}
	
}

