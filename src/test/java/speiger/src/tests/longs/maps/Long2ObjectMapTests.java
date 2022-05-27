package speiger.src.tests.longs.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2ObjectConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ObjectLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2ObjectOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2ObjectArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ObjectAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2ObjectRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectSortedMap;
import speiger.src.testers.longs.builder.maps.Long2ObjectMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2ObjectNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2ObjectMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Long2ObjectMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2ObjectMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2ObjectOpenHashMap", Long2ObjectOpenHashMap::new));
		suite.addTest(mapSuite("Long2ObjectLinkedOpenHashMap", Long2ObjectLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2ObjectArrayMap", Long2ObjectArrayMap::new));
		suite.addTest(mapSuite("Long2ObjectConcurrentOpenHashMap", Long2ObjectConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2ObjectRBTreeMap", Long2ObjectRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2ObjectAVLTreeMap", Long2ObjectAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], String[], Long2ObjectMap<String>> factory) {
		SimpleLong2ObjectMapTestGenerator.Maps<String> generator = new SimpleLong2ObjectMapTestGenerator.Maps<>(factory);
		generator.setValues(createValues());
		Long2ObjectMapTestSuiteBuilder<String> builder = Long2ObjectMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], String[], Long2ObjectSortedMap<String>> factory) {
		SimpleLong2ObjectMapTestGenerator.SortedMaps<String> generator = new SimpleLong2ObjectMapTestGenerator.SortedMaps<>(factory);
		generator.setValues(createValues());
		Long2ObjectNavigableMapTestSuiteBuilder<String> builder = Long2ObjectNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createValues() {
		return new String[]{"January", "February", "March", "April", "May", "below view", "below view", "above view", "above view"};
	}
	
}