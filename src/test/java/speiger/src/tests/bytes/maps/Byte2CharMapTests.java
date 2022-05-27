package speiger.src.tests.bytes.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.impl.concurrent.Byte2CharConcurrentOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2CharLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.impl.hash.Byte2CharOpenHashMap;
import speiger.src.collections.bytes.maps.impl.misc.Byte2CharArrayMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2CharAVLTreeMap;
import speiger.src.collections.bytes.maps.impl.tree.Byte2CharRBTreeMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharSortedMap;
import speiger.src.testers.bytes.builder.maps.Byte2CharMapTestSuiteBuilder;
import speiger.src.testers.bytes.builder.maps.Byte2CharNavigableMapTestSuiteBuilder;
import speiger.src.testers.bytes.impl.maps.SimpleByte2CharMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Byte2CharMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Byte2CharMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Byte2CharOpenHashMap", Byte2CharOpenHashMap::new));
		suite.addTest(mapSuite("Byte2CharLinkedOpenHashMap", Byte2CharLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Byte2CharArrayMap", Byte2CharArrayMap::new));
		suite.addTest(mapSuite("Byte2CharConcurrentOpenHashMap", Byte2CharConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Byte2CharRBTreeMap", Byte2CharRBTreeMap::new));
		suite.addTest(navigableMapSuite("Byte2CharAVLTreeMap", Byte2CharAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<byte[], char[], Byte2CharMap> factory) {
		Byte2CharMapTestSuiteBuilder builder = Byte2CharMapTestSuiteBuilder.using(new SimpleByte2CharMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<byte[], char[], Byte2CharSortedMap> factory) {
		Byte2CharNavigableMapTestSuiteBuilder builder = Byte2CharNavigableMapTestSuiteBuilder.using(new SimpleByte2CharMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}