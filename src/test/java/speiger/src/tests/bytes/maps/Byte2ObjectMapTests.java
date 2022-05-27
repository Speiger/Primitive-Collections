package speiger.src.tests.bytes.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2ObjectConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2ObjectLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2ObjectOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2ObjectArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2ObjectAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2ObjectRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2ObjectMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2ObjectNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2ObjectMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Byte2ObjectMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2ObjectMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2ObjectOpenHashMap", Byte2ObjectOpenHashMap::new));
		suite.addTest(mapSuite("Byte2ObjectLinkedOpenHashMap", Byte2ObjectLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2ObjectArrayMap", Byte2ObjectArrayMap::new));
		suite.addTest(mapSuite("Byte2ObjectConcurrentOpenHashMap", Byte2ObjectConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2ObjectRBTreeMap", Byte2ObjectRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2ObjectAVLTreeMap", Byte2ObjectAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], String[], Byte2ObjectMap<String>> factory) {
		SimpleByte2ObjectMapTestGenerator.Maps<String> generator = new SimpleByte2ObjectMapTestGenerator.Maps<>(factory);
		generator.setValues(createValues());
		Byte2ObjectMapTestSuiteBuilder<String> builder = Byte2ObjectMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], String[], Byte2ObjectSortedMap<String>> factory) {
		SimpleByte2ObjectMapTestGenerator.SortedMaps<String> generator = new SimpleByte2ObjectMapTestGenerator.SortedMaps<>(factory);
		generator.setValues(createValues());
		Byte2ObjectNavigableMapTestSuiteBuilder<String> builder = Byte2ObjectNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createValues() {
		return new String[]{"January", "February", "March", "April", "May", "below view", "below view", "above view", "above view"};
	}
	
}