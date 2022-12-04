package speiger.src.builder;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

import speiger.src.builder.modules.BaseModule;

@SuppressWarnings("javadoc")
public class SettingsManager
{
	boolean loaded;
	JsonObject data = new JsonObject();
	Set<String> moduleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
	
	public boolean isModuleEnabled(BaseModule base, ClassType keyType, ClassType valueType) {
		if(!loaded) return true;
		if(!isEnabled(data, base.getModuleName())) return false;
		JsonObject result = getObject(data, keyType.getClassPath(), false);
		if(!isEnabled(result, "enabled")) return false;
		if(base.isBiModule()) {
			result = getObject(result, valueType.getClassPath(), false);
			if(!isEnabled(result, "enabled")) return false;
		}
		result = getObject(result, base.getModuleName(), false);
		return result.size() <= 0 || isEnabled(result, "enabled");		
	}
	
	public boolean isModuleEnabled(BaseModule base, ClassType keyType, ClassType valueType, String entry)
	{
		if(!loaded) return true;
		if(!isEnabled(data, base.getModuleName())) return false;
		JsonObject result = getObject(data, keyType.getClassPath(), false);
		if(!isEnabled(result, "enabled")) return false;
		if(base.isBiModule()) {
			result = getObject(result, valueType.getClassPath(), false);
			if(!isEnabled(result, "enabled")) return false;
		}
		result = getObject(result, base.getModuleName(), false);
		return result.size() <= 0 || (isEnabled(result, "enabled") && isEnabled(result, entry));
	}
	
	public void addModule(BaseModule module) {
		if(loaded) return;
		String moduleName = module.getModuleName();
		moduleNames.add(moduleName);
		data.addProperty(moduleName, true);
		if(module.isBiModule()) {
			for(ClassType keyType : ModulePackage.TYPE) {
				if(keyType == ClassType.BOOLEAN) continue;
				for(ClassType valueType : ModulePackage.TYPE) {
					JsonObject obj = new JsonObject();
					obj.addProperty("enabled", true);
					for(String key : module.getModuleKeys(keyType, valueType)) {
						obj.addProperty(key, true);
					}
					addModule(keyType, valueType, true, moduleName, obj);
				}
			}
			return;
		}
		for(ClassType keyType : ModulePackage.TYPE) {
			JsonObject obj = new JsonObject();
			obj.addProperty("enabled", true);
			for(String key : module.getModuleKeys(keyType, keyType)) {
				obj.addProperty(key, true);
			}
			addModule(keyType, keyType, false, moduleName, obj);			
		}
	}
	
	public void load() {
		try(BufferedReader reader = Files.newBufferedReader(Paths.get("ModulSettings.json"))) {
			data = JsonParser.parseReader(reader).getAsJsonObject();
			loaded = true;
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public void save() {
		data.asMap().keySet().removeAll(moduleNames);
		JsonObject result = new JsonObject();
		for(String s : moduleNames) {
			result.addProperty(s, true);
		}
		result.asMap().putAll(data.asMap());
		
		try(JsonWriter writer = new JsonWriter(Files.newBufferedWriter(Paths.get("ModulSettings.json")))) {
			writer.setIndent("\t");
			Streams.write(result, writer);
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	private void addModule(ClassType keyType, ClassType valueType, boolean bi, String moduleName, JsonObject obj) {
		JsonObject result = getObject(data, keyType.getClassPath(), true);
		if(bi) {
			result = getObject(result, valueType.getClassPath(), true);
		}
		result.add(moduleName, obj);
	}
	
	private JsonObject getObject(JsonObject data, String name, boolean create) {
		JsonObject obj = data.getAsJsonObject(name);
		if(obj == null) {
			obj = new JsonObject();
			data.add(name, obj);
			if(create) obj.addProperty("enabled", true);
		}
		return obj;
	}
	
	private boolean isEnabled(JsonObject obj, String key) {
		if(obj.has(key)) return obj.getAsJsonPrimitive(key).getAsBoolean();
		return true;
	}
}
