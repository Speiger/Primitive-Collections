package speiger.src.tests.shorts.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2IntConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2IntLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2IntOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2IntArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2IntAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2IntRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2IntMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2IntNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2IntMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2IntMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2IntMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2IntOpenHashMap", Short2IntOpenHashMap::new));
		suite.addTest(mapSuite("Short2IntLinkedOpenHashMap", Short2IntLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2IntArrayMap", Short2IntArrayMap::new));
		suite.addTest(mapSuite("Short2IntConcurrentOpenHashMap", Short2IntConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2IntRBTreeMap", Short2IntRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2IntAVLTreeMap", Short2IntAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], int[], Short2IntMap> factory) {
		Short2IntMapTestSuiteBuilder builder = Short2IntMapTestSuiteBuilder.using(new SimpleShort2IntMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], int[], Short2IntSortedMap> factory) {
		Short2IntNavigableMapTestSuiteBuilder builder = Short2IntNavigableMapTestSuiteBuilder.using(new SimpleShort2IntMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}