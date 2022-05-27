package speiger.src.tests.chars.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.impl.concurrent.Char2DoubleConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2DoubleLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2DoubleOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2DoubleArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2DoubleAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2DoubleRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleSortedMap;
import speiger.src.testers.chars.builder.maps.Char2DoubleMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2DoubleNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2DoubleMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Char2DoubleMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2DoubleMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2DoubleOpenHashMap", Char2DoubleOpenHashMap::new));
		suite.addTest(mapSuite("Char2DoubleLinkedOpenHashMap", Char2DoubleLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2DoubleArrayMap", Char2DoubleArrayMap::new));
		suite.addTest(mapSuite("Char2DoubleConcurrentOpenHashMap", Char2DoubleConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2DoubleRBTreeMap", Char2DoubleRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2DoubleAVLTreeMap", Char2DoubleAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], double[], Char2DoubleMap> factory) {
		Char2DoubleMapTestSuiteBuilder builder = Char2DoubleMapTestSuiteBuilder.using(new SimpleChar2DoubleMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], double[], Char2DoubleSortedMap> factory) {
		Char2DoubleNavigableMapTestSuiteBuilder builder = Char2DoubleNavigableMapTestSuiteBuilder.using(new SimpleChar2DoubleMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}