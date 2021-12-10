package speiger.src.collections.objects.map;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.TestStringMapGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import speiger.src.collections.objects.maps.impl.hash.Object2ObjectOpenHashMap;

@SuppressWarnings("javadoc")
public final class MapTest extends TestCase
{
	public static Test suite()
	{
		return suite("Object2ObjectOpenHashMap", Object2ObjectOpenHashMap::new);
	}
	
	public static Test suite(String name, Supplier<Map<String, String>> factory)
	{
		return MapTestSuiteBuilder.using(new TestStringMapGenerator()
		{
			@Override
			protected Map<String, String> create(Map.Entry<String, String>[] entries)
			{
				Map<String, String> map = factory.get();
				for(Map.Entry<String, String> entry : entries)
				{
					map.put(entry.getKey(), entry.getValue());
				}
				return map;
			}
		}).named(name).withFeatures(CollectionSize.ANY, MapFeature.GENERAL_PURPOSE, MapFeature.ALLOWS_NULL_KEYS, MapFeature.ALLOWS_NULL_VALUES, MapFeature.ALLOWS_ANY_NULL_QUERIES, CollectionFeature.SUPPORTS_ITERATOR_REMOVE).createTestSuite();
	}
}
