package speiger.src.tests.floats.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.maps.impl.concurrent.Float2CharConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2CharLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2CharOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2CharArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2CharAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2CharRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.collections.floats.maps.interfaces.Float2CharSortedMap;
import speiger.src.testers.floats.builder.maps.Float2CharMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2CharNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2CharMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Float2CharMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2CharMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2CharOpenHashMap", Float2CharOpenHashMap::new));
		suite.addTest(mapSuite("Float2CharLinkedOpenHashMap", Float2CharLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2CharArrayMap", Float2CharArrayMap::new));
		suite.addTest(mapSuite("Float2CharConcurrentOpenHashMap", Float2CharConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2CharRBTreeMap", Float2CharRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2CharAVLTreeMap", Float2CharAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], char[], Float2CharMap> factory) {
		Float2CharMapTestSuiteBuilder builder = Float2CharMapTestSuiteBuilder.using(new SimpleFloat2CharMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], char[], Float2CharSortedMap> factory) {
		Float2CharNavigableMapTestSuiteBuilder builder = Float2CharNavigableMapTestSuiteBuilder.using(new SimpleFloat2CharMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}