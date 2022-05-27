package speiger.src.tests.doubles.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2FloatConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2FloatLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2FloatOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2FloatArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2FloatAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2FloatRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2FloatMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2FloatNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2FloatMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Double2FloatMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2FloatMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2FloatOpenHashMap", Double2FloatOpenHashMap::new));
		suite.addTest(mapSuite("Double2FloatLinkedOpenHashMap", Double2FloatLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2FloatArrayMap", Double2FloatArrayMap::new));
		suite.addTest(mapSuite("Double2FloatConcurrentOpenHashMap", Double2FloatConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2FloatRBTreeMap", Double2FloatRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2FloatAVLTreeMap", Double2FloatAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], float[], Double2FloatMap> factory) {
		Double2FloatMapTestSuiteBuilder builder = Double2FloatMapTestSuiteBuilder.using(new SimpleDouble2FloatMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], float[], Double2FloatSortedMap> factory) {
		Double2FloatNavigableMapTestSuiteBuilder builder = Double2FloatNavigableMapTestSuiteBuilder.using(new SimpleDouble2FloatMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}