package speiger.src.tests.ints.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.impl.concurrent.Int2CharConcurrentOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2CharLinkedOpenHashMap;
import speiger.src.collections.ints.maps.impl.hash.Int2CharOpenHashMap;
import speiger.src.collections.ints.maps.impl.misc.Int2CharArrayMap;
import speiger.src.collections.ints.maps.impl.tree.Int2CharAVLTreeMap;
import speiger.src.collections.ints.maps.impl.tree.Int2CharRBTreeMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharSortedMap;
import speiger.src.testers.ints.builder.maps.Int2CharMapTestSuiteBuilder;
import speiger.src.testers.ints.builder.maps.Int2CharNavigableMapTestSuiteBuilder;
import speiger.src.testers.ints.impl.maps.SimpleInt2CharMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Int2CharMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Int2CharMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Int2CharOpenHashMap", Int2CharOpenHashMap::new));
		suite.addTest(mapSuite("Int2CharLinkedOpenHashMap", Int2CharLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Int2CharArrayMap", Int2CharArrayMap::new));
		suite.addTest(mapSuite("Int2CharConcurrentOpenHashMap", Int2CharConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Int2CharRBTreeMap", Int2CharRBTreeMap::new));
		suite.addTest(navigableMapSuite("Int2CharAVLTreeMap", Int2CharAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<int[], char[], Int2CharMap> factory) {
		Int2CharMapTestSuiteBuilder builder = Int2CharMapTestSuiteBuilder.using(new SimpleInt2CharMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<int[], char[], Int2CharSortedMap> factory) {
		Int2CharNavigableMapTestSuiteBuilder builder = Int2CharNavigableMapTestSuiteBuilder.using(new SimpleInt2CharMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}