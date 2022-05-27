package speiger.src.tests.doubles.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2ShortConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2ShortLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2ShortOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2ShortArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2ShortAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2ShortRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2ShortMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2ShortNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2ShortMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Double2ShortMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2ShortMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2ShortOpenHashMap", Double2ShortOpenHashMap::new));
		suite.addTest(mapSuite("Double2ShortLinkedOpenHashMap", Double2ShortLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2ShortArrayMap", Double2ShortArrayMap::new));
		suite.addTest(mapSuite("Double2ShortConcurrentOpenHashMap", Double2ShortConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2ShortRBTreeMap", Double2ShortRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2ShortAVLTreeMap", Double2ShortAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], short[], Double2ShortMap> factory) {
		Double2ShortMapTestSuiteBuilder builder = Double2ShortMapTestSuiteBuilder.using(new SimpleDouble2ShortMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], short[], Double2ShortSortedMap> factory) {
		Double2ShortNavigableMapTestSuiteBuilder builder = Double2ShortNavigableMapTestSuiteBuilder.using(new SimpleDouble2ShortMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}