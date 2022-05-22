package speiger.src.tests.doubles.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.sets.ImmutableDoubleOpenHashSet;
import speiger.src.collections.doubles.sets.DoubleAVLTreeSet;
import speiger.src.collections.doubles.sets.DoubleArraySet;
import speiger.src.collections.doubles.sets.DoubleLinkedOpenCustomHashSet;
import speiger.src.collections.doubles.sets.DoubleLinkedOpenHashSet;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleOpenCustomHashSet;
import speiger.src.collections.doubles.sets.DoubleOpenHashSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleRBTreeSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.utils.DoubleStrategy;
import speiger.src.testers.doubles.builder.DoubleNavigableSetTestSuiteBuilder;
import speiger.src.testers.doubles.builder.DoubleOrderedSetTestSuiteBuilder;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.builder.DoubleSortedSetTestSuiteBuilder;
import speiger.src.testers.doubles.impl.SimpleDoubleTestGenerator;

public class DoubleSetTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("DoubleSets");
		suite(suite);
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("DoubleOpenHashSet", DoubleOpenHashSet::new));
		suite.addTest(orderedSetSuite("DoubleLinkedOpenHashSet", DoubleLinkedOpenHashSet::new));
		suite.addTest(setSuite("DoubleOpenCustomHashSet", T -> new DoubleOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(orderedSetSuite("DoubleLinkedOpenCustomHashSet", T -> new DoubleLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE)));
		suite.addTest(immutableOrderedSetSuite("ImmutableDoubleOpenHashSet", ImmutableDoubleOpenHashSet::new));
		suite.addTest(setSuite("DoubleArraySet", DoubleArraySet::new));
		suite.addTest(navigableSetSuite("DoubleRBTreeSet", DoubleRBTreeSet::new));
		suite.addTest(navigableSetSuite("DoubleAVLTreeSet", DoubleAVLTreeSet::new));
	}
		
	public static Test setSuite(String name, Function<double[], DoubleSet> factory) {
		return DoubleSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Sets(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE)
				.createTestSuite();
	}
	
	public static Test setImmutableSuite(String name, Function<double[], DoubleSet> factory) {
		return DoubleSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Sets(factory)).named(name)
				.withFeatures(CollectionSize.ANY)
				.createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<double[], DoubleOrderedSet> factory) {
		return DoubleOrderedSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.OrderedSets(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE)
				.createTestSuite();
	}
	
	public static Test immutableOrderedSetSuite(String name, Function<double[], DoubleOrderedSet> factory) {
		return DoubleOrderedSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.OrderedSets(factory)).named(name)
				.withFeatures(CollectionSize.ANY)
				.createTestSuite();
	}
	
	public static Test sortedSetSuite(String name, Function<double[], DoubleSortedSet> factory) {
		return DoubleSortedSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.SortedSets(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE)
				.createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<double[], DoubleNavigableSet> factory) {
		return DoubleNavigableSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.NavigableSets(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE)
				.createTestSuite();
	}
	
	private static class HashStrategy implements DoubleStrategy{
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(double o) { return Double.hashCode(o); }
		@Override
		public boolean equals(double key, double value) { return Double.doubleToLongBits(key) == Double.doubleToLongBits(value); }
	}
}