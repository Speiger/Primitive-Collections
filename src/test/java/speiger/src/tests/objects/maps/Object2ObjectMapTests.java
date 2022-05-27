package speiger.src.tests.objects.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2ObjectConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ObjectLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ObjectOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2ObjectArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ObjectAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ObjectRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectSortedMap;
import speiger.src.testers.objects.builder.maps.Object2ObjectMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2ObjectNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2ObjectMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Object2ObjectMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2ObjectMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2ObjectOpenHashMap", Object2ObjectOpenHashMap::new));
		suite.addTest(mapSuite("Object2ObjectLinkedOpenHashMap", Object2ObjectLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2ObjectArrayMap", Object2ObjectArrayMap::new));
		suite.addTest(mapSuite("Object2ObjectConcurrentOpenHashMap", Object2ObjectConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2ObjectRBTreeMap", Object2ObjectRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2ObjectAVLTreeMap", Object2ObjectAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], String[], Object2ObjectMap<String, String>> factory) {
		SimpleObject2ObjectMapTestGenerator.Maps<String, String> generator = new SimpleObject2ObjectMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		generator.setValues(createValues());
		Object2ObjectMapTestSuiteBuilder<String, String> builder = Object2ObjectMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, MapFeature.ALLOWS_NULL_KEY_QUERIES, MapFeature.ALLOWS_NULL_ENTRY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], String[], Object2ObjectSortedMap<String, String>> factory) {
		SimpleObject2ObjectMapTestGenerator.SortedMaps<String, String> generator = new SimpleObject2ObjectMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		generator.setValues(createValues());
		Object2ObjectNavigableMapTestSuiteBuilder<String, String> builder = Object2ObjectNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
	private static String[] createValues() {
		return new String[]{"January", "February", "March", "April", "May", "below view", "below view", "above view", "above view"};
	}
	
}