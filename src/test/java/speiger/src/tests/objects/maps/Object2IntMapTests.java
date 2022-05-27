package speiger.src.tests.objects.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2IntConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2IntLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2IntOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2IntArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2IntAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2IntRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntSortedMap;
import speiger.src.testers.objects.builder.maps.Object2IntMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2IntNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2IntMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Object2IntMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2IntMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2IntOpenHashMap", Object2IntOpenHashMap::new));
		suite.addTest(mapSuite("Object2IntLinkedOpenHashMap", Object2IntLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2IntArrayMap", Object2IntArrayMap::new));
		suite.addTest(mapSuite("Object2IntConcurrentOpenHashMap", Object2IntConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2IntRBTreeMap", Object2IntRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2IntAVLTreeMap", Object2IntAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], int[], Object2IntMap<String>> factory) {
		SimpleObject2IntMapTestGenerator.Maps<String> generator = new SimpleObject2IntMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		Object2IntMapTestSuiteBuilder<String> builder = Object2IntMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], int[], Object2IntSortedMap<String>> factory) {
		SimpleObject2IntMapTestGenerator.SortedMaps<String> generator = new SimpleObject2IntMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		Object2IntNavigableMapTestSuiteBuilder<String> builder = Object2IntNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
}