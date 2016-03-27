package com.berserkbentobox.coldwar.logic.mechanics.event;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.EventOuterClass.Event;
import com.berserkbentobox.coldwar.EventOuterClass.EventMechanicSettings;
import com.berserkbentobox.coldwar.EventOuterClass.EventMechanicState;
import com.berserkbentobox.coldwar.EventOuterClass.ProvinceDissidentsSuppressedEvent;
import com.berserkbentobox.coldwar.EventOuterClass.ProvinceFauxPasEvent;
import com.berserkbentobox.coldwar.EventOuterClass.ProvinceRepublicEvent;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Province.ProvinceState;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.Status;

public class EventMechanic extends Mechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private EventMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getEventSettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public EventMechanicState initialState() {
			EventMechanicState.Builder state = EventMechanicState.newBuilder();
			return state.build();
		}
	}
	
	private Settings settings;
	private EventMechanicState.Builder state;

	public EventMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getEventState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public EventMechanicState buildState() {
		return this.state.build();
	}
		
	// Logic
	public void triggerEvents() {

		
//		// TODO: LeaderSpawn
//		// TODO: LeaderDeath
//		// TODO: ProvinceCoup
//		// TODO: ProvinceDemocracy
//		// TODO: ProvinceCommunism
//		// TODO: ProvinceAutocracy
//		// ProvinceRepublic
//		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
//			if (p.getGov() == Government.AUTOCRACY) {
//				if (happens.apply(this.state.getSettings().getEventSettings().getRandomProvinceRepublicChance())) {
//					p.setGov(Government.REPUBLIC);
//					nextStateBuilder.getTurnLogBuilder()
//						.addEvents(Event.newBuilder()
//							.setProvinceRepublic(ProvinceRepublicEvent.newBuilder()
//								.setProvinceId(p.getId())
//								.build())
//							.build());
//				}
//			}
//		}
//		// ProvinceFauxPas
//		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
//			if (p.getInfluence() != 0) {
//				if (happens.apply(this.state.getSettings().getEventSettings().getRandomProvinceFauxPasChance())) {
//					int sgn;
//				    if (p.getInfluence() < 0) {
//				    	sgn = -1;
//				    } else {
//				    	sgn = 1;
//				    }
//				    int mag = Math.min(Math.abs(p.getInfluence()), r.nextInt(2) + 1);
//				    p.setInfluence(sgn * (Math.abs(p.getInfluence()) - mag));
//					p.setGov(Government.REPUBLIC);
//					nextStateBuilder.getTurnLogBuilder()
//						.addEvents(Event.newBuilder()
//							.setProvinceFauxPas(ProvinceFauxPasEvent.newBuilder()
//								.setProvinceId(p.getId())
//								.setMagnitude(mag)
//								.build())
//							.build());
//				}
//			}
//		}
//		// ProvinceDissidentsSuppressed
//		for (ProvinceState.Builder p : nextStateBuilder.getProvinceStateBuilder().getProvinceStateBuilderList()) {
//			if (hasDissidents(p.getId())) {
//				int chance;
//				if (p.getGov() == Government.DEMOCRACY) {
//					chance = this.state.getSettings().getEventSettings().getRandomProvinceDemocracyDissidentsSuppressedChance();
//				} else {
//					chance = this.state.getSettings().getEventSettings().getRandomProvinceDefaultDissidentsSuppressedChance();
//				}
//				if (happens.apply(chance)) {
//					Logger.Vrb("Dissidents suppressed in " + p.getId());
//					p.setDissidents(Dissidents.getDefaultInstance());
//					nextStateBuilder.getTurnLogBuilder()
//						.addEvents(Event.newBuilder()
//							.setProvinceDissidentsSuppressed(ProvinceDissidentsSuppressedEvent.newBuilder()
//								.setProvinceId(p.getId())
//								.build())
//							.build());
//				}
//			}
//		}
//		// TODO: UsAllyDemocracy
//		// TODO: UssrAllyCommunism	
	}
}
