package speiger.src.testers.objects.builder;

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
import speiger.src.testers.objects.generators.TestObjectListGenerator;
import speiger.src.testers.objects.tests.list.ObjectListAbsentTester;
import speiger.src.testers.objects.tests.list.ObjectListAddAllArrayAtIndexTester;
import speiger.src.testers.objects.tests.list.ObjectListAddAllAtIndexTester;
import speiger.src.testers.objects.tests.list.ObjectListAddAllTester;
import speiger.src.testers.objects.tests.list.ObjectListAddAtIndexTester;
import speiger.src.testers.objects.tests.list.ObjectListAddTester;
import speiger.src.testers.objects.tests.list.ObjectListCreationTester;
import speiger.src.testers.objects.tests.list.ObjectListEqualsTester;
import speiger.src.testers.objects.tests.list.ObjectListExtractElementsTester;
import speiger.src.testers.objects.tests.list.ObjectListGetElementsTester;
import speiger.src.testers.objects.tests.list.ObjectListGetTester;
import speiger.src.testers.objects.tests.list.ObjectListIndexOfTester;
import speiger.src.testers.objects.tests.list.ObjectListLastIndexOfTester;
import speiger.src.testers.objects.tests.list.ObjectListListIteratorTester;
import speiger.src.testers.objects.tests.list.ObjectListPresentTester;
import speiger.src.testers.objects.tests.list.ObjectListRemoveAllTester;
import speiger.src.testers.objects.tests.list.ObjectListRemoveAtIndexTester;
import speiger.src.testers.objects.tests.list.ObjectListRemoveElementsTester;
import speiger.src.testers.objects.tests.list.ObjectListRemoveTester;
import speiger.src.testers.objects.tests.list.ObjectListReplaceAllTester;
import speiger.src.testers.objects.tests.list.ObjectListRetainAllTester;
import speiger.src.testers.objects.tests.list.ObjectListSetTester;
import speiger.src.testers.objects.tests.list.ObjectListSubListTester;
import speiger.src.testers.objects.tests.list.ObjectListSwapRemoveAtIndexTester;
import speiger.src.testers.objects.tests.list.ObjectListSwapRemoveTester;
import speiger.src.testers.objects.tests.list.ObjectListToArrayTester;

@SuppressWarnings("javadoc")
public class ObjectListTestSuiteBuilder<T> extends ObjectCollectionTestSuiteBuilder<T> {
	public static <T> ObjectListTestSuiteBuilder<T> using(TestObjectListGenerator<T> generator) {
		return (ObjectListTestSuiteBuilder<T>) new ObjectListTestSuiteBuilder<T>().usingGenerator(generator);
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
		
		testers.add(ObjectListAddAllAtIndexTester.class);
		testers.add(ObjectListAddAllArrayAtIndexTester.class);
		testers.add(ObjectListAddAllTester.class);
		testers.add(ObjectListAddAtIndexTester.class);
		testers.add(ObjectListAddTester.class);
		testers.add(ObjectListAbsentTester.class);
		testers.add(ObjectListPresentTester.class);
		testers.add(ObjectListCreationTester.class);
		testers.add(ObjectListEqualsTester.class);
		testers.add(ObjectListGetTester.class);
		testers.add(ObjectListGetElementsTester.class);
		testers.add(ObjectListExtractElementsTester.class);
		testers.add(ObjectListIndexOfTester.class);
		testers.add(ObjectListLastIndexOfTester.class);
		testers.add(ObjectListListIteratorTester.class);
		testers.add(ObjectListRemoveAllTester.class);
		testers.add(ObjectListRemoveAtIndexTester.class);
		testers.add(ObjectListRemoveTester.class);
		testers.add(ObjectListRemoveElementsTester.class);
		testers.add(ObjectListSwapRemoveAtIndexTester.class);
		testers.add(ObjectListSwapRemoveTester.class);
		testers.add(ObjectListReplaceAllTester.class);
		testers.add(ObjectListRetainAllTester.class);
		testers.add(ObjectListSetTester.class);
		testers.add(ObjectListSubListTester.class);
		testers.add(ObjectListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<T>, T>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<T>(parentBuilder.getSubjectGenerator()))
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