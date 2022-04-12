package speiger.src.collections.objects.map;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.google.common.collect.testing.AnEnum;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.NavigableMapTestSuiteBuilder;
import com.google.common.collect.testing.TestEnumMapGenerator;
import com.google.common.collect.testing.TestStringMapGenerator;
import com.google.common.collect.testing.TestStringSortedMapGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2ObjectConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.customHash.Object2ObjectLinkedOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.customHash.Object2ObjectOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ObjectLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ObjectOpenHashMap;
import speiger.src.collections.objects.maps.impl.immutable.ImmutableObject2ObjectOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Enum2ObjectMap;
import speiger.src.collections.objects.maps.impl.misc.LinkedEnum2ObjectMap;
import speiger.src.collections.objects.maps.impl.misc.Object2ObjectArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ObjectAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ObjectRBTreeMap;
import speiger.src.collections.objects.utils.ObjectStrategy;

@SuppressWarnings("javadoc")
public class ObjectMapTests extends TestCase
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Maps");
		suite.addTest(suite("HashMap", Object2ObjectOpenHashMap::new, true));
		suite.addTest(suite("LinkedHashMap", Object2ObjectLinkedOpenHashMap::new, true));
		suite.addTest(suite("CustomHashMap", () -> new Object2ObjectOpenCustomHashMap<>(Strategy.INSTANCE), true));
		suite.addTest(suite("LinkedCustomHashMap", () -> new Object2ObjectLinkedOpenCustomHashMap<>(Strategy.INSTANCE), true));
		suite.addTest(suite("ConcurrentHashMap", Object2ObjectConcurrentOpenHashMap::new, true));
		suite.addTest(navigableSuite("RBTreeMap_NonNull", Object2ObjectRBTreeMap::new, false));
		suite.addTest(navigableSuite("AVLTreeMap_NonNull", Object2ObjectAVLTreeMap::new, false));
		suite.addTest(navigableSuite("RBTreeMap_Null", () -> new Object2ObjectRBTreeMap<>(new NullFriendlyComparator()), true));
		suite.addTest(navigableSuite("AVLTreeMap_Null", () -> new Object2ObjectAVLTreeMap<>(new NullFriendlyComparator()), true));
		suite.addTest(immutableSuit("ImmutableMap", ImmutableObject2ObjectOpenHashMap::new));
		suite.addTest(suite("ArrayMap", Object2ObjectArrayMap::new, true));
		suite.addTest(enumSuite("EnumMap", () -> new Enum2ObjectMap<>(AnEnum.class)));
		suite.addTest(enumSuite("LinkedEnumMap", () -> new LinkedEnum2ObjectMap<>(AnEnum.class)));
		return suite;
	}
	
	public static Test suite(String name, Supplier<Map<String, String>> factory, boolean allowNull)
	{
		MapTestSuiteBuilder<String, String> builder = MapTestSuiteBuilder.using(new TestStringMapGenerator() {
			@Override
			protected Map<String, String> create(Map.Entry<String, String>[] entries) {
				Map<String, String> map = factory.get();
				for(Map.Entry<String, String> entry : entries) {
					map.put(entry.getKey(), entry.getValue());
				}
				return map;
			}
		}).named(name).withFeatures(CollectionSize.ANY, MapFeature.GENERAL_PURPOSE, MapFeature.ALLOWS_NULL_VALUES, CollectionFeature.SUPPORTS_ITERATOR_REMOVE);
		if(allowNull) builder.withFeatures(MapFeature.ALLOWS_NULL_KEYS, MapFeature.ALLOWS_ANY_NULL_QUERIES);
		return builder.createTestSuite();
	}
	
	public static Test navigableSuite(String name, Supplier<NavigableMap<String, String>> factory, boolean allowNull)
	{
		MapTestSuiteBuilder<String, String> builder = NavigableMapTestSuiteBuilder.using(new TestStringSortedMapGenerator() {
			@Override
			protected NavigableMap<String, String> create(Map.Entry<String, String>[] entries) {
				NavigableMap<String, String> map = factory.get();
				for(Map.Entry<String, String> entry : entries) {
					map.put(entry.getKey(), entry.getValue());
				}
				return map;
			}
		}).named(name).withFeatures(CollectionSize.ANY, MapFeature.GENERAL_PURPOSE, MapFeature.ALLOWS_NULL_VALUES, CollectionFeature.SUPPORTS_ITERATOR_REMOVE);
		if(allowNull) builder.withFeatures(MapFeature.ALLOWS_NULL_KEYS, MapFeature.ALLOWS_ANY_NULL_QUERIES);
		return builder.createTestSuite();
	}
	
	public static Test immutableSuit(String name, BiFunction<String[], String[], Map<String, String>> factory)  {
		MapTestSuiteBuilder<String, String> builder = MapTestSuiteBuilder.using(new TestStringMapGenerator() {
			@Override
			protected Map<String, String> create(Map.Entry<String, String>[] entries) {
				String[] keys = new String[entries.length];
				String[] values = new String[entries.length];
				for(int i = 0;i<entries.length;i++) {
					keys[i] = entries[i].getKey();
					values[i] = entries[i].getValue();
				}
				return factory.apply(keys, values);
			}
		}).named(name).withFeatures(CollectionSize.ANY, MapFeature.ALLOWS_NULL_KEYS, MapFeature.ALLOWS_NULL_VALUES, MapFeature.ALLOWS_ANY_NULL_QUERIES);
		return builder.createTestSuite();
	}
	
	public static Test enumSuite(String name, Supplier<Map<AnEnum, String>> factory)
	{
		MapTestSuiteBuilder<AnEnum, String> builder = MapTestSuiteBuilder.using(new TestEnumMapGenerator() {
			@Override
			protected Map<AnEnum, String> create(Map.Entry<AnEnum, String>[] entries) {
				Map<AnEnum, String> map = factory.get();
				for(Map.Entry<AnEnum, String> entry : entries) {
					map.put(entry.getKey(), entry.getValue());
				}
				return map;
			}
		}).named(name).withFeatures(CollectionSize.ANY, MapFeature.GENERAL_PURPOSE, MapFeature.ALLOWS_NULL_VALUES, CollectionFeature.SUPPORTS_ITERATOR_REMOVE);
		return builder.createTestSuite();
	}
	
	private static final class NullFriendlyComparator implements Comparator<String>
	{
		@Override
		public int compare(String left, String right) {
			return String.valueOf(left).compareTo(String.valueOf(right));
		}
	}
	
	private static class Strategy implements ObjectStrategy<String>
	{
		static final Strategy INSTANCE = new Strategy();
		
		@Override
		public int hashCode(String o) {
			return Objects.hashCode(o);
		}

		@Override
		public boolean equals(String key, String value) {
			return Objects.equals(key, value);
		}
		
	}
}
