package coldwar;

import coldwar.GameStateOuterClass.GameState;
import com.google.protobuf.TextFormat;

public class Game {

	static Renderer renderer;
	
	public static void main(String[] args) {
		GameState initialGameState = GameState.newBuilder().setTurn(0).build();
		renderer = new Renderer();
		renderer.TestUpdate("Hello, Capitalist World!");
		System.out.println(TextFormat.printToString(initialGameState));
	}

}
