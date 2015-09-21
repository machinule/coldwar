package coldwar;

public class Game {

	static Renderer renderer;
	
	public static void main(String[] args) {
		renderer = new Renderer();
		renderer.TestUpdate("Hello, Capitalist World!");
	}

}
