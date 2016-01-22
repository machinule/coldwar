package com.berserkbentobox.coldwar.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.berserkbentobox.coldwar.GameStateOuterClass.Crisis;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.TurnLogEntry;
import com.berserkbentobox.coldwar.Heat.HeatMechanicState;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.EventOuterClass.CivilWarEvent;
import com.berserkbentobox.coldwar.EventOuterClass.CoupEvent;
import com.berserkbentobox.coldwar.EventOuterClass.Event;
import com.berserkbentobox.coldwar.EventOuterClass.ProvinceDissidentsEvent;
import com.berserkbentobox.coldwar.EventOuterClass.ProvinceDissidentsSuppressedEvent;
import com.berserkbentobox.coldwar.EventOuterClass.ProvinceFauxPasEvent;
import com.berserkbentobox.coldwar.EventOuterClass.ProvinceRepublicEvent;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.Province.Conflict;
import com.berserkbentobox.coldwar.Province.ProvinceGameState;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Province.ProvinceRegion;
import com.berserkbentobox.coldwar.Province.ProvinceState;
import com.berserkbentobox.coldwar.MoveOuterClass.Move;
import com.berserkbentobox.coldwar.logic.Client.Player;

/**
 * A ComputedGameState contains public, final variables representing variables computed from a given game state and move lists.
 */
public class ComputedGameState {

	public final GameState state;
	public final MoveList usaMoves;
	public final MoveList ussrMoves;
	public final int year;
	public final int heat;
	public final int partyUnity;
	public final int patriotism;
		
	public final Map<Player, Integer> polStore;
	public final Map<Player, Integer> milStore;
	public final Map<Player, Integer> covStore;

	public final Map<Player, Integer> basePolIncome;
	public final Map<Player, Integer> baseMilIncome;
	public final Map<Player, Integer> baseCovIncome;
	
	public final Map<Player, Integer> polIncomeModifier;
	public final Map<Player, Integer> milIncomeModifier;
	public final Map<Player, Integer> covIncomeModifier;
	
	public final Map<ProvinceRegion, Integer> usaRegionControl;
	public final Map<ProvinceRegion, Integer> ussrRegionControl;
	public final Map<ProvinceRegion, Integer> regionTotal;
	
	public final Map<ProvinceId, Integer> initialInfluence;
	public final Map<ProvinceId, Integer> polInfluence;
	public final Map<ProvinceId, Integer> milInfluence;
	public final Map<ProvinceId, Integer> covInfluence;
	public final Map<ProvinceId, Integer> totalInfluence;

	public final Map<ProvinceId, Player>  alliances; // NULL -> Neither player
	public final Map<ProvinceId, Boolean> usaAdjacencies;
	public final Map<ProvinceId, Boolean> ussrAdjacencies;
	
	public final Map<ProvinceId, Dissidents> dissidents;
	public final Map<ProvinceId, Player> bases;
	public final Map<ProvinceId, Government> governments;
	public final Map<ProvinceId, Leader> leaders;
	public final Map<ProvinceId, Conflict> activeConflicts;
	public final Map<ProvinceId, Conflict> conflictZones;
	
	public final Map<ProvinceId, Integer> stabilityBase;
	public final Map<ProvinceId, Integer> stabilityModifier;
	
	public final Map<ProvinceId, ProvinceSettings> provinceSettings;
	
	public final Map<ProvinceId, Integer> coups;
	
	public final Map<ProvinceId, Boolean> acted;
	
	public final GameState nextState;
	
	public ComputedGameState(final GameState state, final MoveList usaMoves, final MoveList ussrMoves) {
		this.state = state;
		this.usaMoves = usaMoves;
		this.ussrMoves = ussrMoves;
		
		this.year = state.getTurn() + 1948;
		int heatCounter = state.getHeatState().getHeat();
		int partyUnityCounter = 0; //state.getUssr().getPartyUnity();
		int patriotismCounter = 0; //state.getUsa().getPatriotism();
		
		Crisis.Builder crisis = state.getCrises().toBuilder();
		
		EnumMap<Player, Integer> polStoreMap = new EnumMap<Player, Integer>(Player.class);
		this.polStore = Collections.unmodifiableMap(polStoreMap);
		EnumMap<Player, Integer> milStoreMap = new EnumMap<Player, Integer>(Player.class);
		this.milStore = Collections.unmodifiableMap(milStoreMap);
		EnumMap<Player, Integer> covStoreMap = new EnumMap<Player, Integer>(Player.class);
		this.covStore = Collections.unmodifiableMap(covStoreMap);
		
		EnumMap<Player, Integer> basePolIncomeMap = new EnumMap<Player, Integer>(Player.class);
		this.basePolIncome = Collections.unmodifiableMap(basePolIncomeMap);
		EnumMap<Player, Integer> baseMilIncomeMap = new EnumMap<Player, Integer>(Player.class);
		this.baseMilIncome = Collections.unmodifiableMap(baseMilIncomeMap);
		EnumMap<Player, Integer> baseCovIncomeMap = new EnumMap<Player, Integer>(Player.class);
		this.baseCovIncome = Collections.unmodifiableMap(baseCovIncomeMap);

		EnumMap<Player, Integer> polIncomeModifierMap = new EnumMap<Player, Integer>(Player.class);
		this.polIncomeModifier = Collections.unmodifiableMap(polIncomeModifierMap);
		EnumMap<Player, Integer> milIncomeModifierMap = new EnumMap<Player, Integer>(Player.class);
		this.milIncomeModifier = Collections.unmodifiableMap(milIncomeModifierMap);
		EnumMap<Player, Integer> covIncomeModifierMap = new EnumMap<Player, Integer>(Player.class);
		this.covIncomeModifier = Collections.unmodifiableMap(covIncomeModifierMap);
		
		EnumMap<ProvinceRegion, Integer> usaRegionControlMap = new EnumMap<ProvinceRegion, Integer>(ProvinceRegion.class);
		this.usaRegionControl = Collections.unmodifiableMap(usaRegionControlMap);
		EnumMap<ProvinceRegion, Integer> ussrRegionControlMap = new EnumMap<ProvinceRegion, Integer>(ProvinceRegion.class);
		this.ussrRegionControl = Collections.unmodifiableMap(ussrRegionControlMap);
		EnumMap<ProvinceRegion, Integer> regionTotalMap = new EnumMap<ProvinceRegion, Integer>(ProvinceRegion.class);
		this.regionTotal = Collections.unmodifiableMap(regionTotalMap);
		
		EnumMap<ProvinceId, Integer> baseInfluenceMap = new EnumMap<ProvinceId, Integer>(ProvinceId.class);
		this.initialInfluence = Collections.unmodifiableMap(baseInfluenceMap);
		EnumMap<ProvinceId, Integer> polInfluenceMap = new EnumMap<ProvinceId, Integer>(ProvinceId.class);
		this.polInfluence = Collections.unmodifiableMap(polInfluenceMap);
		EnumMap<ProvinceId, Integer> milInfluenceMap = new EnumMap<ProvinceId, Integer>(ProvinceId.class);
		this.milInfluence = Collections.unmodifiableMap(milInfluenceMap);
		EnumMap<ProvinceId, Integer> covInfluenceMap = new EnumMap<ProvinceId, Integer>(ProvinceId.class);
		this.covInfluence = Collections.unmodifiableMap(covInfluenceMap);
		EnumMap<ProvinceId, Integer> totalInfluenceMap = new EnumMap<ProvinceId, Integer>(ProvinceId.class);
		this.totalInfluence = Collections.unmodifiableMap(totalInfluenceMap);
		
		EnumMap<ProvinceId, Player> allianceMap = new EnumMap<ProvinceId, Player>(ProvinceId.class);
		this.alliances = Collections.unmodifiableMap(allianceMap);
		EnumMap<ProvinceId, Boolean> usaAdjacencyMap = new EnumMap<ProvinceId, Boolean>(ProvinceId.class);
		this.usaAdjacencies = Collections.unmodifiableMap(usaAdjacencyMap);
		EnumMap<ProvinceId, Boolean> ussrAdjacencyMap = new EnumMap<ProvinceId, Boolean>(ProvinceId.class);
		this.ussrAdjacencies = Collections.unmodifiableMap(ussrAdjacencyMap);

		EnumMap<ProvinceId, Dissidents> dissidentsMap = new EnumMap<ProvinceId, Dissidents>(ProvinceId.class);
		this.dissidents = Collections.unmodifiableMap(dissidentsMap);
		EnumMap<ProvinceId, Player> baseMap = new EnumMap<ProvinceId, Player>(ProvinceId.class);
		this.bases = Collections.unmodifiableMap(baseMap);
		EnumMap<ProvinceId, Government> governmentMap = new EnumMap<ProvinceId, Government>(ProvinceId.class);
		this.governments = Collections.unmodifiableMap(governmentMap);
		EnumMap<ProvinceId, Conflict> activeConflictMap = new EnumMap<ProvinceId, Conflict>(ProvinceId.class);
		this.activeConflicts = Collections.unmodifiableMap(activeConflictMap);
		EnumMap<ProvinceId, Conflict> conflictZoneMap = new EnumMap<ProvinceId, Conflict>(ProvinceId.class);
		this.conflictZones = Collections.unmodifiableMap(conflictZoneMap);
		
		EnumMap<ProvinceId, Leader> leaderMap = new EnumMap<ProvinceId, Leader>(ProvinceId.class);
		this.leaders = Collections.unmodifiableMap(leaderMap);

		EnumMap<ProvinceId, Integer> stabilityBaseMap = new EnumMap<ProvinceId, Integer>(ProvinceId.class);
		this.stabilityBase = Collections.unmodifiableMap(stabilityBaseMap);
		EnumMap<ProvinceId, Integer> stabilityModifierMap = new EnumMap<ProvinceId, Integer>(ProvinceId.class);
		this.stabilityModifier = Collections.unmodifiableMap(stabilityModifierMap);	

		EnumMap<ProvinceId, ProvinceSettings> provinceSettingsMap = new EnumMap<ProvinceId, ProvinceSettings>(ProvinceId.class);
		this.provinceSettings = Collections.unmodifiableMap(provinceSettingsMap);

		EnumMap<ProvinceId, Integer> coupMap = new EnumMap<ProvinceId, Integer>(ProvinceId.class);
		this.coups = Collections.unmodifiableMap(coupMap);

		EnumMap<ProvinceId, Boolean> actedMap = new EnumMap<ProvinceId, Boolean>(ProvinceId.class);
		this.acted = Collections.unmodifiableMap(actedMap);
		
		// Computations:
		// NOTE: computations should only expose a read-only version of themselves as public variables.
		// For collections, Collections.unmodifiable* produces an appropriate view.
		
		this.state.getSettings().getProvinceSettings().getProvinceList().forEach(p -> {
			stabilityBaseMap.put(p.getId(), p.getStabilityBase());
			provinceSettingsMap.put(p.getId(), p);
			regionTotalMap.put(p.getRegion(), regionTotalMap.getOrDefault(p.getRegion(), 0) + 1);
		});
		
		this.state.getProvinceState().getProvinceStateList().forEach(p -> {
			baseInfluenceMap.put(p.getId(), p.getInfluence());
			baseMap.put(p.getId(), toPlayer(p.getBase()));
			governmentMap.put(p.getId(), p.getGov());
			actedMap.put(p.getId(), false);
			//leaderMap.put(p.getId(), p.getLeader());
			dissidentsMap.put(p.getId(), p.getDissidents());
			activeConflictMap.put(p.getId(), p.getConflict());
		});
		
		totalInfluenceMap.putAll(baseInfluenceMap);
		
		this.state.getProvinceState().getProvinceStateList().forEach(p -> {
			usaAdjacencyMap.put(p.getId(), hasAdjacencyInfluence(Player.USA, p.getId()));
			ussrAdjacencyMap.put(p.getId(), hasAdjacencyInfluence(Player.USSR, p.getId()));
		});
		
		polStoreMap.put(Player.USA, 0);//state.getUsa().getInfluenceStore().getPolitical());
		milStoreMap.put(Player.USA, 0);//state.getUsa().getInfluenceStore().getMilitary());
		covStoreMap.put(Player.USA, 0);//state.getUsa().getInfluenceStore().getCovert());
		
		polStoreMap.put(Player.USSR, 0);//state.getUssr().getInfluenceStore().getPolitical());
		milStoreMap.put(Player.USSR, 0);//state.getUssr().getInfluenceStore().getMilitary());
		covStoreMap.put(Player.USSR, 0);//state.getUssr().getInfluenceStore().getCovert());
		
		basePolIncomeMap.put(Player.USA, state.getSettings().getSuperpowerSettings().getUsaSettings().getInfluenceStoreSettings().getPoliticalIncomeBase());
		baseMilIncomeMap.put(Player.USA, state.getSettings().getSuperpowerSettings().getUsaSettings().getInfluenceStoreSettings().getMilitaryIncomeBase());
		baseCovIncomeMap.put(Player.USA, state.getSettings().getSuperpowerSettings().getUsaSettings().getInfluenceStoreSettings().getCovertIncomeBase());

		basePolIncomeMap.put(Player.USSR, state.getSettings().getSuperpowerSettings().getUssrSettings().getInfluenceStoreSettings().getPoliticalIncomeBase());
		baseMilIncomeMap.put(Player.USSR, state.getSettings().getSuperpowerSettings().getUssrSettings().getInfluenceStoreSettings().getMilitaryIncomeBase());
		baseCovIncomeMap.put(Player.USSR, state.getSettings().getSuperpowerSettings().getUssrSettings().getInfluenceStoreSettings().getCovertIncomeBase());

		for (Player player : new Player[]{Player.USA, Player.USSR}) {
			MoveList moves;
			int inflSign;
			if (player == Player.USA) {
				moves = usaMoves;
				inflSign = 1;
			} else {
				moves = ussrMoves;
				inflSign = -1;
			}
			for (Move move : moves.getMovesList()) {
				if (move.hasDiaDipMove()) {
					ProvinceId id = move.getDiaDipMove().getProvinceId();
					if(isValidDiaDipMove(player, id)) {
						final int mag = move.getDiaDipMove().getMagnitude();
						final int mod;
						if(governments.get(id) == Government.AUTOCRACY) mod = -1;
						else mod = 0;
						final int effect_multiplier;
						if(getAlly(move.getDiaDipMove().getProvinceId()) == otherPlayer(player)) {
							effect_multiplier = 2;
						} else {
							effect_multiplier = 1;
						}
						polInfluenceMap.compute(id, (i, infl) -> infl == null ? ((mag/effect_multiplier) + mod) * inflSign : infl + ((mag/effect_multiplier) + mod) * inflSign);
						polStoreMap.compute(player, (p, pol) -> pol == null ? -mag : pol - mag);
						int nonAdjCost = getNonAdjacentDiaMoveCost(player, id);
						if(nonAdjCost != 0) {
							Logger.Dbg("Adding non-adjacent cost of " + nonAdjCost);
							polStoreMap.compute(player, (p, pol) -> pol == null ? -nonAdjCost : pol - nonAdjCost);
						}
						actedMap.put(id, true);
					}
					
				}
				if (move.hasDiaMilMove()) {
					ProvinceId id = move.getDiaMilMove().getProvinceId();
					if(isValidDiaMilMove(player, id)) {
						final int mag = move.getDiaMilMove().getMagnitude();
						final int mod;
						if(governments.get(id) == Government.AUTOCRACY) mod = -1;
						else mod = 0;
						milInfluenceMap.compute(id, (i, infl) -> infl == null ? (mag + mod) * inflSign : infl + (mag + mod) * inflSign);
						milStoreMap.compute(player, (p, mil) -> mil == null ? -mag : mil - mag);
						int nonAdjCost = getNonAdjacentDiaMoveCost(player, id);
						if(nonAdjCost != 0) {
							Logger.Dbg("Adding non-adjacent cost of " + nonAdjCost);
							polStoreMap.compute(player, (p, pol) -> pol == null ? -nonAdjCost : pol - nonAdjCost);
						}
						actedMap.put(id, true);
					}
				}
				if (move.hasDiaCovMove()) {
					ProvinceId id = move.getDiaCovMove().getProvinceId();
					if(isValidDiaCovMove(player, id)) {
						final int mag = move.getDiaCovMove().getMagnitude();
						final int mod;
						if(governments.get(id) == Government.AUTOCRACY) mod = -1;
						else mod = 0;
						covInfluenceMap.compute(id, (i, infl) -> infl == null ? (mag + mod) * inflSign : infl + (mag + mod) * inflSign);
						covStoreMap.compute(player, (p, cov) -> cov == null ? -mag : cov - mag);
						int nonAdjCost = getNonAdjacentDiaMoveCost(player, id);
						if(nonAdjCost != 0) {
							Logger.Dbg("Adding non-adjacent cost of " + nonAdjCost);
							polStoreMap.compute(player, (p, pol) -> pol == null ? -nonAdjCost : pol - nonAdjCost);
						}
						actedMap.put(id, true);
					}
				}
				if (move.hasFundDissidentsMove()) {
					ProvinceId id = move.getFundDissidentsMove().getProvinceId();
					if(isValidFundDissidentsMove(player, id)) {
						final int cost = getFundDissidentsMoveCost();
						Dissidents.Builder d = Dissidents.newBuilder();
						if(player == Player.USA)
							d.setGov(Government.REPUBLIC);
						else if(player == Player.USSR)
							d.setGov(Government.COMMUNISM);
						dissidentsMap.put(id, d.build());
						Logger.Dbg("Adding " + d.getGov() + " to " + id);
						// TODO: Dissident leaders
						dissidentsMap.put(id, d.build());
						covStoreMap.compute(player, (p, cov) -> cov == null ? -cost : cov - cost);
						heatCounter += this.state.getSettings().getMoveSettings().getActionDissidentsHeat();
						actedMap.put(id, true);
					}
				}
				if (move.hasEstablishBaseMove()) {
					ProvinceId id = move.getEstablishBaseMove().getProvinceId();
					if(isValidEstablishBaseMove(player, id)) {
						final int cost = getEstablishBaseMoveCost();
						baseMap.put(id, player);
						milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
						heatCounter += this.state.getSettings().getMoveSettings().getActionDissidentsHeat();
						actedMap.put(id, true);
					}
				}
				if (move.hasPoliticalPressureMove()) {
					ProvinceId id = move.getPoliticalPressureMove().getProvinceId();
					if(isValidPoliticalPressureMove(player, id)) {
						final int cost = getPoliticalPressureMoveCost();
						int netInfl = 0;
						for (ProvinceId adj : provinceSettingsMap.get(id).getAdjacencyList()) {
							Logger.Dbg("Seeing pressure from: " + adj);
							if(getAlly(adj) == otherPlayer(player)) {
								netInfl -= 1;
								Logger.Dbg("Neighboring enemy ally -> -1");
							} else if(getAlly(adj) == player) {
								netInfl += 1;
								Logger.Dbg("Neighboring friendly ally -> +1");
							}
							if(governmentMap.get(adj) == getIdealGov(player)) {
								netInfl += 1;
								Logger.Dbg("Neighboring friendly government -> +1");
							} else if(governmentMap.get(adj) == getIdealGov(otherPlayer(player))) {
								netInfl -= 1;
								Logger.Dbg("Neighboring enemy government -> -1");
							} 
						}
						Logger.Dbg("Net influence: " + netInfl);
						final int finInfl = netInfl * inflSign;
						polInfluenceMap.compute(id, (i, infl) -> infl == null ? finInfl : infl + finInfl);
						polStoreMap.compute(player,  (p, pol) -> pol == null ? -cost : pol - cost);
						if(getAlly(id) != null) {
							heatCounter += this.state.getSettings().getMoveSettings().getActionPressureHeatExtra(); // More if enemy ally
						}
						heatCounter += this.state.getSettings().getMoveSettings().getActionPressureHeat();
						actedMap.put(id, true);
					}
				}
				if (move.hasCoupMove()) {
					ProvinceId id = move.getCoupMove().getProvinceId();
					if(isValidCoupMove(player, id)) {
						int result = inflSign * (getAllianceThreshold(id) + 1);
						coupMap.put(id, result);
						int cov_cost = getCoupMoveCost(id);
						covStoreMap.compute(player,  (p, mil) -> mil == null ? -cov_cost : mil - cov_cost);
						int heatPerStab = this.state.getSettings().getMoveSettings().getActionCoupHeatPerStab()*getNetStability(id);
						heatCounter += this.state.getSettings().getMoveSettings().getActionCoupHeatFixed() + heatPerStab;
						actedMap.put(id, true);
					}
				}
				if (move.hasConflictOvertFundAttackerMove()) {
					ProvinceId id = move.getConflictOvertFundAttackerMove().getProvinceId();
					if(isValidOvertFundAttackerMove(player, id) || true) {
						final int cost = getOvertFundAttackerCost();
						Conflict.Builder c = activeConflictMap.get(id).toBuilder();
						c.setActive(true);
						c.setAttackerSupporter(toProvinceId(player));
						c.setAttChanceMod(100000);
						milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
						// TODO: Heat
						c.build();
						activeConflictMap.put(id, c.build());
						Logger.Dbg(activeConflictMap.get(id) + "");
						actedMap.put(id, true);
					}
				}
				if (move.hasConflictOvertFundDefenderMove()) {
					ProvinceId id = move.getConflictOvertFundDefenderMove().getProvinceId();
					if(isValidOvertFundAttackerMove(player, id) || true) {
						final int cost = getOvertFundDefenderCost();
						Conflict.Builder c = activeConflictMap.get(id).toBuilder();
						c.setActive(true);
						c.setDefenderSupporter(toProvinceId(player));
						c.setDefChanceMod(100000);
						milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
						// TODO: Heat
						activeConflictMap.put(id, c.build());
						Logger.Dbg(activeConflictMap.get(id) + "");
						actedMap.put(id, true);
					}
				}
				// Crises
				if (move.hasUsaBerlinBlockadeAirliftMove()) {
					int cost = 3; // TODO: Real value; starting MIL point cost
					milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
					crisis.setUsaActed1(true);
				}
				if (move.hasUssrBerlinBlockadeLiftBlockadeMove()) {
					int cost = 100; // TODO: Real value
					milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
					crisis.setUssrActed1(true);
				}
			}			
		}
		
		totalInfluenceMap.replaceAll((p, infl) -> infl + polInfluenceMap.getOrDefault(p, 0) + milInfluenceMap.getOrDefault(p, 0) + covInfluenceMap.getOrDefault(p, 0));
		governmentMap.forEach((p, gov) -> {
			if (isStrongGov(gov)) {
				stabilityModifierMap.compute(p, (q, mod) -> mod == null ? 1 : mod + 1 );
			}
		});
//		leaderMap.forEach((p, l) -> {
//			if(hasLeader(p)) {
//				stabilityModifierMap.compute(p, (q, mod) -> mod == null ? 1 : mod + 1 );
//				Leader.Type type = l.getType();
//				Player player = getAlly(p);
//				if(player != null) {
//					int lead_income;
//					switch (type) {
//						case POLITICAL:
//							lead_income = Settings.getConstInt("leader_income_pol");
//							polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
//							break;
//						case MILITARY:
//							lead_income = Settings.getConstInt("leader_income_mil");
//							polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
//							break;
//						case COVERT:
//							lead_income = Settings.getConstInt("leader_income_cov");
//							polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
//							break;
//						default:
//							Logger.Err("Leader " + l.getName() + " has no type!");
//							break;				
//					}
//				}
//				if(type == Leader.Type.ISOLATIONIST) {
//					polInfluenceMap.compute(p, (i, infl) -> infl == null ? -1 * inflSign(player) : infl + -1 * inflSign(player));
//				}
//			}
//		});
		dissidentsMap.forEach((p, dissidents) -> {
			if(hasDissidents(p)) {
				stabilityModifierMap.compute(p, (q, mod) -> mod == null ? -1 : mod - 1 );
				if(getNetStability(p) < 1) {
					Conflict.Builder c;
					if (isInArmedConflict(p))
						c = activeConflictMap.get(p).toBuilder();
					else
						c = Conflict.newBuilder();
					c.setType(Conflict.Type.CIVIL_WAR);
					c.setActive(true);
					c.setRebels(dissidents);
					c.setName(provinceSettings.get(p).getLabel() + " Civil War");
					dissidentsMap.remove(p);
					activeConflictMap.put(p, c.build());
				}
			}
		});
		
		heatCounter = Math.max(heatCounter - 10, this.state.getSettings().getHeatSettings().getMinHeat());
		this.heat = heatCounter;
		
		patriotismCounter = 0; //this.state.getUsa().getPatriotism();
		partyUnityCounter = 0; //this.state.getUssr().getPartyUnity();
		
		if (this.heat > this.state.getSettings().getMoveSettings().getHeatBleedThreshold()) {
			patriotismCounter -= this.state.getSettings().getMoveSettings().getHeatBleed();
			partyUnityCounter -= this.state.getSettings().getMoveSettings().getHeatBleed();
		}
		
		// CRISIS HANDLING
		
		if(crisis.getBerlinBlockade()) {
			Logger.Dbg("Resolving Berlin Blockade");
			boolean airlift = crisis.getUsaActed1();
			boolean blockade = !crisis.getUssrActed1();
			if(airlift) {
				if(blockade) { // Airlift while blockade maintained
					patriotismCounter += 5; // TODO: Settings value
				} else { // Airlift while blockade lifted
					totalInfluenceMap.compute(ProvinceId.EAST_GERMANY, (i, infl) -> infl == null ? -1 : infl - 1);
				}
			} else {
				if(blockade) { // Blockade with no airlift
					patriotismCounter -= 5;
					// Berlin flag unset
				} else { // No blockade and no airlift
					totalInfluenceMap.compute(ProvinceId.EAST_GERMANY, (i, infl) -> infl == null ? -1 : infl - 1);
				}
			}
			Logger.Dbg("Berlin airlift enacted: " + airlift);
			Logger.Dbg("Berlin blockade maintained: " + blockade);
		}
		
		GameState.Builder nextStateBuilder = GameState.newBuilder()
				.setSettings(this.state.getSettings())
				.setTurnLog(TurnLogEntry.newBuilder()
						.setInitialState(GameState.newBuilder()
								.mergeFrom(this.state)
								.clearSettings()
								.build())
						.setUsaMoves(this.usaMoves)
						.setUssrMoves(this.ussrMoves)
						.build())
				.setTurn(this.state.getTurn() + 1)
				.setHeatState(HeatMechanicState.newBuilder()
						.setHeat(heatCounter)
						.build())
				.setProvinceState(ProvinceGameState.newBuilder()
						.addAllProvinceState(this.state.getProvinceState().getProvinceStateList()));
		
		for (final ProvinceState.Builder province : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
			allianceMap.put(province.getId(), getAlly(province.getId()));
			province.setGov(governmentMap.get(province.getId()));
			province.setInfluence(totalInfluenceMap.get(province.getId()));
			province.setConflict(activeConflictMap.get(province.getId()));
			Player baseOwner = baseMap.get(province.getId());
			if(hasDissidents(province.getId()))
					province.setDissidents(dissidentsMap.get(province.getId()));
			if(baseOwner != null)
				province.setBase(toProvinceId(baseOwner));
			int totalStability = getNetStability(province.getId());
			if (Math.abs(province.getInfluence()) > totalStability*2) {
				province.setInfluence(Integer.signum(province.getInfluence()) * totalStability*2);
			}
		}
		
//		nextStateBuilder.getUsaBuilder().getInfluenceStoreBuilder()
//		    .setPolitical(polStoreMap.get(Player.USA) + basePolIncomeMap.get(Player.USA) + polIncomeModifierMap.getOrDefault(Player.USA, 0))
//		    .setMilitary(milStoreMap.get(Player.USA) + baseMilIncomeMap.get(Player.USA) + milIncomeModifierMap.getOrDefault(Player.USA, 0))
//		    .setCovert(covStoreMap.get(Player.USA) + baseCovIncomeMap.get(Player.USA) + covIncomeModifierMap.getOrDefault(Player.USA, 0));
//		nextStateBuilder.getUssrBuilder().getInfluenceStoreBuilder()
//			.setPolitical(polStoreMap.get(Player.USSR) + basePolIncomeMap.get(Player.USSR) + polIncomeModifierMap.getOrDefault(Player.USSR, 0))
//			.setMilitary(milStoreMap.get(Player.USSR) + baseMilIncomeMap.get(Player.USSR) + milIncomeModifierMap.getOrDefault(Player.USSR, 0))
//			.setCovert(covStoreMap.get(Player.USSR) + baseCovIncomeMap.get(Player.USSR) + covIncomeModifierMap.getOrDefault(Player.USSR, 0));
		
//		nextStateBuilder.getUsaBuilder().setPatriotism(patriotismCounter);
//		nextStateBuilder.getUssrBuilder().setPartyUnity(partyUnityCounter);
//				
//		nextStateBuilder.setTechs(state.getTechs());
		
		// Random events.
		Random r = new Random(this.state.getPseudorandomState().getSeed());
		Function<Integer, Boolean> happens = c -> r.nextInt(1000000) < c;
		
		// LEADER EFFECTS
		
		for (ProvinceState p : nextStateBuilder.getProvinceState().getProvinceStateList()) {
			if(p.hasHasLeader()) {
				if(true) {
				//if(p.getLeader().getIsolationist()) {
				//	int inflChange = 1;
				//	if(p.getInfluence() > 0)
				//		p.setInfluence(p.getInfluence()-inflChange);
				//	else if(p.getInfluence() < 0)
				//		p.setInfluence(p.getInfluence()+inflChange);
				} else {
					List<ProvinceId> adj = getAdjacencies(p.getId(), 3);
				    int index = (int)(Math.random()*adj.size());
					switch (p.getGov()) {
						case DEMOCRACY:
							for (ProvinceState b : nextStateBuilder.getProvinceState().getProvinceStateList()) {
								if(adj.get(index) == b.getId()) {
									int inflChange = 1;
									if(p.getAlly() == ProvinceId.USSR)
										inflChange = inflChange * -1;
									//b.setInfluence((b.getInfluence()+inflChange));
								}
							}
							break;
						case AUTOCRACY:
							// Autocratic special effect is passive; it increases cost by 1
							break;
						case COMMUNISM:
							if (happens.apply(200000)) {
								for (ProvinceState b : nextStateBuilder.getProvinceState().getProvinceStateList()) {
									if(adj.get(index) == b.getId()) {
										int inflChange = 1;
										if(p.getAlly() == ProvinceId.USSR)
											inflChange = inflChange * -1;
										//b.setInfluence((b.getInfluence()+inflChange));
									}
								}
							}
							break;
						case REPUBLIC:
							// TODO: Republic
							// Greatly increases effect of another goverment arising
							break;
						default:
							break;
					}
				}
			}
		}
		
		// RANDOM EVENTS
	
		// TODO: LeaderSpawn
		// TODO: LeaderDeath
		// TODO: ProvinceCoup
		// TODO: ProvinceDemocracy
		// TODO: ProvinceCommunism
		// TODO: ProvinceAutocracy
		// ProvinceRepublic
		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
			if (p.getGov() == Government.AUTOCRACY) {
				if (happens.apply(this.state.getSettings().getEventSettings().getRandomProvinceRepublicChance())) {
					p.setGov(Government.REPUBLIC);
					nextStateBuilder.getTurnLogBuilder()
						.addEvents(Event.newBuilder()
							.setProvinceRepublic(ProvinceRepublicEvent.newBuilder()
								.setProvinceId(p.getId())
								.build())
							.build());
				}
			}
		}
		// ProvinceFauxPas
		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
			if (p.getInfluence() != 0) {
				if (happens.apply(this.state.getSettings().getEventSettings().getRandomProvinceFauxPasChance())) {
					int sgn;
				    if (p.getInfluence() < 0) {
				    	sgn = -1;
				    } else {
				    	sgn = 1;
				    }
				    int mag = Math.min(Math.abs(p.getInfluence()), r.nextInt(2) + 1);
				    p.setInfluence(sgn * (Math.abs(p.getInfluence()) - mag));
					p.setGov(Government.REPUBLIC);
					nextStateBuilder.getTurnLogBuilder()
						.addEvents(Event.newBuilder()
							.setProvinceFauxPas(ProvinceFauxPasEvent.newBuilder()
								.setProvinceId(p.getId())
								.setMagnitude(mag)
								.build())
							.build());
				}
			}
		}
		// ProvinceDissidents
		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
			if (!hasDissidents(p.getId())) {
				int chance;
				if (p.getGov() == Government.AUTOCRACY) 
					chance = this.state.getSettings().getEventSettings().getRandomAutocracyDissidentsChance();
				else if (p.getGov() == Government.COMMUNISM)
					chance = this.state.getSettings().getEventSettings().getRandomCommunismDissidentsChance();
				else if (p.getGov() == Government.DEMOCRACY)
					chance = this.state.getSettings().getEventSettings().getRandomDemocracyDissidentsChance();
				else chance = this.state.getSettings().getEventSettings().getRandomProvinceDefaultDissidentsChance();
				if (happens.apply(chance)) {
					Logger.Dbg("Adding random dissidents to " + p.getId());
					Dissidents.Builder d = Dissidents.newBuilder();
					Government gov = p.getGov();
					int result = r.nextInt(1000000);
					if(!p.hasAlly()) {
						switch (gov) {
							case AUTOCRACY:
								if(result < this.state.getSettings().getEventSettings().getNeutralAutocracyDemocraticDissidents())
									d.setGov(Government.DEMOCRACY);
								else if (result - this.state.getSettings().getEventSettings().getNeutralAutocracyDemocraticDissidents()
										< this.state.getSettings().getEventSettings().getNeutralAutocracyCommunistDissidents())
									d.setGov(Government.COMMUNISM);
								else
									d.setGov(Government.REPUBLIC);
								break;
							case REPUBLIC:
								if(result < this.state.getSettings().getEventSettings().getNeutralRepublicDemocraticDissidents())
									d.setGov(Government.DEMOCRACY);
								else if (result - this.state.getSettings().getEventSettings().getNeutralRepublicDemocraticDissidents() < 
										this.state.getSettings().getEventSettings().getNeutralRepublicCommunistDissidents())
									d.setGov(Government.COMMUNISM);
								else
									d.setGov(Government.REPUBLIC);
								break;
							case DEMOCRACY:
								if(happens.apply(this.state.getSettings().getEventSettings().getNeutralDemocracyDemocraticDissidents()))
									d.setGov(Government.DEMOCRACY);
								else if (result - this.state.getSettings().getEventSettings().getNeutralDemocracyDemocraticDissidents() < 
										this.state.getSettings().getEventSettings().getNeutralDemocracyCommunistDissidents())
									d.setGov(Government.COMMUNISM);
								else
									d.setGov(Government.REPUBLIC);
								break;
							case COMMUNISM:
								d.setGov(Government.DEMOCRACY);
							default:
								break;
						}
					} else {
						switch (gov) {
							case AUTOCRACY:
								if(happens.apply(this.state.getSettings().getEventSettings().getInfluencedAutocracyNeutralDissidents()))
									d.setGov(Government.REPUBLIC);
								else if (result - this.state.getSettings().getEventSettings().getInfluencedAutocracyNeutralDissidents() < 
										this.state.getSettings().getEventSettings().getInfluencedAutocracyOpposingDissidents())
									d.setGov(getAlly(p.getId()) == Player.USA ? Government.DEMOCRACY : Government.COMMUNISM);
								else
									d.setGov(getIdealGov(getAlly(p.getId())));
								break;
							case REPUBLIC:
								if(happens.apply(this.state.getSettings().getEventSettings().getInfluencedRepublicNeutralDissidents()))
									d.setGov(Government.REPUBLIC);
								else if (result - this.state.getSettings().getEventSettings().getInfluencedRepublicNeutralDissidents() < 
										this.state.getSettings().getEventSettings().getInfluencedRepublicOpposingDissidents())
									d.setGov(getAlly(p.getId()) == Player.USA ? Government.DEMOCRACY : Government.COMMUNISM);
								else
									d.setGov(getIdealGov(getAlly(p.getId())));
								break;
							case DEMOCRACY:
								if(happens.apply(this.state.getSettings().getEventSettings().getInfluencedDemocracyDemocraticDissidents()))
									d.setGov(Government.REPUBLIC);
								else if (result - this.state.getSettings().getEventSettings().getInfluencedDemocracyDemocraticDissidents() < 
										this.state.getSettings().getEventSettings().getInfluencedDemocracyCommunistDissidents())
									d.setGov(getAlly(p.getId()) == Player.USA ? Government.DEMOCRACY : Government.COMMUNISM);
								else
									d.setGov(getIdealGov(getAlly(p.getId())));
								break;
							case COMMUNISM:
								d.setGov(Government.DEMOCRACY);
							default:
								break;
						}
					}
					// TODO: Dissident leader
					p.setDissidents(d.build());
					nextStateBuilder.getTurnLogBuilder()
						.addEvents(Event.newBuilder()
							.setProvinceDissidents(ProvinceDissidentsEvent.newBuilder()
								.setProvinceId(p.getId())
								.build())
							.build());
				}
			}
		}
		// ProvinceDissidentsSuppressed
		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
			if (hasDissidents(p.getId())) {
				int chance;
				if (p.getGov() == Government.DEMOCRACY) {
					chance = this.state.getSettings().getEventSettings().getRandomProvinceDemocracyDissidentsSuppressedChance();
				} else {
					chance = this.state.getSettings().getEventSettings().getRandomProvinceDefaultDissidentsSuppressedChance();
				}
				if (happens.apply(chance)) {
					Logger.Vrb("Dissidents suppressed in " + p.getId());
					p.setDissidents(Dissidents.getDefaultInstance());
					nextStateBuilder.getTurnLogBuilder()
						.addEvents(Event.newBuilder()
							.setProvinceDissidentsSuppressed(ProvinceDissidentsSuppressedEvent.newBuilder()
								.setProvinceId(p.getId())
								.build())
							.build());
				}
			}
		}
		// TODO: UsAllyDemocracy
		// TODO: UssrAllyCommunism		
		
		// ACTION CONSEQUENCES
		
		// ProvinceCivilWar		
		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
			if (isInArmedConflict(p.getId())) {
				Logger.Vrb("Civil war in " + p.getId());
				Logger.Vrb(p.getConflict().toString());
				p.setInfluence(0);
				coupMap.put(p.getId(), 0);
				p.setBase(ProvinceId.UNKNOWN_PROVINCE);
				p.setDissidents(Dissidents.getDefaultInstance());
				stabilityModifierMap.put(p.getId(), 0);
				Conflict.Builder c = p.getConflict().toBuilder();
				Logger.Vrb("" + c);
				if(c.getLength() != -1) {
					c.setLength(c.getLength()+1);
					int chance = c.getBaseChance();
					boolean attacker, defender;
					attacker = happens.apply(chance);
					defender = happens.apply(chance);
					if (attacker && defender)
						c.setGoal(c.getGoal()+1);
					if (attacker)
						c.setAttackerProgress(c.getAttackerProgress()+1);
					if (defender)
						c.setDefenderProgress(c.getDefenderProgress()+1);
					if(c.getDefenderProgress() >= c.getGoal()) {
						p.setDissidents(Dissidents.getDefaultInstance());
						if(c.getDefenderSupporter() == ProvinceId.USSR)
							p.setInfluence(-getNetStability(p.getId()));
						if(c.getDefenderSupporter() == ProvinceId.USA)
							p.setInfluence(getNetStability(p.getId()));
						c = Conflict.getDefaultInstance().toBuilder();
					} else if(c.getAttackerProgress() >= c.getGoal()) {
						int newInfl = 0;
						p.setGov(c.getRebels().getGov());
//						p.setLeader(c.getRebels().getLeader());
						if(p.getHasLeader())
							newInfl ++;
						if(isStrongGov(p.getGov()))
							newInfl ++;
						if(c.getAttackerSupporter() == ProvinceId.USSR)
							newInfl += -1*getNetStability(p.getId());
						if(c.getAttackerSupporter() == ProvinceId.USA)
							newInfl += getNetStability(p.getId());
						p.setInfluence(newInfl);
						c = Conflict.getDefaultInstance().toBuilder();
					}
				} else
					c.setLength(0);
				p.setConflict(c);
				nextStateBuilder.getTurnLogBuilder()
					.addEvents(Event.newBuilder()
						.setCivilWar(CivilWarEvent.newBuilder()
							.setProvinceId(p.getId())
							.build())
						.build());
			}
		}
		
		// Coup
		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
			if (coups.getOrDefault(p.getId(), 0) != 0) {
				int result = coups.get(p.getId());
				if (happens.apply(this.state.getSettings().getMoveSettings().getCoupSuccessChance())) {
					p.setInfluence(result);
					Logger.Dbg("Coup success in " + p.getId() + "; result = " + result);
				}
				nextStateBuilder.getTurnLogBuilder()
					.addEvents(Event.newBuilder()
						.setCoupEvent(CoupEvent.newBuilder()
							.setProvinceId(p.getId())
							.build())
						.build());
			}
		}
		
		// CRISES
		
		nextStateBuilder.getPseudorandomStateBuilder().setSeed(r.nextLong());
		
		this.patriotism = patriotismCounter;
		this.partyUnity = partyUnityCounter;

		int ussrCentralAmerica = 0, usaCentralAmerica = 0,
			ussrSouthAmerica = 0, usaSouthAmerica = 0;
		
		for (ProvinceSettings p : this.state.getSettings().getProvinceSettings().getProvinceList()) {
			switch (p.getRegion()) {
				case CENTRAL_AMERICA:
					if (getAlly(p.getId()) == Player.USSR)
						ussrCentralAmerica++;
					if (getAlly(p.getId()) == Player.USA)
						usaCentralAmerica++;
					break;
				case SOUTH_AMERICA:
					if (getAlly(p.getId()) == Player.USSR)
						ussrSouthAmerica++;
					if (getAlly(p.getId()) == Player.USA)
						usaSouthAmerica++;
					break;
				default:
					break;
			}
		}
		
		Logger.Dbg("USA currently holds " + usaCentralAmerica + " provinces in Central America");
		Logger.Dbg("USSR currently holds " + ussrCentralAmerica + " provinces in Central America");
		Logger.Dbg("USA currently holds " + usaSouthAmerica + " provinces in South America");
		Logger.Dbg("USSR currently holds " + ussrSouthAmerica + " provinces in South America");
		
		usaRegionControlMap.put(ProvinceRegion.CENTRAL_AMERICA, usaCentralAmerica);
		usaRegionControlMap.put(ProvinceRegion.SOUTH_AMERICA, usaSouthAmerica);
		
		ussrRegionControlMap.put(ProvinceRegion.CENTRAL_AMERICA, ussrCentralAmerica);
		ussrRegionControlMap.put(ProvinceRegion.SOUTH_AMERICA, ussrSouthAmerica);
		
//		nextStateBuilder.getUsaBuilder().setPatriotism(patriotism);
//		nextStateBuilder.getUssrBuilder().setPartyUnity(partyUnity);
		
		if(getPatriotismModifier() <= 0) {} // Lose
		if(getPartyUnityModifier() <= 0) {} // Lose
		
		this.nextState = nextStateBuilder.build();
	}
	
	// VALIDATION
	
	public boolean isValidDiaDipMove(Player player, ProvinceId id){
		return polStore.get(player) >= getDiaDipMoveMin(player, id) + getNonAdjacentDiaMoveCost(player, id) &&
			   !isInArmedConflict(id);
	}
	
	public boolean isValidDiaMilMove (Player player, ProvinceId id){
		return milStore.get(player) >= getDiaMilMoveMin(id) &&
			   polStore.get(player) >= getNonAdjacentDiaMoveCost(player, id) && 
			   getAlly(id) != otherPlayer(player) && 
			   !isInArmedConflict(id);
	}
	
	public boolean isValidDiaCovMove (Player player, ProvinceId id){
		return covStore.get(player) >= getDiaCovMoveMin(id) && 
			   polStore.get(player) >= getNonAdjacentDiaMoveCost(player, id) && 
			   !isInArmedConflict(id);
	}
	
	public boolean isValidFundDissidentsMove(Player player, ProvinceId id) {
		return covStore.get(player) >= getFundDissidentsMoveCost() &&
			   !(hasDissidents(id)) && 
			   !isInArmedConflict(id);
	}
	
	public boolean isValidEstablishBaseMove(Player player, ProvinceId id) {
		return milStore.get(player) >= getEstablishBaseMoveCost() &&
			   bases.get(id) == null &&
			   getAlly(id) == player &&
			   !isInArmedConflict(id);
	}
	
	public boolean isValidPoliticalPressureMove(Player player, ProvinceId id) {
		return polStore.get(player) >= getPoliticalPressureMoveCost() && 
			   !isInArmedConflict(id) &&
			   bases.get(id) != otherPlayer(player);
	}
	
	public boolean isValidCoupMove(Player player, ProvinceId id) {
		return covStore.get(player) >= getCoupMoveCost(id) &&
			   bases.get(id) == null &&
			   !hasInfluence(player, id) &&
			   getNetStability(id) <= this.state.getSettings().getMoveSettings().getActionCoupStabThreshold() &&
			   !isInArmedConflict(id);
	}
	
	public boolean isValidOvertFundAttackerMove(Player player, ProvinceId id) {
		if(isInArmedConflict(id)) {
			Conflict c = activeConflicts.get(id);
			return milStore.get(player) >= getOvertFundAttackerCost() &&
				   c.getRebels().getGov() != getIdealGov(otherPlayer(player)) &&
				   c.getAttackerSupporter() != ProvinceId.USSR &&
				   c.getAttackerSupporter() != ProvinceId.USA;
		}
		return false;
			   
	}
	
	public boolean isValidOvertFundDefenderMove(Player player, ProvinceId id) {
		if(isInArmedConflict(id)) {
			Conflict c = activeConflicts.get(id);
			return milStore.get(player) >= getOvertFundDefenderCost() &&
				   governments.get(id) != getIdealGov(otherPlayer(player)) &&
				   c.getDefenderSupporter() != ProvinceId.USSR &&
				   c.getDefenderSupporter() != ProvinceId.USA;
		}
		return false;
	}
	
	public boolean isValidCovertFundAttackerMove(Player player, ProvinceId id) {
		if(isInArmedConflict(id)) {
			Conflict c = activeConflicts.get(id);
			return covStore.get(player) >= getCovertFundAttackerCost() &&
				   c.getRebels().getGov() != getIdealGov(otherPlayer(player)) &&
				   c.getDefenderSupporter() != ProvinceId.USSR &&
				   c.getDefenderSupporter() != ProvinceId.USA;
		}
		return false;
	}
	
	public boolean isValidCovertFundDefenderMove(Player player, ProvinceId id) {
		if(isInArmedConflict(id)) {
			Conflict c = activeConflicts.get(id);
			return covStore.get(player) >= getCovertFundDefenderCost() &&
				   governments.get(id) != getIdealGov(otherPlayer(player)) &&
				   c.getDefenderSupporter() != ProvinceId.USSR &&
				   c.getDefenderSupporter() != ProvinceId.USA;
		}
		return false;
	}
	
//	public boolean isTechAvailable(Player player, Tech.Id id) {
//		if (!isTechCompleted(player, id)) {
//			for (Tech.Id prereq : getTechSettings(id).getPrereqsList())
//				if(!isTechCompleted(player, prereq)) return false;
//			return true;
//		}
//		return false;
//	}
	
	// Crises
	
	public boolean isBerlinBlockadeActive() {
		if(state.getCrises().getBerlinBlockade()) {
			return true;
		}
		return false;
	}
	
	// COST AND COST RANGES

	public int getNonAdjacentDiaMoveCost(Player player, ProvinceId id) {
		if((player == Player.USA && !usaAdjacencies.get(id)) ||
		   (player == Player.USSR && !ussrAdjacencies.get(id)) )
			return this.state.getSettings().getMoveSettings().getNonAdjacentCost();
		return 0;
	}
	
	public int getDiaDipMoveMin(Player player, ProvinceId id) {
		int ret = 1;
		if(getDiaDipMoveIncrement(player, id) == 2) ret = 2;
		if(governments.get(id) == Government.AUTOCRACY) ret = ret + this.state.getSettings().getMoveSettings().getActionInfluenceAutocracyMod();;
		return ret;
	}
	
	public int getDiaDipMoveMax(Player player, ProvinceId id) {
		int ret = Math.min(polStore.get(player), 2*getNetStability(id));
		return ret;
	}
	
	public int getDiaDipMoveIncrement(Player player, ProvinceId id) {
		if(getAlly(id) == otherPlayer(player)) return 2;
		return 1;
	}
	
	public int getDiaMilMoveMin(ProvinceId id) {
		int ret = 1;
		if(governments.get(id) == Government.AUTOCRACY) ret = ret + this.state.getSettings().getMoveSettings().getActionInfluenceAutocracyMod();
		return 1;
	}
	
	public int getDiaMilMoveMax(Player player, ProvinceId id) {
		int ret = Math.min(milStore.get(player), 2*getNetStability(id));
		return ret;
	}
	
	public int getDiaMilMoveIncrement() {
		return 1;
	}
	
	public int getDiaCovMoveMin(ProvinceId id) {
		int ret = 1;
		if(governments.get(id) == Government.AUTOCRACY) ret = ret + this.state.getSettings().getMoveSettings().getActionInfluenceAutocracyMod();
		return 1;
	}
	
	public int getDiaCovMoveMax(Player player, ProvinceId id) {
		int ret = Math.min(covStore.get(player), 2*getNetStability(id));
		return ret;
	}
	
	public int getDiaCovMoveIncrement() {
		return 1;
	}
	
	public int getFundDissidentsMoveCost() {
		return this.state.getSettings().getMoveSettings().getActionDissidentsCost();
	}
	
	public int getEstablishBaseMoveCost() {
		return this.state.getSettings().getMoveSettings().getActionBaseCost();
	}
	
	public int getPoliticalPressureMoveCost() {
		return this.state.getSettings().getMoveSettings().getActionPressureCost();
	}
	
	public int getCoupMoveCost(ProvinceId id) {
		return (this.state.getSettings().getMoveSettings().getActionCoupCostPerStab() * getNetStability(id)) + 1;
	}
	
	public int getOvertFundAttackerCost() {
		return this.state.getSettings().getMoveSettings().getConflictOvertFundAttacker();
	}
	
	public int getOvertFundDefenderCost() {
		return this.state.getSettings().getMoveSettings().getConflictOvertFundDefender();
	}
	
	public int getCovertFundAttackerCost() {
		return this.state.getSettings().getMoveSettings().getConflictCovertFundAttacker();
	}
	
	public int getCovertFundDefenderCost() {
		return this.state.getSettings().getMoveSettings().getConflictCovertFundDefender();
	}
	
	// OTHER HELPER FUNCTIONS
	
	public boolean hasActed(ProvinceId id) {
		return acted.get(id);
	}
	
	public Player getAlly(ProvinceId province) {
		if(totalInfluence.get(province) > getAllianceThreshold(province) &&
				governments.get(province) != Government.COMMUNISM) {
			return Player.USA;
		} else if(totalInfluence.get(province) < -getAllianceThreshold(province) &&
				governments.get(province) != Government.DEMOCRACY) {
			return Player.USSR;
		}
		return null;
	}
	
	public int getNetStability(ProvinceId province) {
		return stabilityBase.get(province) + stabilityModifier.getOrDefault(province, 0);
	}
	
	public Player otherPlayer(Player player) {
		if(player == Player.USSR) {
			return Player.USA;
		} else {
			return Player.USSR;
		}
	}
	
	public int getAllianceThreshold(ProvinceId province) {
		if(getNetStability(province) > 0) return getNetStability(province)-1;
		return 0;
	}
	
	public static Player toPlayer(ProvinceId id) {
		if (id == ProvinceId.USA) return Player.USA;
		if (id == ProvinceId.USSR) return Player.USSR;
		return null;
	}
	
	public static ProvinceId toProvinceId(Player player) {
		if (player == Player.USA) return ProvinceId.USA;
		if (player == Player.USSR) return ProvinceId.USSR;
		return null;
	}
	
	public boolean hasDissidents(final ProvinceId id) {
		if( dissidents.get(id) != Dissidents.getDefaultInstance() && dissidents.get(id) != null )
			return true;
		return false;
	}
	
//	public boolean hasLeader(final ProvinceId id) {
//		if( leaders.get(id) != Leader.getDefaultInstance() && leaders.get(id) != null )
//			return true;
//		return false;
//	}
	
	public Government getDissidentsGov(final ProvinceId id) {
		return dissidents.get(id).getGov();
	}
	
	public Conflict getConflict(final ProvinceId id) {
		return activeConflicts.get(id);
	}
	
	public Government getIdealGov(Player player) {
		if(player == Player.USSR) {
			return Government.COMMUNISM;
		} else {
			return Government.DEMOCRACY;
		} 
	}
	
	protected boolean hasAdjacencyInfluence(Player player, ProvinceId id) {
		if(hasInfluence(player, id)) return true;
		for (ProvinceId adj : provinceSettings.get(id).getAdjacencyList()) {
			if(hasInfluence(player, adj)) return true;
		}
		return false;
	}
	
	public boolean isInRange(Player player, ProvinceId id) {
		if (player == Player.USA)
			return usaAdjacencies.get(id);
		else //if (player == Player.USSR)
			return ussrAdjacencies.get(id);
	}
	
	public List<ProvinceId> getAdjacencies(ProvinceId id, int range) {
		List<ProvinceId> ret = new ArrayList<ProvinceId>();
		if (range == 0) return null;
		else {
			for (ProvinceId p : provinceSettings.get(id).getAdjacencyList()) {
				ret.add(p);
				if(range - 1 > 0) {
					ret.addAll(getAdjacencies(id, range-1));
				}
			}
			return ret;
		}
	} 
	
	public boolean hasInfluence(Player player, ProvinceId id) {
		try {
			if((inflSign(player) * totalInfluence.get(id) > 0 ||
					bases.get(id) == player ||
					governments.get(id) == getIdealGov(player))) return true;
			return false;
		} catch (Exception e) {
			Logger.Dbg("Error getting influence for - " + id);
			throw e;
		} finally { }
		//return false;
	}
	
	public boolean isInArmedConflict(ProvinceId id) {
		if(activeConflicts.get(id) != null &&
		   activeConflicts.get(id) != Conflict.getDefaultInstance() &&
		   activeConflicts.get(id).getActive()) {
			return true;
		}
		return false;
	}
	
	public boolean isStrongGov(Government gov) {
		if(gov == Government.AUTOCRACY ||
		   gov == Government.COMMUNISM ||
		   gov == Government.DEMOCRACY)
			return true;
		return false;
	}
	
	public int getPatriotismModifier() {
		int sum = 0;
		for(ProvinceRegion r : ProvinceRegion.values()) {
			int usaControl = usaRegionControl.getOrDefault(r, 0); 
			int ussrControl = ussrRegionControl.getOrDefault(r, 0); 
			if(usaControl > regionTotal.getOrDefault(r, 0)/2) { // TODO: Wrong getOrDefault
				sum++;
				if(usaControl > (int)regionTotal.getOrDefault(r, 0)*0.75)
					sum++;
			}
			if(usaControl == 0 && ussrControl > 0)
				sum--;
			if(ussrControl*3 > usaControl) {
				sum--;
			}
		}
		sum = sum*this.state.getSettings().getMoveSettings().getVpRegionModifier();
		return sum;
	}
	
	public int getPartyUnityModifier() {
		int sum = 0;
		for(ProvinceRegion r : ProvinceRegion.values()) {
			int usaControl = usaRegionControl.getOrDefault(r, 0); 
			int ussrControl = ussrRegionControl.getOrDefault(r, 0); 
			if(ussrControl > regionTotal.getOrDefault(r, 0)/2) { // TODO: Wrong getOrDefault
				sum++;
				if(ussrControl > (int)regionTotal.get(r)*0.75)
					sum++;
			}
			if(ussrControl == 0 && usaControl > 0)
				sum--;
			if(usaControl*3 > ussrControl) {
				sum--;
			}
		}
		sum = sum*this.state.getSettings().getMoveSettings().getVpRegionModifier();
		return sum;
	}
	
//	public int getNetPatriotism() {
//		return state.getUsa().getPatriotism() + getPatriotismModifier();
//	}
//	
//	public int getNetPartyUnity() {
//		return state.getUssr().getPartyUnity() + getPartyUnityModifier();
//	}
	
	protected int inflSign(Player player) {
		return player == Player.USA ? 1 : -1;
	}
	
	public String getCrisisInfo() {
		return state.getCrises().getInfo();
	}
	
	public String getCrisisUsaOption1() {
		return state.getCrises().getUsaOption1();
	}
	
	public String getCrisisUssrOption1() {
		return state.getCrises().getUssrOption1();
	}
	
	/*
	 * TODO: Fix
	public TechGroupSettings getTechGroupSettings(TechGroup.Id id) {
		for (TechGroupSettings t : state.getSettings().getTechGroupsList())
			if (t.getId() == id) return t;
		return null;
	}

	public TechGroup getTechGroup(Player player, TechGroup.Id id) {
		if(player == Player.USA) {
			for (TechGroup t : state.getTech().getUsaList())
				if (t.getId() == id) return t;
		} else {

			for (TechGroup t : state.getTech().getUssrList())
				if (t.getId() == id) return t;
		}
		return null;
	}
	
	public TechSettings getTechSettings(Tech.Id id) {
		for (TechGroupSettings g : state.getSettings().getTechGroupsList()) {
			for (TechSettings t : g.getTechsList())
				if (t.getId() == id) return t;
		}
		return null;
	}
	
	public Tech getTech(Player player, Tech.Id id) {
		if(player == Player.USA) {
			for (TechGroup g : state.getTech().getUsaList()) {
				for (Tech t : g.getTechsList())
					if (t.getId() == id) return t;
			}
		} else {
			for (TechGroup g : state.getTech().getUssrList()) {
				for (Tech t : g.getTechsList())
					if (t.getId() == id) return t;
			}
		}
		return Tech.getDefaultInstance();
	}
	
	public boolean isTechCompleted(Player player, Tech.Id id) {
		Tech t = getTech(player, id);
		TechSettings ts = getTechSettings(id);
		if (t.getProgress() >= ts.getNumProgressions()) return true;
		return false;
	}
	*/
	
	/*
	 * Retrieve a list of events that happened since the previous turn, from player's perspective.
	 */
	static List<String> getEventMessages(GameState state, Player player) {
		ArrayList<String> messages = new ArrayList<String>();
		List<Event> events  = state.getTurnLog().getEventsList();
		MoveList own;
		MoveList other;
		Player otherPlayer;
		if (player == Player.USA) {
			own = state.getTurnLog().getUsaMoves();
			other = state.getTurnLog().getUssrMoves();
			otherPlayer = Player.USSR;
		} else {
			own = state.getTurnLog().getUssrMoves();
			other = state.getTurnLog().getUsaMoves();
			otherPlayer = Player.USA;
		}
		for (Move move : own.getMovesList()) {
			messages.addAll(getMoveMessages(move, player, true));
 		}
		for (Move move : other.getMovesList()) {
			messages.addAll(getMoveMessages(move, otherPlayer, false));
 		}
		for (Event event : events) {
			messages.addAll(getEventMessages(event));			
		}
		return messages;
	}
	
	static private List<String> getMoveMessages(Move move, Player player, boolean hasCovertVisibility) {
		ArrayList<String> messages = new ArrayList<String>();
		if (move.hasDiaDipMove()) {
			messages.add(String.format("%s directly influenced the government of %s.", player, move.getDiaDipMove().getProvinceId()));
		}
		if (move.hasDiaMilMove()) {				
			messages.add(String.format("%s directly influenced the military of %s.", player, move.getDiaMilMove().getProvinceId()));
		}
		if (move.hasDiaCovMove()) {
			if (hasCovertVisibility) {
				messages.add(String.format("%s covertly influenced the leaders of %s.", player, move.getDiaCovMove().getProvinceId()));								
			} else {
				messages.add(String.format("The leaders of %s have been influenced.", player, move.getDiaCovMove().getProvinceId()));												
			}
		}
		if (move.hasCoupMove()) {
			if (hasCovertVisibility) {
				messages.add(String.format("%s instigated a coup in %s.", player, move.getCoupMove().getProvinceId()));		
			} else {
				messages.add(String.format("The members of government in %s have been gruesomely murdered and replaced!", move.getCoupMove().getProvinceId()));												
			}
		}
		if (move.hasConflictOvertFundAttackerMove()) {
			messages.add(String.format("%s funded attackers in %s.", player, move.getConflictOvertFundAttackerMove().getProvinceId()));								
		}
		if (move.hasConflictOvertFundDefenderMove()) {
			messages.add(String.format("%s funded defenders in %s.", player, move.getConflictOvertFundDefenderMove().getProvinceId()));												
		}
		if (move.hasEstablishBaseMove()) {
			messages.add(String.format("%s established a base in %s.", player, move.getEstablishBaseMove().getProvinceId()));								
		}
		if (move.hasFundDissidentsMove()) {
			if (hasCovertVisibility) {
				messages.add(String.format("%s funded dissidents in %s.", player, move.getFundDissidentsMove().getProvinceId()));				
			} else {
				messages.add(String.format("The local rebels in %s are bored and acting up again.", move.getFundDissidentsMove().getProvinceId()));												
			}
		}
		if (move.hasPoliticalPressureMove()) {
			messages.add(String.format("%s pressured the government in %s.", player, move.getPoliticalPressureMove().getProvinceId()));												
		}
		return messages;
	}
	
	static private List<String> getEventMessages(Event event) {
		ArrayList<String> messages = new ArrayList<String>();
		if (event.hasCivilWar()) {
			messages.add(String.format("A civil war has started in %s!", event.getCivilWar().getProvinceId()));
		}
		if (event.hasEndCivilWar()) {
			messages.add(String.format("The civil war in %s is over!", event.getEndCivilWar().getProvinceId()));
		}
		if (event.hasLeaderDeath()) {
			messages.add(String.format("The glorious leader %s is dedified!", "%s"));
		}
		if (event.hasLeaderSpawn()) {
			messages.add(String.format("The glorious leader %s is enlivened!", "%s"));
		}
		if (event.hasProvinceAutocracy()) {
			messages.add(String.format("Oh noes! %s is now lead by an autocracy!", event.getProvinceAutocracy().getProvinceId()));
		}
		if (event.hasProvinceCommunism()) {
			messages.add(String.format("Comrades rejoice, for %s is now a communist state.", event.getProvinceCommunism().getProvinceId()));
		}
		if (event.hasProvinceCoup()) {
			messages.add(String.format("The members of government in %s have been gruesomely murdered and replaced!", event.getProvinceCoup().getProvinceId()));
		}
		if (event.hasProvinceDemocracy()) {			
			messages.add(String.format("Citizens rejoice, for %s is now a democratic state.", event.getProvinceDemocracy().getProvinceId()));
		}
		if (event.hasProvinceDissidents()) {			
			messages.add(String.format("The local rebels in %s are bored and acting up again.", event.getProvinceDissidents().getProvinceId()));												
		}
		if (event.hasProvinceDissidentsSuppressed()) {
			messages.add(String.format("The dissidents in %s have been put down.", event.getProvinceDissidentsSuppressed().getProvinceId()));
		}
		if (event.hasProvinceFauxPas()) {
			messages.add(String.format("Oopsies! It turns out that ordering pizza drunk and half naked outside the capitol of %s is frowned on.", event.getProvinceFauxPas().getProvinceId()));
		}
		if (event.hasProvinceRepublic()) {
			messages.add(String.format("Republic? What is that? Oh, the new government of %s", event.getProvinceRepublic().getProvinceId()));
		}
		if (event.hasUsAllyDemocracy()) {
			messages.add(String.format("The USA is pushing the democracy thing, as usual. %s is the next to fall.", event.getUsAllyDemocracy().getProvinceId()));
		}
		if (event.hasUssrAllyCommunism()) {
			messages.add(String.format("The motherland has a new child, %s.", event.getUssrAllyCommunism().getProvinceId()));
		}
		return messages;
	}
}
