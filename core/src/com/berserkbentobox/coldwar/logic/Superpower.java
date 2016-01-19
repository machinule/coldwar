package com.berserkbentobox.coldwar.logic;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.InfluenceStore.InfluenceStoreSettingsOrBuilder;
import com.berserkbentobox.coldwar.InfluenceStore.InfluenceStoreState;
import com.berserkbentobox.coldwar.Superpower.SuperpowerSettingsOrBuilder;
import com.berserkbentobox.coldwar.Superpower.SuperpowerState;
import com.berserkbentobox.coldwar.Superpower.SuperpowerStateOrBuilder;
import com.berserkbentobox.coldwar.Superpower.UsaLeader;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderSettingsOrBuilder;
import com.berserkbentobox.coldwar.Superpower.UssrLeader;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderSettingsOrBuilder;

public class Superpower {

	static public class SettingsValidationResult {
		public boolean ok;
		static public enum Reason {
		}
		public Reason reason;
		public SettingsValidationResult(boolean ok, Reason reason) {
			this.ok = ok;
			this.reason = reason;
		}
	}
	
	static public class InfluenceStore {
		static InfluenceStoreState.Builder buildInitialState(InfluenceStoreSettingsOrBuilder settings) {
			InfluenceStoreState.Builder state = InfluenceStoreState.newBuilder();
			state
				.setPolitical(settings.getPoliticalStoreInit())
				.setMilitary(settings.getMilitaryStoreInit())
				.setCovert(settings.getCovertStoreInit());
			return state;
		}
	}
	
	static public class UsaLeaderUtil {
		static UsaLeader.Builder buildInitialState(UsaLeaderSettingsOrBuilder settings) {
			UsaLeader.Builder state = UsaLeader.newBuilder();
			state
				.setId(settings.getId())
				.setAvailable(settings.getInitAvailable())
				.setNumTermsAsPresident(settings.getInitNumTermsAsPresident())
				.setNumTermsAsVicePresident(settings.getInitNumTermsAsVicePresident());
			return state;
		}
	}
	
	static public class UssrLeaderUtil {
		static UssrLeader.Builder buildInitialState(UssrLeaderSettingsOrBuilder settings) {
			UssrLeader.Builder state = UssrLeader.newBuilder();
			state
				.setId(settings.getId())
				.setAvailable(settings.getInitAvailable())
				.setPartySupport(settings.getInitPartySupport());
			return state;
		}
	}
	
	static SettingsValidationResult validateSettings(SuperpowerSettingsOrBuilder settings) {
		return new SettingsValidationResult(true, null);
	}

	static public class StateValidationResult {
		public boolean ok;
		static public enum Reason {
		}
		public Reason reason;
		public StateValidationResult(boolean ok, Reason reason) {
			this.ok = ok;
			this.reason = reason;
		}
	}
	
	static StateValidationResult validateState(SuperpowerStateOrBuilder state) {
		return new StateValidationResult(true, null);
	}
	
	static SuperpowerState.Builder buildInitialState(SuperpowerSettingsOrBuilder settings) {
		SuperpowerState.Builder state = SuperpowerState.newBuilder();
		state
			.getUsaStateBuilder()
				.setPresident(settings.getUsaSettings().getPresidencySettings().getInitPresident())
				.setVicePresident(settings.getUsaSettings().getPresidencySettings().getInitVicePresident())
				.setPatriotism(settings.getUsaSettings().getInitPatriotism())
				.addAllLeader(settings.getUsaSettings().getLeaderSettingsList().stream().map(s -> UsaLeaderUtil.buildInitialState(s).build()).collect(Collectors.toList()))
				.setInfluenceStore(InfluenceStore.buildInitialState(settings.getUsaSettings().getInfluenceStoreSettings()));

		state
			.getUssrStateBuilder()
				.setPremier(settings.getUssrSettings().getSecretariatSettings().getInitPremier())
				.addAllTroika(settings.getUssrSettings().getSecretariatSettings().getInitTroikaList())
				.addAllLeader(settings.getUssrSettings().getLeaderSettingsList().stream().map(s -> UssrLeaderUtil.buildInitialState(s).build()).collect(Collectors.toList()))
				.setPartyUnity(settings.getUssrSettings().getInitPartyUnity())
				.setInfluenceStore(InfluenceStore.buildInitialState(settings.getUssrSettings().getInfluenceStoreSettings()));
		return state;
	}
}
