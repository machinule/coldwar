package com.berserkbentobox.coldwar.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.Province.ProvinceSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.TurnLogEntry;
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
import com.berserkbentobox.coldwar.Conflict.ConflictMechanicState;
import com.berserkbentobox.coldwar.Conflict.ConflictState;
import com.berserkbentobox.coldwar.Conflict.ConflictType;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicState;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Leader.LeaderSettings;
import com.berserkbentobox.coldwar.Leader.LeaderState;
import com.berserkbentobox.coldwar.Id.ProvinceRegion;
import com.berserkbentobox.coldwar.Province.ProvinceState;
import com.berserkbentobox.coldwar.MoveOuterClass.Move;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.mechanics.heat.HeatMechanic;

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
	public final Map<ProvinceId, ProvinceId> occupiers;
	
	public final Map<ProvinceId, LeaderState> leaders;
	
	public final Map<ProvinceId, ConflictState> activeConflicts;
	public final Map<ProvinceId, ConflictState> dormantConflicts;
	public final Map<ProvinceId, ConflictState> possibleConflicts;
	
	public final Map<ProvinceId, Integer> stabilityBase;
	public final Map<ProvinceId, Integer> stabilityModifier;
	
	public final Map<ProvinceId, ProvinceSettings> provinceSettings;
	
	public final Map<ProvinceId, Integer> coups;
	
	public final Map<ProvinceId, Boolean> acted;
	
	public final GameState nextState;
		
	public final MechanicSettings settings;
	
	public Mechanics mechanics;
	
	public ComputedGameState(final GameState state, final MoveList usaMoves, final MoveList ussrMoves, final MechanicSettings settings, Mechanics mechanics) {
		this.state = state;
		this.usaMoves = usaMoves;
		this.ussrMoves = ussrMoves;
		this.settings = settings;
		this.mechanics = mechanics;

		Random r = new Random(this.state.getPseudorandomState().getSeed());
			
		this.year = state.getTurn() + 1948;
		HeatMechanic heat = mechanics.getHeat();
		int partyUnityCounter = state.getSuperpowerState().getUssrState().getPartyUnity();
		int patriotismCounter = state.getSuperpowerState().getUsaState().getPatriotism();
		
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
		EnumMap<ProvinceId, ProvinceId> occupierMap = new EnumMap<ProvinceId, ProvinceId>(ProvinceId.class);
		this.occupiers = Collections.unmodifiableMap(occupierMap);
		
		EnumMap<ProvinceId, ConflictState> activeConflictMap = new EnumMap<ProvinceId, ConflictState>(ProvinceId.class);
		this.activeConflicts = Collections.unmodifiableMap(activeConflictMap);		
		EnumMap<ProvinceId, ConflictState> dormantConflictMap = new EnumMap<ProvinceId, ConflictState>(ProvinceId.class);
		this.dormantConflicts = Collections.unmodifiableMap(dormantConflictMap);		
		EnumMap<ProvinceId, ConflictState> possibleConflictMap = new EnumMap<ProvinceId, ConflictState>(ProvinceId.class);
		this.possibleConflicts = Collections.unmodifiableMap(possibleConflictMap);
		
		EnumMap<ProvinceId, LeaderState> leaderMap = new EnumMap<ProvinceId, LeaderState>(ProvinceId.class);
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
		
		this.state.getLeadersState().getLeaderList().forEach(l -> {
			leaderMap.put(l.getProvince(), l);
		});
		
		this.state.getConflictState().getActiveList().forEach(c -> {
			activeConflictMap.put(c.getLocation(), c);
		});
		this.state.getConflictState().getDormantList().forEach(c -> {
			dormantConflictMap.put(c.getLocation(), c);
		});
		this.state.getConflictState().getPossibleList().forEach(c -> {
			possibleConflictMap.put(c.getLocation(), c);
		});
		
		this.state.getProvinceState().getProvinceStateList().forEach(p -> {
			baseInfluenceMap.put(p.getId(), p.getInfluence());
			baseMap.put(p.getId(), toPlayer(p.getBase()));
			governmentMap.put(p.getId(), p.getGov());
			occupierMap.put(p.getId(), p.getOccupier());
			actedMap.put(p.getId(), false);
			dissidentsMap.put(p.getId(), p.getDissidents());
		});
		
		totalInfluenceMap.putAll(baseInfluenceMap);
		
		this.state.getProvinceState().getProvinceStateList().forEach(p -> {
			usaAdjacencyMap.put(p.getId(), hasAdjacencyInfluence(Player.USA, p.getId()));
			ussrAdjacencyMap.put(p.getId(), hasAdjacencyInfluence(Player.USSR, p.getId()));
		});
		
		polStoreMap.put(Player.USA, state.getInfluenceState().getUsaState().getPolitical());
		milStoreMap.put(Player.USA, state.getInfluenceState().getUsaState().getMilitary());
		covStoreMap.put(Player.USA, state.getInfluenceState().getUsaState().getCovert());
		
		polStoreMap.put(Player.USSR, state.getInfluenceState().getUssrState().getPolitical());
		milStoreMap.put(Player.USSR, state.getInfluenceState().getUssrState().getMilitary());
		covStoreMap.put(Player.USSR, state.getInfluenceState().getUssrState().getCovert());
				
		basePolIncomeMap.put(Player.USA, state.getSettings().getInfluenceSettings().getUsaSettings().getPoliticalIncomeBase());
		baseMilIncomeMap.put(Player.USA, state.getSettings().getInfluenceSettings().getUsaSettings().getMilitaryIncomeBase());
		baseCovIncomeMap.put(Player.USA, state.getSettings().getInfluenceSettings().getUsaSettings().getCovertIncomeBase());

		basePolIncomeMap.put(Player.USSR, state.getSettings().getInfluenceSettings().getUssrSettings().getPoliticalIncomeBase());
		baseMilIncomeMap.put(Player.USSR, state.getSettings().getInfluenceSettings().getUssrSettings().getMilitaryIncomeBase());
		baseCovIncomeMap.put(Player.USSR, state.getSettings().getInfluenceSettings().getUssrSettings().getCovertIncomeBase());

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
//				if (move.hasEstablishBaseMove()) {
//					ProvinceId id = move.getEstablishBaseMove().getProvinceId();
//					if(isValidEstablishBaseMove(player, id)) {
//						final int cost = getEstablishBaseMoveCost();
//						baseMap.put(id, player);
//						milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
//						heat.increase(this.state.getSettings().getMoveSettings().getActionDissidentsHeat());
//						actedMap.put(id, true);
//					}
////				}
//				if (move.hasPoliticalPressureMove()) {
//					ProvinceId id = move.getPoliticalPressureMove().getProvinceId();
//					if(isValidPoliticalPressureMove(player, id)) {
//						final int cost = getPoliticalPressureMoveCost();
//						int netInfl = 0;
//						for (ProvinceId adj : provinceSettingsMap.get(id).getAdjacencyList()) {
//							Logger.Dbg("Seeing pressure from: " + adj);
//							if(getAlly(adj) == otherPlayer(player)) {
//								netInfl -= 1;
//								Logger.Dbg("Neighboring enemy ally -> -1");
//							} else if(getAlly(adj) == player) {
//								netInfl += 1;
//								Logger.Dbg("Neighboring friendly ally -> +1");
//							}
//							if(governmentMap.get(adj) == getIdealGov(player)) {
//								netInfl += 1;
//								Logger.Dbg("Neighboring friendly government -> +1");
//							} else if(governmentMap.get(adj) == getIdealGov(otherPlayer(player))) {
//								netInfl -= 1;
//								Logger.Dbg("Neighboring enemy government -> -1");
//							} 
//						}
//						Logger.Dbg("Net influence: " + netInfl);
//						final int finInfl = netInfl * inflSign;
//						polInfluenceMap.compute(id, (i, infl) -> infl == null ? finInfl : infl + finInfl);
//						polStoreMap.compute(player,  (p, pol) -> pol == null ? -cost : pol - cost);
//						if(getAlly(id) != null) {
//							heat.increase(this.state.getSettings().getMoveSettings().getActionPressureHeatExtra()); // More if enemy ally
//						}
//						heat.increase(this.state.getSettings().getMoveSettings().getActionPressureHeat());
//						actedMap.put(id, true);
//					}
//				}
				if (move.hasCoupMove()) {
					ProvinceId id = move.getCoupMove().getProvinceId();
					if(isValidCoupMove(player, id)) {
						int result = inflSign * (getAllianceThreshold(id) + 1);
						coupMap.put(id, result);
						int cov_cost = getCoupMoveCost(id);
						covStoreMap.compute(player,  (p, mil) -> mil == null ? -cov_cost : mil - cov_cost);
						int heatPerStab = this.state.getSettings().getMoveSettings().getActionCoupHeatPerStab()*getNetStability(id);
						heat.increase(this.state.getSettings().getMoveSettings().getActionCoupHeatFixed() + heatPerStab);
						actedMap.put(id, true);
					}
				}
				if (move.hasConflictOvertFundAttackerMove()) {
					ProvinceId id = move.getConflictOvertFundAttackerMove().getProvinceId();
					if(isValidOvertFundAttackerMove(player, id) || true) {
						final int cost = getOvertFundAttackerCost();
						ConflictState.Builder c = activeConflictMap.get(id).toBuilder();
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
						ConflictState.Builder c = activeConflictMap.get(id).toBuilder();
						c.setDefenderSupporter(toProvinceId(player));
						c.setDefChanceMod(100000);
						milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
						// TODO: Heat
						activeConflictMap.put(id, c.build());
						Logger.Dbg(activeConflictMap.get(id) + "");
						actedMap.put(id, true);
					}
				}
			}			
		}
		
		totalInfluenceMap.replaceAll((p, infl) -> infl + polInfluenceMap.getOrDefault(p, 0) + milInfluenceMap.getOrDefault(p, 0) + covInfluenceMap.getOrDefault(p, 0));
		governmentMap.forEach((p, gov) -> {
			if (isStrongGov(gov)) {
				stabilityModifierMap.compute(p, (q, mod) -> mod == null ? 1 : mod + 1 );
			}
		});
		leaderMap.forEach((p, l) -> {
		if(hasLeader(p)) {
			stabilityModifierMap.compute(p, (q, mod) -> mod == null ? 1 : mod + 1 );
//			Leader.Type type = l.getType();
//			Player player = getAlly(p);
//			if(player != null) {
//				int lead_income;
//				switch (type) {
//					case POLITICAL:
//						lead_income = Settings.getConstInt("leader_income_pol");
//						polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
//						break;
//					case MILITARY:
//						lead_income = Settings.getConstInt("leader_income_mil");
//						polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
//						break;
//					case COVERT:
//						lead_income = Settings.getConstInt("leader_income_cov");
//						polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
//						break;
//					default:
//						Logger.Err("Leader " + l.getName() + " has no type!");
//						break;				
//				}
//			}
//			if(type == Leader.Type.ISOLATIONIST) {
//				polInfluenceMap.compute(p, (i, infl) -> infl == null ? -1 * inflSign(player) : infl + -1 * inflSign(player));
//			}
		}
	});
		dissidentsMap.forEach((p, dissidents) -> {
			if(hasDissidents(p)) {
				stabilityModifierMap.compute(p, (q, mod) -> mod == null ? -1 : mod - 1 );
				if(getNetStability(p) < 1) {
					ConflictState.Builder c;
					if (isInArmedConflict(p))
						c = activeConflictMap.get(p).toBuilder();
					else
						c = ConflictState.newBuilder();
					if(governments.get(p) != Government.COLONY)
						c.setType(ConflictType.CIVIL_WAR);
					else {
						c.setType(ConflictType.COLONIAL_WAR);
						c.setDefender(occupiers.get(p));
					}
					c.setRebels(dissidents);
					c.setName(provinceSettings.get(p).getLabel() + " Civil War");
					dissidentsMap.remove(p);
					activeConflictMap.put(p, c.build());
				}
			}
		});
		
		this.heat = heat.getHeat();
		
		patriotismCounter = this.state.getSuperpowerState().getUsaState().getPatriotism();
		partyUnityCounter = this.state.getSuperpowerState().getUssrState().getPartyUnity();
		
		if (this.heat > this.state.getSettings().getMoveSettings().getHeatBleedThreshold()) {
			patriotismCounter -= this.state.getSettings().getMoveSettings().getHeatBleed();
			partyUnityCounter -= this.state.getSettings().getMoveSettings().getHeatBleed();
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
				.setProvinceState(ProvinceMechanicState.newBuilder()
						.addAllProvinceState(this.state.getProvinceState().getProvinceStateList()))
				.setConflictState(ConflictMechanicState.newBuilder()
						.addAllActive(this.state.getConflictState().getActiveList())
						.addAllDormant(this.state.getConflictState().getDormantList())
						.addAllPossible(this.state.getConflictState().getPossibleList()));
		
		for (final ProvinceState.Builder province : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
//			allianceMap.put(province.getId(), getAlly(province.getId()));
			province.setGov(governmentMap.get(province.getId()));
			province.setOccupier(occupierMap.get(province.getId()));
			province.setInfluence(totalInfluenceMap.get(province.getId()));
			Player baseOwner = baseMap.get(province.getId());
			if(baseOwner != null)
				province.setBase(toProvinceId(baseOwner));
			int totalStability = getNetStability(province.getId());
			if (Math.abs(province.getInfluence()) > totalStability*2) {
				province.setInfluence(Integer.signum(province.getInfluence()) * totalStability*2);
			}
		}
		
		nextStateBuilder.getInfluenceStateBuilder().getUsaStateBuilder()
		    .setPolitical(polStoreMap.get(Player.USA) + basePolIncomeMap.get(Player.USA) + polIncomeModifierMap.getOrDefault(Player.USA, 0))
		    .setMilitary(milStoreMap.get(Player.USA) + baseMilIncomeMap.get(Player.USA) + milIncomeModifierMap.getOrDefault(Player.USA, 0))
		    .setCovert(covStoreMap.get(Player.USA) + baseCovIncomeMap.get(Player.USA) + covIncomeModifierMap.getOrDefault(Player.USA, 0));
		nextStateBuilder.getInfluenceStateBuilder().getUssrStateBuilder()
			.setPolitical(polStoreMap.get(Player.USSR) + basePolIncomeMap.get(Player.USSR) + polIncomeModifierMap.getOrDefault(Player.USSR, 0))
			.setMilitary(milStoreMap.get(Player.USSR) + baseMilIncomeMap.get(Player.USSR) + milIncomeModifierMap.getOrDefault(Player.USSR, 0))
			.setCovert(covStoreMap.get(Player.USSR) + baseCovIncomeMap.get(Player.USSR) + covIncomeModifierMap.getOrDefault(Player.USSR, 0));

		nextStateBuilder.getSuperpowerStateBuilder().getUsaStateBuilder().setPatriotism(patriotismCounter);
		nextStateBuilder.getSuperpowerStateBuilder().getUssrStateBuilder().setPartyUnity(partyUnityCounter);
		
//		nextStateBuilder.setTechs(state.getTechs());
		
		// Random events.
		Function<Integer, Boolean> happens = c -> r.nextInt(1000000) < c;

		// LEADER EFFECTS
		
		for (ProvinceState p : nextStateBuilder.getProvinceState().getProvinceStateList()) {
			if(hasLeader(p.getId())) {
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
		
		// ACTION CONSEQUENCES
		
		// Conflict Resolution
		int conflictIndex = 0;
		List<Integer> removeConflicts = new ArrayList<Integer>();
		for (ConflictState.Builder c : nextStateBuilder.getConflictStateBuilder().getActiveBuilderList()) {
			for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
				if(p.getId() == c.getLocation())
				{
					if (isInArmedConflict(c.getLocation())) {
						Logger.Vrb("Conflict in " + c.getLocation());
						p.setInfluence(0);
						coupMap.put(c.getLocation(), 0);
						p.setBase(ProvinceId.NONE);
						p.setDissidents(Dissidents.getDefaultInstance());
						stabilityModifierMap.put(c.getLocation(), 0);
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
								// Defenders win
								// TODO: Conventional Wars
								if(c.getType() == ConflictType.CIVIL_WAR || c.getType() == ConflictType.COLONIAL_WAR)
									p.setDissidents(Dissidents.getDefaultInstance());
								if(c.getType() == ConflictType.CIVIL_WAR) {
									if(c.getDefenderSupporter() == ProvinceId.USSR);
										p.setInfluence(-getNetStability(c.getLocation()));
									if(c.getDefenderSupporter() == ProvinceId.USA)
										p.setInfluence(getNetStability(c.getLocation()));
								}
								else if (c.getType() == ConflictType.COLONIAL_WAR) {
									for (ProvinceState.Builder o : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
										if(c.getDefender() == o.getId()) {
											if(c.getDefenderSupporter() == ProvinceId.USSR)
												o.setInfluence(o.getInfluence()-1);
											if(c.getDefenderSupporter() == ProvinceId.USA)
												o.setInfluence(o.getInfluence()+1);
											if(c.getAttackerSupporter() == ProvinceId.USSR)
												o.setInfluence(o.getInfluence()+1);
											if(c.getAttackerSupporter() == ProvinceId.USA)
												o.setInfluence(o.getInfluence()-1);
										}
									}
								}
								removeConflicts.add(conflictIndex);
							} else if(c.getAttackerProgress() >= c.getGoal()) {
								// Attackers win
								// TODO: Conventional Wars
								int newInfl = 0;
								if(c.getType() == ConflictType.CIVIL_WAR || c.getType() == ConflictType.COLONIAL_WAR) {
									p.setGov(c.getRebels().getGov());
//									p.setLeader(c.getRebels().getLeader());
									// TODO: Colonizer calls in superpower; refuse to lose influence?
									if(c.getType() == ConflictType.COLONIAL_WAR) {
										for (ProvinceState.Builder o : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
											if(c.getDefender() == o.getId()) {
												if(c.getDefenderSupporter() == ProvinceId.USSR)
													o.setInfluence(o.getInfluence()-1);
												if(c.getDefenderSupporter() == ProvinceId.USA)
													o.setInfluence(o.getInfluence()+1);
												if(c.getAttackerSupporter() == ProvinceId.USSR)
													o.setInfluence(o.getInfluence()+2);
												if(c.getAttackerSupporter() == ProvinceId.USA)
													o.setInfluence(o.getInfluence()-2);
											}
										}
									}
								}
								if(hasLeader(p.getId()))
									newInfl ++;
								if(isStrongGov(p.getGov()))
									newInfl ++;
								if(c.getAttackerSupporter() == ProvinceId.USSR)
									newInfl += -1*getNetStability(c.getLocation());
								if(c.getAttackerSupporter() == ProvinceId.USA)
									newInfl += getNetStability(c.getLocation());
								p.setInfluence(newInfl);
								removeConflicts.add(conflictIndex);
							}
						} else
							c.setLength(0);
						nextStateBuilder.getTurnLogBuilder()
							.addEvents(Event.newBuilder()
								.setCivilWar(CivilWarEvent.newBuilder()
									.setProvinceId(c.getLocation())
									.build())
								.build());
					}
				}
			}
			conflictIndex++;
		}
		Collections.reverse(removeConflicts);
		for(int index : removeConflicts)
			nextStateBuilder.getConflictStateBuilder().removeActive(index);
		
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
		
		this.patriotism = patriotismCounter;
		this.partyUnity = partyUnityCounter;

		int ussrCentralAmerica = 0, usaCentralAmerica = 0,
			ussrSouthAmerica = 0, usaSouthAmerica = 0;
		
//		for (ProvinceSettings p : this.state.getSettings().getProvinceSettings().getProvinceList()) {
//			switch (p.getRegion()) {
//				case CENTRAL_AMERICA:
//					if (getAlly(p.getId()) == Player.USSR)
//						ussrCentralAmerica++;
//					if (getAlly(p.getId()) == Player.USA)
//						usaCentralAmerica++;
//					break;
//				case SOUTH_AMERICA:
//					if (getAlly(p.getId()) == Player.USSR)
//						ussrSouthAmerica++;
//					if (getAlly(p.getId()) == Player.USA)
//						usaSouthAmerica++;
//					break;
//				default:
//					break;
//			}
//		}
		
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
//			   getAlly(id) != otherPlayer(player) && 
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
//			   getAlly(id) == player &&
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
			ConflictState c = activeConflicts.get(id);
			return milStore.get(player) >= getOvertFundAttackerCost() &&
				   c.getRebels().getGov() != getIdealGov(otherPlayer(player)) &&
				   c.getAttackerSupporter() != ProvinceId.USSR &&
				   c.getAttackerSupporter() != ProvinceId.USA;
		}
		return false;
			   
	}
	
	public boolean isValidOvertFundDefenderMove(Player player, ProvinceId id) {
		if(isInArmedConflict(id)) {
			ConflictState c = activeConflicts.get(id);
			return milStore.get(player) >= getOvertFundDefenderCost() &&
				   governments.get(id) != getIdealGov(otherPlayer(player)) &&
				   c.getDefenderSupporter() != ProvinceId.USSR &&
				   c.getDefenderSupporter() != ProvinceId.USA;
		}
		return false;
	}
	
	public boolean isValidCovertFundAttackerMove(Player player, ProvinceId id) {
		if(isInArmedConflict(id)) {
			ConflictState c = activeConflicts.get(id);
			return covStore.get(player) >= getCovertFundAttackerCost() &&
				   c.getRebels().getGov() != getIdealGov(otherPlayer(player)) &&
				   c.getDefenderSupporter() != ProvinceId.USSR &&
				   c.getDefenderSupporter() != ProvinceId.USA;
		}
		return false;
	}
	
	public boolean isValidCovertFundDefenderMove(Player player, ProvinceId id) {
		if(isInArmedConflict(id)) {
			ConflictState c = activeConflicts.get(id);
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
//		if(getAlly(id) == otherPlayer(player)) return 2;
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
	
//	public Player getAlly(ProvinceId province) {
//		return mechanics.getProvinces().getProvince(province).getAlly();
//	}
	
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
	
	public boolean hasLeader(final ProvinceId id) {
		if( leaders.get(id) != LeaderState.getDefaultInstance() && leaders.get(id) != null )
			return true;
		return false;
	}
	
	public Government getDissidentsGov(final ProvinceId id) {
		return dissidents.get(id).getGov();
	}
	
	public ConflictState getConflict(final ProvinceId id) {
		return activeConflicts.get(id);
	}
	
	public Government getIdealGov(Player player) {
		if(player == Player.USSR) {
			return Government.COMMUNISM;
		} else {
			return Government.DEMOCRACY;
		} 
	}
	
	public LeaderSettings getLeaderSettings(String id) {
		for(LeaderSettings l : state.getSettings().getLeaderSettings().getLeaderList()) {
			if(l.getId() == id) return l;
		}
		for(LeaderSettings l : state.getSettings().getLeaderSettings().getInitLeaderList()) {
			if(l.getId() == id) return l;
		}
		return null;
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
		   activeConflicts.get(id) != ConflictState.getDefaultInstance()) {
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
	
	public int getNetPatriotism() {
		return state.getSuperpowerState().getUsaState().getPatriotism() + getPatriotismModifier();
	}
	
	public int getNetPartyUnity() {
		return state.getSuperpowerState().getUssrState().getPartyUnity() + getPartyUnityModifier();
	}
	
	protected int inflSign(Player player) {
		return player == Player.USA ? 1 : -1;
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
