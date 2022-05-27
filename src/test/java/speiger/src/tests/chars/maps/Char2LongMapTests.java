package speiger.src.tests.chars.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.impl.concurrent.Char2LongConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2LongLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2LongOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2LongArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2LongAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2LongRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongSortedMap;
import speiger.src.testers.chars.builder.maps.Char2LongMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2LongNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2LongMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Char2LongMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2LongMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2LongOpenHashMap", Char2LongOpenHashMap::new));
		suite.addTest(mapSuite("Char2LongLinkedOpenHashMap", Char2LongLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2LongArrayMap", Char2LongArrayMap::new));
		suite.addTest(mapSuite("Char2LongConcurrentOpenHashMap", Char2LongConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2LongRBTreeMap", Char2LongRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2LongAVLTreeMap", Char2LongAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], long[], Char2LongMap> factory) {
		Char2LongMapTestSuiteBuilder builder = Char2LongMapTestSuiteBuilder.using(new SimpleChar2LongMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], long[], Char2LongSortedMap> factory) {
		Char2LongNavigableMapTestSuiteBuilder builder = Char2LongNavigableMapTestSuiteBuilder.using(new SimpleChar2LongMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}