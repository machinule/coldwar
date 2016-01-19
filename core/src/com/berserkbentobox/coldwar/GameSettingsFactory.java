package com.berserkbentobox.coldwar;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.berserkbentobox.coldwar.EventOuterClass.EventSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.Heat.HeatSettings;
import com.berserkbentobox.coldwar.LeaderOuterClass.LeaderSettings;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveSettings;
import com.berserkbentobox.coldwar.PolicyOuterClass.PolicySettings;
import com.berserkbentobox.coldwar.Software.SoftwareSettings;
import com.berserkbentobox.coldwar.Superpower.SuperpowerGameSettings;
import com.berserkbentobox.coldwar.Technology.TechnologySettings;
import com.berserkbentobox.coldwar.TreatyOuterClass.TreatySettings;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;

public class GameSettingsFactory {
	private String settingsPath = "";
	
	public GameSettingsFactory(String settingsPath) {
		this.settingsPath = settingsPath;
	}
	
	public GameSettings load(String gameName) {
		GameSettings.Builder settings = GameSettings.newBuilder();
		try {
			this.loadSoftware(settings.getSoftwareSettingsBuilder());
			this.loadPolicies(gameName, settings.getPolicySettingsBuilder());
			this.loadLeaders(gameName, settings.getLeaderSettingsBuilder());
			this.loadTreaties(gameName, settings.getTreatySettingsBuilder());
			this.loadSuperpowers(gameName, settings.getSuperpowerSettingsBuilder());
			this.loadMoves(gameName, settings.getMoveSettingsBuilder());
			this.loadEvents(gameName, settings.getEventSettingsBuilder());
			this.loadTechnologies(gameName, settings.getTechnologySettingsBuilder());
			this.loadHeat(gameName, settings.getHeatSettingsBuilder());
			this.loadProvinces(gameName, settings.getProvinceSettingsBuilder());
		} catch (ParseException e) {
			Logger.Info(e.toString());
		}
		Logger.Info(settings.toString());
		return settings.build();
	}
	
	private static String joinPath(String a, String b) {
		return new File(a, b).toString();
	}
	
	protected String loadFile(String fileName) {
		return new String(Gdx.files.internal(joinPath(this.settingsPath, fileName)).readString());
	}
	
	protected void loadSoftware(SoftwareSettings.Builder settings) {
		settings.setSeedInit(0);
		settings.setVersion("0.0.1");
	}
	
	protected void loadPolicies(String gameName, PolicySettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "policy_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadLeaders(String gameName, LeaderSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "leader_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadTreaties(String gameName, TreatySettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "treaty_settings.proto.txt"));        
		TextFormat.merge(input, settings);
	}
	
	protected void loadSuperpowers(String gameName, SuperpowerGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "superpower_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadMoves(String gameName, MoveSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "move_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadEvents(String gameName, EventSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "event_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadTechnologies(String gameName, TechnologySettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "technology_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadHeat(String gameName, HeatSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "heat_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadProvinces(String gameName, ProvinceSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "province_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	public GameSettings newGameSettings() {
		return this.load("default");
	}
}
