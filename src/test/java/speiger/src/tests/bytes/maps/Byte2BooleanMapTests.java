package speiger.src.tests.bytes.maps;

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
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2BooleanConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2BooleanLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2BooleanOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2BooleanArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2BooleanAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2BooleanRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2BooleanMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2BooleanNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2BooleanMapTestGenerator;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapPutIfAbsentTester;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Byte2BooleanMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2BooleanMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2BooleanOpenHashMap", Byte2BooleanOpenHashMap::new));
		suite.addTest(mapSuite("Byte2BooleanLinkedOpenHashMap", Byte2BooleanLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2BooleanArrayMap", Byte2BooleanArrayMap::new));
		suite.addTest(mapSuite("Byte2BooleanConcurrentOpenHashMap", Byte2BooleanConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2BooleanRBTreeMap", Byte2BooleanRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2BooleanAVLTreeMap", Byte2BooleanAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], boolean[], Byte2BooleanMap> factory) {
		Byte2BooleanMapTestSuiteBuilder builder = Byte2BooleanMapTestSuiteBuilder.using(new SimpleByte2BooleanMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], boolean[], Byte2BooleanSortedMap> factory) {
		Byte2BooleanNavigableMapTestSuiteBuilder builder = Byte2BooleanNavigableMapTestSuiteBuilder.using(new SimpleByte2BooleanMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		builder.suppressing(getSuppression());
		return builder.named(name).createTestSuite();
	}
	
	public static List<Method> getSuppression() {
		List<Method> list = new ArrayList<>();
		TestUtils.getSurpession(list, Byte2BooleanMapComputeTester.class, "testCompute_absentToPresent", "testCompute_presentToPresent");
		TestUtils.getSurpession(list, Byte2BooleanMapComputeIfAbsentTester.class, "testComputeIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Byte2BooleanMapComputeIfPresentTester.class, "testCompute_presentToPresent", "testComputeIfPresent_supportedPresent");
		TestUtils.getSurpession(list, Byte2BooleanMapSupplyIfAbsentTester.class, "testSupplyIfAbsent_supportedAbsent");
		TestUtils.getSurpession(list, Byte2BooleanMapMergeTester.class, "testAbsent");
		TestUtils.getSurpession(list, Byte2BooleanMapPutIfAbsentTester.class, "testPutIfAbsent_supportedAbsent");
		
		TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
		TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, Byte2BooleanMapContainsValueTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
		TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
		TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
		TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
		TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
		return list;
	}
	
}