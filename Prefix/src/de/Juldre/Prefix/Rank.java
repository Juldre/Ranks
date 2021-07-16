package de.Juldre.Prefix;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Rank")
public class Rank implements ConfigurationSerializable {
	public String name;
	public String prefix;
	public String permission;

	public Rank() {}
	
	public Rank(String name, String permission, String prefix) {
		this.name = name;
		this.prefix = prefix;
		this.permission = permission;
	}

	@Override
	public Map<String, Object> serialize() {
		return ObjectUtil.serialize(this);
	}

	public static Rank deserialize(Map<String, Object> args) {
		return (Rank) ObjectUtil.deserialize(args, Rank.class);
	}
}
