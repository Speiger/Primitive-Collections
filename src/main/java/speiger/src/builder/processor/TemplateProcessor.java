package speiger.src.builder.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import speiger.src.builder.base.Template;
import speiger.src.builder.misc.FileUtils;

public abstract class TemplateProcessor
{
	Path sourceFolder;
	Path outputFolder;
	Path dataFolder;
	boolean init = false;
	
	public TemplateProcessor(Path sourceFolder, Path outputFolder, Path dataFolder)
	{
		this.sourceFolder = sourceFolder;
		this.outputFolder = outputFolder;
		this.dataFolder = dataFolder;
	}
	
	protected abstract void init();
	
	protected abstract boolean isFileValid(Path fileName);
	
	public abstract void createProcesses(String fileName, Consumer<TemplateProcess> process);
	
	protected abstract boolean relativePackages();
	
	public boolean process(boolean force) throws IOException, InterruptedException
	{
		if(!init)
		{
			init = true;
			init();
		}
		Map<String, String> existing = FileUtils.loadMappings(dataFolder);
		List<Path> pathsLeft = Files.walk(sourceFolder).filter(Files::isRegularFile).filter(T -> isFileValid(T.getFileName())).filter(T -> force || FileUtils.isValid(T, existing)).collect(Collectors.toList());
		if(pathsLeft.isEmpty() && !force)
		{
			System.out.println("Nothing has changed");
			return false;
		}
		final boolean relative = relativePackages();
		ThreadPoolExecutor service = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		service.setKeepAliveTime(10, TimeUnit.MILLISECONDS);
		service.allowCoreThreadTimeOut(true);
		service.submit(() -> {
			for(int i = 0,m=pathsLeft.size();i<m;i++)
			{
				Path path = pathsLeft.get(i);
				try
				{
					Template template = Template.parse(path);
					createProcesses(FileUtils.getFileName(path), T -> service.execute(new BuildTask(relative ? outputFolder.resolve(sourceFolder.relativize(path).getParent()) : outputFolder, template, T)));
				}
				catch(Exception e) 
				{ 
					e.printStackTrace();
					continue; 
				}
			}
		});
		long start = System.currentTimeMillis();
		System.out.println("Started Tasks");
		for(int i = 0,m=pathsLeft.size();i<m;i++)
		{
			Path path = pathsLeft.get(i);
			existing.put(FileUtils.getFileName(path), FileUtils.getMD5String(path));
		}
		while(service.getActiveCount() > 0)
		{
			Thread.sleep(10);
		}
		System.out.println("Finished Tasks: "+(System.currentTimeMillis() - start)+"ms");
		FileUtils.saveMappings(existing, dataFolder);
		System.out.print("Saved Changes");
		return true;
	}
}
