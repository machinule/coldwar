package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.Province.CovertMove;
import com.berserkbentobox.coldwar.Province.DiplomacyMove;
import com.berserkbentobox.coldwar.Province.FundDissidentsMove;
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
import com.berserkbentobox.coldwar.EffectOuterClass.Effect;
import com.berserkbentobox.coldwar.EffectOuterClass.ProvinceEffect;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Id.ProvinceRegion;
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
		ProvinceUtil.set(provinces, mechanics.getPseudorandom());
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
	
	public Government getGovernment(ProvinceId id) {
		return this.getProvince(id).getState().getGov();
	}
	
	public boolean hasDissidents(ProvinceId id) {
		return this.getProvince(id).getState().getDissidents() != Dissidents.getDefaultInstance();
	}
	
	public Dissidents getDissidents(ProvinceId id) {
		return this.getProvince(id).getState().getDissidents();
	}
	
	public Government getDissidentsGovernment(ProvinceId id) {
		return this.getProvince(id).getState().getDissidents().getGov();
	}
	
	// Validation
	
	public boolean isValidDiplomacyMove(Player player, ProvinceId id){
		return true;
//		return polStore.get(player) >= getDiaDipMoveMin(player, id) + getNonAdjacentDiaMoveCost(player, id) &&
//			   !isInArmedConflict(id);
	}
//	
	public boolean isValidMilitaryMove(Player player, ProvinceId id){
		return true;
//		return milStore.get(player) >= getDiaMilMoveMin(id) &&
//			   polStore.get(player) >= getNonAdjacentDiaMoveCost(player, id) && 
//			   getAlly(id) != otherPlayer(player) && 
//			   !isInArmedConflict(id);
	}
//	
	public boolean isValidCovertMove(Player player, ProvinceId id){
		return true;
//		return covStore.get(player) >= getDiaCovMoveMin(id) && 
//			   polStore.get(player) >= getNonAdjacentDiaMoveCost(player, id) && 
//			   !isInArmedConflict(id);
	}
//	
	public boolean isValidFundDissidentsMove(Player player, ProvinceId id) {
	return true;
//		return covStore.get(player) >= getFundDissidentsMoveCost() &&
//			   !(hasDissidents(id)) && 
//			   !isInArmedConflict(id);
	}
	
	// Cost
	
	public int getDiplomacyMoveBaseCost(Player player, ProvinceId id) {
		return this.settings.settings.getDiplomacyMoveBaseCost().getPoliticalPoints();
	}
	
	public int getDiplomacyMoveIncrementCost(Player player, ProvinceId id) {
		return this.settings.settings.getDiplomacyMoveIncrementCost().getPoliticalPoints();
	}

	public int getMilitaryMoveBaseCost(Player player, ProvinceId id) {
		return this.settings.settings.getMilitaryMoveBaseCost().getMilitaryPoints();
	}
	
	public int getMilitaryMoveIncrementCost(Player player, ProvinceId id) {
		return this.settings.settings.getMilitaryMoveIncrementCost().getMilitaryPoints();
	}
	
	public int getCovertMoveBaseCost(Player player, ProvinceId id) {
		return this.settings.settings.getCovertMoveBaseCost().getCovertPoints();
	}
	
	public int getCovertMoveIncrementCost(Player player, ProvinceId id) {
		return this.settings.settings.getCovertMoveIncrementCost().getCovertPoints();
	}
	
	public int getFundDissidentsMoveBaseCost() {
		return this.settings.settings.getFundDissidentsMoveBaseCost().getPoliticalPoints();
	}
	
	// Util
	
	public Player toPlayer(ProvinceId player) {
		if(player == ProvinceId.USA) 
			return Player.USA;
		else if(player == ProvinceId.USSR)
			return Player.USSR;
		else return null;
	}
	
	public ProvinceId toProvince(Player player) {
		if(player == Player.USA) 
			return ProvinceId.USA;
		else if(player == Player.USSR)
			return ProvinceId.USSR;
		else return null;
	}
	
	public Government getIdealGov(Player player) {
		if(player == Player.USA)
			return Government.DEMOCRACY;
		else
			return Government.COMMUNISM;
	}
	
	// Logic
	
	public void influenceProvince(Player player, ProvinceId id, int magnitude) {
		InfluenceUtil.influenceProvince(player, id, magnitude);
	}
	
	public void influenceProvince(ProvinceId id, int magnitude) {
		InfluenceUtil.influenceProvince(id, magnitude);
	}
	
	// Moves
	
	public void processEffects(Collection<ProvinceEffect> effects) {
		for(ProvinceEffect e : effects) {
			this.processEffect(e);
		}
	}
	
	public void processEffect(ProvinceEffect e) {
		List<ProvinceId> targets = e.getProvinceTargetsList();
		for(ProvinceRegion r : e.getRegionTargetsList()) {
			for(ProvinceId id : e.getProvinceTargetsList()) {
				if(provinces.get(id).getSettings().getSettings().getRegion() == r) {
					targets.add(id);
				}
			}
		}
		for(ProvinceId id : targets) {
			if(e.getInfluenceMod() > 0) {
				InfluenceUtil.influenceProvince(id, e.getInfluenceMod());
			}
			if(e.hasPerspective()) {
				Player player = toPlayer(e.getPerspective());
				if(e.hasAddDissidents())
					DissidentUtil.addDissidents(id, getIdealGov(player));
				if(e.hasRollDissidents()) {
					DissidentUtil.addDissidents(id, player);
				}
			} else {
				DissidentUtil.addDissidents(id);
			}
		}
	}
	
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
		if (moves.getFundDissidentsMoveCount() > 0) {
			makeFundDissidentMoves(player, moves.getFundDissidentsMoveList());
		}
	}

	public void makeDiplomacyMoves(Player player, List<DiplomacyMove> moves) {
		for(DiplomacyMove m : moves) {
			int magnitude = m.getMagnitude();
			this.getMechanics().getInfluence().spendPOL(player, magnitude);
			InfluenceUtil.influenceProvince(m.getProvinceId(), magnitude);
		}
	}
	
	public void makeMilitaryMoves(Player player, List<MilitaryMove> moves) {
		for(MilitaryMove m : moves) {
			int magnitude = m.getMagnitude();
			this.getMechanics().getInfluence().spendPOL(player, magnitude);
			InfluenceUtil.influenceProvince(m.getProvinceId(), magnitude);
		}
	}
	
	public void makeCovertMoves(Player player, List<CovertMove> moves) {
		for(CovertMove m : moves) {
			int magnitude = m.getMagnitude();
			this.getMechanics().getInfluence().spendCOV(player, magnitude);
			InfluenceUtil.influenceProvince(player, m.getProvinceId(), magnitude);
		}
	}
	
	public void makeFundDissidentMoves(Player player, List<FundDissidentsMove> moves) {
		for(FundDissidentsMove m : moves) {
			DissidentUtil.addDissidents(m.getProvinceId(), player);
		}
	}
}