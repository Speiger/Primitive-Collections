package speiger.src.tests.chars.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.impl.concurrent.Char2IntConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2IntLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2IntOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2IntArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2IntAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2IntRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntSortedMap;
import speiger.src.testers.chars.builder.maps.Char2IntMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2IntNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2IntMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Char2IntMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2IntMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2IntOpenHashMap", Char2IntOpenHashMap::new));
		suite.addTest(mapSuite("Char2IntLinkedOpenHashMap", Char2IntLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2IntArrayMap", Char2IntArrayMap::new));
		suite.addTest(mapSuite("Char2IntConcurrentOpenHashMap", Char2IntConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2IntRBTreeMap", Char2IntRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2IntAVLTreeMap", Char2IntAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], int[], Char2IntMap> factory) {
		Char2IntMapTestSuiteBuilder builder = Char2IntMapTestSuiteBuilder.using(new SimpleChar2IntMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], int[], Char2IntSortedMap> factory) {
		Char2IntNavigableMapTestSuiteBuilder builder = Char2IntNavigableMapTestSuiteBuilder.using(new SimpleChar2IntMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}