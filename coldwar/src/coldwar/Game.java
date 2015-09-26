package coldwar;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.io.IOException;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveListOuterClass.MoveList.Builder;

import com.google.protobuf.TextFormat;

// Specific libGDX backend implementation imports.
import com.badlogic.gdx.backends.lwjgl.*;

final class Pipe extends Thread {
	public Pipe(InputStream in, OutputStream out) {
		super(new Runnable() {
			@Override
			public void run() {
				try {
					int b = 0;
					while (b != -1) {
						b = in.read();
						out.write(b);
					}					
				} catch (IOException ex) {
					System.out.println("Oops");
					return;
				}
			}
		});
		start();
	}
}

public class Game {

	static Renderer renderer;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		Scanner s = new Scanner(System.in);
		String ip, port;
		
		GameState initialGameState = GameState.newBuilder().setTurn(0).build();
		renderer = new Renderer();
		renderer.TestUpdate("Hello, Capitalist World!");
		System.out.println(TextFormat.printToString(initialGameState));
		renderer.TestUpdate("Enter IP: ");
		ip = s.next();
		renderer.TestUpdate("Enter port: ");
		port = s.next();
		renderer.TestUpdate(ip);
		Peer peer = new Peer(new LwjglNet());
		if (args[0].equals("connect")) {
			peer.Connect(ip, Integer.valueOf(port));
			Builder moveList = MoveList.newBuilder();
			renderer.TestUpdate("Move 1:");
			moveList.addMovesBuilder().setType(s.next());
			renderer.TestUpdate("Move 2:");
			moveList.addMovesBuilder().setType(s.next());
			peer.sendMoveList(moveList.build());
		} else if (args[0].equals("host")) {
			peer.Host(Integer.valueOf(args[1]));
			MoveList moveList = peer.getMoveList();
			System.out.println(TextFormat.printToString(moveList));
		}

	}

}
