package com.berserkbentobox.coldwar;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.berserkbentobox.coldwar.EventOuterClass.EventGameSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceGameSettings;
import com.berserkbentobox.coldwar.Heat.HeatGameSettings;
import com.berserkbentobox.coldwar.Leader.LeaderGameSettings;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveGameSettings;
import com.berserkbentobox.coldwar.Policy.PolicyGameSettings;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomGameSettings;
import com.berserkbentobox.coldwar.Superpower.SuperpowerGameSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyGameSettings;
import com.berserkbentobox.coldwar.Treaty.TreatyGameSettings;
import com.berserkbentobox.coldwar.Version.VersionGameSettings;
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
			this.loadVersion(settings.getVersionSettingsBuilder());
			this.loadPolicies(gameName, settings.getPolicySettingsBuilder());
			this.loadLeaders(gameName, settings.getLeaderSettingsBuilder());
			this.loadTreaties(gameName, settings.getTreatySettingsBuilder());
			this.loadSuperpowers(gameName, settings.getSuperpowerSettingsBuilder());
			this.loadMoves(gameName, settings.getMoveSettingsBuilder());
			this.loadEvents(gameName, settings.getEventSettingsBuilder());
			this.loadTechnologies(gameName, settings.getTechnologySettingsBuilder());
			this.loadHeat(gameName, settings.getHeatSettingsBuilder());
			this.loadProvinces(gameName, settings.getProvinceSettingsBuilder());
			this.loadPseudorandom(settings.getPseudorandomSettingsBuilder());
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
	
	protected void loadVersion(VersionGameSettings.Builder settings) {
		settings.setVersion("0.0.1");
	}
	
	protected void loadPseudorandom(PseudorandomGameSettings.Builder settings) {
		settings.setInitSeed(0);
	}
	
	protected void loadPolicies(String gameName, PolicyGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "policy_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadLeaders(String gameName, LeaderGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "leader_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadTreaties(String gameName, TreatyGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "treaty_settings.proto.txt"));        
		TextFormat.merge(input, settings);
	}
	
	protected void loadSuperpowers(String gameName, SuperpowerGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "superpower_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadMoves(String gameName, MoveGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "move_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadEvents(String gameName, EventGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "event_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadTechnologies(String gameName, TechnologyGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "technology_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadHeat(String gameName, HeatGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "heat_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadProvinces(String gameName, ProvinceGameSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "province_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	public GameSettings newGameSettings() {
		return this.load("default");
	}
}
