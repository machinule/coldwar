package coldwar.logic;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import coldwar.GameStateOuterClass.GameState;
import coldwar.GameStateOuterClass.TurnLogEntry;
import coldwar.Logger;
import coldwar.EventOuterClass.Event;
import coldwar.EventOuterClass.ProvinceDissidentsEvent;
import coldwar.EventOuterClass.ProvinceDissidentsSuppressedEvent;
import coldwar.EventOuterClass.ProvinceFauxPasEvent;
import coldwar.EventOuterClass.ProvinceRepublicEvent;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;
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
		
	public final Map<Player, Integer> polStore;
	public final Map<Player, Integer> milStore;
	public final Map<Player, Integer> covStore;

	public final Map<Player, Integer> basePolIncome;
	public final Map<Player, Integer> baseMilIncome;
	public final Map<Player, Integer> baseCovIncome;
	
	public final Map<Province.Id, Integer> initialInfluence;
	public final Map<Province.Id, Integer> polInfluence;
	public final Map<Province.Id, Integer> milInfluence;
	public final Map<Province.Id, Integer> covInfluence;
	public final Map<Province.Id, Integer> totalInfluence;
	
	public final Map<Province.Id, Player>  alliances; // NULL -> Neither player
	
	public final Map<Province.Id, Boolean> dissidents;
	public final Map<Province.Id, Player> bases; // NULL -> Neither player

	public final Map<Province.Id, Integer> stabilityBase;
	public final Map<Province.Id, Integer> stabilityModifier;
	
	public final GameState nextState;
	
	public ComputedGameState(final GameState state, final MoveList usaMoves, final MoveList ussrMoves) {
		this.state = state;
		this.usaMoves = usaMoves;
		this.ussrMoves = ussrMoves;
		
		this.year = state.getTurn() + 1947;
		int heatCounter = state.getHeat();
		
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

		EnumMap<Province.Id, Boolean> dissidentsMap = new EnumMap<Province.Id, Boolean>(Province.Id.class);
		this.dissidents = Collections.unmodifiableMap(dissidentsMap);
		EnumMap<Province.Id, Player> baseMap = new EnumMap<Province.Id, Player>(Province.Id.class);
		this.bases = Collections.unmodifiableMap(baseMap);

		EnumMap<Province.Id, Integer> stabilityBaseMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.stabilityBase = Collections.unmodifiableMap(stabilityBaseMap);
		EnumMap<Province.Id, Integer> stabilityModifierMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.stabilityModifier = Collections.unmodifiableMap(stabilityModifierMap);

		// Computations:
		// NOTE: computations should only expose a read-only version of themselves as public variables.
		// For collections, Collections.unmodifiable* produces an appropriate view.
		
		this.state.getSettings().getProvincesList().forEach(p -> {
			stabilityBaseMap.put(p.getId(), p.getStabilityBase());
		});
		
		this.state.getProvincesList().forEach(p -> {
			baseInfluenceMap.put(p.getId(), p.getInfluence());
			dissidentsMap.put(p.getId(), p.getDissidents());
			baseMap.put(p.getId(), toPlayer(p.getBase()));
		});
		
		totalInfluenceMap.putAll(baseInfluenceMap);
		
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
					if(isValidDiaDipMove(player)) {
						final int mag = move.getDiaDipMove().getMagnitude();
						final int cost_multiplier;
						if(getAlly(move.getDiaDipMove().getProvinceId()) == otherPlayer(player)) {
							cost_multiplier = mag*2;
						} else {
							cost_multiplier = mag;
						}
						polInfluenceMap.compute(id, (i, infl) -> infl == null ? mag * inflSign : infl + mag * inflSign);
						polStoreMap.compute(player, (p, pol) -> pol == null ? -mag : pol - cost_multiplier);
						}
					}
				if (move.hasDiaMilMove()) {
					Province.Id id = move.getDiaMilMove().getProvinceId();
					if(isValidDiaMilMove(player, id)) {
						final int mag = move.getDiaMilMove().getMagnitude();
						milInfluenceMap.compute(id, (i, infl) -> infl == null ? mag * inflSign : infl + mag * inflSign);
						milStoreMap.compute(player, (p, mil) -> mil == null ? -mag : mil - mag);
					}
				}
				if (move.hasDiaCovMove()) {
					Province.Id id = move.getDiaCovMove().getProvinceId();
					if(isValidDiaCovMove(player)) {
						final int mag = move.getDiaCovMove().getMagnitude();
						covInfluenceMap.compute(id, (i, infl) -> infl == null ? mag * inflSign : infl + mag * inflSign);
						covStoreMap.compute(player, (p, cov) -> cov == null ? -mag : cov - mag);
					}
				}
				if (move.hasFundDissidentsMove()) {
					Province.Id id = move.getFundDissidentsMove().getProvinceId();
					if(isValidFundDissidentsMove(player, id)) {
						dissidentsMap.put(id, true);
						covStoreMap.compute(player, (p, cov) -> cov == null ? -1 : cov - 1);
						heatCounter += 4;
					}
				}
				if (move.hasEstablishBaseMove()) {
					Province.Id id = move.getEstablishBaseMove().getProvinceId();
					if(isValidEstablishBaseMove(player, id)) {
						baseMap.put(id, player);
						milStoreMap.compute(player,  (p, mil) -> mil == null ? -2 : mil - 2);
						heatCounter += 4;
					}
				}
				if (move.hasPoliticalPressureMove()) {
					if(isValidPoliticalPressureMove(player, move.getPoliticalPressureMove().getProvinceId())) {
						polStoreMap.compute(player,  (p, pol) -> pol == null ? -2 : pol - 2);
						heatCounter += 4; // More if enemy ally
					}
				}
				if (move.hasFoundNatoMove()) {
					
				}
				if (move.hasFoundPactMove()) {
					
				}
				
			}			
		}
		
		totalInfluenceMap.replaceAll((p, infl) -> infl + polInfluenceMap.getOrDefault(p, 0) + milInfluenceMap.getOrDefault(p, 0) + covInfluenceMap.getOrDefault(p, 0));
		dissidentsMap.forEach((p, dissidents) -> {
			if (dissidents) {stabilityModifierMap.compute(p, (q, mod) -> mod == null ? -1 : mod - 1 );}
		});
		
		heatCounter = Math.max(heatCounter - 10, 0);
		this.heat = heatCounter;
		
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
			province.setDissidents(dissidentsMap.get(province.getId()));
			allianceMap.put(province.getId(), getAlly(province.getId()));
			province.setInfluence(totalInfluenceMap.get(province.getId()));
			int totalStability = this.stabilityBase.get(province.getId()) + this.stabilityModifier.getOrDefault(province.getId(), 0);
			if (Math.abs(province.getInfluence()) > totalStability) {
				province.setInfluence(Integer.signum(province.getInfluence()) * totalStability);
			}
		}
		
		nextStateBuilder.getUsaBuilder().getInfluenceStoreBuilder()
		    .setPolitical(polStoreMap.get(Player.USA) + basePolIncomeMap.get(Player.USA))
		    .setMilitary(milStoreMap.get(Player.USA) + baseMilIncomeMap.get(Player.USA))
		    .setCovert(covStoreMap.get(Player.USA) + baseCovIncomeMap.get(Player.USA));
		nextStateBuilder.getUssrBuilder().getInfluenceStoreBuilder()
			.setPolitical(polStoreMap.get(Player.USSR) + basePolIncomeMap.get(Player.USSR))
			.setMilitary(milStoreMap.get(Player.USSR) + baseMilIncomeMap.get(Player.USSR))
			.setCovert(covStoreMap.get(Player.USSR) + baseCovIncomeMap.get(Player.USSR));
		
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
			if (p.getGov() == Province.Government.AUTOCRACY) {
				if (happens.apply(this.state.getSettings().getRandomProvinceRepublicChance())) {
					p.setGov(Province.Government.REPUBLIC);
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
					p.setGov(Province.Government.REPUBLIC);
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
			if (!p.getDissidents()) {
				int chance;
				if (p.getGov() == Province.Government.AUTOCRACY ||
					p.getGov() == Province.Government.COMMUNISM ||
					p.getGov() == Province.Government.DEMOCRACY) {
					chance = this.state.getSettings().getRandomProvinceACDDissidentsChance();
				} else {
					chance = this.state.getSettings().getRandomProvinceDefaultDissidentsChance();
				}
				if (happens.apply(chance)) {
					p.setDissidents(true);
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
			if (p.getDissidents()) {
				int chance;
				if (p.getGov() == Province.Government.AUTOCRACY ||
					p.getGov() == Province.Government.COMMUNISM ||
					p.getGov() == Province.Government.DEMOCRACY) {
					chance = this.state.getSettings().getRandomProvinceACDDissidentsSuppressedChance();
				} else {
					chance = this.state.getSettings().getRandomProvinceDefaultDissidentsSuppressedChance();
				}
				if (happens.apply(chance)) {
					p.setDissidents(false);
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
		
		nextStateBuilder.setSeed(r.nextLong());
		this.nextState = nextStateBuilder.build();
		Logger.Info("Stability Mod: " + this.stabilityModifier);

	}
	
	// VALIDATION
	
	public boolean isValidDiaDipMove(Player player){
		return polStore.get(player) > 0;
	}
	
	public boolean isValidDiaMilMove (Player player, Province.Id province){
		return milStore.get(player) > 0 && getAlly(province) != otherPlayer(player);
	}
	
	public boolean isValidDiaCovMove (Player player){
		return covStore.get(player) > 0;
	}
	
	public boolean isValidFundDissidentsMove(Player player, Province.Id province) {
		return covStore.get(player) > 0 && !(dissidents.get(province));
	}
	
	public boolean isValidEstablishBaseMove(Player player, Province.Id province) {
		return milStore.get(player) >= 2 && bases.get(province) == null;
		// Check alliances
	}
	
	public boolean isValidPoliticalPressureMove(Player player, Province.Id province) {
		return polStore.get(player) >= 2;
		// Handle adjacencies
	}
	
	// OTHER HELPER FUNCTIONS
	
	public Player getAlly(Province.Id province) {
		if(totalInfluence.get(province) >= getAllianceThreshold(province)) {
			return Player.USA;
		} else if(totalInfluence.get(province) <= -getAllianceThreshold(province)) {
			return Player.USSR;
		}
		return null;
	}
	
	public int getNetStability(Province.Id province) {
		if(stabilityModifier.get(province) != null) {
			return stabilityBase.get(province) - stabilityModifier.get(province);
		} else {
			return stabilityBase.get(province);
		}
	}
	
	public Player otherPlayer(Player player) {
		if(player == Player.USSR) {
			return Player.USA;
		} else {
			return Player.USSR;
		}
	}
	
	public int getAllianceThreshold(Province.Id province) {
		return (int) Math.ceil(((float)getNetStability(province)/2));
	}
	
	public static Player toPlayer(Province.Id id) {
		if (id == Province.Id.USA) return Player.USA;
		if (id == Province.Id.USSR) return Player.USSR;
		return null;
	}

}
