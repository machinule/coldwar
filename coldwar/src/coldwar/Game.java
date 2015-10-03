package coldwar;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.io.IOException;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveListOuterClass.MoveList.Builder;

import com.google.protobuf.TextFormat;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
// Specific libGDX backend implementation imports.
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;


public class Game extends ApplicationAdapter {

	static Renderer renderer;
	
	static final int WORLD_WIDTH = 200;
    static final int WORLD_HEIGHT = 100;
	
	private OrthographicCamera cam;
	private SpriteBatch batch;
	
	private Sprite mapSprite;
	boolean France = false;
	
	@Override
	public void create () {
		
		mapSprite = new Sprite(new Texture(Gdx.files.internal("assets/map2.png")));
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
		
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(500, 500 * (h / w));
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        cam.update();
		
        batch = new SpriteBatch();
	}

    @Override
    public void render() {
        handleInput();
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        mapSprite.draw(batch);
        batch.end();
    }
	
	private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += 0.05;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.05;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-2, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(2, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -2, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, 2, 0);
        }
        if (Gdx.input.justTouched()) {
        	if(France) {
        		France = false;
        	} else {
        		France = true;
        	}
        }
        
        System.out.println(France);
        
        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
        
        cam.zoom = MathUtils.clamp(cam.zoom, 1f, 6);

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, WORLD_WIDTH - effectiveViewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, WORLD_HEIGHT - effectiveViewportHeight / 2f);
    }
	
	@Override
    public void resize(int width, int height) {
        cam.viewportWidth = 30f;
        cam.viewportHeight = 30f * height/width;
        cam.update();
    }
	
	public static void temp_main(String[] args) throws InterruptedException, IOException {
		Scanner s = new Scanner(System.in);
		String ip, port;
		boolean host;
		Logger.Start();
		
		Logger.Dbg("Starting ColdWar!");
		
		GameState initialGameState = GameState.newBuilder().setTurn(0).build();
		renderer = new Renderer();
		renderer.TestUpdate("Hello, Capitalist World!");
		System.out.println(TextFormat.printToString(initialGameState));
		
		Peer peer = new Peer(new LwjglNet());
		
		renderer.TestUpdate("Enter port: ");
		port = s.next();
		Logger.Info("Received port: " + port);
		
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
			Logger.Info("Received IP: " + ip);
			peer.Connect(ip, Integer.valueOf(port));
			Builder moveList = MoveList.newBuilder();
			renderer.TestUpdate("Move 1:");
			moveList.addMovesBuilder().getFoundNatoMoveBuilder();
			peer.sendMoveList(moveList.build());
		}
		
		Logger.Close();
	}

}
