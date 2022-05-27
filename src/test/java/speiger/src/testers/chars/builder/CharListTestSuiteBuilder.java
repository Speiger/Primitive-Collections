package speiger.src.testers.chars.builder;

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
import speiger.src.testers.chars.generators.TestCharListGenerator;
import speiger.src.testers.chars.tests.list.CharListAbsentTester;
import speiger.src.testers.chars.tests.list.CharListAddAllArrayAtIndexTester;
import speiger.src.testers.chars.tests.list.CharListAddAllAtIndexTester;
import speiger.src.testers.chars.tests.list.CharListAddAllTester;
import speiger.src.testers.chars.tests.list.CharListAddAtIndexTester;
import speiger.src.testers.chars.tests.list.CharListAddTester;
import speiger.src.testers.chars.tests.list.CharListCreationTester;
import speiger.src.testers.chars.tests.list.CharListEqualsTester;
import speiger.src.testers.chars.tests.list.CharListExtractElementsTester;
import speiger.src.testers.chars.tests.list.CharListGetElementsTester;
import speiger.src.testers.chars.tests.list.CharListGetTester;
import speiger.src.testers.chars.tests.list.CharListIndexOfTester;
import speiger.src.testers.chars.tests.list.CharListLastIndexOfTester;
import speiger.src.testers.chars.tests.list.CharListListIteratorTester;
import speiger.src.testers.chars.tests.list.CharListPresentTester;
import speiger.src.testers.chars.tests.list.CharListRemoveAllTester;
import speiger.src.testers.chars.tests.list.CharListRemoveAtIndexTester;
import speiger.src.testers.chars.tests.list.CharListRemoveElementsTester;
import speiger.src.testers.chars.tests.list.CharListRemoveTester;
import speiger.src.testers.chars.tests.list.CharListFillBufferTester;import speiger.src.testers.chars.tests.list.CharListReplaceAllTester;
import speiger.src.testers.chars.tests.list.CharListRetainAllTester;
import speiger.src.testers.chars.tests.list.CharListSetTester;
import speiger.src.testers.chars.tests.list.CharListSubListTester;
import speiger.src.testers.chars.tests.list.CharListSwapRemoveAtIndexTester;
import speiger.src.testers.chars.tests.list.CharListSwapRemoveTester;
import speiger.src.testers.chars.tests.list.CharListToArrayTester;

@SuppressWarnings("javadoc")
public class CharListTestSuiteBuilder extends CharCollectionTestSuiteBuilder {
	public static CharListTestSuiteBuilder using(TestCharListGenerator generator) {
		return (CharListTestSuiteBuilder) new CharListTestSuiteBuilder().usingGenerator(generator);
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
		
		testers.add(CharListAddAllAtIndexTester.class);
		testers.add(CharListAddAllArrayAtIndexTester.class);
		testers.add(CharListAddAllTester.class);
		testers.add(CharListAddAtIndexTester.class);
		testers.add(CharListAddTester.class);
		testers.add(CharListAbsentTester.class);
		testers.add(CharListPresentTester.class);
		testers.add(CharListCreationTester.class);
		testers.add(CharListEqualsTester.class);
		testers.add(CharListGetTester.class);
		testers.add(CharListGetElementsTester.class);
		testers.add(CharListExtractElementsTester.class);
		testers.add(CharListIndexOfTester.class);
		testers.add(CharListLastIndexOfTester.class);
		testers.add(CharListListIteratorTester.class);
		testers.add(CharListRemoveAllTester.class);
		testers.add(CharListRemoveAtIndexTester.class);
		testers.add(CharListRemoveTester.class);
		testers.add(CharListRemoveElementsTester.class);
		testers.add(CharListSwapRemoveAtIndexTester.class);
		testers.add(CharListSwapRemoveTester.class);
		testers.add(CharListFillBufferTester.class);		testers.add(CharListReplaceAllTester.class);
		testers.add(CharListRetainAllTester.class);
		testers.add(CharListSetTester.class);
		testers.add(CharListSubListTester.class);
		testers.add(CharListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Character>, Character>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<Character>(parentBuilder.getSubjectGenerator()))
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