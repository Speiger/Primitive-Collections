package speiger.src.tests.bytes.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2IntConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2IntLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2IntOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2IntArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2IntAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2IntRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2IntMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2IntNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2IntMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Byte2IntMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2IntMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2IntOpenHashMap", Byte2IntOpenHashMap::new));
		suite.addTest(mapSuite("Byte2IntLinkedOpenHashMap", Byte2IntLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2IntArrayMap", Byte2IntArrayMap::new));
		suite.addTest(mapSuite("Byte2IntConcurrentOpenHashMap", Byte2IntConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2IntRBTreeMap", Byte2IntRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2IntAVLTreeMap", Byte2IntAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], int[], Byte2IntMap> factory) {
		Byte2IntMapTestSuiteBuilder builder = Byte2IntMapTestSuiteBuilder.using(new SimpleByte2IntMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], int[], Byte2IntSortedMap> factory) {
		Byte2IntNavigableMapTestSuiteBuilder builder = Byte2IntNavigableMapTestSuiteBuilder.using(new SimpleByte2IntMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}