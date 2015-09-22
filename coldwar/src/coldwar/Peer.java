package coldwar;

import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.*;

public class Peer {

	private Net.Protocol protocol;
	private Socket socket;  // TODO(hesswill): dispose.
	private Net net;
	
	public Peer(Net networkImpl) {
		protocol = Net.Protocol.TCP;
		net = networkImpl;
	}

	public synchronized void Host(int port) {
		if (socket != null) {
			return;
		}
		ServerSocketHints hints = new ServerSocketHints();
		hints.acceptTimeout = 0;
		socket = net.newServerSocket(protocol, port, hints).accept(new SocketHints());
	}
	
	public synchronized void Connect(String host, int port) {
		if (socket != null) {
			return;
		}
		// TODO(machinule): timeouts, error handling.
		socket = net.newClientSocket(protocol, host, port, new SocketHints());
	}
	
	public InputStream getInputStream() {
		if (socket == null) {
			return null;
		}
		return socket.getInputStream();
	}
	
	public OutputStream getOutputStream() {
		if (socket == null) {
			return null;
		}
		return socket.getOutputStream();
	}
}
