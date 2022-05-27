package speiger.src.tests.doubles.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2LongConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2LongLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2LongOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2LongArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2LongAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2LongRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2LongMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2LongNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2LongMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Double2LongMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2LongMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2LongOpenHashMap", Double2LongOpenHashMap::new));
		suite.addTest(mapSuite("Double2LongLinkedOpenHashMap", Double2LongLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2LongArrayMap", Double2LongArrayMap::new));
		suite.addTest(mapSuite("Double2LongConcurrentOpenHashMap", Double2LongConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2LongRBTreeMap", Double2LongRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2LongAVLTreeMap", Double2LongAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], long[], Double2LongMap> factory) {
		Double2LongMapTestSuiteBuilder builder = Double2LongMapTestSuiteBuilder.using(new SimpleDouble2LongMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], long[], Double2LongSortedMap> factory) {
		Double2LongNavigableMapTestSuiteBuilder builder = Double2LongNavigableMapTestSuiteBuilder.using(new SimpleDouble2LongMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}