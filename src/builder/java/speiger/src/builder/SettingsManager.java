package speiger.src.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

import speiger.src.builder.dependencies.IDependency;
import speiger.src.builder.dependencies.IDependency.LoadingState;
import speiger.src.builder.modules.BaseModule;

@SuppressWarnings("javadoc")
public class SettingsManager
{
	boolean loaded;
	Map<String, LoadingState> parsedData = new TreeMap<>();
	JsonObject data = new JsonObject();
	Set<String> moduleNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
	Set<IDependency> allDependencies = new LinkedHashSet<>();
	
	public void resolve() {
		if(!loaded) return;
		Set<IDependency> roots = new LinkedHashSet<>();
		Set<IDependency> leafs = new LinkedHashSet<>();
		for(IDependency entry : allDependencies) {
			if(entry.isRoot()) {
				roots.add(entry);
			}
			if(entry.isLeaf()) {
				leafs.add(entry);
			}
		}
		/**
		 * This has to be 2 iteration passes.
		 * Due to Key Value Pairs, first pass does all initials keys, and the second pass processes the values.
		 * May require more passes but extremely unlikely
		 */
		for(int i = 0;i<2;i++) {
			for(ClassType keyType : ModulePackage.TYPE) {
				for(ClassType valueType : ModulePackage.TYPE) {
					for(IDependency entry : roots) {
						entry.resolveRequirements(keyType, valueType);
					}
				}
			}
		}
		
		List<String> errors = new ArrayList<>();
		for(ClassType keyType : ModulePackage.TYPE) {
			for(ClassType valueType : ModulePackage.TYPE) {
				for(IDependency entry : leafs) {
					entry.validateDependency(errors::add, keyType, valueType);
				}
			}
		}
		if(errors.size() > 0) {
			throw new IllegalStateException("Issues with dependencies found: "+String.join("\n", errors));
		}
	}
	
	public void addModule(BaseModule module) {
		if(loaded) {
			if(module.isBiModule()) {
				for(ClassType keyType : ModulePackage.TYPE) {
					for(ClassType valueType : ModulePackage.TYPE) {
						if(!module.isModuleValid(keyType, valueType)) continue;
						for(IDependency dependency : module.getDependencies(keyType, valueType)) {
							dependency.set(parsedData);
							allDependencies.add(dependency);
						}
					}
				}
				return;
			}
			for(ClassType keyType : ModulePackage.TYPE) {
				if(!module.isModuleValid(keyType, keyType)) continue;
				for(IDependency dependency : module.getDependencies(keyType, keyType)) {
					dependency.set(parsedData);
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
							if(key != null) obj.addProperty(key, dependency.isLoaded(keyType, valueType).getJsonResult());
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
					if(key != null) obj.addProperty(key, dependency.isLoaded(keyType, keyType).getJsonResult());
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
			IDependency.flatten("", false, data, parsedData);
			JsonElement element = data.get("Default");
			LoadingState.setOptionalResolver(LoadingState.of(element == null ? true : element.getAsBoolean()));
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
