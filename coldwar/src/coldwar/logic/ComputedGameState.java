package coldwar.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import coldwar.GameStateOuterClass.Crisis;
import coldwar.GameStateOuterClass.Crisis.Builder;
import coldwar.GameStateOuterClass.GameState;
import coldwar.GameSettingsOuterClass.ProvinceSettings;
import coldwar.GameStateOuterClass.TurnLogEntry;
import coldwar.LeaderOuterClass.Leader;
import coldwar.Logger;
import coldwar.DissidentsOuterClass.Dissidents;
import coldwar.DissidentsOuterClass.Government;
import coldwar.EventOuterClass.BerlinBlockadeEvent;
import coldwar.EventOuterClass.CivilWarEvent;
import coldwar.EventOuterClass.CoupEvent;
import coldwar.EventOuterClass.Event;
import coldwar.EventOuterClass.ProvinceDissidentsEvent;
import coldwar.EventOuterClass.ProvinceDissidentsSuppressedEvent;
import coldwar.EventOuterClass.ProvinceFauxPasEvent;
import coldwar.EventOuterClass.ProvinceRepublicEvent;
import coldwar.MoveOuterClass.MoveList;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Conflict;
import coldwar.ProvinceOuterClass.Province;
import coldwar.Settings;
import coldwar.logic.Client.Player;

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
	public final boolean kgbFounded;
	public final boolean ciaFounded;
		
	public final Map<Player, Integer> polStore;
	public final Map<Player, Integer> milStore;
	public final Map<Player, Integer> covStore;

	public final Map<Player, Integer> basePolIncome;
	public final Map<Player, Integer> baseMilIncome;
	public final Map<Player, Integer> baseCovIncome;
	
	public final Map<Player, Integer> polIncomeModifier;
	public final Map<Player, Integer> milIncomeModifier;
	public final Map<Player, Integer> covIncomeModifier;
	
	public final Map<Province.Region, Integer> usaRegionControl;
	public final Map<Province.Region, Integer> ussrRegionControl;
	public final Map<Province.Region, Integer> regionTotal;
	
	public final Map<Province.Id, Integer> initialInfluence;
	public final Map<Province.Id, Integer> polInfluence;
	public final Map<Province.Id, Integer> milInfluence;
	public final Map<Province.Id, Integer> covInfluence;
	public final Map<Province.Id, Integer> totalInfluence;

	public final Map<Province.Id, Player>  alliances; // NULL -> Neither player
	public final Map<Province.Id, Boolean> usaAdjacencies;
	public final Map<Province.Id, Boolean> ussrAdjacencies;
	
	public final Map<Province.Id, Dissidents> dissidents;
	public final Map<Province.Id, Player> bases;
	public final Map<Province.Id, Government> governments;
	public final Map<Province.Id, Leader> leaders;
	public final Map<Province.Id, Conflict> activeConflicts;
	public final Map<Province.Id, Conflict> conflictZones;

	public final Map<Province.Id, Integer> stabilityBase;
	public final Map<Province.Id, Integer> stabilityModifier;
	
	public final Map<Province.Id, ProvinceSettings> provinceSettings;
	
	public final Map<Province.Id, Integer> coups;
	
	public final Map<Province.Id, Boolean> acted;
	
	public final GameState nextState;
	
	public ComputedGameState(final GameState state, final MoveList usaMoves, final MoveList ussrMoves) {
		this.state = state;
		this.usaMoves = usaMoves;
		this.ussrMoves = ussrMoves;
		
		this.year = state.getTurn() + 1948;
		int heatCounter = state.getHeat();
		int partyUnityCounter = state.getUssr().getPartyUnity();
		int patriotismCounter = state.getUsa().getPatriotism();
		
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
		
		EnumMap<Province.Region, Integer> usaRegionControlMap = new EnumMap<Province.Region, Integer>(Province.Region.class);
		this.usaRegionControl = Collections.unmodifiableMap(usaRegionControlMap);
		EnumMap<Province.Region, Integer> ussrRegionControlMap = new EnumMap<Province.Region, Integer>(Province.Region.class);
		this.ussrRegionControl = Collections.unmodifiableMap(ussrRegionControlMap);
		EnumMap<Province.Region, Integer> regionTotalMap = new EnumMap<Province.Region, Integer>(Province.Region.class);
		this.regionTotal = Collections.unmodifiableMap(regionTotalMap);
		
		EnumMap<Province.Id, Integer> baseInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.initialInfluence = Collections.unmodifiableMap(baseInfluenceMap);
		EnumMap<Province.Id, Integer> polInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.polInfluence = Collections.unmodifiableMap(polInfluenceMap);
		EnumMap<Province.Id, Integer> milInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.milInfluence = Collections.unmodifiableMap(milInfluenceMap);
		EnumMap<Province.Id, Integer> covInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.covInfluence = Collections.unmodifiableMap(covInfluenceMap);
		EnumMap<Province.Id, Integer> totalInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.totalInfluence = Collections.unmodifiableMap(totalInfluenceMap);
		
		EnumMap<Province.Id, Player> allianceMap = new EnumMap<Province.Id, Player>(Province.Id.class);
		this.alliances = Collections.unmodifiableMap(allianceMap);
		EnumMap<Province.Id, Boolean> usaAdjacencyMap = new EnumMap<Province.Id, Boolean>(Province.Id.class);
		this.usaAdjacencies = Collections.unmodifiableMap(usaAdjacencyMap);EnumMap<Province.Id, Boolean> adjacencyMap = new EnumMap<Province.Id, Boolean>(Province.Id.class);
		EnumMap<Province.Id, Boolean> ussrAdjacencyMap = new EnumMap<Province.Id, Boolean>(Province.Id.class);
		this.ussrAdjacencies = Collections.unmodifiableMap(ussrAdjacencyMap);

		EnumMap<Province.Id, Dissidents> dissidentsMap = new EnumMap<Province.Id, Dissidents>(Province.Id.class);
		this.dissidents = Collections.unmodifiableMap(dissidentsMap);
		EnumMap<Province.Id, Player> baseMap = new EnumMap<Province.Id, Player>(Province.Id.class);
		this.bases = Collections.unmodifiableMap(baseMap);
		EnumMap<Province.Id, Government> governmentMap = new EnumMap<Province.Id, Government>(Province.Id.class);
		this.governments = Collections.unmodifiableMap(governmentMap);
		EnumMap<Province.Id, Conflict> activeConflictMap = new EnumMap<Province.Id, Conflict>(Province.Id.class);
		this.activeConflicts = Collections.unmodifiableMap(activeConflictMap);
		EnumMap<Province.Id, Conflict> conflictZoneMap = new EnumMap<Province.Id, Conflict>(Province.Id.class);
		this.conflictZones = Collections.unmodifiableMap(conflictZoneMap);
		
		EnumMap<Province.Id, Leader> leaderMap = new EnumMap<Province.Id, Leader>(Province.Id.class);
		this.leaders = Collections.unmodifiableMap(leaderMap);

		EnumMap<Province.Id, Integer> stabilityBaseMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.stabilityBase = Collections.unmodifiableMap(stabilityBaseMap);
		EnumMap<Province.Id, Integer> stabilityModifierMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.stabilityModifier = Collections.unmodifiableMap(stabilityModifierMap);	

		EnumMap<Province.Id, ProvinceSettings> provinceSettingsMap = new EnumMap<Province.Id, ProvinceSettings>(Province.Id.class);
		this.provinceSettings = Collections.unmodifiableMap(provinceSettingsMap);

		EnumMap<Province.Id, Integer> coupMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.coups = Collections.unmodifiableMap(coupMap);

		EnumMap<Province.Id, Boolean> actedMap = new EnumMap<Province.Id, Boolean>(Province.Id.class);
		this.acted = Collections.unmodifiableMap(actedMap);
		
		boolean ciaFoundedFlag = false;
		boolean kgbFoundedFlag = false;
		
		// Computations:
		// NOTE: computations should only expose a read-only version of themselves as public variables.
		// For collections, Collections.unmodifiable* produces an appropriate view.
		
		ciaFoundedFlag = this.state.getUsa().getCiaFounded();
		kgbFoundedFlag = this.state.getUssr().getKgbFounded();
		
		this.state.getSettings().getProvincesList().forEach(p -> {
			stabilityBaseMap.put(p.getId(), p.getStabilityBase());
			provinceSettingsMap.put(p.getId(), p);
			regionTotalMap.put(p.getRegion(), regionTotalMap.getOrDefault(p.getRegion(), 0) + 1);
		});
		
		this.state.getProvincesList().forEach(p -> {
			baseInfluenceMap.put(p.getId(), p.getInfluence());
			baseMap.put(p.getId(), toPlayer(p.getBase()));
			governmentMap.put(p.getId(), p.getGov());
			actedMap.put(p.getId(), false);
			leaderMap.put(p.getId(), p.getLeader());
			dissidentsMap.put(p.getId(), p.getDissidents());
			activeConflictMap.put(p.getId(), p.getConflict());
		});
		
		totalInfluenceMap.putAll(baseInfluenceMap);
		
		this.state.getProvincesList().forEach(p -> {
			usaAdjacencyMap.put(p.getId(), hasAdjacencyInfluence(Player.USA, p.getId()));
			ussrAdjacencyMap.put(p.getId(), hasAdjacencyInfluence(Player.USSR, p.getId()));
		});
		
		polStoreMap.put(Player.USA, state.getUsa().getInfluenceStore().getPolitical());
		milStoreMap.put(Player.USA, state.getUsa().getInfluenceStore().getMilitary());
		covStoreMap.put(Player.USA, state.getUsa().getInfluenceStore().getCovert());
		
		polStoreMap.put(Player.USSR, state.getUssr().getInfluenceStore().getPolitical());
		milStoreMap.put(Player.USSR, state.getUssr().getInfluenceStore().getMilitary());
		covStoreMap.put(Player.USSR, state.getUssr().getInfluenceStore().getCovert());
		
		basePolIncomeMap.put(Player.USA, state.getSettings().getUsaPolIncomeBase());
		baseMilIncomeMap.put(Player.USA, state.getSettings().getUsaMilIncomeBase());
		baseCovIncomeMap.put(Player.USA, state.getSettings().getUsaCovIncomeBase());

		basePolIncomeMap.put(Player.USSR, state.getSettings().getUssrPolIncomeBase());
		baseMilIncomeMap.put(Player.USSR, state.getSettings().getUssrMilIncomeBase());
		baseCovIncomeMap.put(Player.USSR, state.getSettings().getUssrCovIncomeBase());

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
					Province.Id id = move.getDiaDipMove().getProvinceId();
					if(isValidDiaDipMove(player, id)) {
						final int mag = move.getDiaDipMove().getMagnitude();
						final int effect_multiplier;
						if(getAlly(move.getDiaDipMove().getProvinceId()) == otherPlayer(player)) {
							effect_multiplier = 2;
						} else {
							effect_multiplier = 1;
						}
						polInfluenceMap.compute(id, (i, infl) -> infl == null ? (mag/effect_multiplier) * inflSign : infl + (mag/effect_multiplier) * inflSign);
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
					Province.Id id = move.getDiaMilMove().getProvinceId();
					if(isValidDiaMilMove(player, id)) {
						final int mag = move.getDiaMilMove().getMagnitude();
						milInfluenceMap.compute(id, (i, infl) -> infl == null ? mag * inflSign : infl + mag * inflSign);
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
					Province.Id id = move.getDiaCovMove().getProvinceId();
					if(isValidDiaCovMove(player, id)) {
						final int mag = move.getDiaCovMove().getMagnitude();
						covInfluenceMap.compute(id, (i, infl) -> infl == null ? mag * inflSign : infl + mag * inflSign);
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
					Province.Id id = move.getFundDissidentsMove().getProvinceId();
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
						heatCounter += Settings.getConstInt("action_dissidents_heat");
						actedMap.put(id, true);
					}
				}
				if (move.hasEstablishBaseMove()) {
					Province.Id id = move.getEstablishBaseMove().getProvinceId();
					if(isValidEstablishBaseMove(player, id)) {
						final int cost = getEstablishBaseMoveCost();
						baseMap.put(id, player);
						milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
						heatCounter += Settings.getConstInt("action_dissidents_heat");
						actedMap.put(id, true);
					}
				}
				if (move.hasPoliticalPressureMove()) {
					Province.Id id = move.getPoliticalPressureMove().getProvinceId();
					if(isValidPoliticalPressureMove(player, id)) {
						final int cost = getPoliticalPressureMoveCost();
						int netInfl = 0;
						for (Province.Id adj : provinceSettingsMap.get(id).getAdjacencyList()) {
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
							heatCounter += Settings.getConstInt("action_pressure_heat_extra"); // More if enemy ally
						}
						heatCounter += Settings.getConstInt("action_pressure_heat");
						actedMap.put(id, true);
					}
				}
				if (move.hasCoupMove()) {
					Province.Id id = move.getCoupMove().getProvinceId();
					if(isValidCoupMove(player, id)) {
						int result = inflSign * (getAllianceThreshold(id) + 1);
						coupMap.put(id, result);
						int cov_cost = getCoupMoveCost(id);
						covStoreMap.compute(player,  (p, mil) -> mil == null ? -cov_cost : mil - cov_cost);
						int heatPerStab = Settings.getConstInt("action_coup_heat_per_stab")*getNetStability(id);
						heatCounter += Settings.getConstInt("action_coup_heat_fixed") + heatPerStab;
						actedMap.put(id, true);
					}
				}
				if (move.hasConflictOvertFundAttackerMove()) {
					Province.Id id = move.getConflictOvertFundAttackerMove().getProvinceId();
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
					Province.Id id = move.getConflictOvertFundDefenderMove().getProvinceId();
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
				if (move.hasFoundNatoMove()) {
					
				}
				if (move.hasFoundPactMove()) {
					
				}
				if (move.hasFoundKgbMove()) {
					kgbFoundedFlag = true;
				}
				if (move.hasFoundCiaMove()) {
					ciaFoundedFlag = true;					
				}
				// Crises
				if (move.hasUsaBerlinBlockadeAirliftMove()) {
					int cost = 100; // TODO: Real value
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
		
		kgbFounded = kgbFoundedFlag;
		ciaFounded = ciaFoundedFlag;
		
		if (kgbFoundedFlag) {
			int covModifier = this.state.getSettings().getKgbCovIncomeModifier();
			covIncomeModifierMap.putIfAbsent(Player.USSR, 0);
			covIncomeModifierMap.computeIfPresent(Player.USSR, (p, i) -> i + covModifier);
		}
		
		if (ciaFoundedFlag) {
			int covModifier = this.state.getSettings().getCiaCovIncomeModifier();
			covIncomeModifierMap.putIfAbsent(Player.USA, 0);
			covIncomeModifierMap.computeIfPresent(Player.USA, (p, i) -> i + covModifier);
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
				Leader.Type type = l.getType();
				Player player = getAlly(p);
				if(player != null) {
					int lead_income;
					switch (type) {
						case POLITICAL:
							lead_income = Settings.getConstInt("leader_income_pol");
							polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
							break;
						case MILITARY:
							lead_income = Settings.getConstInt("leader_income_mil");
							polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
							break;
						case COVERT:
							lead_income = Settings.getConstInt("leader_income_cov");
							polIncomeModifierMap.compute(player, (q, mod) -> mod == null ? lead_income : mod + lead_income);
							break;
						default:
							Logger.Err("Leader " + l.getName() + " has no type!");
							break;				
					}
				}
				if(type == Leader.Type.ISOLATIONIST) {
					polInfluenceMap.compute(p, (i, infl) -> infl == null ? -1 * inflSign(player) : infl + -1 * inflSign(player));
				}
			}
		});
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
		
		heatCounter = Math.max(heatCounter - 10, this.state.getSettings().getHeatMin());
		this.heat = heatCounter;
		
		patriotismCounter = this.state.getUsa().getPatriotism();
		partyUnityCounter = this.state.getUssr().getPartyUnity();
		
		if (this.heat > Settings.getConstInt("heat_bleed_threshold")) {
			patriotismCounter -= Settings.getConstInt("heat_bleed");
			partyUnityCounter -= Settings.getConstInt("heat_bleed");
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
					// Add 1 USSR influence to E. Ger
				}
			} else {
				if(blockade) { // Blockade with no airlift
					patriotismCounter -= 5;
					// Berlin flag unset
				} else { // No blockade and no airlift
					// Add 1 USSR influence to E. Ger
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
				.setHeat(heatCounter)
				.addAllProvinces(this.state.getProvincesList());
		
		for (final Province.Builder province : nextStateBuilder.getProvincesBuilderList()) {
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
		
		nextStateBuilder.getUsaBuilder().getInfluenceStoreBuilder()
		    .setPolitical(polStoreMap.get(Player.USA) + basePolIncomeMap.get(Player.USA) + polIncomeModifierMap.getOrDefault(Player.USA, 0))
		    .setMilitary(milStoreMap.get(Player.USA) + baseMilIncomeMap.get(Player.USA) + milIncomeModifierMap.getOrDefault(Player.USA, 0))
		    .setCovert(covStoreMap.get(Player.USA) + baseCovIncomeMap.get(Player.USA) + covIncomeModifierMap.getOrDefault(Player.USA, 0));
		nextStateBuilder.getUssrBuilder().getInfluenceStoreBuilder()
			.setPolitical(polStoreMap.get(Player.USSR) + basePolIncomeMap.get(Player.USSR) + polIncomeModifierMap.getOrDefault(Player.USSR, 0))
			.setMilitary(milStoreMap.get(Player.USSR) + baseMilIncomeMap.get(Player.USSR) + milIncomeModifierMap.getOrDefault(Player.USSR, 0))
			.setCovert(covStoreMap.get(Player.USSR) + baseCovIncomeMap.get(Player.USSR) + covIncomeModifierMap.getOrDefault(Player.USSR, 0));
		
		nextStateBuilder.getUsaBuilder().setPatriotism(patriotismCounter);
		nextStateBuilder.getUssrBuilder().setPartyUnity(partyUnityCounter);
		
		nextStateBuilder.getUsaBuilder().setCiaFounded(ciaFoundedFlag);
		nextStateBuilder.getUssrBuilder().setKgbFounded(kgbFoundedFlag);
		
		// Random events.
		Random r = new Random(this.state.getSeed());
		Function<Integer, Boolean> happens = c -> r.nextInt(1000000) < c;
		// TODO: LeaderSpawn
		// TODO: LeaderDeath
		// TODO: ProvinceCoup
		// TODO: ProvinceDemocracy
		// TODO: ProvinceCommunism
		// TODO: ProvinceAutocracy
		// ProvinceRepublic
		for (Province.Builder p : nextStateBuilder.getProvincesBuilderList()) {
			if (p.getGov() == Government.AUTOCRACY) {
				if (happens.apply(this.state.getSettings().getRandomProvinceRepublicChance())) {
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
		for (Province.Builder p : nextStateBuilder.getProvincesBuilderList()) {
			if (p.getInfluence() != 0) {
				if (happens.apply(this.state.getSettings().getRandomProvinceFauxPasChance())) {
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
		for (Province.Builder p : nextStateBuilder.getProvincesBuilderList()) {
			if (!hasDissidents(p.getId())) {
				int chance;
				if (p.getGov() == Government.AUTOCRACY) 
					chance = this.state.getSettings().getRandomAutocracyDissidentsChance();
				else if (p.getGov() == Government.COMMUNISM)
					chance = this.state.getSettings().getRandomCommunismDissidentsChance();
				else if (p.getGov() == Government.DEMOCRACY)
					chance = this.state.getSettings().getRandomDemocracyDissidentsChance();
				else chance = this.state.getSettings().getRandomProvinceDefaultDissidentsChance();
				if (happens.apply(chance)) {
					Logger.Dbg("Adding random dissidents to " + p.getId());
					Dissidents.Builder d = Dissidents.newBuilder();
					Government gov = p.getGov();
					int result = r.nextInt(1000000);
					if(!p.hasAlly()) {
						switch (gov) {
							case AUTOCRACY:
								if(result < this.state.getSettings().getNeutralAutocracyDemocraticDissidents())
									d.setGov(Government.DEMOCRACY);
								else if (result - this.state.getSettings().getNeutralAutocracyDemocraticDissidents()
										< this.state.getSettings().getNeutralAutocracyCommunistDissidents())
									d.setGov(Government.COMMUNISM);
								else
									d.setGov(Government.REPUBLIC);
								break;
							case REPUBLIC:
								if(result < this.state.getSettings().getNeutralRepublicDemocraticDissidents())
									d.setGov(Government.DEMOCRACY);
								else if (result - this.state.getSettings().getNeutralRepublicDemocraticDissidents() < 
										this.state.getSettings().getNeutralRepublicCommunistDissidents())
									d.setGov(Government.COMMUNISM);
								else
									d.setGov(Government.REPUBLIC);
								break;
							case DEMOCRACY:
								if(happens.apply(this.state.getSettings().getNeutralDemocracyDemocraticDissidents()))
									d.setGov(Government.DEMOCRACY);
								else if (result - this.state.getSettings().getNeutralDemocracyDemocraticDissidents() < 
										this.state.getSettings().getNeutralDemocracyCommunistDissidents())
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
								if(happens.apply(this.state.getSettings().getInfluencedAutocracyNeutralDissidents()))
									d.setGov(Government.REPUBLIC);
								else if (result - this.state.getSettings().getInfluencedAutocracyNeutralDissidents() < 
										this.state.getSettings().getInfluencedAutocracyOpposingDissidents())
									d.setGov(getAlly(p.getId()) == Player.USA ? Government.DEMOCRACY : Government.COMMUNISM);
								else
									d.setGov(getIdealGov(getAlly(p.getId())));
								break;
							case REPUBLIC:
								if(happens.apply(this.state.getSettings().getInfluencedRepublicNeutralDissidents()))
									d.setGov(Government.REPUBLIC);
								else if (result - this.state.getSettings().getInfluencedRepublicNeutralDissidents() < 
										this.state.getSettings().getInfluencedRepublicOpposingDissidents())
									d.setGov(getAlly(p.getId()) == Player.USA ? Government.DEMOCRACY : Government.COMMUNISM);
								else
									d.setGov(getIdealGov(getAlly(p.getId())));
								break;
							case DEMOCRACY:
								if(happens.apply(this.state.getSettings().getInfluencedDemocracyDemocraticDissidents()))
									d.setGov(Government.REPUBLIC);
								else if (result - this.state.getSettings().getInfluencedDemocracyDemocraticDissidents() < 
										this.state.getSettings().getInfluencedDemocracyCommunistDissidents())
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
		for (Province.Builder p : nextStateBuilder.getProvincesBuilderList()) {
			if (hasDissidents(p.getId())) {
				int chance;
				if (p.getGov() == Government.DEMOCRACY) {
					chance = this.state.getSettings().getRandomProvinceDemocracyDissidentsSuppressedChance();
				} else {
					chance = this.state.getSettings().getRandomProvinceDefaultDissidentsSuppressedChance();
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
		for (Province.Builder p : nextStateBuilder.getProvincesBuilderList()) {
			if (isInArmedConflict(p.getId())) {
				Logger.Vrb("Civil war in " + p.getId());
				Logger.Vrb(p.getConflict().toString());
				p.setInfluence(0);
				coupMap.put(p.getId(), 0);
				p.setBase(Province.Id.NONE);
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
						if(c.getDefenderSupporter() == Province.Id.USSR)
							p.setInfluence(-getNetStability(p.getId()));
						if(c.getDefenderSupporter() == Province.Id.USA)
							p.setInfluence(getNetStability(p.getId()));
						c = Conflict.getDefaultInstance().toBuilder();
					} else if(c.getAttackerProgress() >= c.getGoal()) {
						int newInfl = 0;
						p.setGov(c.getRebels().getGov());
						p.setLeader(c.getRebels().getLeader());
						if(p.getHasLeader())
							newInfl ++;
						if(isStrongGov(p.getGov()))
							newInfl ++;
						if(c.getAttackerSupporter() == Province.Id.USSR)
							newInfl += -1*getNetStability(p.getId());
						if(c.getAttackerSupporter() == Province.Id.USA)
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
		for (Province.Builder p : nextStateBuilder.getProvincesBuilderList()) {
			if (coups.getOrDefault(p.getId(), 0) != 0) {
				int result = coups.get(p.getId());
				if (happens.apply(this.state.getSettings().getCoupSuccessChance())) {
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
		
		nextStateBuilder.setSeed(r.nextLong());
		
		this.patriotism = patriotismCounter;
		this.partyUnity = partyUnityCounter;

		int ussrCentralAmerica = 0, usaCentralAmerica = 0,
			ussrSouthAmerica = 0, usaSouthAmerica = 0;
		
		for (ProvinceSettings p : this.state.getSettings().getProvincesList()) {
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
		
		usaRegionControlMap.put(Province.Region.CENTRAL_AMERICA, usaCentralAmerica);
		usaRegionControlMap.put(Province.Region.SOUTH_AMERICA, usaSouthAmerica);
		
		ussrRegionControlMap.put(Province.Region.CENTRAL_AMERICA, ussrCentralAmerica);
		ussrRegionControlMap.put(Province.Region.SOUTH_AMERICA, ussrSouthAmerica);
		
		nextStateBuilder.getUsaBuilder().setPatriotism(patriotism);
		nextStateBuilder.getUssrBuilder().setPartyUnity(partyUnity);
		
		if(getPatriotismModifier() <= 0) {} // Lose
		if(getPartyUnityModifier() <= 0) {} // Lose
		
		this.nextState = nextStateBuilder.build();
	}
	
	// VALIDATION
	
	public boolean isValidDiaDipMove(Player player, Province.Id id){
		return polStore.get(player) >= getDiaDipMoveMin(player, id) + getNonAdjacentDiaMoveCost(player, id) &&
			   !isInArmedConflict(id);
	}
	
	public boolean isValidDiaMilMove (Player player, Province.Id id){
		return milStore.get(player) >= getDiaMilMoveMin() &&
			   polStore.get(player) >= getNonAdjacentDiaMoveCost(player, id) && 
			   getAlly(id) != otherPlayer(player) && 
			   !isInArmedConflict(id);
	}
	
	public boolean isValidDiaCovMove (Player player, Province.Id id){
		return covStore.get(player) >= getDiaCovMoveMin() && 
			   polStore.get(player) >= getNonAdjacentDiaMoveCost(player, id) && 
			   !isInArmedConflict(id);
	}
	
	public boolean isValidFundDissidentsMove(Player player, Province.Id id) {
		return covStore.get(player) >= getFundDissidentsMoveCost() &&
			   !(hasDissidents(id)) && 
			   !isInArmedConflict(id);
	}
	
	public boolean isValidEstablishBaseMove(Player player, Province.Id id) {
		return milStore.get(player) >= getEstablishBaseMoveCost() &&
			   bases.get(id) == null &&
			   getAlly(id) == player &&
			   !isInArmedConflict(id);
	}
	
	public boolean isValidPoliticalPressureMove(Player player, Province.Id id) {
		return polStore.get(player) >= getPoliticalPressureMoveCost() && 
			   !isInArmedConflict(id) &&
			   bases.get(id) != otherPlayer(player);
	}
	
	public boolean isValidCoupMove(Player player, Province.Id id) {
		return covStore.get(player) >= getCoupMoveCost(id) &&
			   bases.get(id) == null &&
			   !hasInfluence(player, id) &&
			   getNetStability(id) <= Settings.getConstInt("action_coup_stab_threshold") &&
			   !isInArmedConflict(id);
	}
	
	public boolean isValidOvertFundAttackerMove(Player player, Province.Id id) {
		if(isInArmedConflict(id)) {
			Conflict c = activeConflicts.get(id);
			return milStore.get(player) >= getOvertFundAttackerCost() &&
				   c.getRebels().getGov() != getIdealGov(otherPlayer(player)) &&
				   c.getAttackerSupporter() != Province.Id.USSR &&
				   c.getAttackerSupporter() != Province.Id.USA;
		}
		return false;
			   
	}
	
	public boolean isValidOvertFundDefenderMove(Player player, Province.Id id) {
		if(isInArmedConflict(id)) {
			Conflict c = activeConflicts.get(id);
			return milStore.get(player) >= getOvertFundDefenderCost() &&
				   governments.get(id) != getIdealGov(otherPlayer(player)) &&
				   c.getDefenderSupporter() != Province.Id.USSR &&
				   c.getDefenderSupporter() != Province.Id.USA;
		}
		return false;
	}
	
	public boolean isValidCovertFundAttackerMove(Player player, Province.Id id) {
		if(isInArmedConflict(id)) {
			Conflict c = activeConflicts.get(id);
			return covStore.get(player) >= getCovertFundAttackerCost() &&
				   c.getRebels().getGov() != getIdealGov(otherPlayer(player)) &&
				   c.getDefenderSupporter() != Province.Id.USSR &&
				   c.getDefenderSupporter() != Province.Id.USA;
		}
		return false;
	}
	
	public boolean isValidCovertFundDefenderMove(Player player, Province.Id id) {
		if(isInArmedConflict(id)) {
			Conflict c = activeConflicts.get(id);
			return covStore.get(player) >= getCovertFundDefenderCost() &&
				   governments.get(id) != getIdealGov(otherPlayer(player)) &&
				   c.getDefenderSupporter() != Province.Id.USSR &&
				   c.getDefenderSupporter() != Province.Id.USA;
		}
		return false;
	}
	
	public boolean isValidFoundKGBMove() {
		return !kgbFounded;
	}
	
	public boolean isValidFoundCIAMove() {
		return !ciaFounded;
	}
	
	// Crises
	
	public boolean isBerlinBlockadeActive() {
		if(state.getCrises().getBerlinBlockade()) {
			return true;
		}
		return false;
	}
	
	// COST AND COST RANGES

	public int getNonAdjacentDiaMoveCost(Player player, Province.Id id) {
		if((player == Player.USA && !usaAdjacencies.get(id)) ||
		   (player == Player.USSR && !ussrAdjacencies.get(id)) )
			return Settings.getConstInt("non_adjacent_cost");
		return 0;
	}
	
	public int getDiaDipMoveMin(Player player, Province.Id id) {
		if(getDiaDipMoveIncrement(player, id) == 2) return 2;
		return 1;
	}
	
	public int getDiaDipMoveMax(Player player, Province.Id id) {
		int ret = Math.min(polStore.get(player), 2*getNetStability(id));
		return ret;
	}
	
	public int getDiaDipMoveIncrement(Player player, Province.Id id) {
		if(getAlly(id) == otherPlayer(player)) return 2;
		return 1;
	}
	
	public int getDiaMilMoveMin() {
		return 1;
	}
	
	public int getDiaMilMoveMax(Player player, Province.Id id) {
		int ret = Math.min(milStore.get(player), 2*getNetStability(id));
		return ret;
	}
	
	public int getDiaMilMoveIncrement() {
		return 1;
	}
	
	public int getDiaCovMoveMin() {
		return 1;
	}
	
	public int getDiaCovMoveMax(Player player, Province.Id id) {
		int ret = Math.min(covStore.get(player), 2*getNetStability(id));
		return ret;
	}
	
	public int getDiaCovMoveIncrement() {
		return 1;
	}
	
	public int getFundDissidentsMoveCost() {
		return Settings.getConstInt("action_dissidents_cost");
	}
	
	public int getEstablishBaseMoveCost() {
		return Settings.getConstInt("action_base_cost");
	}
	
	public int getPoliticalPressureMoveCost() {
		return Settings.getConstInt("action_pressure_cost");
	}
	
	public int getCoupMoveCost(Province.Id id) {
		return (Settings.getConstInt("action_coup_cost_per_stab") * getNetStability(id)) + 1;
	}
	
	public int getOvertFundAttackerCost() {
		return Settings.getConstInt("conflict_overt_fund_attacker");
	}
	
	public int getOvertFundDefenderCost() {
		return Settings.getConstInt("conflict_overt_fund_defender");
	}
	
	public int getCovertFundAttackerCost() {
		return Settings.getConstInt("conflict_covert_fund_attacker");
	}
	
	public int getCovertFundDefenderCost() {
		return Settings.getConstInt("conflict_overt_fund_defender");
	}
	
	// OTHER HELPER FUNCTIONS
	
	public boolean hasActed(Province.Id id) {
		return acted.get(id);
	}
	
	public Player getAlly(Province.Id province) {
		if(totalInfluence.get(province) > getAllianceThreshold(province) &&
				governments.get(province) != Government.COMMUNISM) {
			return Player.USA;
		} else if(totalInfluence.get(province) < -getAllianceThreshold(province) &&
				governments.get(province) != Government.DEMOCRACY) {
			return Player.USSR;
		}
		return null;
	}
	
	public int getNetStability(Province.Id province) {
		return stabilityBase.get(province) + stabilityModifier.getOrDefault(province, 0);
	}
	
	public Player otherPlayer(Player player) {
		if(player == Player.USSR) {
			return Player.USA;
		} else {
			return Player.USSR;
		}
	}
	
	public int getAllianceThreshold(Province.Id province) {
		if(getNetStability(province) > 0) return getNetStability(province)-1;
		return 0;
	}
	
	public static Player toPlayer(Province.Id id) {
		if (id == Province.Id.USA) return Player.USA;
		if (id == Province.Id.USSR) return Player.USSR;
		return null;
	}
	
	public static Province.Id toProvinceId(Player player) {
		if (player == Player.USA) return Province.Id.USA;
		if (player == Player.USSR) return Province.Id.USSR;
		return null;
	}
	
	public boolean hasDissidents(final Province.Id id) {
		if( dissidents.get(id) != Dissidents.getDefaultInstance() && dissidents.get(id) != null )
			return true;
		return false;
	}
	
	public boolean hasLeader(final Province.Id id) {
		if( leaders.get(id) != Leader.getDefaultInstance() && leaders.get(id) != null )
			return true;
		return false;
	}
	
	public Government getDissidentsGov(final Province.Id id) {
		return dissidents.get(id).getGov();
	}
	
	public Conflict getConflict(final Province.Id id) {
		return activeConflicts.get(id);
	}
	
	public Government getIdealGov(Player player) {
		if(player == Player.USSR) {
			return Government.COMMUNISM;
		} else {
			return Government.DEMOCRACY;
		} 
	}
	
	protected boolean hasAdjacencyInfluence(Player player, Province.Id id) {
		if(hasInfluence(player, id)) return true;
		for (Province.Id adj : provinceSettings.get(id).getAdjacencyList()) {
			if(hasInfluence(player, adj)) return true;
		}
		return false;
	}
	
	public boolean isInRange(Player player, Province.Id id) {
		if (player == Player.USA)
			return usaAdjacencies.get(id);
		else //if (player == Player.USSR)
			return ussrAdjacencies.get(id);
	}
	
	public boolean hasInfluence(Player player, Province.Id id) {
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
	
	public boolean isInArmedConflict(Province.Id id) {
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
		for(Province.Region r : Province.Region.values()) {
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
		sum = sum*Settings.getConstInt("vp_region_modifier");
		return sum;
	}
	
	public int getPartyUnityModifier() {
		int sum = 0;
		for(Province.Region r : Province.Region.values()) {
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
		sum = sum*Settings.getConstInt("vp_region_modifier");
		return sum;
	}
	
	public int getNetPatriotism() {
		return state.getUsa().getPatriotism() + getPatriotismModifier();
	}
	
	public int getNetPartyUnity() {
		return state.getUssr().getPartyUnity() + getPartyUnityModifier();
	}
	
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
		if (move.hasFoundCiaMove()) {
			messages.add(String.format("%s founded the CIA.", player));												
		}
		if (move.hasFoundKgbMove()) {
			messages.add(String.format("%s founded the KGB.", player));																
		}
		if (move.hasFoundPactMove()) {
			messages.add(String.format("%s founded the Pact.", player));																
		}
		if (move.hasFoundNatoMove()) {
			messages.add(String.format("%s founded NATO.", player));												
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
