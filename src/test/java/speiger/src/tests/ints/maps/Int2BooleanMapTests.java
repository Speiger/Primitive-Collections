package speiger.src.tests.ints.maps;

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
import speiger.src.collections.ints.maps.impl.concurrent.Int2BooleanConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2BooleanLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2BooleanOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2BooleanArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2BooleanAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2BooleanRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanSortedMap;
import speiger.src.testers.ints.builder.maps.Int2BooleanMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2BooleanNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2BooleanMapTestGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapPutIfAbsentTester;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Int2BooleanMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2BooleanMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2BooleanOpenHashMap", Int2BooleanOpenHashMap::new));
		suite.addTest(mapSuite("Int2BooleanLinkedOpenHashMap", Int2BooleanLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2BooleanArrayMap", Int2BooleanArrayMap::new));
		suite.addTest(mapSuite("Int2BooleanConcurrentOpenHashMap", Int2BooleanConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2BooleanRBTreeMap", Int2BooleanRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2BooleanAVLTreeMap", Int2BooleanAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], boolean[], Int2BooleanMap> factory) {
		Int2BooleanMapTestSuiteBuilder builder = Int2BooleanMapTestSuiteBuilder.using(new SimpleInt2BooleanMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], boolean[], Int2BooleanSortedMap> factory) {
		Int2BooleanNavigableMapTestSuiteBuilder builder = Int2BooleanNavigableMapTestSuiteBuilder.using(new SimpleInt2BooleanMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	public static List<Method> getSuppression() {
		List<Method> list = new ArrayList<>();
		TestUtils.getSurpession(list, Int2BooleanMapComputeTester.class, "testCompute_absentToPresent", "testCompute_presentToPresent");
		TestUtils.getSurpession(list, Int2BooleanMapComputeIfAbsentTester.class, "testComputeIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Int2BooleanMapComputeIfPresentTester.class, "testCompute_presentToPresent", "testComputeIfPresent_supportedPresent");
		TestUtils.getSurpession(list, Int2BooleanMapSupplyIfAbsentTester.class, "testSupplyIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Int2BooleanMapMergeTester.class, "testAbsent");
		TestUtils.getSurpession(list, Int2BooleanMapPutIfAbsentTester.class, "testPutIfAbsent_supportedAbsent");
		
		TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
		TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, Int2BooleanMapContainsValueTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
		TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
		TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
		return list;
	}
	
}