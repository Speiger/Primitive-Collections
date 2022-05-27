package speiger.src.tests.doubles.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2DoubleConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2DoubleLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2DoubleOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2DoubleArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2DoubleAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2DoubleRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2DoubleMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2DoubleNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2DoubleMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Double2DoubleMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2DoubleMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2DoubleOpenHashMap", Double2DoubleOpenHashMap::new));
		suite.addTest(mapSuite("Double2DoubleLinkedOpenHashMap", Double2DoubleLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2DoubleArrayMap", Double2DoubleArrayMap::new));
		suite.addTest(mapSuite("Double2DoubleConcurrentOpenHashMap", Double2DoubleConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2DoubleRBTreeMap", Double2DoubleRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2DoubleAVLTreeMap", Double2DoubleAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], double[], Double2DoubleMap> factory) {
		Double2DoubleMapTestSuiteBuilder builder = Double2DoubleMapTestSuiteBuilder.using(new SimpleDouble2DoubleMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], double[], Double2DoubleSortedMap> factory) {
		Double2DoubleNavigableMapTestSuiteBuilder builder = Double2DoubleNavigableMapTestSuiteBuilder.using(new SimpleDouble2DoubleMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}