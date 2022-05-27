package speiger.src.tests.ints.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.sets.ImmutableIntOpenHashSet;
import speiger.src.collections.ints.sets.IntAVLTreeSet;
import speiger.src.collections.ints.sets.IntArraySet;
import speiger.src.collections.ints.sets.IntLinkedOpenCustomHashSet;
import speiger.src.collections.ints.sets.IntLinkedOpenHashSet;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntOpenCustomHashSet;
import speiger.src.collections.ints.sets.IntOpenHashSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.sets.IntRBTreeSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.utils.IntStrategy;
import speiger.src.testers.ints.builder.IntNavigableSetTestSuiteBuilder;
import speiger.src.testers.ints.builder.IntOrderedSetTestSuiteBuilder;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.builder.IntSortedSetTestSuiteBuilder;
import speiger.src.testers.ints.impl.SimpleIntTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class IntSetTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("IntSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("IntOpenHashSet", IntOpenHashSet::new));
		suite.addTest(orderedSetSuite("IntLinkedOpenHashSet", IntLinkedOpenHashSet::new));
		suite.addTest(setSuite("IntOpenCustomHashSet", T -> new IntOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(orderedSetSuite("IntLinkedOpenCustomHashSet", T -> new IntLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(immutableOrderedSetSuite("ImmutableIntOpenHashSet", ImmutableIntOpenHashSet::new));
		suite.addTest(setSuite("IntArraySet", IntArraySet::new));
		suite.addTest(navigableSetSuite("IntRBTreeSet", IntRBTreeSet::new));
		suite.addTest(navigableSetSuite("IntAVLTreeSet", IntAVLTreeSet::new));
	}
		
	public static Test setSuite(String name, Function<int[], IntSet> factory) {
		return IntSetTestSuiteBuilder.using(new SimpleIntTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test setImmutableSuite(String name, Function<int[], IntSet> factory) {
		return IntSetTestSuiteBuilder.using(new SimpleIntTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test orderedSetSuite(String name, Function<int[], IntOrderedSet> factory) {
		return IntOrderedSetTestSuiteBuilder.using(new SimpleIntTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test immutableOrderedSetSuite(String name, Function<int[], IntOrderedSet> factory) {
		return IntOrderedSetTestSuiteBuilder.using(new SimpleIntTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test sortedSetSuite(String name, Function<int[], IntSortedSet> factory) {
		return IntSortedSetTestSuiteBuilder.using(new SimpleIntTestGenerator.SortedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test navigableSetSuite(String name, Function<int[], IntNavigableSet> factory) {
		return IntNavigableSetTestSuiteBuilder.using(new SimpleIntTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}
	
	private static class HashStrategy implements IntStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(int o) { return Integer.hashCode(o); }
		@Override
		public boolean equals(int key, int value) { return key == value; }
	}
}