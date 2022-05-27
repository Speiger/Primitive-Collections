package speiger.src.tests.longs.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2ByteConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ByteLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ByteOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2ByteArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ByteAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ByteRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteSortedMap;
import speiger.src.testers.longs.builder.maps.Long2ByteMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2ByteNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2ByteMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Long2ByteMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2ByteMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2ByteOpenHashMap", Long2ByteOpenHashMap::new));
		suite.addTest(mapSuite("Long2ByteLinkedOpenHashMap", Long2ByteLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2ByteArrayMap", Long2ByteArrayMap::new));
		suite.addTest(mapSuite("Long2ByteConcurrentOpenHashMap", Long2ByteConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2ByteRBTreeMap", Long2ByteRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2ByteAVLTreeMap", Long2ByteAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], byte[], Long2ByteMap> factory) {
		Long2ByteMapTestSuiteBuilder builder = Long2ByteMapTestSuiteBuilder.using(new SimpleLong2ByteMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], byte[], Long2ByteSortedMap> factory) {
		Long2ByteNavigableMapTestSuiteBuilder builder = Long2ByteNavigableMapTestSuiteBuilder.using(new SimpleLong2ByteMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}