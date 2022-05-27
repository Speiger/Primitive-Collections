package speiger.src.tests.chars.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.impl.concurrent.Char2ShortConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2ShortLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2ShortOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2ShortArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2ShortAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2ShortRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortSortedMap;
import speiger.src.testers.chars.builder.maps.Char2ShortMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2ShortNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2ShortMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Char2ShortMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2ShortMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2ShortOpenHashMap", Char2ShortOpenHashMap::new));
		suite.addTest(mapSuite("Char2ShortLinkedOpenHashMap", Char2ShortLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2ShortArrayMap", Char2ShortArrayMap::new));
		suite.addTest(mapSuite("Char2ShortConcurrentOpenHashMap", Char2ShortConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2ShortRBTreeMap", Char2ShortRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2ShortAVLTreeMap", Char2ShortAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], short[], Char2ShortMap> factory) {
		Char2ShortMapTestSuiteBuilder builder = Char2ShortMapTestSuiteBuilder.using(new SimpleChar2ShortMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], short[], Char2ShortSortedMap> factory) {
		Char2ShortNavigableMapTestSuiteBuilder builder = Char2ShortNavigableMapTestSuiteBuilder.using(new SimpleChar2ShortMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}