package speiger.src.tests.floats.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.maps.impl.concurrent.Float2ShortConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ShortLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ShortOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2ShortArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ShortAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ShortRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortSortedMap;
import speiger.src.testers.floats.builder.maps.Float2ShortMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2ShortNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2ShortMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Float2ShortMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2ShortMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2ShortOpenHashMap", Float2ShortOpenHashMap::new));
		suite.addTest(mapSuite("Float2ShortLinkedOpenHashMap", Float2ShortLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2ShortArrayMap", Float2ShortArrayMap::new));
		suite.addTest(mapSuite("Float2ShortConcurrentOpenHashMap", Float2ShortConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2ShortRBTreeMap", Float2ShortRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2ShortAVLTreeMap", Float2ShortAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], short[], Float2ShortMap> factory) {
		Float2ShortMapTestSuiteBuilder builder = Float2ShortMapTestSuiteBuilder.using(new SimpleFloat2ShortMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], short[], Float2ShortSortedMap> factory) {
		Float2ShortNavigableMapTestSuiteBuilder builder = Float2ShortNavigableMapTestSuiteBuilder.using(new SimpleFloat2ShortMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}