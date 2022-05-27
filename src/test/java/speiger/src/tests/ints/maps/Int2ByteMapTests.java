package speiger.src.tests.ints.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.impl.concurrent.Int2ByteConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2ByteLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2ByteOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2ByteArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2ByteAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2ByteRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteSortedMap;
import speiger.src.testers.ints.builder.maps.Int2ByteMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2ByteNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2ByteMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Int2ByteMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2ByteMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2ByteOpenHashMap", Int2ByteOpenHashMap::new));
		suite.addTest(mapSuite("Int2ByteLinkedOpenHashMap", Int2ByteLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2ByteArrayMap", Int2ByteArrayMap::new));
		suite.addTest(mapSuite("Int2ByteConcurrentOpenHashMap", Int2ByteConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2ByteRBTreeMap", Int2ByteRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2ByteAVLTreeMap", Int2ByteAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], byte[], Int2ByteMap> factory) {
		Int2ByteMapTestSuiteBuilder builder = Int2ByteMapTestSuiteBuilder.using(new SimpleInt2ByteMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], byte[], Int2ByteSortedMap> factory) {
		Int2ByteNavigableMapTestSuiteBuilder builder = Int2ByteNavigableMapTestSuiteBuilder.using(new SimpleInt2ByteMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}