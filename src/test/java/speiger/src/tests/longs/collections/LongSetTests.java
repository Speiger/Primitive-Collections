package speiger.src.tests.longs.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.sets.ImmutableLongOpenHashSet;
import speiger.src.collections.longs.sets.LongAVLTreeSet;
import speiger.src.collections.longs.sets.LongArraySet;
import speiger.src.collections.longs.sets.LongLinkedOpenCustomHashSet;
import speiger.src.collections.longs.sets.LongLinkedOpenHashSet;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongOpenCustomHashSet;
import speiger.src.collections.longs.sets.LongOpenHashSet;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.sets.LongRBTreeSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.utils.LongStrategy;
import speiger.src.testers.longs.builder.LongNavigableSetTestSuiteBuilder;
import speiger.src.testers.longs.builder.LongOrderedSetTestSuiteBuilder;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.builder.LongSortedSetTestSuiteBuilder;
import speiger.src.testers.longs.impl.SimpleLongTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class LongSetTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("LongSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("LongOpenHashSet", LongOpenHashSet::new));
		suite.addTest(orderedSetSuite("LongLinkedOpenHashSet", LongLinkedOpenHashSet::new));
		suite.addTest(setSuite("LongOpenCustomHashSet", T -> new LongOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(orderedSetSuite("LongLinkedOpenCustomHashSet", T -> new LongLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(immutableOrderedSetSuite("ImmutableLongOpenHashSet", ImmutableLongOpenHashSet::new));
		suite.addTest(setSuite("LongArraySet", LongArraySet::new));
		suite.addTest(navigableSetSuite("LongRBTreeSet", LongRBTreeSet::new));
		suite.addTest(navigableSetSuite("LongAVLTreeSet", LongAVLTreeSet::new));
	}
		
	public static Test setSuite(String name, Function<long[], LongSet> factory) {
		return LongSetTestSuiteBuilder.using(new SimpleLongTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test setImmutableSuite(String name, Function<long[], LongSet> factory) {
		return LongSetTestSuiteBuilder.using(new SimpleLongTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test orderedSetSuite(String name, Function<long[], LongOrderedSet> factory) {
		return LongOrderedSetTestSuiteBuilder.using(new SimpleLongTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test immutableOrderedSetSuite(String name, Function<long[], LongOrderedSet> factory) {
		return LongOrderedSetTestSuiteBuilder.using(new SimpleLongTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test sortedSetSuite(String name, Function<long[], LongSortedSet> factory) {
		return LongSortedSetTestSuiteBuilder.using(new SimpleLongTestGenerator.SortedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test navigableSetSuite(String name, Function<long[], LongNavigableSet> factory) {
		return LongNavigableSetTestSuiteBuilder.using(new SimpleLongTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}
	
	private static class HashStrategy implements LongStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(long o) { return Long.hashCode(o); }
		@Override
		public boolean equals(long key, long value) { return key == value; }
	}
}