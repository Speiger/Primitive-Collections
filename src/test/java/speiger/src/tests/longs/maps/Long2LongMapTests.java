package speiger.src.tests.longs.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2LongConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2LongLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2LongOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2LongArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2LongAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2LongRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongSortedMap;
import speiger.src.testers.longs.builder.maps.Long2LongMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2LongNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2LongMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Long2LongMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2LongMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2LongOpenHashMap", Long2LongOpenHashMap::new));
		suite.addTest(mapSuite("Long2LongLinkedOpenHashMap", Long2LongLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2LongArrayMap", Long2LongArrayMap::new));
		suite.addTest(mapSuite("Long2LongConcurrentOpenHashMap", Long2LongConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2LongRBTreeMap", Long2LongRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2LongAVLTreeMap", Long2LongAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], long[], Long2LongMap> factory) {
		Long2LongMapTestSuiteBuilder builder = Long2LongMapTestSuiteBuilder.using(new SimpleLong2LongMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], long[], Long2LongSortedMap> factory) {
		Long2LongNavigableMapTestSuiteBuilder builder = Long2LongNavigableMapTestSuiteBuilder.using(new SimpleLong2LongMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}