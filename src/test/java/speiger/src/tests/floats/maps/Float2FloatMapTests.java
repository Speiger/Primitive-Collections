package speiger.src.tests.floats.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.maps.impl.concurrent.Float2FloatConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2FloatLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2FloatOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2FloatArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2FloatAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2FloatRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatSortedMap;
import speiger.src.testers.floats.builder.maps.Float2FloatMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2FloatNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2FloatMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Float2FloatMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2FloatMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2FloatOpenHashMap", Float2FloatOpenHashMap::new));
		suite.addTest(mapSuite("Float2FloatLinkedOpenHashMap", Float2FloatLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2FloatArrayMap", Float2FloatArrayMap::new));
		suite.addTest(mapSuite("Float2FloatConcurrentOpenHashMap", Float2FloatConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2FloatRBTreeMap", Float2FloatRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2FloatAVLTreeMap", Float2FloatAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], float[], Float2FloatMap> factory) {
		Float2FloatMapTestSuiteBuilder builder = Float2FloatMapTestSuiteBuilder.using(new SimpleFloat2FloatMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], float[], Float2FloatSortedMap> factory) {
		Float2FloatNavigableMapTestSuiteBuilder builder = Float2FloatNavigableMapTestSuiteBuilder.using(new SimpleFloat2FloatMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}