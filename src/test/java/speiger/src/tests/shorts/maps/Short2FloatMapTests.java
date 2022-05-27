package speiger.src.tests.shorts.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2FloatConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2FloatLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2FloatOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2FloatArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2FloatAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2FloatRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2FloatMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2FloatNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2FloatMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2FloatMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2FloatMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2FloatOpenHashMap", Short2FloatOpenHashMap::new));
		suite.addTest(mapSuite("Short2FloatLinkedOpenHashMap", Short2FloatLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2FloatArrayMap", Short2FloatArrayMap::new));
		suite.addTest(mapSuite("Short2FloatConcurrentOpenHashMap", Short2FloatConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2FloatRBTreeMap", Short2FloatRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2FloatAVLTreeMap", Short2FloatAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], float[], Short2FloatMap> factory) {
		Short2FloatMapTestSuiteBuilder builder = Short2FloatMapTestSuiteBuilder.using(new SimpleShort2FloatMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], float[], Short2FloatSortedMap> factory) {
		Short2FloatNavigableMapTestSuiteBuilder builder = Short2FloatNavigableMapTestSuiteBuilder.using(new SimpleShort2FloatMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}