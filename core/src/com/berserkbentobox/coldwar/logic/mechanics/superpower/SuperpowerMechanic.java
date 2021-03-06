package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicMoves;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicSettings;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicState;
import com.berserkbentobox.coldwar.Superpower.UsaState;
import com.berserkbentobox.coldwar.Superpower.UssrState;
import com.berserkbentobox.coldwar.Treaty.TreatyMechanicMoves;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;

public class SuperpowerMechanic extends Mechanic {

	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private SuperpowerMechanicSettings settings;
		private Usa.Settings usaSettings;
		private Ussr.Settings ussrSettings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getSuperpowerSettings();
		}
		public Status validate() {
			return Status.OK;
		}
		
		public Usa.Settings getUsaSettings() {
			return this.usaSettings;
		}
		
		public Ussr.Settings getUssrSettings() {
			return this.ussrSettings;
		}
		
		public SuperpowerMechanicState initialState() {
			SuperpowerMechanicState.Builder state = SuperpowerMechanicState.newBuilder();
			usaSettings = new Usa.Settings(this, settings.getUsaSettings());
			ussrSettings = new Ussr.Settings(this, settings.getUssrSettings());
			UsaState usaState = usaSettings.initialState();
			UssrState ussrState = ussrSettings.initialState();
			state.setUsaState(usaState);
			state.setUssrState(ussrState);
			return state.build();
		}
	}
	
	private Settings settings;
	private SuperpowerMechanicState.Builder state;
	private Usa usa;
	private Ussr ussr;
	
	public SuperpowerMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getSuperpowerState().toBuilder();
		this.usa = new Usa(this, this.getSettings().getUsaSettings(), this.state.getUsaStateBuilder());
		this.ussr = new Ussr(this, this.getSettings().getUssrSettings(), this.state.getUssrStateBuilder());
	}
	
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public SuperpowerMechanicState buildState() {
		return this.state.build();
	}
	
	// Getters
	
	public Usa getUsa() {
		return this.usa;
	}
	
	public Ussr getUssr() {
		return this.ussr;
	}
	
	// Logic
	
	public boolean isReady(int year, Player player) {
		if(player == Player.USA) {
			if (this.getUsa().isReady(year))
				return true;
			return false;
		} else {
			return true;
		}
	}
	
	public void makeMoves(Player player, SuperpowerMechanicMoves moves) {
		if (moves.hasNominateMove()) {
			getUsa().setCandidate(moves.getNominateMove().getLeaderId());
		}
	}

	public void updateLeaders() {
		int year = this.getMechanics().getYear().getYear();
		if(this.getUsa().isElectionYear(year)) {
			this.getUsa().elections(year, this.getMechanics().getPseudorandom());
		}
		this.getUsa().maybeKillVicePresident(year, this.getMechanics().getPseudorandom());
		this.getUsa().maybeKillPresident(year, this.getMechanics().getPseudorandom());
		
		this.getUssr().troikaUpdate(this.getMechanics().getPseudorandom());
		this.getUssr().maybeKillLeader(year, this.getMechanics().getPseudorandom());
	}	
}
