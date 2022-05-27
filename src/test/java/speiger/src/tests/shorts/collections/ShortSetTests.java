package speiger.src.tests.shorts.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.sets.ImmutableShortOpenHashSet;
import speiger.src.collections.shorts.sets.ShortAVLTreeSet;
import speiger.src.collections.shorts.sets.ShortArraySet;
import speiger.src.collections.shorts.sets.ShortLinkedOpenCustomHashSet;
import speiger.src.collections.shorts.sets.ShortLinkedOpenHashSet;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortOpenCustomHashSet;
import speiger.src.collections.shorts.sets.ShortOpenHashSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.sets.ShortRBTreeSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.collections.shorts.utils.ShortStrategy;
import speiger.src.testers.shorts.builder.ShortNavigableSetTestSuiteBuilder;
import speiger.src.testers.shorts.builder.ShortOrderedSetTestSuiteBuilder;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.builder.ShortSortedSetTestSuiteBuilder;
import speiger.src.testers.shorts.impl.SimpleShortTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class ShortSetTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("ShortSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("ShortOpenHashSet", ShortOpenHashSet::new));
		suite.addTest(orderedSetSuite("ShortLinkedOpenHashSet", ShortLinkedOpenHashSet::new));
		suite.addTest(setSuite("ShortOpenCustomHashSet", T -> new ShortOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(orderedSetSuite("ShortLinkedOpenCustomHashSet", T -> new ShortLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(immutableOrderedSetSuite("ImmutableShortOpenHashSet", ImmutableShortOpenHashSet::new));
		suite.addTest(setSuite("ShortArraySet", ShortArraySet::new));
		suite.addTest(navigableSetSuite("ShortRBTreeSet", ShortRBTreeSet::new));
		suite.addTest(navigableSetSuite("ShortAVLTreeSet", ShortAVLTreeSet::new));
	}
		
	public static Test setSuite(String name, Function<short[], ShortSet> factory) {
		return ShortSetTestSuiteBuilder.using(new SimpleShortTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test setImmutableSuite(String name, Function<short[], ShortSet> factory) {
		return ShortSetTestSuiteBuilder.using(new SimpleShortTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test orderedSetSuite(String name, Function<short[], ShortOrderedSet> factory) {
		return ShortOrderedSetTestSuiteBuilder.using(new SimpleShortTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test immutableOrderedSetSuite(String name, Function<short[], ShortOrderedSet> factory) {
		return ShortOrderedSetTestSuiteBuilder.using(new SimpleShortTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test sortedSetSuite(String name, Function<short[], ShortSortedSet> factory) {
		return ShortSortedSetTestSuiteBuilder.using(new SimpleShortTestGenerator.SortedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test navigableSetSuite(String name, Function<short[], ShortNavigableSet> factory) {
		return ShortNavigableSetTestSuiteBuilder.using(new SimpleShortTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}
	
	private static class HashStrategy implements ShortStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(short o) { return Short.hashCode(o); }
		@Override
		public boolean equals(short key, short value) { return key == value; }
	}
}