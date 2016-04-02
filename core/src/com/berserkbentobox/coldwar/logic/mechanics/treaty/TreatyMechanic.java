package com.berserkbentobox.coldwar.logic.mechanics.treaty;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.TreatyOuterClass;
import com.berserkbentobox.coldwar.TreatyOuterClass.AmendMove;
import com.berserkbentobox.coldwar.TreatyOuterClass.RatifyMove;
import com.berserkbentobox.coldwar.TreatyOuterClass.Term;
import com.berserkbentobox.coldwar.TreatyOuterClass.TreatyAmendment;
import com.berserkbentobox.coldwar.TreatyOuterClass.TreatyMechanicMove;
import com.berserkbentobox.coldwar.TreatyOuterClass.TreatyMechanicSettings;
import com.berserkbentobox.coldwar.TreatyOuterClass.TreatyMechanicState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;

public class TreatyMechanic extends Mechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private TreatyMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getTreatySettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public TreatyMechanicSettings getSettings() {
			return this.settings;
		}
			
		public TreatyMechanicState initialState(PseudorandomMechanic r) {
			TreatyMechanicState.Builder state = TreatyMechanicState.newBuilder();
			return state.build();
		}
	}
	
	private Settings settings;
	private TreatyMechanicState.Builder state;
	
	public TreatyMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getTreatyState().toBuilder();
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
	
	// Move builder helpers.
	
	public TreatyMechanicMove makeRatifyMove() {
		return TreatyMechanicMove.newBuilder()
				.setRatifyMove(RatifyMove.newBuilder().build())
				.build();
	}

	public TreatyMechanicMove makeAmendMove(AmendMove.Builder move) {
		return TreatyMechanicMove.newBuilder()
				.setAmendMove(move.build())
				.build();
	}
	
	// Logic
	
	public void makeMove(Player player, TreatyMechanicMove move) {
		if (move.hasRatifyMove()) {
			makeRatifyMove(player, move.getRatifyMove());
		}
		if (move.hasAmendMove()) {
			makeAmendMove(player, move.getAmendMove());
		}
	}

	boolean usaRatified = false;
	boolean ussrRatified = false;
	TreatyAmendment usaAmendment = null;
	TreatyAmendment ussrAmendment = null;
	
	public void makeRatifyMove(Player player, RatifyMove move) {
		if (player == Player.USA) {
			usaRatified = true;
		} else {
			ussrRatified = true;
		}
	}
	
	public void makeAmendMove(Player player, AmendMove move) {
		if (player == Player.USA) {
			usaAmendment = move.getTreatyDelta();
		} else {
			ussrAmendment = move.getTreatyDelta();
		}
	}

	public void resolveTreaty() {
		if (usaRatified && ussrRatified) {
			this.ratify();
		} else if (usaRatified) {
			this.halfRatify(Player.USA);
			this.halfAmend(Player.USSR);
		} else if (ussrRatified) {
			this.halfRatify(Player.USSR);			
			this.halfAmend(Player.USA);
		} else {
			this.amend(usaAmendment, ussrAmendment);
		}
	}

	private void halfAmend(Player player) {
		if (player == Player.USA) {
			this.amend(this.usaAmendment, TreatyAmendment.newBuilder().build());
		} else {
			this.amend(TreatyAmendment.newBuilder().build(), this.ussrAmendment);
		}
	}

	private void amend(TreatyAmendment usa, TreatyAmendment ussr) {
		SortedSet<Integer> toRemove = new TreeSet<Integer>(Collections.reverseOrder());
		toRemove.addAll(usa.getRemovedTermIndexList());
		toRemove.addAll(ussr.getRemovedTermIndexList());
		for (int i : toRemove) {
			this.state.getProposedTreatyBuilder().removeTerm(i);
		}

		Set<Integer> toAdd = new HashSet<Integer>();
		toRemove.addAll(usa.getAddedNeutralTermIndexList());
		toRemove.addAll(ussr.getAddedNeutralTermIndexList());
		for (int i : toAdd) {
			this.state.getProposedTreatyBuilder().addTerm(this.state.getAvailableNeutralTerms().getTerm(i));
		}		
		
		for (int i : usa.getAddedUsaTermIndexList()) {
			this.state.getProposedTreatyBuilder().addTerm(this.state.getAvailableUsaTerms().getTerm(i));
		}
		
		for (int i : ussr.getAddedUssrTermIndexList()) {
			this.state.getProposedTreatyBuilder().addTerm(this.state.getAvailableUssrTerms().getTerm(i));
		}
	}

	private void halfRatify(Player player) {
		this.getMechanics().getInfluenceStore().addPOL(player, 5);
	}

	private void ratify() {
		for (TreatyOuterClass.Term term : state.getProposedTreaty().getTermList()) {
			this.enforceTerm(term);
		}
		state.addRatifiedTreaty(state.getProposedTreaty());
	}

	private void enforceTerm(Term term) {
		
	}

}
