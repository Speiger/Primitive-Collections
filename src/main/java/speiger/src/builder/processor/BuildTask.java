package speiger.src.builder.processor;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import speiger.src.builder.base.Template;

public class BuildTask implements Runnable
{
	Path basePath;
	Template template;
	TemplateProcess process;
	
	public BuildTask(Path basePath, Template template, TemplateProcess process)
	{
		this.basePath = basePath;
		this.template = template;
		this.process = process;
	}
	
	@Override
	public void run()
	{
		String s = template.build(process.parsePool, process.mappers);
		Path path = basePath.resolve(process.path).resolve(process.fileName);
		try
		{
			Files.createDirectories(path.getParent());			
		}
		catch(Exception e)
		{
		}
		try(BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.SYNC))
		{
			writer.write(s);
			writer.flush();
			System.out.println("Created: "+process.fileName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
