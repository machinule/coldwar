package com.berserkbentobox.coldwar;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.EventOuterClass.EventSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.Heat.HeatSettings;
import com.berserkbentobox.coldwar.LeaderOuterClass.Leader.Culture;
import com.berserkbentobox.coldwar.LeaderOuterClass.LeaderSettings;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveSettings;
import com.berserkbentobox.coldwar.PolicyOuterClass.PolicySettings;
import com.berserkbentobox.coldwar.ProvinceOuterClass.Conflict;
import com.berserkbentobox.coldwar.ProvinceOuterClass.Province;
import com.berserkbentobox.coldwar.Software.SoftwareSettings;
import com.berserkbentobox.coldwar.Superpower.SuperpowerSettings;
import com.berserkbentobox.coldwar.Technology.TechnologySettings;
import com.berserkbentobox.coldwar.TreatyOuterClass.TreatySettings;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;

public class GameSettingsFactory {
	private String settingsPath = "";
	public GameSettingsFactory(String settingsPath) {
		this.settingsPath = settingsPath;
	}
//	optional SoftwareSettings software_settings = 1;
//	optional PolicySettings policy_settings = 2;
//	optional LeaderSettings leader_settings = 3;
//	optional TreatySettings treaty_settings = 4;
//	optional SuperpowerSettings superpower_settings = 5;
//	optional MoveSettings move_settings = 6;
//	optional EventSettings event_settings = 7;
//	optional TechnologySettings technology_settings = 8;
//	optional HeatSettings heat_settings = 9;
//	optional ProvinceSettings province_settings = 10;
	public GameSettings Load(String gameName) {
		GameSettings.Builder settings = GameSettings.newBuilder();
		try {
			this.LoadSoftware(settings.getSoftwareSettingsBuilder());
			this.LoadPolicies(gameName, settings.getPolicySettingsBuilder());
			this.LoadLeaders(gameName, settings.getLeaderSettingsBuilder());
			this.LoadTreaties(gameName, settings.getTreatySettingsBuilder());
			this.LoadSuperpowers(gameName, settings.getSuperpowerSettingsBuilder());
			this.LoadMoves(gameName, settings.getMoveSettingsBuilder());
			this.LoadEvents(gameName, settings.getEventSettingsBuilder());
			this.LoadTechnologies(gameName, settings.getTechnologySettingsBuilder());
			this.LoadHeat(gameName, settings.getHeatSettingsBuilder());
			this.LoadProvinces(gameName, settings.getProvinceSettingsBuilder());
		} catch (ParseException e) {
			Logger.Info(e.toString());
		}
		Logger.Info(settings.toString());
		return settings.build();
	}
	private static String JoinPath(String a, String b) {
		return new File(a, b).toString();
	}
	protected String LoadFile(String fileName) {
		return new String(Gdx.files.internal(JoinPath(this.settingsPath, fileName)).readString());
	}
	protected void LoadSoftware(SoftwareSettings.Builder settings) {
		settings.setSeedInit(0);
		settings.setVersion("0.0.1");
	}
	protected void LoadPolicies(String gameName, PolicySettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "policy_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	protected void LoadLeaders(String gameName, LeaderSettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "leader_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	protected void LoadTreaties(String gameName, TreatySettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "treaty_settings.proto.txt"));        
		TextFormat.merge(input, settings);
	}
	protected void LoadSuperpowers(String gameName, SuperpowerSettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "superpower_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	protected void LoadMoves(String gameName, MoveSettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "move_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	protected void LoadEvents(String gameName, EventSettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "event_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	protected void LoadTechnologies(String gameName, TechnologySettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "technology_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	protected void LoadHeat(String gameName, HeatSettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "heat_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	protected void LoadProvinces(String gameName, ProvinceSettings.Builder settings) throws ParseException {
		String input = LoadFile(JoinPath(gameName, "province_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	public GameSettings New() {
		return this.Load("default");
	}
}
