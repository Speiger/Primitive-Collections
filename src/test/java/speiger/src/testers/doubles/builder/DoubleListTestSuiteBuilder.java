package speiger.src.testers.doubles.builder;

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
import speiger.src.testers.doubles.generators.TestDoubleListGenerator;
import speiger.src.testers.doubles.tests.list.DoubleListAbsentTester;
import speiger.src.testers.doubles.tests.list.DoubleListAddAllArrayAtIndexTester;
import speiger.src.testers.doubles.tests.list.DoubleListAddAllAtIndexTester;
import speiger.src.testers.doubles.tests.list.DoubleListAddAllTester;
import speiger.src.testers.doubles.tests.list.DoubleListAddAtIndexTester;
import speiger.src.testers.doubles.tests.list.DoubleListAddTester;
import speiger.src.testers.doubles.tests.list.DoubleListCreationTester;
import speiger.src.testers.doubles.tests.list.DoubleListEqualsTester;
import speiger.src.testers.doubles.tests.list.DoubleListExtractElementsTester;
import speiger.src.testers.doubles.tests.list.DoubleListGetElementsTester;
import speiger.src.testers.doubles.tests.list.DoubleListGetTester;
import speiger.src.testers.doubles.tests.list.DoubleListIndexOfTester;
import speiger.src.testers.doubles.tests.list.DoubleListLastIndexOfTester;
import speiger.src.testers.doubles.tests.list.DoubleListListIteratorTester;
import speiger.src.testers.doubles.tests.list.DoubleListPresentTester;
import speiger.src.testers.doubles.tests.list.DoubleListRemoveAllTester;
import speiger.src.testers.doubles.tests.list.DoubleListRemoveAtIndexTester;
import speiger.src.testers.doubles.tests.list.DoubleListRemoveElementsTester;
import speiger.src.testers.doubles.tests.list.DoubleListRemoveTester;
import speiger.src.testers.doubles.tests.list.DoubleListFillBufferTester;import speiger.src.testers.doubles.tests.list.DoubleListReplaceAllTester;
import speiger.src.testers.doubles.tests.list.DoubleListRetainAllTester;
import speiger.src.testers.doubles.tests.list.DoubleListSetTester;
import speiger.src.testers.doubles.tests.list.DoubleListSubListTester;
import speiger.src.testers.doubles.tests.list.DoubleListSwapRemoveAtIndexTester;
import speiger.src.testers.doubles.tests.list.DoubleListSwapRemoveTester;
import speiger.src.testers.doubles.tests.list.DoubleListToArrayTester;

@SuppressWarnings("javadoc")
public class DoubleListTestSuiteBuilder extends DoubleCollectionTestSuiteBuilder {
	public static DoubleListTestSuiteBuilder using(TestDoubleListGenerator generator) {
		return (DoubleListTestSuiteBuilder) new DoubleListTestSuiteBuilder().usingGenerator(generator);
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
		
		testers.add(DoubleListAddAllAtIndexTester.class);
		testers.add(DoubleListAddAllArrayAtIndexTester.class);
		testers.add(DoubleListAddAllTester.class);
		testers.add(DoubleListAddAtIndexTester.class);
		testers.add(DoubleListAddTester.class);
		testers.add(DoubleListAbsentTester.class);
		testers.add(DoubleListPresentTester.class);
		testers.add(DoubleListCreationTester.class);
		testers.add(DoubleListEqualsTester.class);
		testers.add(DoubleListGetTester.class);
		testers.add(DoubleListGetElementsTester.class);
		testers.add(DoubleListExtractElementsTester.class);
		testers.add(DoubleListIndexOfTester.class);
		testers.add(DoubleListLastIndexOfTester.class);
		testers.add(DoubleListListIteratorTester.class);
		testers.add(DoubleListRemoveAllTester.class);
		testers.add(DoubleListRemoveAtIndexTester.class);
		testers.add(DoubleListRemoveTester.class);
		testers.add(DoubleListRemoveElementsTester.class);
		testers.add(DoubleListSwapRemoveAtIndexTester.class);
		testers.add(DoubleListSwapRemoveTester.class);
		testers.add(DoubleListFillBufferTester.class);		testers.add(DoubleListReplaceAllTester.class);
		testers.add(DoubleListRetainAllTester.class);
		testers.add(DoubleListSetTester.class);
		testers.add(DoubleListSubListTester.class);
		testers.add(DoubleListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Double>, Double>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<Double>(parentBuilder.getSubjectGenerator()))
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