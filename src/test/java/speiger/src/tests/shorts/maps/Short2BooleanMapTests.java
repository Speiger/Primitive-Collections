package speiger.src.tests.shorts.maps;

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
import speiger.src.collections.shorts.maps.impl.concurrent.Short2BooleanConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2BooleanLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2BooleanOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2BooleanArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2BooleanAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2BooleanRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2BooleanMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2BooleanNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2BooleanMapTestGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapPutIfAbsentTester;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.utils.SpecialFeature;

public class Short2BooleanMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2BooleanMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2BooleanOpenHashMap", Short2BooleanOpenHashMap::new));
		suite.addTest(mapSuite("Short2BooleanLinkedOpenHashMap", Short2BooleanLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2BooleanArrayMap", Short2BooleanArrayMap::new));
		suite.addTest(mapSuite("Short2BooleanConcurrentOpenHashMap", Short2BooleanConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2BooleanRBTreeMap", Short2BooleanRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2BooleanAVLTreeMap", Short2BooleanAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], boolean[], Short2BooleanMap> factory) {
		Short2BooleanMapTestSuiteBuilder builder = Short2BooleanMapTestSuiteBuilder.using(new SimpleShort2BooleanMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], boolean[], Short2BooleanSortedMap> factory) {
		Short2BooleanNavigableMapTestSuiteBuilder builder = Short2BooleanNavigableMapTestSuiteBuilder.using(new SimpleShort2BooleanMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	public static List<Method> getSuppression() {
		List<Method> list = new ArrayList<>();
		TestUtils.getSurpession(list, Short2BooleanMapComputeTester.class, "testCompute_absentToPresent", "testCompute_presentToPresent");
		TestUtils.getSurpession(list, Short2BooleanMapComputeIfAbsentTester.class, "testComputeIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Short2BooleanMapComputeIfPresentTester.class, "testCompute_presentToPresent", "testComputeIfPresent_supportedPresent");
		TestUtils.getSurpession(list, Short2BooleanMapSupplyIfAbsentTester.class, "testSupplyIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Short2BooleanMapMergeTester.class, "testAbsent");
		TestUtils.getSurpession(list, Short2BooleanMapPutIfAbsentTester.class, "testPutIfAbsent_supportedAbsent");
		
		TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
		TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, Short2BooleanMapContainsValueTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
		TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
		TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
		return list;
	}
	
}