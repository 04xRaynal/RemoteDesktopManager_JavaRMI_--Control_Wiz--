# Remote Desktop Manager using RMI  --Control Wiz--
***

### A Java Application made on Java Swing using RMI (Remote Method Invocation) to Connect two Machines via the network , one being the Server and the other being the Client. 

Both Machines can be connected locally via the intraNet and also via the Internet if not connected locally on the same network.

---

### The Client can control operation on the Server Machine, this is achieved through the Robot Class.
---

Following the RMI Syntax, these are the Classes created:

ScreenEvent.java

A Remote Interface.
This interface, extends the Remote interface and declares the RemoteException for all the methods of the interface.

---

ScreenEventImpl.java

This class provides the implementation of the Remote Interface.

To provide implementation of the remote interface we extend the UnicastRemoteObject class, and define a constructor that declares the Remote Exception.
  
Here we provide the implementation of the interface methods,
 
Methods are: 
 
Check the password entered by the Client Machine and match it with the password of the Server Machine;

Capture the Server Screen as an Image and convert it into a Byte Array;

Return the Dimension(Width and Height) of the Server Machine' Screen;

Provide implementation for the Robot class methods which send inputs such as (Mouse Movement, Mouse Action and Keyboard Action) from the Client Machine to the Server Machine.

---

DesktopServer.java

Server class to run on the Server Machine.
 
Prompts the user to Set up a Password for Remote Connection via a Swing GUI.

Also displays the IP Address of your Machine (private IP for intraNet and public/router IP for Internet) which the Client needs to input in order to establish connection.

After the Password is entered the Naming class method gets and stores the remote object and binds it with your IP (private IP Address) with the port (1888) and a name (burr).

![Capture_RemoteDesktopManager_ServerPassword.PNG](https://github.com/04xRaynal/RemoteDesktopManager_JavaRMI_--Control_Wiz--/blob/798d1b2720da6fd81883ad8e6445e343e170d39d/Captured%20Images/Capture_RemoteDesktopManager_ServerPassword.PNG)

---

DesktopClient.java

Client class to run on the Client Machine
 
A Swing GUI is displayed to input the Server IP Address and Password. 
The password should be similar to what the Server entered while creating Connection.
For IP Address, input private IP for intraNet Connection and public/ Router IP for Internet Connection.
 
If the eneterd password matches to the Server' password, Naming class gets the stub object via the lookup() method.
 
The stub object is then used to get the Server' screen onto the Client Machine and enable the Client to perform actions on the Screen Remotely.

![Capture_RemoteDesktopManager_ClientConnection.PNG](https://github.com/04xRaynal/RemoteDesktopManager_JavaRMI_--Control_Wiz--/blob/798d1b2720da6fd81883ad8e6445e343e170d39d/Captured%20Images/Capture_RemoteDesktopManager_ClientConnection.PNG)

![Capture_RemoteDesktopManager_RemoteScreen.PNG](https://github.com/04xRaynal/RemoteDesktopManager_JavaRMI_--Control_Wiz--/blob/798d1b2720da6fd81883ad8e6445e343e170d39d/Captured%20Images/Capture_RemoteDesktopManager_RemoteScreen.PNG)

---


