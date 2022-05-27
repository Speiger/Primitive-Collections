package speiger.src.tests.shorts.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2CharConcurrentOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2CharLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2CharOpenHashMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2CharArrayMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2CharAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2CharRBTreeMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharSortedMap;
import speiger.src.testers.shorts.builder.maps.Short2CharMapTestSuiteBuilder;
import speiger.src.testers.shorts.builder.maps.Short2CharNavigableMapTestSuiteBuilder;
import speiger.src.testers.shorts.impl.maps.SimpleShort2CharMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2CharMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Short2CharMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Short2CharOpenHashMap", Short2CharOpenHashMap::new));
		suite.addTest(mapSuite("Short2CharLinkedOpenHashMap", Short2CharLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Short2CharArrayMap", Short2CharArrayMap::new));
		suite.addTest(mapSuite("Short2CharConcurrentOpenHashMap", Short2CharConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Short2CharRBTreeMap", Short2CharRBTreeMap::new));
		suite.addTest(navigableMapSuite("Short2CharAVLTreeMap", Short2CharAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<short[], char[], Short2CharMap> factory) {
		Short2CharMapTestSuiteBuilder builder = Short2CharMapTestSuiteBuilder.using(new SimpleShort2CharMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<short[], char[], Short2CharSortedMap> factory) {
		Short2CharNavigableMapTestSuiteBuilder builder = Short2CharNavigableMapTestSuiteBuilder.using(new SimpleShort2CharMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}