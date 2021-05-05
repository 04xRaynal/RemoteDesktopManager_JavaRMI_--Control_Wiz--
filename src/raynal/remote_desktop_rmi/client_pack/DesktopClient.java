/*
 * Client class to run on the Client Machine
 * 
 * A Swing GUI is displayed to input the Server IP Address and Password. 
 * The password should be similar to what the Server entered while creating Connection.
 * For IP Address, input private IP for intraNet Connection and public/ Router IP for Internet Connection.
 * 
 * If the eneterd password matches to the Server' password, Naming class gets the stub object via the lookup() method.
 * 
 * The stub object is then used to get the Server' screen onto the Client Machine -
 * and enable the Client to perform actions on the Screen Remotely.
 * 
 * author 04xRaynal
 */

package raynal.remote_desktop_rmi.client_pack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import raynal.remote_desktop_rmi.ScreenEvent;

public class DesktopClient extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	ScreenEvent stub;
	JTextField serverIP, password;
	double stubWidth, stubHeight;
	
	public DesktopClient() {
		try {   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   }
		catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}        //Refines the look of the ui
		
		//Creating a GUI which inputs the Server IP Address and Password
		JLabel IPlabel = new JLabel("Server IP: ");
		IPlabel.setFont(new Font("Arial", Font.PLAIN, 13));
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		serverIP = new JTextField(15);
		serverIP.setToolTipText("Enter IP of Machine you want to Connect with!");
		serverIP.setFont(new Font("Arial", Font.PLAIN, 16));
		password = new JTextField(15);
		password.setToolTipText("Input Password of Machine you want to Connect with!");
		password.setFont(new Font("Arial", Font.PLAIN, 16));
		JButton submit = new JButton("Submit");
		submit.setFont(new Font("Arial", Font.PLAIN, 13));
		
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.add(IPlabel, BorderLayout.CENTER);
		
		JPanel panel2 = new JPanel();
		panel2.add(serverIP);
		
		JPanel topPanel = new JPanel();
		topPanel.add(panel1);
		topPanel.add(panel2);
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
		panel3.add(passwordLabel, BorderLayout.CENTER);
		
		JPanel panel4 = new JPanel();
		panel4.add(password);
		
		JPanel midPanel = new JPanel();
		midPanel.add(panel3);
		midPanel.add(panel4);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(submit);
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(3, 1));
		gridPanel.add(topPanel);  
		gridPanel.add(midPanel); 
		gridPanel.add(bottomPanel);
		
		
		submit.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(new JPanel().add(new JLabel(" ")), BorderLayout.NORTH);
		add(gridPanel, BorderLayout.CENTER);
		add(new JPanel().add(new JLabel(" ")), BorderLayout.SOUTH);
		
		setVisible(true);
		setSize(330, 175);
		setResizable(false);
		setLocation(500, 300);
		setTitle("Enter Password to Connect!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) {
		new DesktopClient();
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * Naming class gets the stub object via the lookup() method
		 */
		try {
			//use System.setProperty when using publicIP of Server Machine for Interner Connection, 
			//swap publicIP for Router IP if Internet Connection passes through a Router
//			System.setProperty("java.rmi.server.hostname",serverIP.getText());
//			LocateRegistry.getRegistry(serverIP.getText(), 1888);
			
			stub = (ScreenEvent) Naming.lookup("rmi://" + serverIP.getText() + ":1888/burr");

			if(! (stub.checkPassword(password.getText()))) {
				System.out.println("Entered Credentials are wrong!");
				System.exit(0);
			}
			else {
				dispose();
				System.out.println("Connection Established with Server!");
			}
			
			//storing width and height of the Server' Machine Screen
			stubWidth = stub.getWidth();
			stubHeight = stub.getHeight();

			new ScreenFrame();
		} 
		catch (MalformedURLException ex) {
			ex.printStackTrace();
		} 
		catch (RemoteException ex) {
			ex.printStackTrace();
		} 
		catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}
	
	
	class ScreenFrame extends JFrame implements KeyListener, MouseMotionListener, MouseListener {
		private static final long serialVersionUID = 1L;
		JLabel label;
		JPanel panel;
		JInternalFrame internalFrame;
		
		public ScreenFrame() {
			try {   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   }
			catch (ClassNotFoundException e) {}
	        catch (InstantiationException e) {}
	        catch (IllegalAccessException e) {}
	        catch (UnsupportedLookAndFeelException e) {}        //Refines the look of the ui
			
			//GUI which displays the Server Screen
			label = new JLabel();
			JDesktopPane desktopPane = new JDesktopPane();
			add(desktopPane, BorderLayout.CENTER);
			panel = new JPanel();
			panel.add(label);
			
			internalFrame = new JInternalFrame("Screen", true, true, true);
			internalFrame.setLayout(new BorderLayout());
			internalFrame.getContentPane().add(panel, BorderLayout.CENTER);
			
			//To find the Client Machine Screen Dimension, so that the GUI fits the screen perfectly
			Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			setSize(r.width, r.height);
			
			setMaximumSize(new Dimension(r.width, r.height));
			setMinimumSize(new Dimension((int)(r.width /1.2),(int)( r.height /1.2)));
			
			internalFrame.setSize(getWidth(), getHeight());
			internalFrame.setBorder(null);							//removes border of the internal frame
			((javax.swing.plaf.basic.BasicInternalFrameUI) internalFrame.getUI()).setNorthPane(null);		//removes header of internal frame
			
			desktopPane.add(internalFrame);
			setVisible(true);
			internalFrame.setVisible(true);
			try {
				internalFrame.setMaximum(true);						//internal frame is set to max length as that of the outer frame
			} 
			catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			panel.setFocusable(true);								//panel will be the focused Component, required for KeyListener
			setTitle("Remote Desktop Manager - ControlWiz");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						try {
							byte[] bytes = new byte[1024 * 1024];
							
							bytes = stub.sendScreen();							//array of bytes is read from the stub object
							
							BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(bytes));		//byte array is converted back to an image
							label.setIcon(new ImageIcon(bImage));				//image is set to the label
							
							try {
								Thread.sleep(10000);
							} 
							catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						catch (RemoteException e) {
							e.printStackTrace();
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			});
			thread.start();
			
			panel.addKeyListener(this);
			panel.addMouseListener(this);
//			panel.addMouseMotionListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		
		@Override
		public void mousePressed(MouseEvent e) {				//event for when the mouse button is pressed
			int buttonPressed = e.getButton();
			
			try {
				stub.mousePressedEvent(buttonPressed);
			} 
			catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}

		
		@Override
		public void mouseReleased(MouseEvent e) {					//event for when the mouse button is released
			int buttonReleased = e.getButton();
			
			try {
				stub.mouseReleasedEvent(buttonReleased);
			}
			catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}

		
		@Override
		public void mouseEntered(MouseEvent e) {}

		
		@Override
		public void mouseExited(MouseEvent e) {}

		
		@Override
		public void mouseDragged(MouseEvent e) {}

		
		@Override
		public void mouseMoved(MouseEvent e) {						//event for mouse movement
			double xAxis = (double) stubWidth / panel.getWidth();				//scale for the X axis
			double yAxis = (double) stubHeight / panel.getHeight();				//scale for the Y axis
			
			try {
				stub.mouseMovedEvent((int) (e.getX() * xAxis), (int) (e.getY() * yAxis));
			} 
			catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}

		
		@Override
		public void keyTyped(KeyEvent e) {}

		
		@Override
		public void keyPressed(KeyEvent e) {					//event for when a keyboard button is pressed
			try {
				stub.keyPressed(e.getKeyCode());
			}
			catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}

		
		@Override
		public void keyReleased(KeyEvent e) {					//event for when a keyboard button is released
			try {
				stub.keyReleased(e.getKeyCode());
			}
			catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}
	}

}
