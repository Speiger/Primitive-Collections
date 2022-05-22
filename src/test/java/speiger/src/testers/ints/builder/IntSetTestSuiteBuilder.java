package speiger.src.testers.ints.builder;

import static com.google.common.collect.testing.features.CollectionFeature.SERIALIZABLE;
import static com.google.common.collect.testing.features.CollectionFeature.SERIALIZABLE_INCLUDING_VIEWS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestSetGenerator;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.testers.CollectionSerializationEqualTester;
import com.google.common.collect.testing.testers.SetAddAllTester;
import com.google.common.collect.testing.testers.SetAddTester;
import com.google.common.collect.testing.testers.SetCreationTester;
import com.google.common.collect.testing.testers.SetEqualsTester;
import com.google.common.collect.testing.testers.SetHashCodeTester;
import com.google.common.collect.testing.testers.SetRemoveTester;
import com.google.common.testing.SerializableTester;

import junit.framework.TestSuite;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.tests.set.IntSetAddAllTester;
import speiger.src.testers.ints.tests.set.IntSetAddTester;
import speiger.src.testers.ints.tests.set.IntSetCreationTester;
import speiger.src.testers.ints.tests.set.IntSetEqualsTester;
import speiger.src.testers.ints.tests.set.IntSetRemoveTester;

public class IntSetTestSuiteBuilder extends IntCollectionTestSuiteBuilder {
	public static IntSetTestSuiteBuilder using(TestIntSetGenerator generator) {
		return (IntSetTestSuiteBuilder) new IntSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
	    testers.add(CollectionSerializationEqualTester.class);
	    testers.add(SetAddAllTester.class);
	    testers.add(SetAddTester.class);
	    testers.add(SetCreationTester.class);
	    testers.add(SetHashCodeTester.class);
	    testers.add(SetEqualsTester.class);
	    testers.add(SetRemoveTester.class);
	    
		testers.add(IntSetAddAllTester.class);
		testers.add(IntSetAddTester.class);
		testers.add(IntSetCreationTester.class);
		testers.add(IntSetEqualsTester.class);
		testers.add(IntSetRemoveTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(
			FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Integer>, Integer>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));

		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(SetTestSuiteBuilder.using(new ReserializedSetGenerator<Integer>(parentBuilder.getSubjectGenerator()))
							.named(getName() + " reserialized")
							.withFeatures(computeReserializedCollectionFeatures(parentBuilder.getFeatures()))
							.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
							.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		}
		return derivedSuites;
	}

	static class ReserializedSetGenerator<E> implements TestSetGenerator<E> {
		final OneSizeTestContainerGenerator<Collection<E>, E> gen;

		private ReserializedSetGenerator(OneSizeTestContainerGenerator<Collection<E>, E> gen) {
			this.gen = gen;
		}

		@Override
		public SampleElements<E> samples() {
			return gen.samples();
		}

		@Override
		public Set<E> create(Object... elements) {
			return (Set<E>) SerializableTester.reserialize(gen.create(elements));
		}

		@Override
		public E[] createArray(int length) {
			return gen.createArray(length);
		}

		@Override
		public Iterable<E> order(List<E> insertionOrder) {
			return gen.order(insertionOrder);
		}
	}

	private static Set<Feature<?>> computeReserializedCollectionFeatures(Set<Feature<?>> features) {
		Set<Feature<?>> derivedFeatures = new HashSet<>(features);
		derivedFeatures.remove(SERIALIZABLE);
		derivedFeatures.remove(SERIALIZABLE_INCLUDING_VIEWS);
		return derivedFeatures;
	}
}