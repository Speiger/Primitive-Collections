package speiger.src.tests.shorts.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2ByteConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ByteLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2ByteOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2ByteArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ByteAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2ByteRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2ByteMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2ByteNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2ByteMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2ByteMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2ByteMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2ByteOpenHashMap", Short2ByteOpenHashMap::new));
		suite.addTest(mapSuite("Short2ByteLinkedOpenHashMap", Short2ByteLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2ByteArrayMap", Short2ByteArrayMap::new));
		suite.addTest(mapSuite("Short2ByteConcurrentOpenHashMap", Short2ByteConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2ByteRBTreeMap", Short2ByteRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2ByteAVLTreeMap", Short2ByteAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], byte[], Short2ByteMap> factory) {
		Short2ByteMapTestSuiteBuilder builder = Short2ByteMapTestSuiteBuilder.using(new SimpleShort2ByteMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], byte[], Short2ByteSortedMap> factory) {
		Short2ByteNavigableMapTestSuiteBuilder builder = Short2ByteNavigableMapTestSuiteBuilder.using(new SimpleShort2ByteMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}