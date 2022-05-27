package speiger.src.tests.bytes.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2FloatConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2FloatLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2FloatOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2FloatArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2FloatAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2FloatRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2FloatMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2FloatNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2FloatMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Byte2FloatMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2FloatMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2FloatOpenHashMap", Byte2FloatOpenHashMap::new));
		suite.addTest(mapSuite("Byte2FloatLinkedOpenHashMap", Byte2FloatLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2FloatArrayMap", Byte2FloatArrayMap::new));
		suite.addTest(mapSuite("Byte2FloatConcurrentOpenHashMap", Byte2FloatConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2FloatRBTreeMap", Byte2FloatRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2FloatAVLTreeMap", Byte2FloatAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], float[], Byte2FloatMap> factory) {
		Byte2FloatMapTestSuiteBuilder builder = Byte2FloatMapTestSuiteBuilder.using(new SimpleByte2FloatMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], float[], Byte2FloatSortedMap> factory) {
		Byte2FloatNavigableMapTestSuiteBuilder builder = Byte2FloatNavigableMapTestSuiteBuilder.using(new SimpleByte2FloatMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}