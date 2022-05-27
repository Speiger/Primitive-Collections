package speiger.src.tests.chars.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.impl.concurrent.Char2FloatConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2FloatLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2FloatOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2FloatArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2FloatAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2FloatRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatSortedMap;
import speiger.src.testers.chars.builder.maps.Char2FloatMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2FloatNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2FloatMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Char2FloatMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2FloatMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2FloatOpenHashMap", Char2FloatOpenHashMap::new));
		suite.addTest(mapSuite("Char2FloatLinkedOpenHashMap", Char2FloatLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2FloatArrayMap", Char2FloatArrayMap::new));
		suite.addTest(mapSuite("Char2FloatConcurrentOpenHashMap", Char2FloatConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2FloatRBTreeMap", Char2FloatRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2FloatAVLTreeMap", Char2FloatAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], float[], Char2FloatMap> factory) {
		Char2FloatMapTestSuiteBuilder builder = Char2FloatMapTestSuiteBuilder.using(new SimpleChar2FloatMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], float[], Char2FloatSortedMap> factory) {
		Char2FloatNavigableMapTestSuiteBuilder builder = Char2FloatNavigableMapTestSuiteBuilder.using(new SimpleChar2FloatMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}