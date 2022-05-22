package speiger.src.tests.floats.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.sets.ImmutableFloatOpenHashSet;
import speiger.src.collections.floats.sets.FloatAVLTreeSet;
import speiger.src.collections.floats.sets.FloatArraySet;
import speiger.src.collections.floats.sets.FloatLinkedOpenCustomHashSet;
import speiger.src.collections.floats.sets.FloatLinkedOpenHashSet;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatOpenCustomHashSet;
import speiger.src.collections.floats.sets.FloatOpenHashSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatRBTreeSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.testers.floats.builder.FloatNavigableSetTestSuiteBuilder;
import speiger.src.testers.floats.builder.FloatOrderedSetTestSuiteBuilder;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.builder.FloatSortedSetTestSuiteBuilder;
import speiger.src.testers.floats.impl.SimpleFloatTestGenerator;

public class FloatSetTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("FloatSets");
		suite(suite);
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("FloatOpenHashSet", FloatOpenHashSet::new));
		suite.addTest(orderedSetSuite("FloatLinkedOpenHashSet", FloatLinkedOpenHashSet::new));
		suite.addTest(setSuite("FloatOpenCustomHashSet", T -> new FloatOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(orderedSetSuite("FloatLinkedOpenCustomHashSet", T -> new FloatLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(immutableOrderedSetSuite("ImmutableFloatOpenHashSet", ImmutableFloatOpenHashSet::new));
		suite.addTest(setSuite("FloatArraySet", FloatArraySet::new));
		suite.addTest(navigableSetSuite("FloatRBTreeSet", FloatRBTreeSet::new));
		suite.addTest(navigableSetSuite("FloatAVLTreeSet", FloatAVLTreeSet::new));
	}
		
	public static Test setSuite(String name, Function<float[], FloatSet> factory) {
		return FloatSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.Sets(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE)
				.createTestSuite();
	}
	
	public static Test setImmutableSuite(String name, Function<float[], FloatSet> factory) {
		return FloatSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.Sets(factory)).named(name)
				.withFeatures(CollectionSize.ANY)
				.createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<float[], FloatOrderedSet> factory) {
		return FloatOrderedSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.OrderedSets(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE)
				.createTestSuite();
	}
	
	public static Test immutableOrderedSetSuite(String name, Function<float[], FloatOrderedSet> factory) {
		return FloatOrderedSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.OrderedSets(factory)).named(name)
				.withFeatures(CollectionSize.ANY)
				.createTestSuite();
	}
	
	public static Test sortedSetSuite(String name, Function<float[], FloatSortedSet> factory) {
		return FloatSortedSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.SortedSets(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE)
				.createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<float[], FloatNavigableSet> factory) {
		return FloatNavigableSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.NavigableSets(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE)
				.createTestSuite();
	}
	
	private static class HashStrategy implements FloatStrategy{
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(float o) { return Float.hashCode(o); }
		@Override
		public boolean equals(float key, float value) { return Float.floatToIntBits(key) == Float.floatToIntBits(value); }
	}
}