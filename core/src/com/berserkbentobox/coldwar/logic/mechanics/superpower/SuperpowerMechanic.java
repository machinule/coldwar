package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import java.util.Collection;
import java.util.Map;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicSettings;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicState;
import com.berserkbentobox.coldwar.Superpower.UsaSettings;
import com.berserkbentobox.coldwar.Superpower.UsaState;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupState;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyGroup;

public class SuperpowerMechanic {

	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private SuperpowerMechanicSettings settings;
		private Usa.Settings usaSettings;
		//private Ussr ussr;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getSuperpowerSettings();
		}
		public Status validate() {
			return Status.OK;
		}
		
		public SuperpowerMechanicState initialState() {
			SuperpowerMechanicState.Builder state = SuperpowerMechanicState.newBuilder();
			usaSettings = new Usa.Settings(settings.getUsaSettings());
			UsaState usaState = usaSettings.initialState();
			//UsaState usaState = Usa.Settings.initialState(settings.getUsaSettings());
			settings.getUsaSettings();
			state.setUsaState(usaState);
			return state.build();
		}
	}
	
	private Settings settings;
	private SuperpowerMechanicState.Builder state;
	//private Map<String, UsaLeader> usaLeaders;
	//private Map<String, UssrLeader> ussrLeaders;
	
	public SuperpowerMechanic(Settings settings, GameStateOrBuilder state) {
		this.settings = settings;
		this.state = settings.initialState().toBuilder();
		
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
	
	public void USAholdElections(PseudorandomMechanic pseudorandomMechanic) {
		
	}
}
