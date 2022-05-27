package speiger.src.tests.shorts.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2ObjectConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ObjectLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ObjectOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2ObjectArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ObjectAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ObjectRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2ObjectMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2ObjectNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2ObjectMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2ObjectMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2ObjectMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2ObjectOpenHashMap", Short2ObjectOpenHashMap::new));
		suite.addTest(mapSuite("Short2ObjectLinkedOpenHashMap", Short2ObjectLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2ObjectArrayMap", Short2ObjectArrayMap::new));
		suite.addTest(mapSuite("Short2ObjectConcurrentOpenHashMap", Short2ObjectConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2ObjectRBTreeMap", Short2ObjectRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2ObjectAVLTreeMap", Short2ObjectAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], String[], Short2ObjectMap<String>> factory) {
		SimpleShort2ObjectMapTestGenerator.Maps<String> generator = new SimpleShort2ObjectMapTestGenerator.Maps<>(factory);
		generator.setValues(createValues());
		Short2ObjectMapTestSuiteBuilder<String> builder = Short2ObjectMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], String[], Short2ObjectSortedMap<String>> factory) {
		SimpleShort2ObjectMapTestGenerator.SortedMaps<String> generator = new SimpleShort2ObjectMapTestGenerator.SortedMaps<>(factory);
		generator.setValues(createValues());
		Short2ObjectNavigableMapTestSuiteBuilder<String> builder = Short2ObjectNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createValues() {
		return new String[]{"January", "February", "March", "April", "May", "below view", "below view", "above view", "above view"};
	}
	
}