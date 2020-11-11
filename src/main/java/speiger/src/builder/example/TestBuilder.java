package speiger.src.builder.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import speiger.src.builder.mappers.InjectMapper;
import speiger.src.builder.mappers.SimpleMapper;
import speiger.src.builder.processor.TemplateProcess;
import speiger.src.builder.processor.TemplateProcessor;

public class TestBuilder extends TemplateProcessor
{
	public static final String[] KEY_TYPE = new String[]{"byte", "short", "int", "long", "float", "double", "T"};
	public static final String[] CLASS_TYPE = new String[]{"Byte", "Short", "Integer", "Long", "Float", "Double", "Object"};
	public static final String[] FILE_TYPE = new String[]{"Byte", "Short", "Int", "Long", "Float", "Double", "Object"};
	
	List<GlobalVariables> varibles = new ArrayList<GlobalVariables>();
	
	public TestBuilder()
	{
		super(Paths.get("src\\main\\resources\\speiger\\assets\\collections\\templates\\"), Paths.get("src\\main\\java\\speiger\\src\\collections\\example\\"), Paths.get("src\\main\\resources\\speiger\\assets\\collections\\"));
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
		for(int i = 0,m=KEY_TYPE.length;i<m;i++)
		{
			GlobalVariables type = new GlobalVariables(FILE_TYPE[i]);
			type.createInitials(CLASS_TYPE[i], KEY_TYPE[i]);
			type.createClassTypes(FILE_TYPE[i]);
			varibles.add(type);
		}
	}
	
	private List<UnaryOperator<String>> createForType(String lowercase, String upperCase, String classType, String consumer)
	{
		Files
		List<UnaryOperator<String>> list = new ArrayList<>();
		list.add(new SimpleMapper("JAVA_CONSUMER", "java.util.function."+consumer));
		list.add(new SimpleMapper("CONSUMER", classType+"Consumer"));
		list.add(new SimpleMapper("CLASS_TYPE", upperCase));
		list.add(new SimpleMapper("KEY_TYPE", lowercase));
		list.add(new InjectMapper("OBJ_TO_KEY(\\([^)]+\\)|\\S)", "%s."+lowercase+"Value()").removeBraces());
		return list;
	}

	@Override
	public void createProcesses(String name, Consumer<TemplateProcess> acceptor)
	{
		for(Entry<String, List<UnaryOperator<String>>> entry : data.entrySet())
		{
			TemplateProcess process = new TemplateProcess(entry.getKey()+name+".java", Paths.get(""));
			process.addMappers(entry.getValue());
			acceptor.accept(process);
		}
	}
	
	public static void main(String...args)
	{
		Path path = Paths.get("").toAbsolutePath();
		System.out.println(path.toString());
		for(int i = 0,m=path.getNameCount();i<m;i++)
		{
			System.out.println(path.getName(i).toString());
		}
//		try
//		{
//			new TestBuilder().process(true);
//		}
//		catch(InterruptedException e)
//		{
//			e.printStackTrace();
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
	}
	
	
}
