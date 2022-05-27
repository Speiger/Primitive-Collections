package speiger.src.tests.chars.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.impl.concurrent.Char2ObjectConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2ObjectLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2ObjectOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2ObjectArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2ObjectAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2ObjectRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectSortedMap;
import speiger.src.testers.chars.builder.maps.Char2ObjectMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2ObjectNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2ObjectMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Char2ObjectMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2ObjectMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2ObjectOpenHashMap", Char2ObjectOpenHashMap::new));
		suite.addTest(mapSuite("Char2ObjectLinkedOpenHashMap", Char2ObjectLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2ObjectArrayMap", Char2ObjectArrayMap::new));
		suite.addTest(mapSuite("Char2ObjectConcurrentOpenHashMap", Char2ObjectConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2ObjectRBTreeMap", Char2ObjectRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2ObjectAVLTreeMap", Char2ObjectAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], String[], Char2ObjectMap<String>> factory) {
		SimpleChar2ObjectMapTestGenerator.Maps<String> generator = new SimpleChar2ObjectMapTestGenerator.Maps<>(factory);
		generator.setValues(createValues());
		Char2ObjectMapTestSuiteBuilder<String> builder = Char2ObjectMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], String[], Char2ObjectSortedMap<String>> factory) {
		SimpleChar2ObjectMapTestGenerator.SortedMaps<String> generator = new SimpleChar2ObjectMapTestGenerator.SortedMaps<>(factory);
		generator.setValues(createValues());
		Char2ObjectNavigableMapTestSuiteBuilder<String> builder = Char2ObjectNavigableMapTestSuiteBuilder.using(generator);
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, MapFeature.ALLOWS_NULL_VALUE_QUERIES, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static String[] createValues() {
		return new String[]{"January", "February", "March", "April", "May", "below view", "below view", "above view", "above view"};
	}
	
}