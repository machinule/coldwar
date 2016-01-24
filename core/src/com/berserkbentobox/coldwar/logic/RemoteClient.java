package com.berserkbentobox.coldwar.logic;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.badlogic.gdx.Gdx;

import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Peer;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;

public class RemoteClient extends Client {

	private MoveBuilder currentMoveBuilder;
	private Peer peer;
	private boolean isHost;
	private MoveList usa;
	private MoveList ussr;
	
	public RemoteClient(String host, int port) {
		super();
		this.peer = new Peer(Gdx.app.getNet());
		this.peer.Connect(host, port);
		this.isHost = false;
		this.player = Player.USSR;
		this.state = this.getInitialGameState().build();
		this.currentMoveBuilder = new MoveBuilder(this.player, this.state, this.settings);
		Logger.Info("Starting game with initial game state:\n" + this.state.toString());
	}

	public RemoteClient(int port) {
		super();
		this.peer = new Peer(Gdx.app.getNet());
		this.peer.Host(port);
		this.isHost = true;
		this.player = Player.USA;
		this.state = this.getInitialGameState().build();
		this.currentMoveBuilder = new MoveBuilder(this.player, this.state, this.settings);
		Logger.Info("Starting game with initial game state:\n" + this.state.toString());
	}
	
	protected GameState.Builder getInitialGameState() {
		if (this.isHost) {
			GameState.Builder state = super.getInitialGameState();
			this.peer.sendGameState(state.build());
			return state;
		} else {
			GameState state = this.peer.getGameState();
			this.initialGameState = state;
			return state.toBuilder();
		}
	}
	

	@Override
	public MoveList getUSAMove() {
		if (this.player == Player.USA) {
			return this.usa;
		} else {
			this.usa = this.peer.getMoveList();
			return this.usa;
		}
	}

	@Override
	public MoveList getUSSRMove() {
		if (this.player == Player.USSR) {
			return this.ussr;
		} else {
			this.ussr = this.peer.getMoveList();
			return this.ussr;
		}
	}

	@Override
	public MoveBuilder getMoveBuilder() {
		return this.currentMoveBuilder;
	}

	public void finalizeTurn() {
		
	}
	
	@Override
	public Future<Boolean> endTurn() {
		Logger.Info("Ending turn for Player " + this.player);
		this.isWaitingOnPlayer = true;
		FutureTask<Boolean> task = new FutureTask<Boolean>(new Callable<Boolean>() {
			public Boolean call() {
				if (RemoteClient.this.player == Player.USA) {
					RemoteClient.this.usa = RemoteClient.this.currentMoveBuilder.getMoveList();
					RemoteClient.this.peer.sendMoveList(RemoteClient.this.usa);
				} else {
					RemoteClient.this.ussr = RemoteClient.this.currentMoveBuilder.getMoveList();
					RemoteClient.this.peer.sendMoveList(RemoteClient.this.ussr);
				}
				RemoteClient.this.nextTurn();
				RemoteClient.this.isWaitingOnPlayer = false;
				RemoteClient.this.currentMoveBuilder = new MoveBuilder(RemoteClient.this.player, RemoteClient.this.state, RemoteClient.this.settings);
				return true;
			}
		});
		new Thread(task).start();
		return task;
	}

}
