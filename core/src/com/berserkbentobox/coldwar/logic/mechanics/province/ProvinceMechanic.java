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
import com.berserkbentobox.coldwar.EffectOuterClass.ProvinceEffect;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Id.ProvinceRegion;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;

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
	
	private ProvinceUtil provUtil;
	private InfluenceUtil inflUtil;
	private DissidentUtil dissUtil;
	private ChanceUtil chncUtil;
	
	public ProvinceMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getProvinceState().toBuilder();
		provinces = new HashMap<ProvinceId, Province>();
		for (ProvinceState.Builder ps : this.state.getProvinceStateBuilderList()) {
			Province p = new Province(this, this.getSettings().getProvinceSettings(ps.getId()), ps);
			this.provinces.put(p.getState().getId(), p);
		}
		
		provUtil = new ProvinceUtil(provinces, mechanics.getPseudorandom());
		inflUtil = new InfluenceUtil(provUtil);
		dissUtil = new DissidentUtil(provUtil);
		chncUtil = new ChanceUtil(provUtil);
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
	
/*
 * ==============================================================
 * GETTERS
 * ==============================================================
 */
	
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
	
/*
 * ==============================================================
 * VALIDATION
 * ==============================================================
 */
	
	public boolean isValidDiplomacyMove(Player player, ProvinceId id){
		if(1 > this.getDiplomacyMoveMaxValue(player, id)) {
			return false;
		}
		return true;
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
	
/*
 * ==============================================================
 * COST
 * ==============================================================
 */
	
	// DiplomacyMove
	
	public int getDiplomacyMoveBaseCost(Player player, ProvinceId id) {
		return this.settings.settings.getDiplomacyMoveBaseCost().getPoliticalPoints();
	}
	
	public int getDiplomacyMoveIncrementCost(Player player, ProvinceId id) {
		int ret = this.settings.settings.getDiplomacyMoveIncrementCost().getPoliticalPoints();
		if(!this.provinces.get(id).isAdj(player)) {
			ret += this.settings.settings.getDiplomacyMoveNonAdjCost().getPoliticalPoints();
		}
		Logger.Dbg("Increment cost: " + ret);
		return ret;
	}
	
	public int getDiplomacyMoveMaxValue(Player player, ProvinceId id) {
		return Math.min(this.getMechanics().getInfluenceStore().getPolitical(player) * this.getDiplomacyMoveIncrementCost(player, id)
				+ this.getDiplomacyMoveBaseCost(player, id),
				provinces.get(id).getNetStability() + 1);
	}
	
	public int getDiplomacyMoveMinCost(Player player, ProvinceId id) {
		return this.getDiplomacyMoveIncrementCost(player, id) + this.getDiplomacyMoveBaseCost(player, id);
	}
	
	public int getDiplomacyMoveCost(Player player, ProvinceId id, int magnitude) {
		return (magnitude * this.getDiplomacyMoveIncrementCost(player, id)) + this.getDiplomacyMoveBaseCost(player, id);
	}
	
	// MilitaryMove
	
	public int getMilitaryMoveBaseCost(Player player, ProvinceId id) {
		return this.settings.settings.getMilitaryMoveBaseCost().getMilitaryPoints();
	}
	
	public int getMilitaryMoveIncrementCost(Player player, ProvinceId id) {
		int ret = this.settings.settings.getMilitaryMoveIncrementCost().getMilitaryPoints();
		if(!this.provinces.get(id).isAdj(player)) {
			ret += this.settings.settings.getMilitaryMoveNonAdjCost().getMilitaryPoints();
		}
		return ret;
	}
	
	public int getMilitaryMoveMaxValue(Player player, ProvinceId id) {
		return Math.min(this.getMechanics().getInfluenceStore().getMilitary(player) * this.getMilitaryMoveIncrementCost(player, id)
				+ this.getMilitaryMoveBaseCost(player, id),
				provinces.get(id).getNetStability());
	}
	
	public int getMilitaryMoveMinCost(Player player, ProvinceId id) {
		return this.getMilitaryMoveIncrementCost(player, id) + this.getMilitaryMoveBaseCost(player, id);
	}
	
	public int getMilitaryMoveCost(Player player, ProvinceId id, int magnitude) {
		return magnitude * this.getMilitaryMoveIncrementCost(player, id) + this.getMilitaryMoveBaseCost(player, id);
	}	
	
	// CovertMove
	
	public int getCovertMoveBaseCost(Player player, ProvinceId id) {
		return this.settings.settings.getCovertMoveBaseCost().getCovertPoints();
	}
	
	public int getCovertMoveIncrementCost(Player player, ProvinceId id) {
		int ret = this.settings.settings.getCovertMoveIncrementCost().getCovertPoints();
		if(!this.provinces.get(id).isAdj(player)) {
			ret += this.settings.settings.getCovertMoveNonAdjCost().getCovertPoints();
		}
		return ret;
	}
	
	public int getCovertMoveMaxValue(Player player, ProvinceId id) {
		return Math.min(this.getMechanics().getInfluenceStore().getCovert(player) * this.getCovertMoveIncrementCost(player, id)
				+ this.getCovertMoveBaseCost(player, id),
				provinces.get(id).getNetStability());
	}
	
	public int getCovertMoveMinCost(Player player, ProvinceId id) {
		return this.getCovertMoveIncrementCost(player, id) + this.getCovertMoveBaseCost(player, id);
	}
	
	public int getCovertMoveCost(Player player, ProvinceId id, int magnitude) {
		return magnitude * this.getCovertMoveIncrementCost(player, id) + this.getCovertMoveBaseCost(player, id);
	}
	
	// FundDissidentsMove
	
	public int getFundDissidentsMoveBaseCost() {
		return this.settings.settings.getFundDissidentsMoveBaseCost().getPoliticalPoints();
	}
	
/*
 * ==============================================================
 * UTIL
 * ==============================================================
 */
	
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
	
/*
 * ==============================================================
 * LOGIC
 * ==============================================================
 */
	
	public void influenceProvince(Player player, ProvinceId id, int magnitude) {
		inflUtil.influenceProvince(player, id, magnitude);
	}
	
	public void influenceProvince(ProvinceId id, int magnitude) {
		inflUtil.influenceProvince(id, magnitude);
	}
	
/*
 * ==============================================================
 * MOVES
 * ==============================================================
 */
	
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
				inflUtil.influenceProvince(id, e.getInfluenceMod());
			}
			if(e.hasPerspective()) {
				Player player = toPlayer(e.getPerspective());
				if(e.hasAddDissidents())
					dissUtil.addDissidents(id, getIdealGov(player));
				if(e.hasRollDissidents()) {
					dissUtil.addDissidents(id, player);
				}
			} else {
				dissUtil.addDissidents(id);
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
			this.getMechanics().getInfluenceStore().spendPOL(player, this.getDiplomacyMoveCost(player, m.getProvinceId(), magnitude));
			inflUtil.influenceProvince(m.getProvinceId(), magnitude);
		}
	}
	
	public void makeMilitaryMoves(Player player, List<MilitaryMove> moves) {
		for(MilitaryMove m : moves) {
			int magnitude = m.getMagnitude();
			this.getMechanics().getInfluenceStore().spendMIL(player, magnitude * this.getMilitaryMoveIncrementCost(player, m.getProvinceId()));
			inflUtil.influenceProvince(m.getProvinceId(), magnitude);
		}
	}
	
	public void makeCovertMoves(Player player, List<CovertMove> moves) {
		for(CovertMove m : moves) {
			int magnitude = m.getMagnitude();
			this.getMechanics().getInfluenceStore().spendCOV(player, magnitude * this.getCovertMoveIncrementCost(player, m.getProvinceId()));
			inflUtil.influenceProvince(player, m.getProvinceId(), magnitude);
		}
	}
	
	public void makeFundDissidentMoves(Player player, List<FundDissidentsMove> moves) {
		for(FundDissidentsMove m : moves) {
			dissUtil.addDissidents(m.getProvinceId(), player);
		}
	}
}