package speiger.src.tests.bytes.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.sets.ImmutableByteOpenHashSet;
import speiger.src.collections.bytes.sets.ByteAVLTreeSet;
import speiger.src.collections.bytes.sets.ByteArraySet;
import speiger.src.collections.bytes.sets.ByteLinkedOpenCustomHashSet;
import speiger.src.collections.bytes.sets.ByteLinkedOpenHashSet;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteOpenCustomHashSet;
import speiger.src.collections.bytes.sets.ByteOpenHashSet;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.bytes.sets.ByteRBTreeSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.utils.ByteStrategy;
import speiger.src.testers.bytes.builder.ByteNavigableSetTestSuiteBuilder;
import speiger.src.testers.bytes.builder.ByteOrderedSetTestSuiteBuilder;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.builder.ByteSortedSetTestSuiteBuilder;
import speiger.src.testers.bytes.impl.SimpleByteTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class ByteSetTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("ByteSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("ByteOpenHashSet", ByteOpenHashSet::new));
		suite.addTest(orderedSetSuite("ByteLinkedOpenHashSet", ByteLinkedOpenHashSet::new));
		suite.addTest(setSuite("ByteOpenCustomHashSet", T -> new ByteOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(orderedSetSuite("ByteLinkedOpenCustomHashSet", T -> new ByteLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(immutableOrderedSetSuite("ImmutableByteOpenHashSet", ImmutableByteOpenHashSet::new));
		suite.addTest(setSuite("ByteArraySet", ByteArraySet::new));
		suite.addTest(navigableSetSuite("ByteRBTreeSet", ByteRBTreeSet::new));
		suite.addTest(navigableSetSuite("ByteAVLTreeSet", ByteAVLTreeSet::new));
	}
		
	public static Test setSuite(String name, Function<byte[], ByteSet> factory) {
		return ByteSetTestSuiteBuilder.using(new SimpleByteTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test setImmutableSuite(String name, Function<byte[], ByteSet> factory) {
		return ByteSetTestSuiteBuilder.using(new SimpleByteTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test orderedSetSuite(String name, Function<byte[], ByteOrderedSet> factory) {
		return ByteOrderedSetTestSuiteBuilder.using(new SimpleByteTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test immutableOrderedSetSuite(String name, Function<byte[], ByteOrderedSet> factory) {
		return ByteOrderedSetTestSuiteBuilder.using(new SimpleByteTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test sortedSetSuite(String name, Function<byte[], ByteSortedSet> factory) {
		return ByteSortedSetTestSuiteBuilder.using(new SimpleByteTestGenerator.SortedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test navigableSetSuite(String name, Function<byte[], ByteNavigableSet> factory) {
		return ByteNavigableSetTestSuiteBuilder.using(new SimpleByteTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING)
			.createTestSuite();
	}
	
	private static class HashStrategy implements ByteStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(byte o) { return Byte.hashCode(o); }
		@Override
		public boolean equals(byte key, byte value) { return key == value; }
	}
}