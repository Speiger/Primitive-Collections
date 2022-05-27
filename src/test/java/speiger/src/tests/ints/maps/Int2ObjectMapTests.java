package speiger.src.tests.ints.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.impl.concurrent.Int2ObjectConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2ObjectLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2ObjectOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2ObjectArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2ObjectAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2ObjectRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectSortedMap;
import speiger.src.testers.ints.builder.maps.Int2ObjectMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2ObjectNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2ObjectMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Int2ObjectMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2ObjectMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2ObjectOpenHashMap", Int2ObjectOpenHashMap::new));
		suite.addTest(mapSuite("Int2ObjectLinkedOpenHashMap", Int2ObjectLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2ObjectArrayMap", Int2ObjectArrayMap::new));
		suite.addTest(mapSuite("Int2ObjectConcurrentOpenHashMap", Int2ObjectConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2ObjectRBTreeMap", Int2ObjectRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2ObjectAVLTreeMap", Int2ObjectAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], String[], Int2ObjectMap<String>> factory) {
		SimpleInt2ObjectMapTestGenerator.Maps<String> generator = new SimpleInt2ObjectMapTestGenerator.Maps<>(factory);
		generator.setValues(createValues());
		Int2ObjectMapTestSuiteBuilder<String> builder = Int2ObjectMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], String[], Int2ObjectSortedMap<String>> factory) {
		SimpleInt2ObjectMapTestGenerator.SortedMaps<String> generator = new SimpleInt2ObjectMapTestGenerator.SortedMaps<>(factory);
		generator.setValues(createValues());
		Int2ObjectNavigableMapTestSuiteBuilder<String> builder = Int2ObjectNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createValues() {
		return new String[]{"January", "February", "March", "April", "May", "below view", "below view", "above view", "above view"};
	}
	
}