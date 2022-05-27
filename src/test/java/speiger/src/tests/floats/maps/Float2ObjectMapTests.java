package speiger.src.tests.floats.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.maps.impl.concurrent.Float2ObjectConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ObjectLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ObjectOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2ObjectArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ObjectAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ObjectRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectSortedMap;
import speiger.src.testers.floats.builder.maps.Float2ObjectMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2ObjectNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2ObjectMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Float2ObjectMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2ObjectMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2ObjectOpenHashMap", Float2ObjectOpenHashMap::new));
		suite.addTest(mapSuite("Float2ObjectLinkedOpenHashMap", Float2ObjectLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2ObjectArrayMap", Float2ObjectArrayMap::new));
		suite.addTest(mapSuite("Float2ObjectConcurrentOpenHashMap", Float2ObjectConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2ObjectRBTreeMap", Float2ObjectRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2ObjectAVLTreeMap", Float2ObjectAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], String[], Float2ObjectMap<String>> factory) {
		SimpleFloat2ObjectMapTestGenerator.Maps<String> generator = new SimpleFloat2ObjectMapTestGenerator.Maps<>(factory);
		generator.setValues(createValues());
		Float2ObjectMapTestSuiteBuilder<String> builder = Float2ObjectMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], String[], Float2ObjectSortedMap<String>> factory) {
		SimpleFloat2ObjectMapTestGenerator.SortedMaps<String> generator = new SimpleFloat2ObjectMapTestGenerator.SortedMaps<>(factory);
		generator.setValues(createValues());
		Float2ObjectNavigableMapTestSuiteBuilder<String> builder = Float2ObjectNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createValues() {
		return new String[]{"January", "February", "March", "April", "May", "below view", "below view", "above view", "above view"};
	}
	
}