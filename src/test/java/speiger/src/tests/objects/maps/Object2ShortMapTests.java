package speiger.src.tests.objects.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2ShortConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ShortLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ShortOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2ShortArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ShortAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ShortRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortSortedMap;
import speiger.src.testers.objects.builder.maps.Object2ShortMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2ShortNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2ShortMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Object2ShortMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2ShortMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2ShortOpenHashMap", Object2ShortOpenHashMap::new));
		suite.addTest(mapSuite("Object2ShortLinkedOpenHashMap", Object2ShortLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2ShortArrayMap", Object2ShortArrayMap::new));
		suite.addTest(mapSuite("Object2ShortConcurrentOpenHashMap", Object2ShortConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2ShortRBTreeMap", Object2ShortRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2ShortAVLTreeMap", Object2ShortAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], short[], Object2ShortMap<String>> factory) {
		SimpleObject2ShortMapTestGenerator.Maps<String> generator = new SimpleObject2ShortMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		Object2ShortMapTestSuiteBuilder<String> builder = Object2ShortMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], short[], Object2ShortSortedMap<String>> factory) {
		SimpleObject2ShortMapTestGenerator.SortedMaps<String> generator = new SimpleObject2ShortMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		Object2ShortNavigableMapTestSuiteBuilder<String> builder = Object2ShortNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
}