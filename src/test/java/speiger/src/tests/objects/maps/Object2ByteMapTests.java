package speiger.src.tests.objects.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2ByteConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ByteLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2ByteOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2ByteArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ByteAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2ByteRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteSortedMap;
import speiger.src.testers.objects.builder.maps.Object2ByteMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2ByteNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2ByteMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Object2ByteMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2ByteMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2ByteOpenHashMap", Object2ByteOpenHashMap::new));
		suite.addTest(mapSuite("Object2ByteLinkedOpenHashMap", Object2ByteLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2ByteArrayMap", Object2ByteArrayMap::new));
		suite.addTest(mapSuite("Object2ByteConcurrentOpenHashMap", Object2ByteConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2ByteRBTreeMap", Object2ByteRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2ByteAVLTreeMap", Object2ByteAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], byte[], Object2ByteMap<String>> factory) {
		SimpleObject2ByteMapTestGenerator.Maps<String> generator = new SimpleObject2ByteMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		Object2ByteMapTestSuiteBuilder<String> builder = Object2ByteMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], byte[], Object2ByteSortedMap<String>> factory) {
		SimpleObject2ByteMapTestGenerator.SortedMaps<String> generator = new SimpleObject2ByteMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		Object2ByteNavigableMapTestSuiteBuilder<String> builder = Object2ByteNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
}