package speiger.src.tests.shorts.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2LongConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2LongLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2LongOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2LongArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2LongAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2LongRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2LongMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2LongNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2LongMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2LongMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2LongMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2LongOpenHashMap", Short2LongOpenHashMap::new));
		suite.addTest(mapSuite("Short2LongLinkedOpenHashMap", Short2LongLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2LongArrayMap", Short2LongArrayMap::new));
		suite.addTest(mapSuite("Short2LongConcurrentOpenHashMap", Short2LongConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2LongRBTreeMap", Short2LongRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2LongAVLTreeMap", Short2LongAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], long[], Short2LongMap> factory) {
		Short2LongMapTestSuiteBuilder builder = Short2LongMapTestSuiteBuilder.using(new SimpleShort2LongMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], long[], Short2LongSortedMap> factory) {
		Short2LongNavigableMapTestSuiteBuilder builder = Short2LongNavigableMapTestSuiteBuilder.using(new SimpleShort2LongMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}