package speiger.src.testers.booleans.builder;

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
import speiger.src.testers.booleans.generators.TestBooleanListGenerator;
import speiger.src.testers.booleans.tests.list.BooleanListAbsentTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAllArrayAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAllAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAllTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddTester;
import speiger.src.testers.booleans.tests.list.BooleanListCreationTester;
import speiger.src.testers.booleans.tests.list.BooleanListEqualsTester;
import speiger.src.testers.booleans.tests.list.BooleanListExtractElementsTester;
import speiger.src.testers.booleans.tests.list.BooleanListGetElementsTester;
import speiger.src.testers.booleans.tests.list.BooleanListGetTester;
import speiger.src.testers.booleans.tests.list.BooleanListIndexOfTester;
import speiger.src.testers.booleans.tests.list.BooleanListLastIndexOfTester;
import speiger.src.testers.booleans.tests.list.BooleanListListIteratorTester;
import speiger.src.testers.booleans.tests.list.BooleanListPresentTester;
import speiger.src.testers.booleans.tests.list.BooleanListRemoveAllTester;
import speiger.src.testers.booleans.tests.list.BooleanListRemoveAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListRemoveElementsTester;
import speiger.src.testers.booleans.tests.list.BooleanListRemoveTester;
import speiger.src.testers.booleans.tests.list.BooleanListRetainAllTester;
import speiger.src.testers.booleans.tests.list.BooleanListSetTester;
import speiger.src.testers.booleans.tests.list.BooleanListSubListTester;
import speiger.src.testers.booleans.tests.list.BooleanListSwapRemoveAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListSwapRemoveTester;
import speiger.src.testers.booleans.tests.list.BooleanListToArrayTester;

@SuppressWarnings("javadoc")
public class BooleanListTestSuiteBuilder extends BooleanCollectionTestSuiteBuilder {
	public static BooleanListTestSuiteBuilder using(TestBooleanListGenerator generator) {
		return (BooleanListTestSuiteBuilder) new BooleanListTestSuiteBuilder().usingGenerator(generator);
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
		
		testers.add(BooleanListAddAllAtIndexTester.class);
		testers.add(BooleanListAddAllArrayAtIndexTester.class);
		testers.add(BooleanListAddAllTester.class);
		testers.add(BooleanListAddAtIndexTester.class);
		testers.add(BooleanListAddTester.class);
		testers.add(BooleanListAbsentTester.class);
		testers.add(BooleanListPresentTester.class);
		testers.add(BooleanListCreationTester.class);
		testers.add(BooleanListEqualsTester.class);
		testers.add(BooleanListGetTester.class);
		testers.add(BooleanListGetElementsTester.class);
		testers.add(BooleanListExtractElementsTester.class);
		testers.add(BooleanListIndexOfTester.class);
		testers.add(BooleanListLastIndexOfTester.class);
		testers.add(BooleanListListIteratorTester.class);
		testers.add(BooleanListRemoveAllTester.class);
		testers.add(BooleanListRemoveAtIndexTester.class);
		testers.add(BooleanListRemoveTester.class);
		testers.add(BooleanListRemoveElementsTester.class);
		testers.add(BooleanListSwapRemoveAtIndexTester.class);
		testers.add(BooleanListSwapRemoveTester.class);
		testers.add(BooleanListRetainAllTester.class);
		testers.add(BooleanListSetTester.class);
		testers.add(BooleanListSubListTester.class);
		testers.add(BooleanListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Boolean>, Boolean>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<Boolean>(parentBuilder.getSubjectGenerator()))
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