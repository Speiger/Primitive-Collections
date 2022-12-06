package speiger.src.builder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Stream;

import speiger.src.builder.modules.AsyncModule;
import speiger.src.builder.modules.BaseModule;
import speiger.src.builder.modules.CollectionModule;
import speiger.src.builder.modules.FunctionModule;
import speiger.src.builder.modules.JavaModule;
import speiger.src.builder.modules.ListModule;
import speiger.src.builder.modules.MapModule;
import speiger.src.builder.modules.PairModule;
import speiger.src.builder.modules.PrioQueueModule;
import speiger.src.builder.modules.SetModule;
import speiger.src.builder.processor.TemplateProcess;
import speiger.src.builder.processor.TemplateProcessor;

@SuppressWarnings("javadoc")
public class PrimitiveCollectionsBuilder extends TemplateProcessor
{
	private static final int SPECIAL = 0x1; //Detects if the Builder is generating tests
	private static final int LOAD = 0x2; //If Configs should be loaded
	private static final int ANTI_SAVE = SPECIAL | LOAD; //If save should be disabled since load/save shouldn't happen at the same time.
	private static final int SAVE = 0x4; //if the configuration should be created
	Set<String> globalFlags = new HashSet<>();
	List<ModulePackage> simplePackages = new ArrayList<>();
	List<ModulePackage> biPackages = new ArrayList<>();
	List<ModulePackage> enumPackages = new ArrayList<>();
	Map<String, RequiredType> requirements = new HashMap<>();
	SettingsManager manager = new SettingsManager();
	int flags;
	
	public PrimitiveCollectionsBuilder()
	{
		this(false);
	}
	
	public PrimitiveCollectionsBuilder(boolean silencedSuccess)
	{
		super(silencedSuccess, Paths.get("src/builder/resources/speiger/assets/collections/templates/"), Paths.get("src/main/java/speiger/src/collections/"), Paths.get("src/builder/resources/speiger/assets/collections/"));
	}
	
	public PrimitiveCollectionsBuilder(Path sourceFolder, Path outputFolder, Path dataFolder)
	{
		this(false, sourceFolder, outputFolder, dataFolder);
	}
	
	public PrimitiveCollectionsBuilder(boolean silencedSuccess, Path sourceFolder, Path outputFolder, Path dataFolder)
	{
		super(silencedSuccess, sourceFolder, outputFolder, dataFolder);
	}
	
	private PrimitiveCollectionsBuilder setFlags(int flags) {
		this.flags = flags;
		if((flags & ANTI_SAVE) != 0) {
			this.flags &= ~SAVE;
		}
		return this;
	}
	
	private static PrimitiveCollectionsBuilder createTests(boolean silent, int flags) {
		return new PrimitiveCollectionsBuilder(silent, 
				Paths.get("src/builder/resources/speiger/assets/tests/templates/"), 
				Paths.get("src/test/java/speiger/src/tests/"), 
				Paths.get("src/builder/resources/speiger/assets/tests/")).setFlags(flags | SPECIAL);
	}
	
	private static PrimitiveCollectionsBuilder createTesters(boolean silent, int flags) {
		return new PrimitiveCollectionsBuilder(silent, 
				Paths.get("src/builder/resources/speiger/assets/testers/templates/"), 
				Paths.get("src/test/java/speiger/src/testers/"), 
				Paths.get("src/builder/resources/speiger/assets/testers/")).setFlags(flags | SPECIAL);
	}
	
	@Override
	protected boolean isFileValid(Path fileName)
	{
		return true;
	}
	
	@Override
	protected boolean relativePackages()
	{
		return true;
	}
	
	@Override
	protected boolean debugUnusedMappers()
	{
		return false;
	}
	
	@Override
	protected void afterFinish()
	{
		if((flags & SPECIAL) == 0 && getVersion() > 8) 
		{
			Path basePath = Paths.get("src/main/java");
			try(BufferedWriter writer = Files.newBufferedWriter(basePath.resolve("module-info.java")))
			{
				writer.write(getModuleInfo(basePath));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void init() 
	{
		prepPackages();
		//Init Modules here
		addModule(new JavaModule());
		addModule(new FunctionModule());
		addModule(new CollectionModule());
		addModule(new PrioQueueModule());
		addModule(new ListModule());
		addModule(new SetModule());
		addModule(new MapModule());
		addModule(new PairModule());
		addModule(new AsyncModule());
		finishPackages();
	}
	
	public void addModule(BaseModule module)
	{
		module.setManager(manager);
		biPackages.forEach(module::init);
		module.cleanup();
	}
	
	private void finishPackages() 
	{
		biPackages.forEach(ModulePackage::finish);
		if((flags & SAVE) != 0) manager.save();
	}
	
	private void prepPackages()
	{
		if((flags & LOAD) != 0) manager.load();
		for(ModulePackage entry : ModulePackage.createPackages(globalFlags))
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
	
	private String getModuleInfo(Path basePath) {
		StringJoiner joiner = new StringJoiner("\n", "", "\n");
		try(Stream<Path> stream = Files.walk(getOutputFolder()))
		{
			stream.filter(Files::isDirectory)
					.filter(this::containsFiles)
					.map(basePath::relativize)
					.map(Path::toString)
					.map(this::sanitize)
					.forEach(T -> joiner.add("\texports "+T+";"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		StringBuilder builder = new StringBuilder();
		builder.append("/** @author Speiger */\n");
		builder.append("module ").append(sanitize(basePath.relativize(getOutputFolder()).toString())).append(" {\n");
		builder.append(joiner.toString()).append("}");	
		return builder.toString();
	}
	
	private String sanitize(String input)
	{
		return input.replace("\\", ".").replace("/", ".");
	}
	
	private boolean containsFiles(Path path) 
	{
		try(Stream<Path> stream = Files.walk(path, 1))
		{
			return stream.filter(Files::isRegularFile).findFirst().isPresent();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	private int getVersion() 
	{
		String version = System.getProperty("java.version");
		if(version.startsWith("1.")) return Integer.parseInt(version.substring(2, 3));
		int dot = version.indexOf(".");
		return Integer.parseInt(dot != -1 ? version.substring(0, dot) : version);
	}
	
	public static void main(String...args)
	{
		try
		{
			Set<String> flags = new HashSet<>(Arrays.asList(args));
			boolean silent = flags.contains("silent");
			boolean force = flags.contains("force");
			boolean tests = flags.contains("tests");
			boolean forceTests =  flags.contains("force-tests");
			boolean load = flags.contains("load");
			boolean save = !flags.contains("save");
			int flag = (load ? LOAD : 0) | (save ? SAVE : 0);
            new PrimitiveCollectionsBuilder(silent).setFlags(flag).process(force);
            if(tests) {
    			createTests(silent, flag).process(force || forceTests);
    			createTesters(silent, flag).process(force || forceTests);
            }
        }
		catch(InterruptedException | IOException e)
		{
			e.printStackTrace();
		}
	}
}
