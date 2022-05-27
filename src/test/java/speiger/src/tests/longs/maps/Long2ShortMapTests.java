package speiger.src.tests.longs.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2ShortConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ShortLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ShortOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2ShortArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ShortAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ShortRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortSortedMap;
import speiger.src.testers.longs.builder.maps.Long2ShortMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2ShortNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2ShortMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Long2ShortMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2ShortMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2ShortOpenHashMap", Long2ShortOpenHashMap::new));
		suite.addTest(mapSuite("Long2ShortLinkedOpenHashMap", Long2ShortLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2ShortArrayMap", Long2ShortArrayMap::new));
		suite.addTest(mapSuite("Long2ShortConcurrentOpenHashMap", Long2ShortConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2ShortRBTreeMap", Long2ShortRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2ShortAVLTreeMap", Long2ShortAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], short[], Long2ShortMap> factory) {
		Long2ShortMapTestSuiteBuilder builder = Long2ShortMapTestSuiteBuilder.using(new SimpleLong2ShortMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], short[], Long2ShortSortedMap> factory) {
		Long2ShortNavigableMapTestSuiteBuilder builder = Long2ShortNavigableMapTestSuiteBuilder.using(new SimpleLong2ShortMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}