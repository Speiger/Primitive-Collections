package speiger.src.tests.bytes.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2ShortConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2ShortLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2ShortOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2ShortArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2ShortAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2ShortRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2ShortMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2ShortNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2ShortMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Byte2ShortMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2ShortMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2ShortOpenHashMap", Byte2ShortOpenHashMap::new));
		suite.addTest(mapSuite("Byte2ShortLinkedOpenHashMap", Byte2ShortLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2ShortArrayMap", Byte2ShortArrayMap::new));
		suite.addTest(mapSuite("Byte2ShortConcurrentOpenHashMap", Byte2ShortConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2ShortRBTreeMap", Byte2ShortRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2ShortAVLTreeMap", Byte2ShortAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], short[], Byte2ShortMap> factory) {
		Byte2ShortMapTestSuiteBuilder builder = Byte2ShortMapTestSuiteBuilder.using(new SimpleByte2ShortMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], short[], Byte2ShortSortedMap> factory) {
		Byte2ShortNavigableMapTestSuiteBuilder builder = Byte2ShortNavigableMapTestSuiteBuilder.using(new SimpleByte2ShortMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}