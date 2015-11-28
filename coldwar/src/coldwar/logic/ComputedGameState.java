package coldwar.logic;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import coldwar.GameStateOuterClass.GameState;
import coldwar.GameStateOuterClass.TurnLogEntry;
import coldwar.Logger;
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
		
	public final Map<Player, Integer> polStore;
	public final Map<Player, Integer> milStore;
	public final Map<Player, Integer> covStore;

	public final Map<Player, Integer> basePolIncome;
	public final Map<Player, Integer> baseMilIncome;
	public final Map<Player, Integer> baseCovIncome;
	
	public final Map<Province.Id, Integer> baseInfluence;
	public final Map<Province.Id, Integer> polInfluence;
	public final Map<Province.Id, Integer> milInfluence;
	public final Map<Province.Id, Integer> covInfluence;
	public final Map<Province.Id, Integer> totalInfluence;
	
	public final Map<Province.Id, Boolean> dissidents;
	
	public final Map<Province.Id, Integer> stabilityModifier;
	
	public final GameState nextState;
	
	public ComputedGameState(final GameState state, final MoveList usaMoves, final MoveList ussrMoves) {
		this.state = state;
		this.usaMoves = usaMoves;
		this.ussrMoves = ussrMoves;
		
		this.year = state.getTurn() + 1947;
		
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
		this.baseInfluence = Collections.unmodifiableMap(baseInfluenceMap);
		EnumMap<Province.Id, Integer> polInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.polInfluence = Collections.unmodifiableMap(polInfluenceMap);
		EnumMap<Province.Id, Integer> milInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.milInfluence = Collections.unmodifiableMap(milInfluenceMap);
		EnumMap<Province.Id, Integer> covInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.covInfluence = Collections.unmodifiableMap(covInfluenceMap);
		EnumMap<Province.Id, Integer> totalInfluenceMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.totalInfluence = Collections.unmodifiableMap(totalInfluenceMap);

		EnumMap<Province.Id, Boolean> dissidentsMap = new EnumMap<Province.Id, Boolean>(Province.Id.class);
		this.dissidents = Collections.unmodifiableMap(dissidentsMap);

		EnumMap<Province.Id, Integer> stabilityModifierMap = new EnumMap<Province.Id, Integer>(Province.Id.class);
		this.stabilityModifier = Collections.unmodifiableMap(stabilityModifierMap);

		// Computations:
		// NOTE: computations should only expose a read-only version of themselves as public variables.
		// For collections, Collections.unmodifiable* produces an appropriate view.
				
		this.state.getProvincesList().forEach(p -> {
			baseInfluenceMap.put(p.getId(), p.getInfluence());
			dissidentsMap.put(p.getId(), p.getDissidents());
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
					final int mag = move.getDiaDipMove().getMagnitude();
					polInfluenceMap.compute(id, (i, infl) -> infl == null ? mag * inflSign : infl + mag * inflSign);
					polStoreMap.compute(player, (p, pol) -> pol == null ? -mag : pol - mag);
				}
				if (move.hasDiaMilMove()) {
					Province.Id id = move.getDiaMilMove().getProvinceId();
					final int mag = move.getDiaMilMove().getMagnitude();
					milInfluenceMap.compute(id, (i, infl) -> infl == null ? mag * inflSign : infl + mag * inflSign);
					milStoreMap.compute(player, (p, mil) -> mil == null ? -mag : mil - mag);
				}
				if (move.hasDiaCovMove()) {
					Province.Id id = move.getDiaCovMove().getProvinceId();
					final int mag = move.getDiaCovMove().getMagnitude();
					covInfluenceMap.compute(id, (i, infl) -> infl == null ? mag * inflSign : infl + mag * inflSign);
					covStoreMap.compute(player, (p, cov) -> cov == null ? -mag : cov - mag);
				}
				if (move.hasFoundNatoMove()) {
					
				}
				if (move.hasFoundPactMove()) {
					
				}
				if (move.hasFundDissidentsMove()) {
					dissidentsMap.put(move.getFundDissidentsMove().getProvinceId(), true);
				}
			}			
		}
		
		totalInfluenceMap.putAll(baseInfluenceMap);
		totalInfluenceMap.replaceAll((p, infl) -> infl + polInfluenceMap.getOrDefault(p, 0) + milInfluenceMap.getOrDefault(p, 0) + covInfluenceMap.getOrDefault(p, 0));
		dissidentsMap.forEach((p, dissidents) -> {
			if (dissidents) {stabilityModifierMap.compute(p, (q, mod) -> mod == null ? -1 : mod - 1 );}
		});
		
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
				.addAllProvinces(this.state.getProvincesList());
		
		for (final Province.Builder province : nextStateBuilder.getProvincesBuilderList()) {
			province.setDissidents(dissidentsMap.get(province.getId()));
			province.setInfluence(totalInfluenceMap.get(province.getId()));
		}
		
		nextStateBuilder.getUsaBuilder().getInfluenceStoreBuilder()
		    .setPolitical(polStoreMap.get(Player.USA) + basePolIncomeMap.get(Player.USA))
		    .setMilitary(milStoreMap.get(Player.USA) + baseMilIncomeMap.get(Player.USA))
		    .setCovert(covStoreMap.get(Player.USA) + baseCovIncomeMap.get(Player.USA));
		nextStateBuilder.getUssrBuilder().getInfluenceStoreBuilder()
			.setPolitical(polStoreMap.get(Player.USSR) + basePolIncomeMap.get(Player.USSR))
			.setMilitary(milStoreMap.get(Player.USSR) + baseMilIncomeMap.get(Player.USSR))
			.setCovert(covStoreMap.get(Player.USSR) + baseCovIncomeMap.get(Player.USSR));
		
		this.nextState = nextStateBuilder.build();
		Logger.Info("Stability Mod: " + this.stabilityModifier);

	}
	
	public boolean CanMakeMove(Player player, Move move) {
		return true;
	}

}
