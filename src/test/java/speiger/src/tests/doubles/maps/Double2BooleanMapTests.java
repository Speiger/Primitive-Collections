package speiger.src.tests.doubles.maps;

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
import speiger.src.collections.doubles.maps.impl.concurrent.Double2BooleanConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2BooleanLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2BooleanOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2BooleanArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2BooleanAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2BooleanRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2BooleanMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2BooleanNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2BooleanMapTestGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapPutIfAbsentTester;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Double2BooleanMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2BooleanMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2BooleanOpenHashMap", Double2BooleanOpenHashMap::new));
		suite.addTest(mapSuite("Double2BooleanLinkedOpenHashMap", Double2BooleanLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2BooleanArrayMap", Double2BooleanArrayMap::new));
		suite.addTest(mapSuite("Double2BooleanConcurrentOpenHashMap", Double2BooleanConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2BooleanRBTreeMap", Double2BooleanRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2BooleanAVLTreeMap", Double2BooleanAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], boolean[], Double2BooleanMap> factory) {
		Double2BooleanMapTestSuiteBuilder builder = Double2BooleanMapTestSuiteBuilder.using(new SimpleDouble2BooleanMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], boolean[], Double2BooleanSortedMap> factory) {
		Double2BooleanNavigableMapTestSuiteBuilder builder = Double2BooleanNavigableMapTestSuiteBuilder.using(new SimpleDouble2BooleanMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	public static List<Method> getSuppression() {
		List<Method> list = new ArrayList<>();
		TestUtils.getSurpession(list, Double2BooleanMapComputeTester.class, "testCompute_absentToPresent", "testCompute_presentToPresent");
		TestUtils.getSurpession(list, Double2BooleanMapComputeIfAbsentTester.class, "testComputeIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Double2BooleanMapComputeIfPresentTester.class, "testCompute_presentToPresent", "testComputeIfPresent_supportedPresent");
		TestUtils.getSurpession(list, Double2BooleanMapSupplyIfAbsentTester.class, "testSupplyIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Double2BooleanMapMergeTester.class, "testAbsent");
		TestUtils.getSurpession(list, Double2BooleanMapPutIfAbsentTester.class, "testPutIfAbsent_supportedAbsent");
		
		TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
		TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, Double2BooleanMapContainsValueTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
		TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
		TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
		return list;
	}
	
}