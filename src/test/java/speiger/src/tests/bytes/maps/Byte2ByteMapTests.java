package speiger.src.tests.bytes.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2ByteConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2ByteLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2ByteOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2ByteArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2ByteAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2ByteRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2ByteMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2ByteNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2ByteMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Byte2ByteMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2ByteMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2ByteOpenHashMap", Byte2ByteOpenHashMap::new));
		suite.addTest(mapSuite("Byte2ByteLinkedOpenHashMap", Byte2ByteLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2ByteArrayMap", Byte2ByteArrayMap::new));
		suite.addTest(mapSuite("Byte2ByteConcurrentOpenHashMap", Byte2ByteConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2ByteRBTreeMap", Byte2ByteRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2ByteAVLTreeMap", Byte2ByteAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], byte[], Byte2ByteMap> factory) {
		Byte2ByteMapTestSuiteBuilder builder = Byte2ByteMapTestSuiteBuilder.using(new SimpleByte2ByteMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], byte[], Byte2ByteSortedMap> factory) {
		Byte2ByteNavigableMapTestSuiteBuilder builder = Byte2ByteNavigableMapTestSuiteBuilder.using(new SimpleByte2ByteMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}