package speiger.src.builder.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import speiger.src.builder.mappers.SimpleMapper;
import speiger.src.builder.processor.TemplateProcess;
import speiger.src.builder.processor.TemplateProcessor;

public class TestBuilder extends TemplateProcessor
{
	Map<String, List<UnaryOperator<String>>> data;
	
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
	protected void init()
	{
		data = new LinkedHashMap<>();
		data.put("Byte", createForType("byte", "Byte", "Byte"));
		data.put("Short", createForType("short", "Short", "Short"));
		data.put("Int", createForType("int", "Integer", "Int"));
		data.put("Long", createForType("long", "Long", "Long"));
		data.put("Float", createForType("float", "Float", "Float"));
		data.put("Double", createForType("double", "Double", "Double"));
		data.put("Object", createForType("Object", "T", "Object"));
	}
	
	private List<UnaryOperator<String>> createForType(String lowercase, String upperCase, String classType)
	{
		List<UnaryOperator<String>> list = new ArrayList<>();
		list.add(new SimpleMapper("LIST", classType+"List"));
		list.add(new SimpleMapper("CLASS_TYPE", upperCase));
		list.add(new SimpleMapper("KEY_TYPE", lowercase));
		list.add(new SimpleMapper("KEY_GENERIC_TYPE", "<T>"));
		return list;
	}

	@Override
	public void createProcesses(String name, Consumer<TemplateProcess> acceptor)
	{
		for(Entry<String, List<UnaryOperator<String>>> entry : data.entrySet())
		{
			TemplateProcess process = new TemplateProcess(entry.getKey()+name+".java", Paths.get(""));
			process.addMappers(entry.getValue());
			if(entry.getKey().equals("Object"))
			{
				process.addFlags("OBJECT");
			}
			acceptor.accept(process);
		}
	}
	
	public static void main(String...args)
	{
		try
		{
			new TestBuilder().process(true);
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
