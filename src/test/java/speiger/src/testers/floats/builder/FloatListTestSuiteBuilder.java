package speiger.src.testers.floats.builder;

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
import speiger.src.testers.floats.generators.TestFloatListGenerator;
import speiger.src.testers.floats.tests.list.FloatListAbsentTester;
import speiger.src.testers.floats.tests.list.FloatListAddAllArrayAtIndexTester;
import speiger.src.testers.floats.tests.list.FloatListAddAllAtIndexTester;
import speiger.src.testers.floats.tests.list.FloatListAddAllTester;
import speiger.src.testers.floats.tests.list.FloatListAddAtIndexTester;
import speiger.src.testers.floats.tests.list.FloatListAddTester;
import speiger.src.testers.floats.tests.list.FloatListCreationTester;
import speiger.src.testers.floats.tests.list.FloatListEqualsTester;
import speiger.src.testers.floats.tests.list.FloatListExtractElementsTester;
import speiger.src.testers.floats.tests.list.FloatListGetElementsTester;
import speiger.src.testers.floats.tests.list.FloatListGetTester;
import speiger.src.testers.floats.tests.list.FloatListIndexOfTester;
import speiger.src.testers.floats.tests.list.FloatListLastIndexOfTester;
import speiger.src.testers.floats.tests.list.FloatListListIteratorTester;
import speiger.src.testers.floats.tests.list.FloatListPresentTester;
import speiger.src.testers.floats.tests.list.FloatListRemoveAllTester;
import speiger.src.testers.floats.tests.list.FloatListRemoveAtIndexTester;
import speiger.src.testers.floats.tests.list.FloatListRemoveElementsTester;
import speiger.src.testers.floats.tests.list.FloatListRemoveTester;
import speiger.src.testers.floats.tests.list.FloatListFillBufferTester;import speiger.src.testers.floats.tests.list.FloatListReplaceAllTester;
import speiger.src.testers.floats.tests.list.FloatListRetainAllTester;
import speiger.src.testers.floats.tests.list.FloatListSetTester;
import speiger.src.testers.floats.tests.list.FloatListSubListTester;
import speiger.src.testers.floats.tests.list.FloatListSwapRemoveAtIndexTester;
import speiger.src.testers.floats.tests.list.FloatListSwapRemoveTester;
import speiger.src.testers.floats.tests.list.FloatListToArrayTester;

@SuppressWarnings("javadoc")
public class FloatListTestSuiteBuilder extends FloatCollectionTestSuiteBuilder {
	public static FloatListTestSuiteBuilder using(TestFloatListGenerator generator) {
		return (FloatListTestSuiteBuilder) new FloatListTestSuiteBuilder().usingGenerator(generator);
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
		
		testers.add(FloatListAddAllAtIndexTester.class);
		testers.add(FloatListAddAllArrayAtIndexTester.class);
		testers.add(FloatListAddAllTester.class);
		testers.add(FloatListAddAtIndexTester.class);
		testers.add(FloatListAddTester.class);
		testers.add(FloatListAbsentTester.class);
		testers.add(FloatListPresentTester.class);
		testers.add(FloatListCreationTester.class);
		testers.add(FloatListEqualsTester.class);
		testers.add(FloatListGetTester.class);
		testers.add(FloatListGetElementsTester.class);
		testers.add(FloatListExtractElementsTester.class);
		testers.add(FloatListIndexOfTester.class);
		testers.add(FloatListLastIndexOfTester.class);
		testers.add(FloatListListIteratorTester.class);
		testers.add(FloatListRemoveAllTester.class);
		testers.add(FloatListRemoveAtIndexTester.class);
		testers.add(FloatListRemoveTester.class);
		testers.add(FloatListRemoveElementsTester.class);
		testers.add(FloatListSwapRemoveAtIndexTester.class);
		testers.add(FloatListSwapRemoveTester.class);
		testers.add(FloatListFillBufferTester.class);		testers.add(FloatListReplaceAllTester.class);
		testers.add(FloatListRetainAllTester.class);
		testers.add(FloatListSetTester.class);
		testers.add(FloatListSubListTester.class);
		testers.add(FloatListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Float>, Float>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<Float>(parentBuilder.getSubjectGenerator()))
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