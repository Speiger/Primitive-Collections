package speiger.src.tests.shorts.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2ShortConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ShortLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ShortOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2ShortArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ShortAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ShortRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2ShortMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2ShortNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2ShortMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2ShortMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2ShortMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2ShortOpenHashMap", Short2ShortOpenHashMap::new));
		suite.addTest(mapSuite("Short2ShortLinkedOpenHashMap", Short2ShortLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2ShortArrayMap", Short2ShortArrayMap::new));
		suite.addTest(mapSuite("Short2ShortConcurrentOpenHashMap", Short2ShortConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2ShortRBTreeMap", Short2ShortRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2ShortAVLTreeMap", Short2ShortAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], short[], Short2ShortMap> factory) {
		Short2ShortMapTestSuiteBuilder builder = Short2ShortMapTestSuiteBuilder.using(new SimpleShort2ShortMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], short[], Short2ShortSortedMap> factory) {
		Short2ShortNavigableMapTestSuiteBuilder builder = Short2ShortNavigableMapTestSuiteBuilder.using(new SimpleShort2ShortMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}