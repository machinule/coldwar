package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Province.CovertMove;
import com.berserkbentobox.coldwar.Province.DiplomacyMove;
import com.berserkbentobox.coldwar.Province.MilitaryMove;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicMoves;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicSettings;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicState;
import com.berserkbentobox.coldwar.Province.ProvinceSettings;
import com.berserkbentobox.coldwar.Province.ProvinceState;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderState;
import com.berserkbentobox.coldwar.Superpower.UsaState;
import com.berserkbentobox.coldwar.Technology.ResearchMove;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicMoves;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.mechanics.influencestore.InfluenceStoreMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.UsaLeader;

public class ProvinceMechanic extends Mechanic {

	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private ProvinceMechanicSettings settings;
		private Map<ProvinceId, Province.Settings> provinceSettings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getProvinceSettings();
			provinceSettings = new HashMap<ProvinceId, Province.Settings>(); 
			for (ProvinceSettings p : this.settings.getProvinceList()) {
				Province.Settings ps = new Province.Settings(p);
				this.provinceSettings.put(ps.getSettings().getId(), ps);
			}
		}
		
		public Province.Settings getProvinceSettings(ProvinceId id) {
			return this.provinceSettings.get(id);
		}
		
		public Collection<Province.Settings> getProvinceSettings() {
			return this.provinceSettings.values();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public ProvinceMechanicState initialState() {
			ProvinceMechanicState.Builder state = ProvinceMechanicState.newBuilder();
			for (Province.Settings ps : this.getProvinceSettings()) {
				state.addProvinceState(ps.initialState());
			}
			return state.build();
		}
	}
	
	private Settings settings;
	private ProvinceMechanicState.Builder state;
	private Map<ProvinceId, Province> provinces;
	
	public ProvinceMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getProvinceState().toBuilder();
		provinces = new HashMap<ProvinceId, Province>();
		for (ProvinceState.Builder ps : this.state.getProvinceStateBuilderList()) {
			Province p = new Province(this, this.getSettings().getProvinceSettings(ps.getId()), ps);
			this.provinces.put(p.getState().getId(), p);
		}
	}
	
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public ProvinceMechanicState buildState() {
		return this.state.build();
	}
	
	// Getters
	
	public Province getProvince(ProvinceId id) {
		return this.provinces.get(id);
	}
	
	// Logic
	
	public void influenceProvince(Player player, ProvinceId id, int magnitude) {
		if(player == Player.USSR)
			magnitude = magnitude * -1;
		influenceProvince(id, magnitude);
	}
	
	public void influenceProvince(ProvinceId id, int magnitude) {
		Province target = this.provinces.get(id);
		target.influence(magnitude);
	}
	
	// Moves
	
	public void makeMoves(Player player, ProvinceMechanicMoves moves) {
		if (moves.getDiplomacyMoveCount() > 0) {
			makeDiplomacyMoves(player, moves.getDiplomacyMoveList());
		}
		if (moves.getMilitaryMoveCount() > 0) {
			makeMilitaryMoves(player, moves.getMilitaryMoveList());
		}
		if (moves.getCovertMoveCount() > 0) {
			makeCovertMoves(player, moves.getCovertMoveList());
		}
	}	

	public void makeDiplomacyMoves(Player player, List<DiplomacyMove> moves) {
		int inflSign = 1;
		if (player == Player.USSR)
			inflSign = inflSign * -1;
		for(DiplomacyMove m : moves) {
			int magnitude = m.getMagnitude() * inflSign;
			this.getMechanics().getInfluence().spendPOL(player, magnitude);
			this.provinces.get(m.getProvinceId()).influence(magnitude);
		}
	}
	
	public void makeMilitaryMoves(Player player, List<MilitaryMove> moves) {
		int inflSign = 1;
		if (player == Player.USSR)
			inflSign = inflSign * -1;
		for(MilitaryMove m : moves) {
			int magnitude = m.getMagnitude() * inflSign;
			this.getMechanics().getInfluence().spendPOL(player, magnitude);
			this.provinces.get(m.getProvinceId()).influence(magnitude);
		}
	}
	
	public void makeCovertMoves(Player player, List<CovertMove> moves) {
		int inflSign = 1;
		if (player == Player.USSR)
			inflSign = inflSign * -1;
		for(CovertMove m : moves) {
			int magnitude = m.getMagnitude() * inflSign;
			this.getMechanics().getInfluence().spendCOV(player, magnitude);
			this.provinces.get(m.getProvinceId()).influence(magnitude);
		}
	}
	
}