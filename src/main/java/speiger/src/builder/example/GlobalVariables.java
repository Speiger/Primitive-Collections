package speiger.src.builder.example;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import speiger.src.builder.mappers.SimpleMapper;
import speiger.src.builder.processor.TemplateProcess;

public class GlobalVariables
{
	String fileName;
	String folderName;
	List<UnaryOperator<String>> operators = new ArrayList<>();
	Set<String> flags = new LinkedHashSet<>();
	
	public GlobalVariables(String fileName, String folderName)
	{
		this.fileName = fileName;
		this.folderName = folderName;
	}
	
	public GlobalVariables createInitials(String classType, String keyType)
	{
		operators.add(new SimpleMapper("CLASS_TYPE", classType));
		operators.add(new SimpleMapper("KEY_TYPE", keyType));
		return this;
	}
	
	public GlobalVariables createClassTypes(String fileType)
	{
		operators.add(new SimpleMapper("CONSUMER", fileType+"Consumer"));
		return this;
	}
	
	public TemplateProcess create(String fileName)
	{
		
//		TemplateProcess process = new TemplateProcess(entry.getKey()+name+".java", Paths.get(""));
		return null;
	}
}
