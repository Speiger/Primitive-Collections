package speiger.src.tests.doubles.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2IntConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2IntLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2IntOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2IntArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2IntAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2IntRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2IntMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2IntNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2IntMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Double2IntMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2IntMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2IntOpenHashMap", Double2IntOpenHashMap::new));
		suite.addTest(mapSuite("Double2IntLinkedOpenHashMap", Double2IntLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2IntArrayMap", Double2IntArrayMap::new));
		suite.addTest(mapSuite("Double2IntConcurrentOpenHashMap", Double2IntConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2IntRBTreeMap", Double2IntRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2IntAVLTreeMap", Double2IntAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], int[], Double2IntMap> factory) {
		Double2IntMapTestSuiteBuilder builder = Double2IntMapTestSuiteBuilder.using(new SimpleDouble2IntMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], int[], Double2IntSortedMap> factory) {
		Double2IntNavigableMapTestSuiteBuilder builder = Double2IntNavigableMapTestSuiteBuilder.using(new SimpleDouble2IntMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}