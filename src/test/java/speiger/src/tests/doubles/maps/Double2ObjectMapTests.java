package speiger.src.tests.doubles.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2ObjectConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2ObjectLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2ObjectOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2ObjectArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2ObjectAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2ObjectRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2ObjectMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2ObjectNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2ObjectMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Double2ObjectMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2ObjectMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2ObjectOpenHashMap", Double2ObjectOpenHashMap::new));
		suite.addTest(mapSuite("Double2ObjectLinkedOpenHashMap", Double2ObjectLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2ObjectArrayMap", Double2ObjectArrayMap::new));
		suite.addTest(mapSuite("Double2ObjectConcurrentOpenHashMap", Double2ObjectConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2ObjectRBTreeMap", Double2ObjectRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2ObjectAVLTreeMap", Double2ObjectAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], String[], Double2ObjectMap<String>> factory) {
		SimpleDouble2ObjectMapTestGenerator.Maps<String> generator = new SimpleDouble2ObjectMapTestGenerator.Maps<>(factory);
		generator.setValues(createValues());
		Double2ObjectMapTestSuiteBuilder<String> builder = Double2ObjectMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], String[], Double2ObjectSortedMap<String>> factory) {
		SimpleDouble2ObjectMapTestGenerator.SortedMaps<String> generator = new SimpleDouble2ObjectMapTestGenerator.SortedMaps<>(factory);
		generator.setValues(createValues());
		Double2ObjectNavigableMapTestSuiteBuilder<String> builder = Double2ObjectNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createValues() {
		return new String[]{"January", "February", "March", "April", "May", "below view", "below view", "above view", "above view"};
	}
	
}