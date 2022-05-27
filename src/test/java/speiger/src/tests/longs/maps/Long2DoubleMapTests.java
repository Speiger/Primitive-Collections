package speiger.src.tests.longs.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2DoubleConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2DoubleLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2DoubleOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2DoubleArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2DoubleAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2DoubleRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.collections.longs.maps.interfaces.Long2DoubleSortedMap;
import speiger.src.testers.longs.builder.maps.Long2DoubleMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2DoubleNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2DoubleMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Long2DoubleMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2DoubleMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2DoubleOpenHashMap", Long2DoubleOpenHashMap::new));
		suite.addTest(mapSuite("Long2DoubleLinkedOpenHashMap", Long2DoubleLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2DoubleArrayMap", Long2DoubleArrayMap::new));
		suite.addTest(mapSuite("Long2DoubleConcurrentOpenHashMap", Long2DoubleConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2DoubleRBTreeMap", Long2DoubleRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2DoubleAVLTreeMap", Long2DoubleAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], double[], Long2DoubleMap> factory) {
		Long2DoubleMapTestSuiteBuilder builder = Long2DoubleMapTestSuiteBuilder.using(new SimpleLong2DoubleMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], double[], Long2DoubleSortedMap> factory) {
		Long2DoubleNavigableMapTestSuiteBuilder builder = Long2DoubleNavigableMapTestSuiteBuilder.using(new SimpleLong2DoubleMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}