/*
 * Creating a Remote Interface.
 * This interface, extends the Remote interface 
 * and declares the RemoteException for all the methods of the interface.
 * 
 * author 04xRaynal
 */
package raynal.remote_desktop_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScreenEvent extends Remote{
	public boolean checkPassword(String inputPassword) throws RemoteException;
	public byte[] sendScreen() throws RemoteException;
	public double getWidth() throws RemoteException;
	public double getHeight() throws RemoteException;
	public void mouseMovedEvent(int xScale, int yScale) throws RemoteException;
	public void mousePressedEvent(int buttonPressed) throws RemoteException;
	public void mouseReleasedEvent(int buttonReleased) throws RemoteException;
	public void keyPressed(int keyPressed) throws RemoteException;
	public void keyReleased(int keyReleased) throws RemoteException;
}
