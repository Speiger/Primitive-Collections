package speiger.src.tests.ints.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.impl.concurrent.Int2ShortConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2ShortLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2ShortOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2ShortArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2ShortAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2ShortRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortSortedMap;
import speiger.src.testers.ints.builder.maps.Int2ShortMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2ShortNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2ShortMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Int2ShortMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2ShortMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2ShortOpenHashMap", Int2ShortOpenHashMap::new));
		suite.addTest(mapSuite("Int2ShortLinkedOpenHashMap", Int2ShortLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2ShortArrayMap", Int2ShortArrayMap::new));
		suite.addTest(mapSuite("Int2ShortConcurrentOpenHashMap", Int2ShortConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2ShortRBTreeMap", Int2ShortRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2ShortAVLTreeMap", Int2ShortAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], short[], Int2ShortMap> factory) {
		Int2ShortMapTestSuiteBuilder builder = Int2ShortMapTestSuiteBuilder.using(new SimpleInt2ShortMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], short[], Int2ShortSortedMap> factory) {
		Int2ShortNavigableMapTestSuiteBuilder builder = Int2ShortNavigableMapTestSuiteBuilder.using(new SimpleInt2ShortMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}