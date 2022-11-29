package speiger.src.builder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import speiger.src.builder.processor.TemplateProcess;
import speiger.src.builder.processor.TemplateProcessor;

@SuppressWarnings("javadoc")
public class PrimitiveCollectionsBuilder extends TemplateProcessor
{
	Map<String, EnumSet<ClassType>> blocked = new HashMap<>();
	EnumMap<ClassType, List<Predicate<String>>> blockedPredicate = new EnumMap<>(ClassType.class);
	Map<String, String> nameRemapper = new HashMap<>();
	Map<String, String> biRequired = new HashMap<>();
	Set<String> enumRequired = new HashSet<>();
	public static final ClassType[] TYPE = ClassType.values();
	List<GlobalVariables> variables = new ArrayList<>();
	List<GlobalVariables> biVariables = new ArrayList<>();
	List<GlobalVariables> enumVariables = new ArrayList<>();
	boolean special = false;
	
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
	
	private PrimitiveCollectionsBuilder setSpecial() {
		special = true;
		return this;
	}
	
	private static PrimitiveCollectionsBuilder createTests(boolean silent) {
		return new PrimitiveCollectionsBuilder(silent, Paths.get("src/builder/resources/speiger/assets/tests/templates/"), Paths.get("src/test/java/speiger/src/tests/"), Paths.get("src/builder/resources/speiger/assets/tests/")).setSpecial();
	}
	
	private static PrimitiveCollectionsBuilder createTesters(boolean silent) {
		return new PrimitiveCollectionsBuilder(silent, Paths.get("src/builder/resources/speiger/assets/testers/templates/"), Paths.get("src/test/java/speiger/src/testers/"), Paths.get("src/builder/resources/speiger/assets/testers/")).setSpecial();
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
		if(!special && getVersion() > 8) 
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
		variables.clear();
		for(ClassType clzType : TYPE)
		{
			for(ClassType subType : TYPE)
			{
				create(clzType, subType);
			}
		}
		enumRequired.add("EnumMap");
		enumRequired.add("LinkedEnumMap");
		biRequired.put("BiConsumer", "");
		biRequired.put("UnaryOperator", "");
		biRequired.put("Pair", "");
		biRequired.put("MutablePair", "");
		biRequired.put("ImmutablePair", "");
		addBiClass("Function", "Maps", "Map", "SortedMap", "OrderedMap", "NavigableMap", "ConcurrentMap", "AbstractMap", "ConcurrentOpenHashMap", "ImmutableOpenHashMap", "OpenHashMap", "LinkedOpenHashMap", "OpenCustomHashMap", "LinkedOpenCustomHashMap", "ArrayMap", "RBTreeMap", "AVLTreeMap");
		nameRemapper.put("BiConsumer", "%sConsumer");
		nameRemapper.put("IArray", "I%sArray");
		nameRemapper.put("AbstractMap", "Abstract%sMap");
		nameRemapper.put("AbstractCollection", "Abstract%sCollection");
		nameRemapper.put("AbstractPriorityQueue", "Abstract%sPriorityQueue");
		nameRemapper.put("AbstractSet", "Abstract%sSet");
		nameRemapper.put("AbstractList", "Abstract%sList");
		nameRemapper.put("EnumMap", "Enum2%sMap");
		nameRemapper.put("LinkedEnumMap", "LinkedEnum2%sMap");
		nameRemapper.put("ImmutableList", "Immutable%sList");
		nameRemapper.put("CopyOnWriteList", "CopyOnWrite%sArrayList");
		nameRemapper.put("ImmutableOpenHashSet", "Immutable%sOpenHashSet");
		nameRemapper.put("ImmutableOpenHashMap", "Immutable%sOpenHashMap");
		
		
		addBlockage(ClassType.OBJECT, "Consumer", "Comparator", "Stack");
		addBlockage(ClassType.BOOLEAN, "ArraySet", "AVLTreeSet", "RBTreeSet", "SortedSet", "OrderedSet", "NavigableSet", "OpenHashSet", "OpenCustomHashSet", "LinkedOpenHashSet", "LinkedOpenCustomHashSet");
		addBlockage(ClassType.BOOLEAN, "ConcurrentOpenHashMap", "ImmutableOpenHashMap", "ImmutableOpenHashSet", "SortedMap", "OrderedMap", "NavigableMap", "ConcurrentMap", "OpenHashMap", "LinkedOpenHashMap", "OpenCustomHashMap", "LinkedOpenCustomHashMap", "ArrayMap", "RBTreeMap", "AVLTreeMap");
		
		//UnitTesters
		nameRemapper.put("AbstractIteratorTester", "Abstract%sIteratorTester");
		nameRemapper.put("MinimalCollection", "Minimal%sCollection");
		nameRemapper.put("MinimalSet", "Minimal%sSet");
		nameRemapper.put("TestCollectionGenerator", "Test%sCollectionGenerator");
		nameRemapper.put("TestQueueGenerator", "Test%sQueueGenerator");
		nameRemapper.put("TestListGenerator", "Test%sListGenerator");
		nameRemapper.put("TestNavigableSetGenerator", "Test%sNavigableSetGenerator");
		nameRemapper.put("TestSortedSetGenerator", "Test%sSortedSetGenerator");
		nameRemapper.put("TestOrderedSetGenerator", "Test%sOrderedSetGenerator");
		nameRemapper.put("TestSetGenerator", "Test%sSetGenerator");
		nameRemapper.put("AbstractContainerTester", "Abstract%sContainerTester");
		nameRemapper.put("AbstractCollectionTester", "Abstract%sCollectionTester");
		nameRemapper.put("AbstractQueueTester", "Abstract%sQueueTester");
		nameRemapper.put("AbstractListTester", "Abstract%sListTester");
		nameRemapper.put("AbstractListIndexOfTester", "Abstract%sListIndexOfTester");
		nameRemapper.put("AbstractSetTester", "Abstract%sSetTester");
		nameRemapper.put("SimpleTestGenerator", "Simple%sTestGenerator");
		nameRemapper.put("SimpleQueueTestGenerator", "Simple%sQueueTestGenerator");
		nameRemapper.put("TestMapGenerator", "Test%sMapGenerator");
		nameRemapper.put("TestSortedMapGenerator", "Test%sSortedMapGenerator");
		nameRemapper.put("TestOrderedMapGenerator", "Test%sOrderedMapGenerator");
		nameRemapper.put("SimpleMapTestGenerator", "Simple%sMapTestGenerator");
		nameRemapper.put("DerivedMapGenerators", "Derived%sMapGenerators");
		nameRemapper.put("AbstractMapTester", "Abstract%sMapTester");
		nameRemapper.put("TestMap", "Test%sMap");
		biRequired.put("PairTester", "");
		
		addBiClass("TestMapGenerator", "TestSortedMapGenerator", "TestOrderedMapGenerator", "SimpleMapTestGenerator", "DerivedMapGenerators", "AbstractMapTester", "MapTestSuiteBuilder", "SortedMapTestSuiteBuilder", "NavigableMapTestSuiteBuilder", "OrderedMapTestSuiteBuilder", "MapTests", "MapConstructorTests", "TestMap");
		addBiClass("MapAddToTester", "MapSubFromTester", "MapClearTester", "MapComputeIfAbsentTester", "MapComputeIfPresentTester", "MapComputeTester", "MapCopyTester", "MapContainsTester", "MapContainsKeyTester", "MapContainsValueTester", "MapCreatorTester", "MapEntrySetTester",
			"MapEqualsTester", "MapForEachTester", "MapGetOrDefaultTester", "MapGetTester", "MapHashCodeTester", "MapIsEmptyTester", "MapMergeTester", "MapMergeBulkTester", "MapPutAllArrayTester", "MapPutAllTester", "MapPutIfAbsentTester", "MapPutTester",
			"MapRemoveEntryTester", "MapRemoveOrDefaultTester", "MapRemoveTester", "MapReplaceAllTester", "MapReplaceEntryTester", "MapReplaceTester", "MapSizeTester", "MapSupplyIfAbsentTester", "MapToStringTester",
			"NavigableMapNavigationTester", "SortedMapNavigationTester", "OrderedMapNavigationTester", "OrderedMapMoveTester", "MapConstructorTester");
		
		addBlockage(ClassType.OBJECT, "CollectionStreamTester", "ListFillBufferTester");
		addBlockage(ClassType.BOOLEAN, "TestOrderedSetGenerator", "TestSortedSetGenerator", "TestNavigableSetGenerator", "CollectionRemoveIfTester", "CollectionStreamTester", "ListFillBufferTester", "ListReplaceAllTester", "NavigableSetNavigationTester", "SetTests", "MapConstructorTests", "TestMap", "QueueTests");
		addBlockage(ClassType.BOOLEAN, "OrderedSetMoveTester", "OrderedSetNavigationTester", "SortedSetNaviationTester", "SetTestSuiteBuilder", "OrderedSetTestSuiteBuilder", "SortedSetTestSuiteBuilder", "NavigableSetTestSuiteBuilder", "SortedSetSubsetTestSetGenerator", "OrderedMapNavigationTester", "OrderedMapTestSuiteBuilder", "OrderedSetIterationTester", "SortedSetIterationTester");
		addBlockage(ClassType.BOOLEAN, "TestMapGenerator", "TestSortedMapGenerator", "TestOrderedMapGenerator", "SimpleMapTestGenerator", "DerivedMapGenerators", "AbstractMapTester", "MapTestSuiteBuilder", "SortedMapTestSuiteBuilder", "NavigableMapTestSuiteBuilder", "MapTests");
		addBlockage(ClassType.BOOLEAN, T -> T.endsWith("Tester") && (T.startsWith("Iterable") ||T.startsWith("Map") || T.startsWith("OrderedMap") || T.startsWith("SortedMap") || T.startsWith("NavigableMap")));
	}
	
	protected void create(ClassType mainType, ClassType subType) 
	{
		GlobalVariables type = new GlobalVariables(mainType, subType);
		type.createFlags();
		type.createHelperVariables();
		type.createVariables();
		type.createPreFunctions();
		type.createClassTypes();
		type.createFunctions();
		if(mainType == subType) variables.add(type);
		biVariables.add(type);
		if(mainType.isObject()) enumVariables.add(type);
	}
	
	protected void addBiClass(String...classNames) 
	{
		for(String s : classNames)
		{
			biRequired.put(s, "2");
		}
	}
	
	protected void addBlockage(ClassType type, Predicate<String> filter)
	{
		blockedPredicate.computeIfAbsent(type, T -> new ArrayList<>()).add(filter);
	}
	
	protected void addBlockage(ClassType type, String...args) 
	{
		for(String s : args) 
		{
			EnumSet<ClassType> set = blocked.get(s);
			if(set == null) 
			{
				set = EnumSet.noneOf(ClassType.class);
				blocked.put(s, set);
			}
			set.add(type);
		}
	}
	
	public void test() throws InterruptedException, IOException
	{
		init();
		List<String> keys = new ArrayList<>();
		List<String> values = new ArrayList<>();
		for(int i = 0,m=variables.size();i<m;i++) {
			variables.get(i).testComparason(keys, values);
		}
		System.out.println("Original: "+keys);
		System.out.println("Copy:     "+values);
	}
	
	@Override
	public void createProcesses(String name, Consumer<TemplateProcess> acceptor)
	{
		String splitter = biRequired.get(name);
		boolean valueRequired = enumRequired.contains(name);
		List<GlobalVariables> vars = getVariablesByClass(name, splitter != null);
		for(int i = 0,m=vars.size();i<m;i++)
		{
			GlobalVariables type = vars.get(i);
			if(isAllowed(type.getType(), name))
			{
				acceptor.accept(type.create(nameRemapper.getOrDefault(name, "%s"+name), splitter, valueRequired));
			}
		}
	}
	
	protected boolean isAllowed(ClassType type, String fileName)
	{
		EnumSet<ClassType> types = blocked.get(fileName);
		if(types != null && types.contains(type)) return false;
		List<Predicate<String>> list = blockedPredicate.get(type);
		if(list != null) {
			for(int i = 0,m=list.size();i<m;i++) {
				if(list.get(i).test(fileName)) return false;
			}
		}
		return true;
	}
	
	protected List<GlobalVariables> getVariablesByClass(String name, boolean bi) {
		if(enumRequired.contains(name)) return enumVariables;
		if(bi) return biVariables;
		return variables;
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
			boolean force = !flags.contains("force");
			boolean tests = flags.contains("tests");
			boolean forceTests =  flags.contains("force-tests");
//			new PrimitiveCollectionsBuilder(silent).test();
            new PrimitiveCollectionsBuilder(silent).process(force);
            if(tests) {
    			createTests(silent).process(force || forceTests);
    			createTesters(silent).process(force || forceTests);
            }
        }
		catch(InterruptedException | IOException e)
		{
			e.printStackTrace();
		}
	}
}
