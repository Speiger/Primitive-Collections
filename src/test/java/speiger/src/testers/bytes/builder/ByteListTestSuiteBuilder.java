package speiger.src.testers.bytes.builder;

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
import speiger.src.testers.bytes.generators.TestByteListGenerator;
import speiger.src.testers.bytes.tests.list.ByteListAbsentTester;
import speiger.src.testers.bytes.tests.list.ByteListAddAllArrayAtIndexTester;
import speiger.src.testers.bytes.tests.list.ByteListAddAllAtIndexTester;
import speiger.src.testers.bytes.tests.list.ByteListAddAllTester;
import speiger.src.testers.bytes.tests.list.ByteListAddAtIndexTester;
import speiger.src.testers.bytes.tests.list.ByteListAddTester;
import speiger.src.testers.bytes.tests.list.ByteListCreationTester;
import speiger.src.testers.bytes.tests.list.ByteListEqualsTester;
import speiger.src.testers.bytes.tests.list.ByteListExtractElementsTester;
import speiger.src.testers.bytes.tests.list.ByteListGetElementsTester;
import speiger.src.testers.bytes.tests.list.ByteListGetTester;
import speiger.src.testers.bytes.tests.list.ByteListIndexOfTester;
import speiger.src.testers.bytes.tests.list.ByteListLastIndexOfTester;
import speiger.src.testers.bytes.tests.list.ByteListListIteratorTester;
import speiger.src.testers.bytes.tests.list.ByteListPresentTester;
import speiger.src.testers.bytes.tests.list.ByteListRemoveAllTester;
import speiger.src.testers.bytes.tests.list.ByteListRemoveAtIndexTester;
import speiger.src.testers.bytes.tests.list.ByteListRemoveElementsTester;
import speiger.src.testers.bytes.tests.list.ByteListRemoveTester;
import speiger.src.testers.bytes.tests.list.ByteListFillBufferTester;import speiger.src.testers.bytes.tests.list.ByteListReplaceAllTester;
import speiger.src.testers.bytes.tests.list.ByteListRetainAllTester;
import speiger.src.testers.bytes.tests.list.ByteListSetTester;
import speiger.src.testers.bytes.tests.list.ByteListSubListTester;
import speiger.src.testers.bytes.tests.list.ByteListSwapRemoveAtIndexTester;
import speiger.src.testers.bytes.tests.list.ByteListSwapRemoveTester;
import speiger.src.testers.bytes.tests.list.ByteListToArrayTester;

@SuppressWarnings("javadoc")
public class ByteListTestSuiteBuilder extends ByteCollectionTestSuiteBuilder {
	public static ByteListTestSuiteBuilder using(TestByteListGenerator generator) {
		return (ByteListTestSuiteBuilder) new ByteListTestSuiteBuilder().usingGenerator(generator);
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
		
		testers.add(ByteListAddAllAtIndexTester.class);
		testers.add(ByteListAddAllArrayAtIndexTester.class);
		testers.add(ByteListAddAllTester.class);
		testers.add(ByteListAddAtIndexTester.class);
		testers.add(ByteListAddTester.class);
		testers.add(ByteListAbsentTester.class);
		testers.add(ByteListPresentTester.class);
		testers.add(ByteListCreationTester.class);
		testers.add(ByteListEqualsTester.class);
		testers.add(ByteListGetTester.class);
		testers.add(ByteListGetElementsTester.class);
		testers.add(ByteListExtractElementsTester.class);
		testers.add(ByteListIndexOfTester.class);
		testers.add(ByteListLastIndexOfTester.class);
		testers.add(ByteListListIteratorTester.class);
		testers.add(ByteListRemoveAllTester.class);
		testers.add(ByteListRemoveAtIndexTester.class);
		testers.add(ByteListRemoveTester.class);
		testers.add(ByteListRemoveElementsTester.class);
		testers.add(ByteListSwapRemoveAtIndexTester.class);
		testers.add(ByteListSwapRemoveTester.class);
		testers.add(ByteListFillBufferTester.class);		testers.add(ByteListReplaceAllTester.class);
		testers.add(ByteListRetainAllTester.class);
		testers.add(ByteListSetTester.class);
		testers.add(ByteListSubListTester.class);
		testers.add(ByteListToArrayTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		withFeatures(KNOWN_ORDER);
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Byte>, Byte>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
		if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
			derivedSuites.add(ListTestSuiteBuilder.using(new ReserializedListGenerator<Byte>(parentBuilder.getSubjectGenerator()))
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