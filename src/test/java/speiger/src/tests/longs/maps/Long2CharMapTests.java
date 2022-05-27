package speiger.src.tests.longs.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.maps.impl.concurrent.Long2CharConcurrentOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2CharLinkedOpenHashMap;
import speiger.src.collections.longs.maps.impl.hash.Long2CharOpenHashMap;
import speiger.src.collections.longs.maps.impl.misc.Long2CharArrayMap;
import speiger.src.collections.longs.maps.impl.tree.Long2CharAVLTreeMap;
import speiger.src.collections.longs.maps.impl.tree.Long2CharRBTreeMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharSortedMap;
import speiger.src.testers.longs.builder.maps.Long2CharMapTestSuiteBuilder;
import speiger.src.testers.longs.builder.maps.Long2CharNavigableMapTestSuiteBuilder;
import speiger.src.testers.longs.impl.maps.SimpleLong2CharMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Long2CharMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Long2CharMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Long2CharOpenHashMap", Long2CharOpenHashMap::new));
		suite.addTest(mapSuite("Long2CharLinkedOpenHashMap", Long2CharLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Long2CharArrayMap", Long2CharArrayMap::new));
		suite.addTest(mapSuite("Long2CharConcurrentOpenHashMap", Long2CharConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Long2CharRBTreeMap", Long2CharRBTreeMap::new));
		suite.addTest(navigableMapSuite("Long2CharAVLTreeMap", Long2CharAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<long[], char[], Long2CharMap> factory) {
		Long2CharMapTestSuiteBuilder builder = Long2CharMapTestSuiteBuilder.using(new SimpleLong2CharMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<long[], char[], Long2CharSortedMap> factory) {
		Long2CharNavigableMapTestSuiteBuilder builder = Long2CharNavigableMapTestSuiteBuilder.using(new SimpleLong2CharMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}