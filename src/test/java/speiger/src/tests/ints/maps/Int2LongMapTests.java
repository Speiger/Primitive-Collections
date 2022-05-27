package speiger.src.tests.ints.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.impl.concurrent.Int2LongConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2LongLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2LongOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2LongArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2LongAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2LongRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongSortedMap;
import speiger.src.testers.ints.builder.maps.Int2LongMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2LongNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2LongMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Int2LongMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2LongMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2LongOpenHashMap", Int2LongOpenHashMap::new));
		suite.addTest(mapSuite("Int2LongLinkedOpenHashMap", Int2LongLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2LongArrayMap", Int2LongArrayMap::new));
		suite.addTest(mapSuite("Int2LongConcurrentOpenHashMap", Int2LongConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2LongRBTreeMap", Int2LongRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2LongAVLTreeMap", Int2LongAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], long[], Int2LongMap> factory) {
		Int2LongMapTestSuiteBuilder builder = Int2LongMapTestSuiteBuilder.using(new SimpleInt2LongMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], long[], Int2LongSortedMap> factory) {
		Int2LongNavigableMapTestSuiteBuilder builder = Int2LongNavigableMapTestSuiteBuilder.using(new SimpleInt2LongMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}