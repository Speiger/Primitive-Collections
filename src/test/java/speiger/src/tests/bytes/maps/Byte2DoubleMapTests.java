package speiger.src.tests.bytes.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2DoubleConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2DoubleLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2DoubleOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2DoubleArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2DoubleAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2DoubleRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2DoubleMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2DoubleNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2DoubleMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Byte2DoubleMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2DoubleMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2DoubleOpenHashMap", Byte2DoubleOpenHashMap::new));
		suite.addTest(mapSuite("Byte2DoubleLinkedOpenHashMap", Byte2DoubleLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2DoubleArrayMap", Byte2DoubleArrayMap::new));
		suite.addTest(mapSuite("Byte2DoubleConcurrentOpenHashMap", Byte2DoubleConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2DoubleRBTreeMap", Byte2DoubleRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2DoubleAVLTreeMap", Byte2DoubleAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], double[], Byte2DoubleMap> factory) {
		Byte2DoubleMapTestSuiteBuilder builder = Byte2DoubleMapTestSuiteBuilder.using(new SimpleByte2DoubleMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], double[], Byte2DoubleSortedMap> factory) {
		Byte2DoubleNavigableMapTestSuiteBuilder builder = Byte2DoubleNavigableMapTestSuiteBuilder.using(new SimpleByte2DoubleMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}