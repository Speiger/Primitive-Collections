package speiger.src.builder.modules;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import speiger.src.builder.processor.TemplateProcess;
import speiger.src.builder.processor.TemplateProcessor;

@SuppressWarnings("javadoc")
public class ModuleBuilder extends TemplateProcessor
{
	List<ModulePackage> simplePackages = new ArrayList<>();
	List<ModulePackage> biPackages = new ArrayList<>();
	List<ModulePackage> enumPackages = new ArrayList<>();
	Map<String, RequiredType> requirements = new HashMap<>();
	
	public ModuleBuilder(boolean silencedSuccess, Path sourceFolder, Path outputFolder, Path dataFolder)
	{
		super(silencedSuccess, sourceFolder, outputFolder, dataFolder);
	}
	
	@Override
	protected void init() 
	{
		prepPackages();
		//Init Modules here
		finishPackages();
	}
	
	public void addModule(BaseModule module)
	{
		biPackages.forEach(module::init);
		module.cleanup();
	}
	
	private void finishPackages() 
	{
		biPackages.forEach(ModulePackage::finish);
	}
	
	private void prepPackages()
	{
		for(ModulePackage entry : ModulePackage.createPackages())
		{
			entry.setRequirements(requirements::put);
			biPackages.add(entry);
			if(entry.isSame()) simplePackages.add(entry);
			if(entry.isEnumValid()) enumPackages.add(entry);
		}		
	}
	
	@Override
	public void createProcesses(String fileName, Consumer<TemplateProcess> process)
	{
		List<ModulePackage> packages = getPackagesByRequirement(requirements.get(fileName));
		for(int i = 0,m=packages.size();i<m;i++)
		{
			packages.get(i).process(fileName, process);
		}
	}
	
	protected List<ModulePackage> getPackagesByRequirement(RequiredType type) {
		if(type == null) return simplePackages;
		if(type == RequiredType.BI_CLASS) return biPackages;
		if(type == RequiredType.ENUM) return enumPackages;
		return Collections.emptyList();
	}
	
	@Override
	protected boolean isFileValid(Path fileName) { return true; }
	@Override
	protected boolean relativePackages() { return true; }
	@Override
	protected boolean debugUnusedMappers() { return false; }
}