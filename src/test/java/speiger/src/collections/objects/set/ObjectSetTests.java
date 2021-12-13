package speiger.src.collections.objects.set;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.testing.NavigableSetTestSuiteBuilder;
import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.TestStringSortedSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.sets.ImmutableObjectOpenHashSet;
import speiger.src.collections.objects.sets.ObjectAVLTreeSet;
import speiger.src.collections.objects.sets.ObjectArraySet;
import speiger.src.collections.objects.sets.ObjectLinkedOpenCustomHashSet;
import speiger.src.collections.objects.sets.ObjectLinkedOpenHashSet;
import speiger.src.collections.objects.sets.ObjectOpenCustomHashSet;
import speiger.src.collections.objects.sets.ObjectOpenHashSet;
import speiger.src.collections.objects.sets.ObjectRBTreeSet;
import speiger.src.collections.objects.utils.ObjectStrategy;

@SuppressWarnings("javadoc")
public class ObjectSetTests extends TestCase
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Sets");
		suite.addTest(suite("HashSet", ObjectOpenHashSet::new, true));
		suite.addTest(suite("LinkedHashSet", ObjectLinkedOpenHashSet::new, true));
		suite.addTest(suite("CustomHashSet", T -> new ObjectOpenCustomHashSet<>(T, Strategy.INSTANCE), true));
		suite.addTest(suite("LinkedCustomHashSet", T -> new ObjectLinkedOpenCustomHashSet<>(T, Strategy.INSTANCE), true));
		suite.addTest(immutableSuite("ImmutableHashSet", ImmutableObjectOpenHashSet::new));
		suite.addTest(suite("ArraySet", ObjectArraySet::new, true));
		suite.addTest(navigableSuite("RBTreeSet_NonNull", ObjectRBTreeSet::new, false));
		suite.addTest(navigableSuite("AVLTreeSet_NonNull", ObjectAVLTreeSet::new, false));
		suite.addTest(navigableSuite("RBTreeSet_Null", T -> new ObjectRBTreeSet<>(T, Comparator.nullsFirst(Comparator.naturalOrder())), true));
		suite.addTest(navigableSuite("AVLTreeSet_Null", T -> new ObjectAVLTreeSet<>(T, Comparator.nullsFirst(Comparator.naturalOrder())), true));
		return suite;
	}
	
	public static Test suite(String name, Function<String[], Set<String>> factory, boolean allowNull) {
		SetTestSuiteBuilder<String> generator = SetTestSuiteBuilder.using(new TestStringSetGenerator() {
			@Override
			protected Set<String> create(String[] elements) { return factory.apply(elements); }
		}).named(name).withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE);
		if(allowNull) generator.withFeatures(CollectionFeature.ALLOWS_NULL_VALUES);
		return generator.createTestSuite();
	}
	
	public static Test navigableSuite(String name, Function<String[], NavigableSet<String>> factory, boolean allowNull) {
		SetTestSuiteBuilder<String> generator = NavigableSetTestSuiteBuilder.using(new TestStringSortedSetGenerator() {
			@Override
			protected NavigableSet<String> create(String[] elements) { return factory.apply(elements); }
		}).named(name).withFeatures(CollectionSize.ANY, SetFeature.GENERAL_PURPOSE);
		if(allowNull) generator.withFeatures(CollectionFeature.ALLOWS_NULL_VALUES);
		return generator.createTestSuite();
	}
	
	public static Test immutableSuite(String name, Function<String[], Set<String>> factory) {
		return SetTestSuiteBuilder.using(new TestStringSetGenerator() {
			@Override
			protected Set<String> create(String[] elements) { return factory.apply(elements); }
		}).named(name).withFeatures(CollectionSize.ANY, CollectionFeature.ALLOWS_NULL_VALUES).createTestSuite();
	}
	
	private static class Strategy implements ObjectStrategy<String> {
		static final Strategy INSTANCE = new Strategy();
		
		@Override
		public int hashCode(String o) {
			return Objects.hashCode(o);
		}

		@Override
		public boolean equals(String key, String value) {
			return Objects.equals(key, value);
		}
	}
}