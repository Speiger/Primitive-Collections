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
	public static final ClassType[] TYPE = ClassType.values();
	
	List<GlobalVariables> varibles = new ArrayList<GlobalVariables>();
	
	public TestBuilder()
	{
		super(Paths.get("src\\main\\resources\\speiger\\assets\\collections\\templates\\"), Paths.get("src\\main\\java\\speiger\\src\\collections\\"), Paths.get("src\\main\\resources\\speiger\\assets\\collections\\"));
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
	protected void init()
	{
		varibles.clear();
		for(ClassType clzType : TYPE)
		{
			GlobalVariables type = new GlobalVariables(clzType);
			type.createFlags();
			type.createVariables();
			type.createClassTypes();
			type.createFunctions();
			varibles.add(type);
		}
		blocked.put("Consumer", EnumSet.of(ClassType.OBJECT));
	}
	
	@Override
	public void createProcesses(String name, Consumer<TemplateProcess> acceptor)
	{
		EnumSet<ClassType> types = blocked.get(name);
		for(int i = 0,m=varibles.size();i<m;i++)
		{
			GlobalVariables type = varibles.get(i);
			if(types == null || !types.contains(type.getType()))
			{
				acceptor.accept(type.create(name));				
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
