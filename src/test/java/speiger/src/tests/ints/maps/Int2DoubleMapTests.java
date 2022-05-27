package speiger.src.tests.ints.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.impl.concurrent.Int2DoubleConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2DoubleLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2DoubleOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2DoubleArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2DoubleAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2DoubleRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleSortedMap;
import speiger.src.testers.ints.builder.maps.Int2DoubleMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2DoubleNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2DoubleMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Int2DoubleMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2DoubleMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2DoubleOpenHashMap", Int2DoubleOpenHashMap::new));
		suite.addTest(mapSuite("Int2DoubleLinkedOpenHashMap", Int2DoubleLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2DoubleArrayMap", Int2DoubleArrayMap::new));
		suite.addTest(mapSuite("Int2DoubleConcurrentOpenHashMap", Int2DoubleConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2DoubleRBTreeMap", Int2DoubleRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2DoubleAVLTreeMap", Int2DoubleAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], double[], Int2DoubleMap> factory) {
		Int2DoubleMapTestSuiteBuilder builder = Int2DoubleMapTestSuiteBuilder.using(new SimpleInt2DoubleMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], double[], Int2DoubleSortedMap> factory) {
		Int2DoubleNavigableMapTestSuiteBuilder builder = Int2DoubleNavigableMapTestSuiteBuilder.using(new SimpleInt2DoubleMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}