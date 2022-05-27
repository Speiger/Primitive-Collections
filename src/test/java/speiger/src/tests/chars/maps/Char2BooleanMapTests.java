package speiger.src.tests.chars.maps;

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
import speiger.src.collections.chars.maps.impl.concurrent.Char2BooleanConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2BooleanLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2BooleanOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2BooleanArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2BooleanAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2BooleanRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanSortedMap;
import speiger.src.testers.chars.builder.maps.Char2BooleanMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2BooleanNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2BooleanMapTestGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2BooleanMapPutIfAbsentTester;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.utils.SpecialFeature;

public class Char2BooleanMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2BooleanMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2BooleanOpenHashMap", Char2BooleanOpenHashMap::new));
		suite.addTest(mapSuite("Char2BooleanLinkedOpenHashMap", Char2BooleanLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2BooleanArrayMap", Char2BooleanArrayMap::new));
		suite.addTest(mapSuite("Char2BooleanConcurrentOpenHashMap", Char2BooleanConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2BooleanRBTreeMap", Char2BooleanRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2BooleanAVLTreeMap", Char2BooleanAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], boolean[], Char2BooleanMap> factory) {
		Char2BooleanMapTestSuiteBuilder builder = Char2BooleanMapTestSuiteBuilder.using(new SimpleChar2BooleanMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], boolean[], Char2BooleanSortedMap> factory) {
		Char2BooleanNavigableMapTestSuiteBuilder builder = Char2BooleanNavigableMapTestSuiteBuilder.using(new SimpleChar2BooleanMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	public static List<Method> getSuppression() {
		List<Method> list = new ArrayList<>();
		TestUtils.getSurpession(list, Char2BooleanMapComputeTester.class, "testCompute_absentToPresent", "testCompute_presentToPresent");
		TestUtils.getSurpession(list, Char2BooleanMapComputeIfAbsentTester.class, "testComputeIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Char2BooleanMapComputeIfPresentTester.class, "testCompute_presentToPresent", "testComputeIfPresent_supportedPresent");
		TestUtils.getSurpession(list, Char2BooleanMapSupplyIfAbsentTester.class, "testSupplyIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Char2BooleanMapMergeTester.class, "testAbsent");
		TestUtils.getSurpession(list, Char2BooleanMapPutIfAbsentTester.class, "testPutIfAbsent_supportedAbsent");
		
		TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
		TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, Char2BooleanMapContainsValueTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
		TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
		TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
		return list;
	}
	
}