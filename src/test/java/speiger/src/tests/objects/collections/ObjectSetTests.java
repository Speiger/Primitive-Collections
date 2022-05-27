package speiger.src.tests.objects.collections;

import java.util.function.Function;
import java.util.Comparator;
import java.util.Objects;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.CollectionFeature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.sets.ImmutableObjectOpenHashSet;
import speiger.src.collections.objects.sets.ObjectAVLTreeSet;
import speiger.src.collections.objects.sets.ObjectArraySet;
import speiger.src.collections.objects.sets.ObjectLinkedOpenCustomHashSet;
import speiger.src.collections.objects.sets.ObjectLinkedOpenHashSet;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectOpenCustomHashSet;
import speiger.src.collections.objects.sets.ObjectOpenHashSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.sets.ObjectRBTreeSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.testers.objects.builder.ObjectNavigableSetTestSuiteBuilder;
import speiger.src.testers.objects.builder.ObjectOrderedSetTestSuiteBuilder;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.builder.ObjectSortedSetTestSuiteBuilder;
import speiger.src.testers.objects.impl.SimpleObjectTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.objects.utils.ObjectSamples;

public class ObjectSetTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("ObjectSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("ObjectOpenHashSet", ObjectOpenHashSet::new));
		suite.addTest(orderedSetSuite("ObjectLinkedOpenHashSet", ObjectLinkedOpenHashSet::new));
		suite.addTest(setSuite("ObjectOpenCustomHashSet", T -> new ObjectOpenCustomHashSet<>(T, HashStrategy.INSTANCE)));
		suite.addTest(orderedSetSuite("ObjectLinkedOpenCustomHashSet", T -> new ObjectLinkedOpenCustomHashSet<>(T, HashStrategy.INSTANCE)));
		suite.addTest(immutableOrderedSetSuite("ImmutableObjectOpenHashSet", ImmutableObjectOpenHashSet::new));
		suite.addTest(setSuite("ObjectArraySet", ObjectArraySet::new));
		suite.addTest(navigableSetSuite("ObjectRBTreeSet", ObjectRBTreeSet::new, false));
		suite.addTest(navigableSetSuite("ObjectAVLTreeSet", ObjectAVLTreeSet::new, false));
		suite.addTest(navigableSetSuite("ObjectRBTreeSet_Null", T -> new ObjectRBTreeSet<>(T, Comparator.nullsFirst(Comparator.naturalOrder())), true));
		suite.addTest(navigableSetSuite("ObjectAVLTreeSet_Null", T -> new ObjectAVLTreeSet<>(T, Comparator.nullsFirst(Comparator.naturalOrder())), true));
	}
		
	public static Test setSuite(String name, Function<String[], ObjectSet<String>> factory) {
		return ObjectSetTestSuiteBuilder.using(new SimpleObjectTestGenerator.Sets<>(factory, createStrings())).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test setImmutableSuite(String name, Function<String[], ObjectSet<String>> factory) {
		return ObjectSetTestSuiteBuilder.using(new SimpleObjectTestGenerator.Sets<>(factory, createStrings())).named(name)
			.withFeatures(CollectionSize.ANY, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test orderedSetSuite(String name, Function<String[], ObjectOrderedSet<String>> factory) {
		return ObjectOrderedSetTestSuiteBuilder.using(new SimpleObjectTestGenerator.OrderedSets<>(factory, createStrings())).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test immutableOrderedSetSuite(String name, Function<String[], ObjectOrderedSet<String>> factory) {
		return ObjectOrderedSetTestSuiteBuilder.using(new SimpleObjectTestGenerator.OrderedSets<>(factory, createStrings())).named(name)
			.withFeatures(CollectionSize.ANY, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test sortedSetSuite(String name, Function<String[], ObjectSortedSet<String>> factory) {
		return ObjectSortedSetTestSuiteBuilder.using(new SimpleObjectTestGenerator.SortedSets<>(factory, createStrings(), "!! a", "!! b", "~~ a", "~~ b")).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING)
			.createTestSuite();
	}

	public static Test navigableSetSuite(String name, Function<String[], ObjectNavigableSet<String>> factory, boolean nullValues) {
		ObjectNavigableSetTestSuiteBuilder<String> builder = (ObjectNavigableSetTestSuiteBuilder<String>)ObjectNavigableSetTestSuiteBuilder.using(new SimpleObjectTestGenerator.NavigableSets<>(factory, createStrings(), "!! a", "!! b", "~~ a", "~~ b")).named(name)
			.withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
		if(nullValues) builder.withFeatures(CollectionFeature.ALLOWS_NULL_VALUES);
			return builder.createTestSuite();
	}
	
	private static class HashStrategy implements ObjectStrategy<String> {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(String o) { return Objects.hashCode(o); }
		@Override
		public boolean equals(String key, String value) { return Objects.equals(key, value); }
	}
	
	private static ObjectSamples<String> createStrings() {
		return new ObjectSamples<>("b", "a", "c", "d", "e");
	}
}