package speiger.src.testers.longs.builder;

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
import speiger.src.testers.longs.generators.TestLongListGenerator;
import speiger.src.testers.longs.tests.list.LongListAbsentTester;
import speiger.src.testers.longs.tests.list.LongListAddAllArrayAtIndexTester;
import speiger.src.testers.longs.tests.list.LongListAddAllAtIndexTester;
import speiger.src.testers.longs.tests.list.LongListAddAllTester;
import speiger.src.testers.longs.tests.list.LongListAddAtIndexTester;
import speiger.src.testers.longs.tests.list.LongListAddTester;
import speiger.src.testers.longs.tests.list.LongListCreationTester;
import speiger.src.testers.longs.tests.list.LongListEqualsTester;
import speiger.src.testers.longs.tests.list.LongListExtractElementsTester;
import speiger.src.testers.longs.tests.list.LongListGetElementsTester;
import speiger.src.testers.longs.tests.list.LongListGetTester;
import speiger.src.testers.longs.tests.list.LongListIndexOfTester;
import speiger.src.testers.longs.tests.list.LongListLastIndexOfTester;
import speiger.src.testers.longs.tests.list.LongListListIteratorTester;
import speiger.src.testers.longs.tests.list.LongListPresentTester;
import speiger.src.testers.longs.tests.list.LongListRemoveAllTester;
import speiger.src.testers.longs.tests.list.LongListRemoveAtIndexTester;
import speiger.src.testers.longs.tests.list.LongListRemoveElementsTester;
import speiger.src.testers.longs.tests.list.LongListRemoveTester;
import speiger.src.testers.longs.tests.list.LongListFillBufferTester;
import speiger.src.testers.longs.tests.list.LongListReplaceAllTester;
import speiger.src.testers.longs.tests.list.LongListRetainAllTester;
import speiger.src.testers.longs.tests.list.LongListSetTester;
import speiger.src.testers.longs.tests.list.LongListSubListTester;
import speiger.src.testers.longs.tests.list.LongListSwapRemoveAtIndexTester;
import speiger.src.testers.longs.tests.list.LongListSwapRemoveTester;
import speiger.src.testers.longs.tests.list.LongListToArrayTester;

public class LongListTestSuiteBuilder extends LongCollectionTestSuiteBuilder {
	public static LongListTestSuiteBuilder using(TestLongListGenerator generator) {
		return (LongListTestSuiteBuilder) new LongListTestSuiteBuilder().usingGenerator(generator);
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
		
		testers.add(LongListAddAllAtIndexTester.class);
		testers.add(LongListAddAllArrayAtIndexTester.class);
		testers.add(LongListAddAllTester.class);
		testers.add(LongListAddAtIndexTester.class);
		testers.add(LongListAddTester.class);
		testers.add(LongListAbsentTester.class);
		testers.add(LongListPresentTester.class);
		testers.add(LongListCreationTester.class);
		testers.add(LongListEqualsTester.class);
		testers.add(LongListGetTester.class);
		testers.add(LongListGetElementsTester.class);
		testers.add(LongListExtractElementsTester.class);
		testers.add(LongListIndexOfTester.class);
		testers.add(LongListLastIndexOfTester.class);
		testers.add(LongListListIteratorTester.class);
		testers.add(LongListRemoveAllTester.class);
		testers.add(LongListRemoveAtIndexTester.class);
		testers.add(LongListRemoveTester.class);
		testers.add(LongListRemoveElementsTester.class);
		testers.add(LongListSwapRemoveAtIndexTester.class);
		testers.add(LongListSwapRemoveTester.class);
		testers.add(LongListFillBufferTester.class);
		testers.add(LongListReplaceAllTester.class);
		testers.add(LongListRetainAllTester.class);
		testers.add(LongListSetTester.class);
		testers.add(LongListSubListTester.class);
		testers.add(LongListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Long>, Long>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<Long>(parentBuilder.getSubjectGenerator()))
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