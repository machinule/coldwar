package com.berserkbentobox.coldwar.logic.mechanics.treaty;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicMoves;
import com.berserkbentobox.coldwar.Treaty.DeescalateMove;
import com.berserkbentobox.coldwar.Treaty.TreatyMechanicMoves;
import com.berserkbentobox.coldwar.Treaty.TreatyMechanicSettings;
import com.berserkbentobox.coldwar.Treaty.TreatyMechanicState;
import com.berserkbentobox.coldwar.Treaty.TreatySettings;
import com.berserkbentobox.coldwar.Treaty.TreatyState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.mechanics.deterrance.DeterrenceMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.heat.HeatMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.technology.Technology;

public class TreatyMechanic extends Mechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private TreatyMechanicSettings settings;
		private Map<String, Treaty.Settings> treatySettings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getTreatySettings();
			this.treatySettings = new LinkedHashMap<String, Treaty.Settings>();
			for (TreatySettings t : this.settings.getTreatyList()) {
				Treaty.Settings ts = new Treaty.Settings(this, t);
				this.treatySettings.put(ts.getSettings().getId(), ts);
			}
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public TreatyMechanicSettings getSettings() {
			return this.settings;
		}
		
		public Collection<Treaty.Settings> getTreatySettings() {
			return this.treatySettings.values();
		}
		
		public Treaty.Settings getTreatySettings(String id) {
			return this.treatySettings.get(id);
		}
		
		public TreatyMechanicState initialState() {
			TreatyMechanicState.Builder state = TreatyMechanicState.newBuilder();
			for (Treaty.Settings ts : this.getTreatySettings()) {
				state.addTreaty(ts.initialState());				
			}
			return state.build();
		}
	}
	
	private Settings settings;
	private TreatyMechanicState.Builder state;
	private Map<String, Treaty> treaties;
	
	public TreatyMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getTreatyState().toBuilder();
		this.treaties = new LinkedHashMap<String, Treaty>();
		for (TreatyState.Builder ts : this.state.getTreatyBuilderList()) {
			Treaty t = new Treaty(this, this.getSettings().getTreatySettings(ts.getId()), ts);
			this.treaties.put(t.getState().getId(), t);
		}
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public TreatyMechanicState buildState() {
		return this.state.build();
	}
	
	private Map<String, Treaty> getTreatyMap() {
		return this.treaties;
	}
	
	public Collection<Treaty> getTreaties() {
		return this.getTreatyMap().values();
	}
	
	public Treaty getTreaty(String id) {
		return this.getTreatyMap().get(id);
	}

	// Logic
	
	public void makeMoves(Player player, TreatyMechanicMoves moves) {
		if (moves.hasDeescalateMove()) {
			makeDeescalateMove(player, moves.getDeescalateMove());
		}
	}	

	boolean usaDeescalate = false;
	boolean ussrDeescalate = false;
	
	public void makeDeescalateMove(Player player, DeescalateMove move) {
		if (player == Player.USA) {
			usaDeescalate = true;
		} else {
			ussrDeescalate = true;
		}
	}

	public void maybeSignTreaty() {
		if (this.usaDeescalate) {
			this.getMechanics().getHeat().decrease(this.getSettings().getSettings().getDeescalateAmount());
		}
		if (this.ussrDeescalate) {
			this.getMechanics().getHeat().decrease(this.getSettings().getSettings().getDeescalateAmount());
		}
		if (!(this.usaDeescalate && this.ussrDeescalate)) {
			return;
		}
		Treaty toSign = this.getFirstUnsignedTreaty();
		if (this.getMechanics().getPseudorandom().happens(toSign.getSettings().getSettings().getSigningChance())) {
			toSign.Sign(this.getMechanics().getHeat(), this.getMechanics().getDeterrence());
		}
	}
	
	public Treaty getFirstUnsignedTreaty() {
		for (Treaty t : this.getTreaties()) {
			if (!t.isSigned()) {
				return t;
			}
		}
		return null;
	}
	
	public boolean hasAnyUnsignedTreaties() {
		return this.getFirstUnsignedTreaty() != null;
	}
}
