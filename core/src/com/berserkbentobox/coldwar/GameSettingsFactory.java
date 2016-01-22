package com.berserkbentobox.coldwar;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.berserkbentobox.coldwar.EventOuterClass.EventMechanicSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceMechanicSettings;
import com.berserkbentobox.coldwar.Heat.HeatMechanicSettings;
import com.berserkbentobox.coldwar.Leader.LeaderMechanicSettings;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveMechanicSettings;
import com.berserkbentobox.coldwar.Policy.PolicyMechanicSettings;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicSettings;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicSettings;
import com.berserkbentobox.coldwar.Treaty.TreatyMechanicSettings;
import com.berserkbentobox.coldwar.Version.VersionMechanicSettings;
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
	
	protected void loadVersion(VersionMechanicSettings.Builder settings) {
		settings.setVersion("0.0.1");
	}
	
	protected void loadPseudorandom(PseudorandomMechanicSettings.Builder settings) {
		settings.setInitSeed(0);
	}
	
	protected void loadPolicies(String gameName, PolicyMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "policy_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadLeaders(String gameName, LeaderMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "leader_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadTreaties(String gameName, TreatyMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "treaty_settings.proto.txt"));        
		TextFormat.merge(input, settings);
	}
	
	protected void loadSuperpowers(String gameName, SuperpowerMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "superpower_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadMoves(String gameName, MoveMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "move_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadEvents(String gameName, EventMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "event_settings.proto.txt"));        
	    TextFormat.merge(input, settings);
	}
	
	protected void loadTechnologies(String gameName, TechnologyMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "technology_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadHeat(String gameName, HeatMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "heat_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	protected void loadProvinces(String gameName, ProvinceMechanicSettings.Builder settings) throws ParseException {
		String input = loadFile(joinPath(gameName, "province_settings.proto.txt"));        
        TextFormat.merge(input, settings);
	}
	
	public GameSettings newGameSettings() {
		return this.load("default");
	}
}
