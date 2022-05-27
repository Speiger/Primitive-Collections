package speiger.src.tests.objects.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2FloatConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2FloatLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2FloatOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2FloatArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2FloatAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2FloatRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatSortedMap;
import speiger.src.testers.objects.builder.maps.Object2FloatMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2FloatNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2FloatMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Object2FloatMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2FloatMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2FloatOpenHashMap", Object2FloatOpenHashMap::new));
		suite.addTest(mapSuite("Object2FloatLinkedOpenHashMap", Object2FloatLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2FloatArrayMap", Object2FloatArrayMap::new));
		suite.addTest(mapSuite("Object2FloatConcurrentOpenHashMap", Object2FloatConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2FloatRBTreeMap", Object2FloatRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2FloatAVLTreeMap", Object2FloatAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], float[], Object2FloatMap<String>> factory) {
		SimpleObject2FloatMapTestGenerator.Maps<String> generator = new SimpleObject2FloatMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		Object2FloatMapTestSuiteBuilder<String> builder = Object2FloatMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], float[], Object2FloatSortedMap<String>> factory) {
		SimpleObject2FloatMapTestGenerator.SortedMaps<String> generator = new SimpleObject2FloatMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		Object2FloatNavigableMapTestSuiteBuilder<String> builder = Object2FloatNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
}