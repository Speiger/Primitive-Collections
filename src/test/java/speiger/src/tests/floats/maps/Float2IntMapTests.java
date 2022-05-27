package speiger.src.tests.floats.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.maps.impl.concurrent.Float2IntConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2IntLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2IntOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2IntArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2IntAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2IntRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntSortedMap;
import speiger.src.testers.floats.builder.maps.Float2IntMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2IntNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2IntMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Float2IntMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2IntMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2IntOpenHashMap", Float2IntOpenHashMap::new));
		suite.addTest(mapSuite("Float2IntLinkedOpenHashMap", Float2IntLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2IntArrayMap", Float2IntArrayMap::new));
		suite.addTest(mapSuite("Float2IntConcurrentOpenHashMap", Float2IntConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2IntRBTreeMap", Float2IntRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2IntAVLTreeMap", Float2IntAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], int[], Float2IntMap> factory) {
		Float2IntMapTestSuiteBuilder builder = Float2IntMapTestSuiteBuilder.using(new SimpleFloat2IntMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], int[], Float2IntSortedMap> factory) {
		Float2IntNavigableMapTestSuiteBuilder builder = Float2IntNavigableMapTestSuiteBuilder.using(new SimpleFloat2IntMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}