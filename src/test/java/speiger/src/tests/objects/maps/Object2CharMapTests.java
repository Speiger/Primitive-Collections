package speiger.src.tests.objects.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.maps.impl.concurrent.Object2CharConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2CharLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2CharOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Object2CharArrayMap;
import speiger.src.collections.objects.maps.impl.tree.Object2CharAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2CharRBTreeMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharSortedMap;
import speiger.src.testers.objects.builder.maps.Object2CharMapTestSuiteBuilder;
import speiger.src.testers.objects.builder.maps.Object2CharNavigableMapTestSuiteBuilder;
import speiger.src.testers.objects.impl.maps.SimpleObject2CharMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Object2CharMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Object2CharMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Object2CharOpenHashMap", Object2CharOpenHashMap::new));
		suite.addTest(mapSuite("Object2CharLinkedOpenHashMap", Object2CharLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Object2CharArrayMap", Object2CharArrayMap::new));
		suite.addTest(mapSuite("Object2CharConcurrentOpenHashMap", Object2CharConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Object2CharRBTreeMap", Object2CharRBTreeMap::new));
		suite.addTest(navigableMapSuite("Object2CharAVLTreeMap", Object2CharAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<String[], char[], Object2CharMap<String>> factory) {
		SimpleObject2CharMapTestGenerator.Maps<String> generator = new SimpleObject2CharMapTestGenerator.Maps<>(factory);
		generator.setKeys(createKeys());
		Object2CharMapTestSuiteBuilder<String> builder = Object2CharMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<String[], char[], Object2CharSortedMap<String>> factory) {
		SimpleObject2CharMapTestGenerator.SortedMaps<String> generator = new SimpleObject2CharMapTestGenerator.SortedMaps<>(factory);
		generator.setKeys(createKeys());
		Object2CharNavigableMapTestSuiteBuilder<String> builder = Object2CharNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_KEY_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createKeys() {
		return new String[]{"one", "two", "three", "four", "five", "!! a", "!! b", "~~ a", "~~ b"};
	}
	
}