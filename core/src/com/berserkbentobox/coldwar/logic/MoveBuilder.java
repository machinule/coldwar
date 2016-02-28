package com.berserkbentobox.coldwar.logic;

import java.util.ArrayList;
import java.util.List;

import com.berserkbentobox.coldwar.Crisis.Choice;
import com.berserkbentobox.coldwar.Crisis.CrisisMechanicMoves;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.MoveOuterClass.ConflictOvertFundAttackerMove;
import com.berserkbentobox.coldwar.MoveOuterClass.ConflictOvertFundDefenderMove;
import com.berserkbentobox.coldwar.MoveOuterClass.EstablishBaseMove;
import com.berserkbentobox.coldwar.MoveOuterClass.Move;
import com.berserkbentobox.coldwar.MoveOuterClass.PoliticalPressureMove;
import com.berserkbentobox.coldwar.Province.CovertMove;
import com.berserkbentobox.coldwar.Province.DiplomacyMove;
import com.berserkbentobox.coldwar.Province.EstablishSpyNetworkMove;
import com.berserkbentobox.coldwar.Province.FundDissidentsMove;
import com.berserkbentobox.coldwar.Province.MilitaryMove;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.mechanics.province.Province;
import com.berserkbentobox.coldwar.logic.mechanics.province.ProvinceMechanic;

public class MoveBuilder {

	private ComputedGameState computedState;
	private final MoveList.Builder moves;
	private final GameState state;
	private final Player player;
	private final MechanicSettings settings;
	private GameStateManager stateManager;
	private Mechanics mechanics;
	private List<ProvinceId> acted;
	
	public MoveBuilder(Player player, GameState state, MechanicSettings settings) {
		this.player = player;
		this.state = state;
		this.moves = MoveList.newBuilder();
		this.settings = settings;
		this.stateManager = new GameStateManager(settings, state);
		this.acted = new ArrayList<ProvinceId>();
		this.computeState();
	}

	public Mechanics getMechanics() {
		return mechanics;
	}
	
	private void computeState() {
		if (this.player == Player.USA) {
			this.mechanics = this.stateManager.computeDeterministicMechanics(this.moves.build(), MoveList.getDefaultInstance());
			this.computedState = new ComputedGameState(this.state, this.moves.build(), MoveList.getDefaultInstance(), this.settings, this.mechanics);
		} else {
			this.mechanics = this.stateManager.computeDeterministicMechanics(MoveList.getDefaultInstance(), this.moves.build());
			this.computedState = new ComputedGameState(this.state, MoveList.getDefaultInstance(), this.moves.build(), this.settings, this.mechanics);
		}
	}
	
	public void EstablishBase(final ProvinceId id) {
		this.moves.addMoves(
				Move.newBuilder().setEstablishBaseMove(EstablishBaseMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Establishing " + player + " base in " + id);
		this.computeState();
	}
	
	public void PoliticalPressure(final ProvinceId id) {
		this.moves.addMoves(
				Move.newBuilder().setPoliticalPressureMove(PoliticalPressureMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Applying political pressure in " + id);
		this.computeState();
	}
	
	public void Coup(final ProvinceId id, final int magnitude) {
//		this.moves.addMoves(
//				Move.newBuilder().setCoupMove(CoupMove.newBuilder().setProvinceId(id).setMagnitude(magnitude)).build());
//		Logger.Dbg("Preparing coup in " + id + " with extra support " + magnitude);
//		this.computeState();
	}
	
	public void FundDefender(final ProvinceId id) {
		this.moves.addMoves(
				Move.newBuilder().setConflictOvertFundDefenderMove(ConflictOvertFundDefenderMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Sending military aid to defender in " + id);
		this.computeState();
	}
	
	public void FundAttacker(final ProvinceId id) {
		this.moves.addMoves(
				Move.newBuilder().setConflictOvertFundAttackerMove(ConflictOvertFundAttackerMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Sending military aid to attacker in " + id);
		this.computeState();
	}
	
	public final ComputedGameState getComputedGameState() {
		return this.computedState;
	}

	public int getInfluence(final ProvinceId provinceId) {
		return this.mechanics.getProvinces().getProvince(provinceId).getInfluence();
	}
	
	public Player getAlly(final ProvinceId provinceId) {
		return this.mechanics.getProvinces().getProvince(provinceId).getAlly();
	}
	
	public int getPolStore() {
		return this.getMechanics().getInfluenceStore().getPolitical(this.player);
	}
	
	public int getMilStore() {
		return this.getMechanics().getInfluenceStore().getMilitary(this.player);
	}
	
	public int getCovStore() {
		return this.getMechanics().getInfluenceStore().getCovert(this.player);
	}
	
	public int getHeat() {
		return this.computedState.heat;
	}
	
	public int getPartyUnity() {
		return this.computedState.partyUnity;
	}

	public int getPatriotism() {
		return this.computedState.patriotism;
	}
	
	public MoveList getMoveList() {
		return this.moves.build();
	}
	
	public Player getBaseOwner(final ProvinceId provinceId) {
		return this.computedState.bases.get(provinceId);
	}

	public void Undo() {
		this.moves.removeMoves(this.moves.getMovesCount());
		this.computeState();
	}

	public int getYear() {
		return this.computedState.year;
	}

	public int getStabilityModifier(ProvinceId provinceId) {
		return this.computedState.stabilityModifier.getOrDefault(provinceId, 0);
	}
	
	// Validation
	
	public boolean hasActed(ProvinceId id) {
		return acted.contains(id);
	}
	
	public boolean isValidDiplomacyMove(Player player, ProvinceId id){
		return !hasActed(id) && this.mechanics.getProvinces().isValidDiplomacyMove(player, id);
	}
	
	public boolean isValidMilitaryMove(Player player, ProvinceId id){
		return !hasActed(id) && this.mechanics.getProvinces().isValidMilitaryMove(player, id);
	}

	public boolean isValidCovertMove(Player player, ProvinceId id){
		return !hasActed(id) && this.mechanics.getProvinces().isValidCovertMove(player, id);
	}
	
	public boolean isValidFundDissidentsMove(Player player, ProvinceId id) {
		return !hasActed(id) && this.mechanics.getProvinces().isValidFundDissidentsMove(player, id);
	}
	
	public boolean isValidEstablishSpyNetworkMove(Player player, ProvinceId id) {
		return !hasActed(id) && this.mechanics.getProvinces().isValidEstablishSpyNetworkMove(player, id);
	}
	
	// Cost
	
	public int getDiplomacyMoveCost(Player player, ProvinceId id, int magnitude) {
		return this.mechanics.getProvinces().getDiplomacyMoveCost(player, id, magnitude);
	}

	public int getMilitaryMoveCost(Player player, ProvinceId id, int magnitude) {
		return this.mechanics.getProvinces().getMilitaryMoveCost(player, id, magnitude);
	}
	
	public int getCovertMoveCost(Player player, ProvinceId id, int magnitude) {
		return this.mechanics.getProvinces().getCovertMoveCost(player, id, magnitude);
	}
	
	public int getFundDissidentsMoveCost(Player player, ProvinceId id) {
		return this.mechanics.getProvinces().getFundDissidentsMoveCost(player, id);
	}
		
	public int getEstablishSpyNetworkMoveCost(Player player, ProvinceId id) {
		return this.mechanics.getProvinces().getEstablishSpyNetworkMoveCost(player, id);
	}
	
	// Provinces
	
	public void influenceDip(final ProvinceId id, int magnitude) {
		acted.add(id);
		Move.Builder move = Move.newBuilder();
		DiplomacyMove.Builder dipMove = DiplomacyMove.newBuilder();
		dipMove
			.setProvinceId(id)
			.setMagnitude(magnitude);
		move.getProvinceMechanicMovesBuilder().addDiplomacyMove(dipMove);
		this.moves.addMoves(move.build());
		this.computeState();
	}
	
	public void influenceMil(final ProvinceId id, int magnitude) {
		acted.add(id);
		Move.Builder move = Move.newBuilder();
		MilitaryMove.Builder milMove = MilitaryMove.newBuilder();
		milMove
			.setProvinceId(id)
			.setMagnitude(magnitude);
		move.getProvinceMechanicMovesBuilder().addMilitaryMove(milMove);
		this.moves.addMoves(move.build());
		this.computeState();
	}
	
	public void influenceCov(final ProvinceId id, int magnitude) {
		acted.add(id);
		Move.Builder move = Move.newBuilder();
		CovertMove.Builder covMove = CovertMove.newBuilder();
		covMove
			.setProvinceId(id)
			.setMagnitude(magnitude);
		move.getProvinceMechanicMovesBuilder().addCovertMove(covMove);
		this.moves.addMoves(move.build());
		this.computeState();
	}
	
	public void fundDissidents(final ProvinceId id) {
		acted.add(id);
		Move.Builder move = Move.newBuilder();
		FundDissidentsMove.Builder dissMove = FundDissidentsMove.newBuilder();
		dissMove
			.setProvinceId(id);
		move.getProvinceMechanicMovesBuilder().addFundDissidentsMove(dissMove);
		this.moves.addMoves(move.build());
		this.computeState();
	}
	
	public void establishSpyNetwork(final ProvinceId id) {
		acted.add(id);
		Move.Builder move = Move.newBuilder();
		EstablishSpyNetworkMove.Builder spyMove = EstablishSpyNetworkMove.newBuilder();
		spyMove
			.setProvinceId(id);
		move.getProvinceMechanicMovesBuilder().addEstablishSpyNetworkMove(spyMove);
		this.moves.addMoves(move.build());
		this.computeState();
	}
	
	// Elections
	
	public void addNominateMove(String name) {
		Move.Builder move = Move.newBuilder();
		move.getSuperpowerMechanicMovesBuilder().getNominateMoveBuilder()
			.setLeaderId(name);
		this.moves.addMoves(move.build());
		this.computeState();
	}
	
	// Technology

	public void addResearchMove(String id) {
		Move.Builder move = Move.newBuilder();
		move.getTechnologyMechanicMovesBuilder().getResearchMoveBuilder()
			.setTechnologyGroupId(id)
			.setMagnitude(1);
		this.moves.addMoves(move.build());
		this.computeState();
	}
	
	// Treaty

	public void addDeescalateMove() {
		// TODO Auto-generated method stub
		Move.Builder move = Move.newBuilder();
		move.getTreatyMechanicMovesBuilder().getDeescalateMoveBuilder();
		this.moves.addMoves(move.build());
		this.computeState();
	}

	// Crises
	
	public void addCrisisMove(String choice) {
		this.moves.addMoves(
				Move.newBuilder().setCrisisMechanicMoves(CrisisMechanicMoves.newBuilder()
						.setChoice(Choice.newBuilder().setName(choice))));
		this.computeState();
	}	
}
