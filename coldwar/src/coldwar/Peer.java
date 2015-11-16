package coldwar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.*;
import coldwar.MoveListOuterClass.MoveList;

public class Peer {

	private Net.Protocol protocol;
	private Socket socket;  // TODO(hesswill): dispose.
	private Net net;
	
	public enum MessageType {
		MOVELIST
	}
	
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
	
	public void sendMoveList(MoveList moveList) {
		writeProto(MessageType.MOVELIST, moveList);
	}

	public MoveList getMoveList() {
		return (MoveList) readProto(MessageType.MOVELIST, MoveList.getDefaultInstance());
	}
	
	public void writeProto(MessageType type, com.google.protobuf.Message message) {
		if (socket == null) {
			return;
		}
		DataOutputStream stream = new DataOutputStream(
			     new BufferedOutputStream(socket.getOutputStream()));
		byte[] serialized = message.toByteArray();
		CRC32 checksum = new CRC32();
		checksum.update(serialized);
		try {
			stream.writeInt(type.ordinal());
			stream.writeInt(serialized.length);
			stream.write(serialized);
			stream.writeLong(checksum.getValue());
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public com.google.protobuf.Message readProto(MessageType type, com.google.protobuf.Message message) {
		if (socket == null) {
			return null;
		}
		DataInputStream stream = new DataInputStream(
			     new BufferedInputStream(socket.getInputStream()));
		try {
			while (stream.readInt() != type.ordinal()) {};  // oh god oh god why.
			int remaining = stream.readInt();
			byte[] serialized = new byte[remaining];
			while (remaining > 0) {
				remaining -= stream.read(serialized, 0, remaining);
			}
			long checksumVal = stream.readLong();
			CRC32 checksum = new CRC32();
			checksum.update(serialized);
			if (checksum.getValue() != checksumVal) {
				Logger.Err("Checksum mismatch.");
			}
			return message.getParserForType().parseFrom(serialized);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
