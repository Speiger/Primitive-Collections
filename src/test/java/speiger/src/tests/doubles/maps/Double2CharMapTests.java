package speiger.src.tests.doubles.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.impl.concurrent.Double2CharConcurrentOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2CharLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.impl.hash.Double2CharOpenHashMap;
import speiger.src.collections.doubles.maps.impl.misc.Double2CharArrayMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2CharAVLTreeMap;
import speiger.src.collections.doubles.maps.impl.tree.Double2CharRBTreeMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharSortedMap;
import speiger.src.testers.doubles.builder.maps.Double2CharMapTestSuiteBuilder;
import speiger.src.testers.doubles.builder.maps.Double2CharNavigableMapTestSuiteBuilder;
import speiger.src.testers.doubles.impl.maps.SimpleDouble2CharMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Double2CharMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Double2CharMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Double2CharOpenHashMap", Double2CharOpenHashMap::new));
		suite.addTest(mapSuite("Double2CharLinkedOpenHashMap", Double2CharLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Double2CharArrayMap", Double2CharArrayMap::new));
		suite.addTest(mapSuite("Double2CharConcurrentOpenHashMap", Double2CharConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Double2CharRBTreeMap", Double2CharRBTreeMap::new));
		suite.addTest(navigableMapSuite("Double2CharAVLTreeMap", Double2CharAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<double[], char[], Double2CharMap> factory) {
		Double2CharMapTestSuiteBuilder builder = Double2CharMapTestSuiteBuilder.using(new SimpleDouble2CharMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<double[], char[], Double2CharSortedMap> factory) {
		Double2CharNavigableMapTestSuiteBuilder builder = Double2CharNavigableMapTestSuiteBuilder.using(new SimpleDouble2CharMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}