package speiger.src.tests.bytes.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2LongConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2LongLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2LongOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2LongArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2LongAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2LongRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2LongMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2LongNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2LongMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Byte2LongMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2LongMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2LongOpenHashMap", Byte2LongOpenHashMap::new));
		suite.addTest(mapSuite("Byte2LongLinkedOpenHashMap", Byte2LongLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2LongArrayMap", Byte2LongArrayMap::new));
		suite.addTest(mapSuite("Byte2LongConcurrentOpenHashMap", Byte2LongConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2LongRBTreeMap", Byte2LongRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2LongAVLTreeMap", Byte2LongAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], long[], Byte2LongMap> factory) {
		Byte2LongMapTestSuiteBuilder builder = Byte2LongMapTestSuiteBuilder.using(new SimpleByte2LongMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], long[], Byte2LongSortedMap> factory) {
		Byte2LongNavigableMapTestSuiteBuilder builder = Byte2LongNavigableMapTestSuiteBuilder.using(new SimpleByte2LongMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}