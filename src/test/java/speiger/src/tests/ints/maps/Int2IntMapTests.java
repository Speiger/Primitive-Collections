package speiger.src.tests.ints.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.impl.concurrent.Int2IntConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2IntLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2IntOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2IntArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2IntAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2IntRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntSortedMap;
import speiger.src.testers.ints.builder.maps.Int2IntMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2IntNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2IntMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Int2IntMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2IntMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2IntOpenHashMap", Int2IntOpenHashMap::new));
		suite.addTest(mapSuite("Int2IntLinkedOpenHashMap", Int2IntLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2IntArrayMap", Int2IntArrayMap::new));
		suite.addTest(mapSuite("Int2IntConcurrentOpenHashMap", Int2IntConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2IntRBTreeMap", Int2IntRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2IntAVLTreeMap", Int2IntAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], int[], Int2IntMap> factory) {
		Int2IntMapTestSuiteBuilder builder = Int2IntMapTestSuiteBuilder.using(new SimpleInt2IntMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], int[], Int2IntSortedMap> factory) {
		Int2IntNavigableMapTestSuiteBuilder builder = Int2IntNavigableMapTestSuiteBuilder.using(new SimpleInt2IntMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}