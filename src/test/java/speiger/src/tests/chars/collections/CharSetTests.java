package speiger.src.tests.chars.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.sets.ImmutableCharOpenHashSet;
import speiger.src.collections.chars.sets.CharAVLTreeSet;
import speiger.src.collections.chars.sets.CharArraySet;
import speiger.src.collections.chars.sets.CharLinkedOpenCustomHashSet;
import speiger.src.collections.chars.sets.CharLinkedOpenHashSet;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharOpenCustomHashSet;
import speiger.src.collections.chars.sets.CharOpenHashSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharRBTreeSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.utils.CharStrategy;
import speiger.src.testers.chars.builder.CharNavigableSetTestSuiteBuilder;
import speiger.src.testers.chars.builder.CharOrderedSetTestSuiteBuilder;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.builder.CharSortedSetTestSuiteBuilder;
import speiger.src.testers.chars.impl.SimpleCharTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class CharSetTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("CharSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("CharOpenHashSet", CharOpenHashSet::new));
		suite.addTest(orderedSetSuite("CharLinkedOpenHashSet", CharLinkedOpenHashSet::new));
		suite.addTest(setSuite("CharOpenCustomHashSet", T -> new CharOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(orderedSetSuite("CharLinkedOpenCustomHashSet", T -> new CharLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(immutableOrderedSetSuite("ImmutableCharOpenHashSet", ImmutableCharOpenHashSet::new));
		suite.addTest(setSuite("CharArraySet", CharArraySet::new));
		suite.addTest(navigableSetSuite("CharRBTreeSet", CharRBTreeSet::new));
		suite.addTest(navigableSetSuite("CharAVLTreeSet", CharAVLTreeSet::new));
	}
		
	public static Test setSuite(String name, Function<char[], CharSet> factory) {
		return CharSetTestSuiteBuilder.using(new SimpleCharTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test setImmutableSuite(String name, Function<char[], CharSet> factory) {
		return CharSetTestSuiteBuilder.using(new SimpleCharTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test orderedSetSuite(String name, Function<char[], CharOrderedSet> factory) {
		return CharOrderedSetTestSuiteBuilder.using(new SimpleCharTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test immutableOrderedSetSuite(String name, Function<char[], CharOrderedSet> factory) {
		return CharOrderedSetTestSuiteBuilder.using(new SimpleCharTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test sortedSetSuite(String name, Function<char[], CharSortedSet> factory) {
		return CharSortedSetTestSuiteBuilder.using(new SimpleCharTestGenerator.SortedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test navigableSetSuite(String name, Function<char[], CharNavigableSet> factory) {
		return CharNavigableSetTestSuiteBuilder.using(new SimpleCharTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}
	
	private static class HashStrategy implements CharStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(char o) { return Character.hashCode(o); }
		@Override
		public boolean equals(char key, char value) { return key == value; }
	}
}