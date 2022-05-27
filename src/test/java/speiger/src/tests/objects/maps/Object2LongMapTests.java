package speiger.src.tests.objects.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2LongConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2LongLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2LongOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2LongArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2LongAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2LongRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongSortedMap;
import speiger.src.testers.objects.builder.maps.Object2LongMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2LongNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2LongMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Object2LongMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2LongMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2LongOpenHashMap", Object2LongOpenHashMap::new));
		suite.addTest(mapSuite("Object2LongLinkedOpenHashMap", Object2LongLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2LongArrayMap", Object2LongArrayMap::new));
		suite.addTest(mapSuite("Object2LongConcurrentOpenHashMap", Object2LongConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2LongRBTreeMap", Object2LongRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2LongAVLTreeMap", Object2LongAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], long[], Object2LongMap<String>> factory) {
		SimpleObject2LongMapTestGenerator.Maps<String> generator = new SimpleObject2LongMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		Object2LongMapTestSuiteBuilder<String> builder = Object2LongMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], long[], Object2LongSortedMap<String>> factory) {
		SimpleObject2LongMapTestGenerator.SortedMaps<String> generator = new SimpleObject2LongMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		Object2LongNavigableMapTestSuiteBuilder<String> builder = Object2LongNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
}