package speiger.src.builder.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import speiger.src.builder.processor.TemplateProcess;
import speiger.src.builder.processor.TemplateProcessor;

public class TestBuilder extends TemplateProcessor
{
	Map<String, EnumSet<ClassType>> blocked = new HashMap<String, EnumSet<ClassType>>();
	Map<String, String> nameRemapper = new HashMap<String, String>();
	Map<String, String> biRequired = new HashMap<String, String>();
	public static final ClassType[] TYPE = ClassType.values();
	List<GlobalVariables> variables = new ArrayList<GlobalVariables>();
	List<GlobalVariables> biVariables = new ArrayList<>();
	
	public TestBuilder()
	{
		super(Paths.get("src/main/resources/speiger/assets/collections/templates/"), Paths.get("src/main/java/speiger/src/collections/"), Paths.get("src/main/resources/speiger/assets/collections/"));
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
		biRequired.put("BiConsumer", "");
		biRequired.put("Function", "2");
		biRequired.put("BiFunction", "2");
		nameRemapper.put("BiConsumer", "%sConsumer");
		nameRemapper.put("IArray", "I%sArray");
		nameRemapper.put("AbstractCollection", "Abstract%sCollection");
		nameRemapper.put("AbstractSet", "Abstract%sSet");
		nameRemapper.put("AbstractList", "Abstract%sList");
		addBlockage(ClassType.OBJECT, "Consumer", "Comparator", "Stack");
		addBlockage(ClassType.BOOLEAN, "Sets", "ArraySet", "AVLTreeSet", "RBTreeSet", "SortedSet", "NavigableSet", "OpenHashSet", "OpenCustomHashSet", "LinkedOpenHashSet", "LinkedOpenCustomHashSet");
	}
	
	protected void create(ClassType mainType, ClassType subType) 
	{
		GlobalVariables type = new GlobalVariables(mainType, subType);
		type.createFlags();
		type.createHelperVariables();
		type.createVariables();
		type.createClassTypes();
		type.createFunctions();
		if(mainType == subType) variables.add(type);
		biVariables.add(type);
	}
	
	protected void addBlockage(ClassType type, String...args) {
		for(String s : args) {
			EnumSet<ClassType> set = blocked.get(s);
			if(set == null) {
				set = EnumSet.noneOf(ClassType.class);
				blocked.put(s, set);
			}
			set.add(type);
		}
	}
	
	@Override
	public void createProcesses(String name, Consumer<TemplateProcess> acceptor)
	{
		EnumSet<ClassType> types = blocked.get(name);
		String splitter = biRequired.get(name);
		if(splitter != null)
		{
			for(int i = 0,m=biVariables.size();i<m;i++)
			{
				GlobalVariables type = biVariables.get(i);
				if(types == null || !types.contains(type.getType()))
				{
					acceptor.accept(type.createBi(nameRemapper.getOrDefault(name, "%s"+name), splitter));
				}
			}
			return;
		}
		for(int i = 0,m=variables.size();i<m;i++)
		{
			GlobalVariables type = variables.get(i);
			if(types == null || !types.contains(type.getType()))
			{
				acceptor.accept(type.create(nameRemapper.getOrDefault(name, "%s"+name)));
			}
		}
	}
	
	public static void main(String...args)
	{
		try
		{
            new TestBuilder().process(false);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
