package speiger.src.tests.longs.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2IntConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2IntLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2IntOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2IntArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2IntAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2IntRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.collections.longs.maps.interfaces.Long2IntSortedMap;
import speiger.src.testers.longs.builder.maps.Long2IntMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2IntNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2IntMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Long2IntMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2IntMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2IntOpenHashMap", Long2IntOpenHashMap::new));
		suite.addTest(mapSuite("Long2IntLinkedOpenHashMap", Long2IntLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2IntArrayMap", Long2IntArrayMap::new));
		suite.addTest(mapSuite("Long2IntConcurrentOpenHashMap", Long2IntConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2IntRBTreeMap", Long2IntRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2IntAVLTreeMap", Long2IntAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], int[], Long2IntMap> factory) {
		Long2IntMapTestSuiteBuilder builder = Long2IntMapTestSuiteBuilder.using(new SimpleLong2IntMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], int[], Long2IntSortedMap> factory) {
		Long2IntNavigableMapTestSuiteBuilder builder = Long2IntNavigableMapTestSuiteBuilder.using(new SimpleLong2IntMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}