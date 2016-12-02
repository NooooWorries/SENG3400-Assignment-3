import HelloApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

public class HelloClient
{
	public static void main(String[] args)
	{
		try
		{
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			String name = "Hello";
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			Hello helloRef = HelloHelper.narrow(ncRef.resolve_str(name));
			
			String hello = helloRef.sayHello();
			System.out.println(hello);

		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
	}
}

