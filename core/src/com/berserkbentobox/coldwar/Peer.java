package com.berserkbentobox.coldwar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;

public class Peer {

	public enum MessageType {
		MOVELIST, GAME_STATE
	}

	private final Net net;
	private final Net.Protocol protocol;

	private Socket socket; // TODO(hesswill): dispose.

	public Peer(final Net networkImpl) {
		this.protocol = Net.Protocol.TCP;
		this.net = networkImpl;
	}

	public synchronized void Connect(final String host, final int port) {
		if (this.socket != null) {
			return;
		}
		// TODO(machinule): timeouts, error handling.
		this.socket = this.net.newClientSocket(this.protocol, host, port, new SocketHints());
	}

	public InputStream getInputStream() {
		if (this.socket == null) {
			return null;
		}
		return this.socket.getInputStream();
	}

	public MoveList getMoveList() {
		return (MoveList) this.readProto(MessageType.MOVELIST, MoveList.getDefaultInstance());
	}

	public GameState getGameState() {
		return (GameState) this.readProto(MessageType.GAME_STATE, GameState.getDefaultInstance());
	}

	public OutputStream getOutputStream() {
		if (this.socket == null) {
			return null;
		}
		return this.socket.getOutputStream();
	}

	public synchronized void Host(final int port) {
		if (this.socket != null) {
			return;
		}
		final ServerSocketHints hints = new ServerSocketHints();
		hints.acceptTimeout = 0;
		this.socket = this.net.newServerSocket(this.protocol, port, hints).accept(new SocketHints());
	}

	public com.google.protobuf.Message readProto(final MessageType type, final com.google.protobuf.Message message) {
		if (this.socket == null) {
			return null;
		}
		final DataInputStream stream = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
		try {
			while (stream.readInt() != type.ordinal()) {
			}
			; // oh god oh god why.
			int remaining = stream.readInt();
			final byte[] serialized = new byte[remaining];
			while (remaining > 0) {
				remaining -= stream.read(serialized, 0, remaining);
			}
			final long checksumVal = stream.readLong();
			final CRC32 checksum = new CRC32();
			checksum.update(serialized);
			if (checksum.getValue() != checksumVal) {
				Logger.Err("Checksum mismatch.");
			}
			return message.getParserForType().parseFrom(serialized);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendMoveList(final MoveList moveList) {
		this.writeProto(MessageType.MOVELIST, moveList);
	}

	public void sendGameState(final GameState gameState) {
		this.writeProto(MessageType.GAME_STATE, gameState);
	}
	
	public void writeProto(final MessageType type, final com.google.protobuf.Message message) {
		if (this.socket == null) {
			return;
		}
		final DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
		final byte[] serialized = message.toByteArray();
		final CRC32 checksum = new CRC32();
		checksum.update(serialized);
		try {
			stream.writeInt(type.ordinal());
			stream.writeInt(serialized.length);
			stream.write(serialized);
			stream.writeLong(checksum.getValue());
			stream.flush();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
