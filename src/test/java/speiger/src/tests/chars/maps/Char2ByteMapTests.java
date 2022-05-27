package speiger.src.tests.chars.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.impl.concurrent.Char2ByteConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2ByteLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2ByteOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2ByteArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2ByteAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2ByteRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteSortedMap;
import speiger.src.testers.chars.builder.maps.Char2ByteMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2ByteNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2ByteMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class Char2ByteMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2ByteMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2ByteOpenHashMap", Char2ByteOpenHashMap::new));
		suite.addTest(mapSuite("Char2ByteLinkedOpenHashMap", Char2ByteLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2ByteArrayMap", Char2ByteArrayMap::new));
		suite.addTest(mapSuite("Char2ByteConcurrentOpenHashMap", Char2ByteConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2ByteRBTreeMap", Char2ByteRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2ByteAVLTreeMap", Char2ByteAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], byte[], Char2ByteMap> factory) {
		Char2ByteMapTestSuiteBuilder builder = Char2ByteMapTestSuiteBuilder.using(new SimpleChar2ByteMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], byte[], Char2ByteSortedMap> factory) {
		Char2ByteNavigableMapTestSuiteBuilder builder = Char2ByteNavigableMapTestSuiteBuilder.using(new SimpleChar2ByteMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
}