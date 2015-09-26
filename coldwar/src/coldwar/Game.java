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


public class Game {

	static Renderer renderer;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		Scanner s = new Scanner(System.in);
		String ip, port;
		boolean host;
		Logger.Start();
		
		Logger.Dbg("Starting");
		
		GameState initialGameState = GameState.newBuilder().setTurn(0).build();
		renderer = new Renderer();
		renderer.TestUpdate("Hello, Capitalist World!");
		System.out.println(TextFormat.printToString(initialGameState));
		
		Peer peer = new Peer(new LwjglNet());
		
		renderer.TestUpdate("Enter port: ");
		port = s.next();
		Logger.Info("Recieved port: " + port);
		
		renderer.TestUpdate("Host(Y/N)?");
		if(s.next().equals("Y")) {
			host = true;
			peer.Host(Integer.valueOf(port));
			MoveList moveList = peer.getMoveList();
			System.out.println(TextFormat.printToString(moveList));
		} else {
			host = false;
			renderer.TestUpdate("Enter IP: ");
			ip = s.next();
			Logger.Info("Recieved IP: " + ip);
			peer.Connect(ip, Integer.valueOf(port));
			Builder moveList = MoveList.newBuilder();
			renderer.TestUpdate("Move 1:");
			moveList.addMovesBuilder().setType(s.next());
			renderer.TestUpdate("Move 2:");
			moveList.addMovesBuilder().setType(s.next());
			peer.sendMoveList(moveList.build());
		}

		if (!host) {
			
		} else if (host) {
			
		}
		
		Logger.Close();
	}

}
