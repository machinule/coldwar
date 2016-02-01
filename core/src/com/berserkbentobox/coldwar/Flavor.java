package com.berserkbentobox.coldwar;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class Flavor {

	private String localization;
	Map<String, Map<String, String>> flavors;

	public Flavor(String localization) {
		this.localization = localization;
		this.flavors = new HashMap<String, Map<String, String>>();
	}
	
	protected String loadFile(String filePath) {
		return new String(Gdx.files.internal(filePath).readString());
	}
	
	private static String joinPath(String a, String b) {
		return new File(a, b).toString();
	}
	
	public void loadFlavor(String id, String dir, String fileName) {
		Map<String, String> flavor = this.flavors.getOrDefault(id, new HashMap<String, String>());
		String contents = loadFile(joinPath(joinPath(dir, this.localization), fileName));
		for (String line : contents.split("\n:")) {
			if (line.startsWith(":")) {
				line = line.substring(1);
			}
			String[] parts = line.split(":");
			flavor.put(parts[0], parts[1]);
		}
		this.flavors.put(id, flavor);
	}
	
	public String get(String id, String name) {
		return this.flavors.getOrDefault(id, new HashMap<String, String>()).getOrDefault(name, "(404) Flavor Not Found");
	}
}
