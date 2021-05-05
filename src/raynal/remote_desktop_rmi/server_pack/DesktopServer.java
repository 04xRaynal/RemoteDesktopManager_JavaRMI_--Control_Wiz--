/*
 * Server class to run on the Server Machine.
 * 
 * Prompts the user to Set up a Password for Remote Connection via a Swing GUI.
 * Also displays the IP Address of your Machine (private IP for intraNet and public/router IP for Internet)
 * which the Client needs to input in order to establish connection.
 * 
 *  After the Password is entered the Naming class method gets and stores the remote object -
 *  and binds it with your IP (private IP Address) with the port (1888) and a name (burr).
 *  
 *  author 04xRaynal
 */

package raynal.remote_desktop_rmi.server_pack;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
//import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
//import java.rmi.server.UnicastRemoteObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import raynal.remote_desktop_rmi.ScreenEvent;
import raynal.remote_desktop_rmi.ScreenEventImpl;


public class DesktopServer extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	InetAddress privateIP;
	JTextField password;
	
	public DesktopServer() {
		try {   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   }
		catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}        //Refines the look of the ui
		
		try {
			privateIP = InetAddress.getLocalHost();			//Local (Private) IP of your Machine
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		//Creating a GUI which prompts the user to Set up a Password
		JLabel label = new JLabel("Set Password:");
		label.setFont(new Font("Arial", Font.PLAIN, 13));
		password = new JTextField(15);
		password.setToolTipText("Set a Password. Share the password to Connect with your Machine!");
		password.setFont(new Font("Arial", Font.PLAIN, 16));
		JButton submit = new JButton("Submit");
		submit.setFont(new Font("Arial", Font.PLAIN, 13));
		
		//using JTextField instead of a JLabel to display information because, the text in textField can be selected and copied
		JTextField  IPlabel = new JTextField ();					
		IPlabel.setText("Your Machine' IP Address is:  " + privateIP.getHostAddress());			//for intraNet Connection
		//for internet Connection (public IP as shown below or RouterIP)
//		URL publicIpUrl = new URL("http://bot.whatismyipaddress.com");			//this website displays the system' public IP address
//		BufferedReader br = new BufferedReader(new InputStreamReader(publicIpUrl.openStream()));					//to read the URL	
//		String publicIP = br.readLine().trim())									//gets the first line of the URL
//		IPlabel.setText("Your Machine' IP Address is:  " + publicIP;			//for Internet Connection 
		IPlabel.setFont(new Font("Arial", Font.PLAIN, 12));
		IPlabel.setEditable(false);
		IPlabel.setBorder(null);
		
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(label, BorderLayout.CENTER);
		JPanel eastPanel = new JPanel();
		eastPanel.add(submit);
		JPanel centerPanel = new JPanel();
		centerPanel.add(password);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(westPanel, BorderLayout.WEST);
		topPanel.add(centerPanel, BorderLayout.CENTER);
		topPanel.add(eastPanel, BorderLayout.EAST);
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(2, 1));
		gridPanel.add(topPanel);  gridPanel.add(IPlabel);
		
		submit.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(new JPanel().add(new JLabel(" ")), BorderLayout.NORTH);
		add(gridPanel, BorderLayout.CENTER);
		add(new JPanel().add(new JLabel(" ")), BorderLayout.SOUTH);
		
		setVisible(true);
		setSize(400, 130);
		password.requestFocusInWindow();						//Caret focus is on this Component
		setResizable(false);
		setLocation(500, 300);
		setTitle("Set a Password!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) {
		new DesktopServer();
	}
		

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();									//when submit is clicked, the GUI is disposed
		/*
		 * Creating RMI Registry on port 1888 (default for RMI is 1099)
		 * Naming class provides methods to get and store the remote object (stub)
		 * and bind the remote object by a name (burr)
		 */
		try {
			//use System.setProperty when using publicIP for Interner Connection, 
			//swap publicIP for Router IP if Internet Connection passes through a Router
//			System.setProperty("java.rmi.server.hostname", publicIP);		
			
			ScreenEvent stub = new ScreenEventImpl(password.getText());
			
			//Uncomment below line for internet Connection and remove extends UnicastRemoteObject class from ScreeEventImpl Class
			//1100 Port here is used as a Server Port
//			ScreenEvent serverStub = (ScreenEvent) UnicastRemoteObject.exportObject(stub, 1100);

			//RMIRegistry on port 1888
			LocateRegistry.createRegistry(1888);
			
			//Naming rebind is done on the privateIP for both intraNet and internet Connection
			Naming.rebind("rmi://" + privateIP.getHostAddress() + ":1888/burr", stub);			//Change remote Object stub to serverStub for internetConnection
			System.out.println("Server Running!!!");
			
		} 
		catch (RemoteException ex) {
			ex.printStackTrace();
		}
		catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}

}
