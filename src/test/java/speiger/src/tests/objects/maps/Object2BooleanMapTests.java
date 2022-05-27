package speiger.src.tests.objects.maps;

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
import speiger.src.collections.objects.maps.impl.concurrent.Object2BooleanConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2BooleanLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2BooleanOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2BooleanArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2BooleanAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2BooleanRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanSortedMap;
import speiger.src.testers.objects.builder.maps.Object2BooleanMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2BooleanNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2BooleanMapTestGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapPutIfAbsentTester;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.utils.SpecialFeature;

public class Object2BooleanMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2BooleanMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2BooleanOpenHashMap", Object2BooleanOpenHashMap::new));
		suite.addTest(mapSuite("Object2BooleanLinkedOpenHashMap", Object2BooleanLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2BooleanArrayMap", Object2BooleanArrayMap::new));
		suite.addTest(mapSuite("Object2BooleanConcurrentOpenHashMap", Object2BooleanConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2BooleanRBTreeMap", Object2BooleanRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2BooleanAVLTreeMap", Object2BooleanAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], boolean[], Object2BooleanMap<String>> factory) {
		SimpleObject2BooleanMapTestGenerator.Maps<String> generator = new SimpleObject2BooleanMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		Object2BooleanMapTestSuiteBuilder<String> builder = Object2BooleanMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], boolean[], Object2BooleanSortedMap<String>> factory) {
		SimpleObject2BooleanMapTestGenerator.SortedMaps<String> generator = new SimpleObject2BooleanMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		Object2BooleanNavigableMapTestSuiteBuilder<String> builder = Object2BooleanNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
	public static List<Method> getSuppression() {
		List<Method> list = new ArrayList<>();
		TestUtils.getSurpession(list, Object2BooleanMapComputeTester.class, "testCompute_absentToPresent", "testCompute_presentToPresent");
		TestUtils.getSurpession(list, Object2BooleanMapComputeIfAbsentTester.class, "testComputeIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Object2BooleanMapComputeIfPresentTester.class, "testCompute_presentToPresent", "testComputeIfPresent_supportedPresent");
		TestUtils.getSurpession(list, Object2BooleanMapSupplyIfAbsentTester.class, "testSupplyIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Object2BooleanMapMergeTester.class, "testAbsent");
		TestUtils.getSurpession(list, Object2BooleanMapPutIfAbsentTester.class, "testPutIfAbsent_supportedAbsent");
		
		TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
		TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, Object2BooleanMapContainsValueTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
		TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
		TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
		return list;
	}
	
}