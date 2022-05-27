package speiger.src.tests.objects.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2DoubleConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2DoubleLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2DoubleOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2DoubleArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2DoubleAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2DoubleRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleSortedMap;
import speiger.src.testers.objects.builder.maps.Object2DoubleMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2DoubleNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2DoubleMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Object2DoubleMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2DoubleMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2DoubleOpenHashMap", Object2DoubleOpenHashMap::new));
		suite.addTest(mapSuite("Object2DoubleLinkedOpenHashMap", Object2DoubleLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2DoubleArrayMap", Object2DoubleArrayMap::new));
		suite.addTest(mapSuite("Object2DoubleConcurrentOpenHashMap", Object2DoubleConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2DoubleRBTreeMap", Object2DoubleRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2DoubleAVLTreeMap", Object2DoubleAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], double[], Object2DoubleMap<String>> factory) {
		SimpleObject2DoubleMapTestGenerator.Maps<String> generator = new SimpleObject2DoubleMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		Object2DoubleMapTestSuiteBuilder<String> builder = Object2DoubleMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], double[], Object2DoubleSortedMap<String>> factory) {
		SimpleObject2DoubleMapTestGenerator.SortedMaps<String> generator = new SimpleObject2DoubleMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		Object2DoubleNavigableMapTestSuiteBuilder<String> builder = Object2DoubleNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
}