package speiger.src.tests.floats.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.maps.impl.concurrent.Float2DoubleConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2DoubleLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2DoubleOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2DoubleArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2DoubleAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2DoubleRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleSortedMap;
import speiger.src.testers.floats.builder.maps.Float2DoubleMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2DoubleNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2DoubleMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Float2DoubleMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2DoubleMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2DoubleOpenHashMap", Float2DoubleOpenHashMap::new));
		suite.addTest(mapSuite("Float2DoubleLinkedOpenHashMap", Float2DoubleLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2DoubleArrayMap", Float2DoubleArrayMap::new));
		suite.addTest(mapSuite("Float2DoubleConcurrentOpenHashMap", Float2DoubleConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2DoubleRBTreeMap", Float2DoubleRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2DoubleAVLTreeMap", Float2DoubleAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], double[], Float2DoubleMap> factory) {
		Float2DoubleMapTestSuiteBuilder builder = Float2DoubleMapTestSuiteBuilder.using(new SimpleFloat2DoubleMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], double[], Float2DoubleSortedMap> factory) {
		Float2DoubleNavigableMapTestSuiteBuilder builder = Float2DoubleNavigableMapTestSuiteBuilder.using(new SimpleFloat2DoubleMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}