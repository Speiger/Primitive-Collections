package speiger.src.tests.shorts.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2DoubleConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2DoubleLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2DoubleOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2DoubleArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2DoubleAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2DoubleRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2DoubleMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2DoubleNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2DoubleMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Short2DoubleMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2DoubleMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2DoubleOpenHashMap", Short2DoubleOpenHashMap::new));
		suite.addTest(mapSuite("Short2DoubleLinkedOpenHashMap", Short2DoubleLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2DoubleArrayMap", Short2DoubleArrayMap::new));
		suite.addTest(mapSuite("Short2DoubleConcurrentOpenHashMap", Short2DoubleConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2DoubleRBTreeMap", Short2DoubleRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2DoubleAVLTreeMap", Short2DoubleAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], double[], Short2DoubleMap> factory) {
		Short2DoubleMapTestSuiteBuilder builder = Short2DoubleMapTestSuiteBuilder.using(new SimpleShort2DoubleMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], double[], Short2DoubleSortedMap> factory) {
		Short2DoubleNavigableMapTestSuiteBuilder builder = Short2DoubleNavigableMapTestSuiteBuilder.using(new SimpleShort2DoubleMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}