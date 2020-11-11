package speiger.src.builder.misc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FileUtils
{
	public static boolean isValid(Path path, Map<String, String> existing)
	{
		String s = existing.get(getFileName(path));
		return s == null || !s.equals(FileUtils.getMD5String(path));
	}
		
	public static Map<String, String> loadMappings(Path dataFolder) throws IOException
	{
		Map<String, String> result = new LinkedHashMap<>();
		dataFolder = dataFolder.resolve("cache.bin");
		if(Files.notExists(dataFolder))
		{
			return result;
		}
		for(String s : Files.readAllLines(dataFolder))
		{
			String[] array = s.split("=");
			if(array.length == 2)
			{
				result.put(array[0], array[1]);
			}
		}
		return result;
	}
	
	public static void saveMappings(Map<String, String> mappings, Path dataFolder)
	{
		try(BufferedWriter writer = Files.newBufferedWriter(dataFolder.resolve("cache.bin"), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.DSYNC))
		{
			for(Entry<String, String> entry : mappings.entrySet())
			{
				writer.write(entry.getKey()+"="+entry.getValue());
				writer.newLine();
			}
			writer.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static String getFileName(Path path)
	{
		String name = path.getFileName().toString();
		int index = name.indexOf(".");
		return index == -1 ? name : name.substring(0, index);
	}
	
	public static String getMD5String(Path path)
	{
		try(InputStream stream = Files.newInputStream(path))
		{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] byteArray = new byte[2048];
			int bytesCount = 0;
			while((bytesCount = stream.read(byteArray)) != -1)
			{
				digest.update(byteArray, 0, bytesCount);
			}
			byte[] bytes = digest.digest();
			StringBuilder sb = new StringBuilder();
			for(int i = 0;i < bytes.length;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
