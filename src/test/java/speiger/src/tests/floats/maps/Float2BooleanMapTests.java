package speiger.src.tests.floats.maps;

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
import speiger.src.collections.floats.maps.impl.concurrent.Float2BooleanConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2BooleanLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2BooleanOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2BooleanArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2BooleanAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2BooleanRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanSortedMap;
import speiger.src.testers.floats.builder.maps.Float2BooleanMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2BooleanNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2BooleanMapTestGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapPutIfAbsentTester;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.utils.SpecialFeature;

public class Float2BooleanMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2BooleanMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2BooleanOpenHashMap", Float2BooleanOpenHashMap::new));
		suite.addTest(mapSuite("Float2BooleanLinkedOpenHashMap", Float2BooleanLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2BooleanArrayMap", Float2BooleanArrayMap::new));
		suite.addTest(mapSuite("Float2BooleanConcurrentOpenHashMap", Float2BooleanConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2BooleanRBTreeMap", Float2BooleanRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2BooleanAVLTreeMap", Float2BooleanAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], boolean[], Float2BooleanMap> factory) {
		Float2BooleanMapTestSuiteBuilder builder = Float2BooleanMapTestSuiteBuilder.using(new SimpleFloat2BooleanMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], boolean[], Float2BooleanSortedMap> factory) {
		Float2BooleanNavigableMapTestSuiteBuilder builder = Float2BooleanNavigableMapTestSuiteBuilder.using(new SimpleFloat2BooleanMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	public static List<Method> getSuppression() {
		List<Method> list = new ArrayList<>();
		TestUtils.getSurpession(list, Float2BooleanMapComputeTester.class, "testCompute_absentToPresent", "testCompute_presentToPresent");
		TestUtils.getSurpession(list, Float2BooleanMapComputeIfAbsentTester.class, "testComputeIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Float2BooleanMapComputeIfPresentTester.class, "testCompute_presentToPresent", "testComputeIfPresent_supportedPresent");
		TestUtils.getSurpession(list, Float2BooleanMapSupplyIfAbsentTester.class, "testSupplyIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Float2BooleanMapMergeTester.class, "testAbsent");
		TestUtils.getSurpession(list, Float2BooleanMapPutIfAbsentTester.class, "testPutIfAbsent_supportedAbsent");
		
		TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
		TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, Float2BooleanMapContainsValueTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
		TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
		TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
		return list;
	}
	
}