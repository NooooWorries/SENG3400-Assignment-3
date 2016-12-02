1. To create the helper files for the client:
   - Move to the folder in which the .idl file resides
   idlj 每td Client 每fall Demo.idl <enter>

2. To create the helper files for the server:
   - Move to the folder in which the .idl file resides
   idlj 每td Server 每fall Demo.idl <enter>

3. To compile the client:
   - Move to the folder in which the client programmer-produced code resides 
   javac DemoClient.java DemoApp\*.java <enter>

4. To compile the server:
   - Move to the folder in which the server programmer-produced code resides 
   javac DemoServer.java DemoApp\*.java <enter>

5. To start the ORB:
   - Move to the folder in which the .idl file resides
   start orbd 每ORBInitialPort 2014

6. To start the server:
   - Move to the folder in which the server programmer-produced code resides 
   start java DemoServer 每ORBInitialHost localhost 每ORBInitialPort 2014 <enter>

7. To start the client:
   - Move to the folder in which the client programmer-produced code resides 
   java DemoClient 每ORBInitialHost localhost 每ORBInitialPort 2014 <enter>