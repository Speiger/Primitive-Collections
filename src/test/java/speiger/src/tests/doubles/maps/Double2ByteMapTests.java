package speiger.src.tests.doubles.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2ByteConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2ByteLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2ByteOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2ByteArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2ByteAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2ByteRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2ByteMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2ByteNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2ByteMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Double2ByteMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2ByteMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2ByteOpenHashMap", Double2ByteOpenHashMap::new));
		suite.addTest(mapSuite("Double2ByteLinkedOpenHashMap", Double2ByteLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2ByteArrayMap", Double2ByteArrayMap::new));
		suite.addTest(mapSuite("Double2ByteConcurrentOpenHashMap", Double2ByteConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2ByteRBTreeMap", Double2ByteRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2ByteAVLTreeMap", Double2ByteAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], byte[], Double2ByteMap> factory) {
		Double2ByteMapTestSuiteBuilder builder = Double2ByteMapTestSuiteBuilder.using(new SimpleDouble2ByteMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], byte[], Double2ByteSortedMap> factory) {
		Double2ByteNavigableMapTestSuiteBuilder builder = Double2ByteNavigableMapTestSuiteBuilder.using(new SimpleDouble2ByteMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}