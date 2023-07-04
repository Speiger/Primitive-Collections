package speiger.src.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

import speiger.src.builder.dependency.IDependency;
import speiger.src.builder.modules.BaseModule;

@SuppressWarnings("javadoc")
public class SettingsManager
{
	boolean loaded;
	JsonObject data = new JsonObject();
	Set<String> moduleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
	Set<IDependency> allDependencies = new LinkedHashSet<>();
	
	public void resolve() {
		boolean valid = true;
		for(int i = 0;i<10000 && valid;i++) {
			boolean result = false;
			for(IDependency entry : allDependencies) {
				result |= entry.resolveDependencies();
			}
			valid = result;
		}
		if(valid) throw new RuntimeException("Couldn't resolve dependencies!");
	}
	
	public void addModule(BaseModule module) {
		if(loaded) {
			if(module.isBiModule()) {
				for(ClassType keyType : ModulePackage.TYPE) {
					for(ClassType valueType : ModulePackage.TYPE) {
						if(!module.isModuleValid(keyType, valueType)) continue;
						for(IDependency dependency : module.getDependencies(keyType, valueType)) {
							dependency.load(data, keyType, valueType);
							allDependencies.add(dependency);
						}
					}
				}
				return;
			}
			for(ClassType keyType : ModulePackage.TYPE) {
				if(!module.isModuleValid(keyType, keyType)) continue;
				for(IDependency dependency : module.getDependencies(keyType, keyType)) {
					dependency.load(data, keyType, keyType);
					allDependencies.add(dependency);
				}
			}
			return;
		}
		String moduleName = module.getModuleName();
		moduleNames.add(moduleName);
		data.addProperty(moduleName, true);
		if(module.isBiModule()) {
			for(ClassType keyType : ModulePackage.TYPE) {
				for(ClassType valueType : ModulePackage.TYPE) {
					if(!module.isModuleValid(keyType, valueType)) continue;
					JsonObject obj = new JsonObject();
					for(IDependency dependency : module.getDependencies(keyType, valueType)) {
						String key = dependency.getName();
						if(key != null) obj.addProperty(key, true);
						dependency.setLoaded();
					}
					addModule(keyType, valueType, true, moduleName, obj);
				}
			}
			return;
		}
		for(ClassType keyType : ModulePackage.TYPE) {
			if(!module.isModuleValid(keyType, keyType)) continue;
			JsonObject obj = new JsonObject();
			for(IDependency dependency : module.getDependencies(keyType, keyType)) {
				String key = dependency.getName();
				if(key != null) obj.addProperty(key, true);
				dependency.setLoaded();
			}
			addModule(keyType, keyType, false, moduleName, obj);			
		}
	}
	
	public void printModuleSettings(List<BaseModule> modules) {
		JsonObject data = new JsonObject();
		for(BaseModule module : modules) {
			String moduleName = module.getModuleName();
			if(module.isBiModule()) {
				for(ClassType keyType : ModulePackage.TYPE) {
					for(ClassType valueType : ModulePackage.TYPE) {
						if(!module.isModuleValid(keyType, valueType)) continue;
						JsonObject obj = new JsonObject();
						for(IDependency dependency : module.getDependencies(keyType, valueType)) {
							String key = dependency.getName();
							if(key != null) obj.addProperty(key, dependency.getState(keyType, valueType).getJsonResult());
						}
						addModule(data, keyType, valueType, true, moduleName, obj);
					}
				}
				continue;
			}
			for(ClassType keyType : ModulePackage.TYPE) {
				if(!module.isModuleValid(keyType, keyType)) continue;
				JsonObject obj = new JsonObject();
				for(IDependency dependency : module.getDependencies(keyType, keyType)) {
					String key = dependency.getName();
					if(key != null) obj.addProperty(key, dependency.getState(keyType, keyType).getJsonResult());
				}
				addModule(data, keyType, keyType, false, moduleName, obj);			
			}
		}
		try {
			System.out.println();
			JsonWriter writer = new JsonWriter(new OutputStreamWriter(System.out));
			writer.setIndent("\t");
			Streams.write(data, writer);
			writer.flush();
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
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
	
	private void addModule(JsonObject data, ClassType keyType, ClassType valueType, boolean bi, String moduleName, JsonObject obj) {
		JsonObject result = getObject(data, keyType.getClassPath(), true);
		if(bi) {
			result = getObject(result, valueType.getClassPath(), true);
		}
		result.add(moduleName, obj);
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
			if(create) obj.addProperty("Enabled", true);
		}
		return obj;
	}
}
