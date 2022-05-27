package speiger.src.tests.ints.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.impl.concurrent.Int2FloatConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2FloatLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2FloatOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2FloatArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2FloatAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2FloatRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatSortedMap;
import speiger.src.testers.ints.builder.maps.Int2FloatMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2FloatNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2FloatMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Int2FloatMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2FloatMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2FloatOpenHashMap", Int2FloatOpenHashMap::new));
		suite.addTest(mapSuite("Int2FloatLinkedOpenHashMap", Int2FloatLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2FloatArrayMap", Int2FloatArrayMap::new));
		suite.addTest(mapSuite("Int2FloatConcurrentOpenHashMap", Int2FloatConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2FloatRBTreeMap", Int2FloatRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2FloatAVLTreeMap", Int2FloatAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], float[], Int2FloatMap> factory) {
		Int2FloatMapTestSuiteBuilder builder = Int2FloatMapTestSuiteBuilder.using(new SimpleInt2FloatMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], float[], Int2FloatSortedMap> factory) {
		Int2FloatNavigableMapTestSuiteBuilder builder = Int2FloatNavigableMapTestSuiteBuilder.using(new SimpleInt2FloatMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}