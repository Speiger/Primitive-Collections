package speiger.src.testers.shorts.builder;

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
import speiger.src.testers.shorts.generators.TestShortListGenerator;
import speiger.src.testers.shorts.tests.list.ShortListAbsentTester;
import speiger.src.testers.shorts.tests.list.ShortListAddAllArrayAtIndexTester;
import speiger.src.testers.shorts.tests.list.ShortListAddAllAtIndexTester;
import speiger.src.testers.shorts.tests.list.ShortListAddAllTester;
import speiger.src.testers.shorts.tests.list.ShortListAddAtIndexTester;
import speiger.src.testers.shorts.tests.list.ShortListAddTester;
import speiger.src.testers.shorts.tests.list.ShortListCreationTester;
import speiger.src.testers.shorts.tests.list.ShortListEqualsTester;
import speiger.src.testers.shorts.tests.list.ShortListExtractElementsTester;
import speiger.src.testers.shorts.tests.list.ShortListGetElementsTester;
import speiger.src.testers.shorts.tests.list.ShortListGetTester;
import speiger.src.testers.shorts.tests.list.ShortListIndexOfTester;
import speiger.src.testers.shorts.tests.list.ShortListLastIndexOfTester;
import speiger.src.testers.shorts.tests.list.ShortListListIteratorTester;
import speiger.src.testers.shorts.tests.list.ShortListPresentTester;
import speiger.src.testers.shorts.tests.list.ShortListRemoveAllTester;
import speiger.src.testers.shorts.tests.list.ShortListRemoveAtIndexTester;
import speiger.src.testers.shorts.tests.list.ShortListRemoveElementsTester;
import speiger.src.testers.shorts.tests.list.ShortListRemoveTester;
import speiger.src.testers.shorts.tests.list.ShortListFillBufferTester;
import speiger.src.testers.shorts.tests.list.ShortListReplaceAllTester;
import speiger.src.testers.shorts.tests.list.ShortListRetainAllTester;
import speiger.src.testers.shorts.tests.list.ShortListSetTester;
import speiger.src.testers.shorts.tests.list.ShortListSubListTester;
import speiger.src.testers.shorts.tests.list.ShortListSwapRemoveAtIndexTester;
import speiger.src.testers.shorts.tests.list.ShortListSwapRemoveTester;
import speiger.src.testers.shorts.tests.list.ShortListToArrayTester;

public class ShortListTestSuiteBuilder extends ShortCollectionTestSuiteBuilder {
	public static ShortListTestSuiteBuilder using(TestShortListGenerator generator) {
		return (ShortListTestSuiteBuilder) new ShortListTestSuiteBuilder().usingGenerator(generator);
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
		
		testers.add(ShortListAddAllAtIndexTester.class);
		testers.add(ShortListAddAllArrayAtIndexTester.class);
		testers.add(ShortListAddAllTester.class);
		testers.add(ShortListAddAtIndexTester.class);
		testers.add(ShortListAddTester.class);
		testers.add(ShortListAbsentTester.class);
		testers.add(ShortListPresentTester.class);
		testers.add(ShortListCreationTester.class);
		testers.add(ShortListEqualsTester.class);
		testers.add(ShortListGetTester.class);
		testers.add(ShortListGetElementsTester.class);
		testers.add(ShortListExtractElementsTester.class);
		testers.add(ShortListIndexOfTester.class);
		testers.add(ShortListLastIndexOfTester.class);
		testers.add(ShortListListIteratorTester.class);
		testers.add(ShortListRemoveAllTester.class);
		testers.add(ShortListRemoveAtIndexTester.class);
		testers.add(ShortListRemoveTester.class);
		testers.add(ShortListRemoveElementsTester.class);
		testers.add(ShortListSwapRemoveAtIndexTester.class);
		testers.add(ShortListSwapRemoveTester.class);
		testers.add(ShortListFillBufferTester.class);
		testers.add(ShortListReplaceAllTester.class);
		testers.add(ShortListRetainAllTester.class);
		testers.add(ShortListSetTester.class);
		testers.add(ShortListSubListTester.class);
		testers.add(ShortListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Short>, Short>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<Short>(parentBuilder.getSubjectGenerator()))
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