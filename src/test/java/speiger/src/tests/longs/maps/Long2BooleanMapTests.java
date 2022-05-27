package speiger.src.tests.longs.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionAddAllTester;
import com.google.common.collect.testing.testers.CollectionAddTester;
import com.google.common.collect.testing.testers.CollectionContainsAllTester;
import com.google.common.collect.testing.testers.CollectionContainsTester;
import com.google.common.collect.testing.testers.CollectionRetainAllTester;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2BooleanConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2BooleanLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2BooleanOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2BooleanArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2BooleanAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2BooleanRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanSortedMap;
import speiger.src.testers.longs.builder.maps.Long2BooleanMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2BooleanNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2BooleanMapTestGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapPutIfAbsentTester;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.utils.SpecialFeature;

public class Long2BooleanMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2BooleanMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2BooleanOpenHashMap", Long2BooleanOpenHashMap::new));
		suite.addTest(mapSuite("Long2BooleanLinkedOpenHashMap", Long2BooleanLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2BooleanArrayMap", Long2BooleanArrayMap::new));
		suite.addTest(mapSuite("Long2BooleanConcurrentOpenHashMap", Long2BooleanConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2BooleanRBTreeMap", Long2BooleanRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2BooleanAVLTreeMap", Long2BooleanAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], boolean[], Long2BooleanMap> factory) {
		Long2BooleanMapTestSuiteBuilder builder = Long2BooleanMapTestSuiteBuilder.using(new SimpleLong2BooleanMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], boolean[], Long2BooleanSortedMap> factory) {
		Long2BooleanNavigableMapTestSuiteBuilder builder = Long2BooleanNavigableMapTestSuiteBuilder.using(new SimpleLong2BooleanMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	public static List<Method> getSuppression() {
		List<Method> list = new ArrayList<>();
		TestUtils.getSurpession(list, Long2BooleanMapComputeTester.class, "testCompute_absentToPresent", "testCompute_presentToPresent");
		TestUtils.getSurpession(list, Long2BooleanMapComputeIfAbsentTester.class, "testComputeIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Long2BooleanMapComputeIfPresentTester.class, "testCompute_presentToPresent", "testComputeIfPresent_supportedPresent");
		TestUtils.getSurpession(list, Long2BooleanMapSupplyIfAbsentTester.class, "testSupplyIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Long2BooleanMapMergeTester.class, "testAbsent");
		TestUtils.getSurpession(list, Long2BooleanMapPutIfAbsentTester.class, "testPutIfAbsent_supportedAbsent");
		
		TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
		TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, Long2BooleanMapContainsValueTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
		TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
		TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
		return list;
	}
	
}