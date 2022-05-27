package speiger.src.tests.longs.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2FloatConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2FloatLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2FloatOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2FloatArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2FloatAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2FloatRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.collections.longs.maps.interfaces.Long2FloatSortedMap;
import speiger.src.testers.longs.builder.maps.Long2FloatMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2FloatNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2FloatMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Long2FloatMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2FloatMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2FloatOpenHashMap", Long2FloatOpenHashMap::new));
		suite.addTest(mapSuite("Long2FloatLinkedOpenHashMap", Long2FloatLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2FloatArrayMap", Long2FloatArrayMap::new));
		suite.addTest(mapSuite("Long2FloatConcurrentOpenHashMap", Long2FloatConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2FloatRBTreeMap", Long2FloatRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2FloatAVLTreeMap", Long2FloatAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], float[], Long2FloatMap> factory) {
		Long2FloatMapTestSuiteBuilder builder = Long2FloatMapTestSuiteBuilder.using(new SimpleLong2FloatMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], float[], Long2FloatSortedMap> factory) {
		Long2FloatNavigableMapTestSuiteBuilder builder = Long2FloatNavigableMapTestSuiteBuilder.using(new SimpleLong2FloatMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}