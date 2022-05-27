package speiger.src.testers.ints.builder;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
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
import com.google.common.collect.testing.ListTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestListGenerator;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.testers.CollectionSerializationEqualTester;
import com.google.common.collect.testing.testers.ListAddAllAtIndexTester;
import com.google.common.collect.testing.testers.ListAddAllTester;
import com.google.common.collect.testing.testers.ListAddAtIndexTester;
import com.google.common.collect.testing.testers.ListAddTester;
import com.google.common.collect.testing.testers.ListCreationTester;
import com.google.common.collect.testing.testers.ListEqualsTester;
import com.google.common.collect.testing.testers.ListGetTester;
import com.google.common.collect.testing.testers.ListHashCodeTester;
import com.google.common.collect.testing.testers.ListIndexOfTester;
import com.google.common.collect.testing.testers.ListLastIndexOfTester;
import com.google.common.collect.testing.testers.ListListIteratorTester;
import com.google.common.collect.testing.testers.ListRemoveAllTester;
import com.google.common.collect.testing.testers.ListRemoveAtIndexTester;
import com.google.common.collect.testing.testers.ListRemoveTester;
import com.google.common.collect.testing.testers.ListReplaceAllTester;
import com.google.common.collect.testing.testers.ListRetainAllTester;
import com.google.common.collect.testing.testers.ListSetTester;
import com.google.common.collect.testing.testers.ListSubListTester;
import com.google.common.collect.testing.testers.ListToArrayTester;
import com.google.common.testing.SerializableTester;

import junit.framework.TestSuite;
import speiger.src.testers.ints.generators.TestIntListGenerator;
import speiger.src.testers.ints.tests.list.IntListAbsentTester;
import speiger.src.testers.ints.tests.list.IntListAddAllArrayAtIndexTester;
import speiger.src.testers.ints.tests.list.IntListAddAllAtIndexTester;
import speiger.src.testers.ints.tests.list.IntListAddAllTester;
import speiger.src.testers.ints.tests.list.IntListAddAtIndexTester;
import speiger.src.testers.ints.tests.list.IntListAddTester;
import speiger.src.testers.ints.tests.list.IntListCreationTester;
import speiger.src.testers.ints.tests.list.IntListEqualsTester;
import speiger.src.testers.ints.tests.list.IntListExtractElementsTester;
import speiger.src.testers.ints.tests.list.IntListGetElementsTester;
import speiger.src.testers.ints.tests.list.IntListGetTester;
import speiger.src.testers.ints.tests.list.IntListIndexOfTester;
import speiger.src.testers.ints.tests.list.IntListLastIndexOfTester;
import speiger.src.testers.ints.tests.list.IntListListIteratorTester;
import speiger.src.testers.ints.tests.list.IntListPresentTester;
import speiger.src.testers.ints.tests.list.IntListRemoveAllTester;
import speiger.src.testers.ints.tests.list.IntListRemoveAtIndexTester;
import speiger.src.testers.ints.tests.list.IntListRemoveElementsTester;
import speiger.src.testers.ints.tests.list.IntListRemoveTester;
import speiger.src.testers.ints.tests.list.IntListFillBufferTester;import speiger.src.testers.ints.tests.list.IntListReplaceAllTester;
import speiger.src.testers.ints.tests.list.IntListRetainAllTester;
import speiger.src.testers.ints.tests.list.IntListSetTester;
import speiger.src.testers.ints.tests.list.IntListSubListTester;
import speiger.src.testers.ints.tests.list.IntListSwapRemoveAtIndexTester;
import speiger.src.testers.ints.tests.list.IntListSwapRemoveTester;
import speiger.src.testers.ints.tests.list.IntListToArrayTester;

public class IntListTestSuiteBuilder extends IntCollectionTestSuiteBuilder {
	public static IntListTestSuiteBuilder using(TestIntListGenerator generator) {
		return (IntListTestSuiteBuilder) new IntListTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());

		testers.add(CollectionSerializationEqualTester.class);
		testers.add(ListAddAllAtIndexTester.class);
		testers.add(ListAddAllTester.class);
		testers.add(ListAddAtIndexTester.class);
		testers.add(ListAddTester.class);
		testers.add(ListCreationTester.class);
		testers.add(ListEqualsTester.class);
		testers.add(ListGetTester.class);
		testers.add(ListHashCodeTester.class);
		testers.add(ListIndexOfTester.class);
		testers.add(ListLastIndexOfTester.class);
		testers.add(ListListIteratorTester.class);
		testers.add(ListRemoveAllTester.class);
		testers.add(ListRemoveAtIndexTester.class);
		testers.add(ListRemoveTester.class);
		testers.add(ListReplaceAllTester.class);
		testers.add(ListRetainAllTester.class);
		testers.add(ListSetTester.class);
		testers.add(ListSubListTester.class);
		testers.add(ListToArrayTester.class);
		
		testers.add(IntListAddAllAtIndexTester.class);
		testers.add(IntListAddAllArrayAtIndexTester.class);
		testers.add(IntListAddAllTester.class);
		testers.add(IntListAddAtIndexTester.class);
		testers.add(IntListAddTester.class);
		testers.add(IntListAbsentTester.class);
		testers.add(IntListPresentTester.class);
		testers.add(IntListCreationTester.class);
		testers.add(IntListEqualsTester.class);
		testers.add(IntListGetTester.class);
		testers.add(IntListGetElementsTester.class);
		testers.add(IntListExtractElementsTester.class);
		testers.add(IntListIndexOfTester.class);
		testers.add(IntListLastIndexOfTester.class);
		testers.add(IntListListIteratorTester.class);
		testers.add(IntListRemoveAllTester.class);
		testers.add(IntListRemoveAtIndexTester.class);
		testers.add(IntListRemoveTester.class);
		testers.add(IntListRemoveElementsTester.class);
		testers.add(IntListSwapRemoveAtIndexTester.class);
		testers.add(IntListSwapRemoveTester.class);
		testers.add(IntListFillBufferTester.class);		testers.add(IntListReplaceAllTester.class);
		testers.add(IntListRetainAllTester.class);
		testers.add(IntListSetTester.class);
		testers.add(IntListSubListTester.class);
		testers.add(IntListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Integer>, Integer>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<Integer>(parentBuilder.getSubjectGenerator()))
							.named(getName() + " reserialized")
							.withFeatures(computeReserializedCollectionFeatures(parentBuilder.getFeatures()))
							.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
							.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		}
		return derivedSuites;
	}
	
	static class ReserializedListGenerator<E> implements TestListGenerator<E> {
		final OneSizeTestContainerGenerator<Collection<E>, E> gen;

		private ReserializedListGenerator(OneSizeTestContainerGenerator<Collection<E>, E> gen) {
			this.gen = gen;
		}

		@Override
		public SampleElements<E> samples() {
			return gen.samples();
		}

		@Override
		public List<E> create(Object... elements) {
			return (List<E>) SerializableTester.reserialize(gen.create(elements));
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