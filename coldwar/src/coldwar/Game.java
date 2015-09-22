package coldwar;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import coldwar.GameStateOuterClass.GameState;
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
		GameState initialGameState = GameState.newBuilder().setTurn(0).build();
		renderer = new Renderer();
		renderer.TestUpdate("Hello, Capitalist World!");
		System.out.println(TextFormat.printToString(initialGameState));
		Peer peer = new Peer(new LwjglNet());
		if (args[0].equals("connect")) {
			peer.Connect(args[1], Integer.valueOf(args[2]));
		} else if (args[0].equals("host")) {
			peer.Host(Integer.valueOf(args[1]));
		}
		Thread in = new Pipe(System.in, peer.getOutputStream());
		Thread out = new Pipe(peer.getInputStream(), System.out);
		in.join();
		out.join();
	}

}
