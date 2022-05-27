package speiger.src.tests.floats.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.maps.impl.concurrent.Float2LongConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2LongLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2LongOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2LongArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2LongAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2LongRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongSortedMap;
import speiger.src.testers.floats.builder.maps.Float2LongMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2LongNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2LongMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Float2LongMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2LongMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2LongOpenHashMap", Float2LongOpenHashMap::new));
		suite.addTest(mapSuite("Float2LongLinkedOpenHashMap", Float2LongLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2LongArrayMap", Float2LongArrayMap::new));
		suite.addTest(mapSuite("Float2LongConcurrentOpenHashMap", Float2LongConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2LongRBTreeMap", Float2LongRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2LongAVLTreeMap", Float2LongAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], long[], Float2LongMap> factory) {
		Float2LongMapTestSuiteBuilder builder = Float2LongMapTestSuiteBuilder.using(new SimpleFloat2LongMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], long[], Float2LongSortedMap> factory) {
		Float2LongNavigableMapTestSuiteBuilder builder = Float2LongNavigableMapTestSuiteBuilder.using(new SimpleFloat2LongMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}