package speiger.src.testers.bytes.builder;

import static com.google.common.collect.testing.features.CollectionFeature.DESCENDING_VIEW;
import static com.google.common.collect.testing.features.CollectionFeature.SUBSET_VIEW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.testers.NavigableSetNavigationTester;

import junit.framework.TestSuite;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.utils.ByteLists;
import speiger.src.testers.bytes.generators.TestByteNavigableSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSortedSetGenerator;
import speiger.src.testers.bytes.impl.ByteSortedSetSubsetTestSetGenerator.ByteNavigableSetSubsetTestSetGenerator;
import speiger.src.testers.bytes.tests.set.ByteNavigableSetNavigationTester;
import speiger.src.testers.bytes.utils.ByteSamples;

public class ByteNavigableSetTestSuiteBuilder extends ByteSortedSetTestSuiteBuilder {
	public static ByteNavigableSetTestSuiteBuilder using(TestByteNavigableSetGenerator generator) {
		return (ByteNavigableSetTestSuiteBuilder) new ByteNavigableSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(NavigableSetNavigationTester.class);
		testers.add(ByteNavigableSetNavigationTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Byte>, Byte>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));

		if (!parentBuilder.getFeatures().contains(SUBSET_VIEW)) {
			// Other combinations are inherited from SortedSetTestSuiteBuilder.
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.NO_BOUND, Bound.INCLUSIVE));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.EXCLUSIVE));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.INCLUSIVE));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.INCLUSIVE, Bound.INCLUSIVE));
		}
		if (!parentBuilder.getFeatures().contains(DESCENDING_VIEW)) {
			derivedSuites.add(createDescendingSuite(parentBuilder));
		}
		return derivedSuites;
	}

	@Override
	ByteSortedSetTestSuiteBuilder newBuilderUsing(TestByteSortedSetGenerator delegate, Bound to, Bound from) {
	    return using(new ByteNavigableSetSubsetTestSetGenerator(delegate, to, from));
	}
	
	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Byte>, Byte>> parentBuilder) {
		TestByteNavigableSetGenerator delegate = (TestByteNavigableSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(DESCENDING_VIEW);
		features.addAll(parentBuilder.getFeatures());

		return ByteNavigableSetTestSuiteBuilder.using(new TestByteSetGenerator() {

			@Override
			public SampleElements<Byte> samples() {
				return delegate.samples();
			}
			
			@Override
			public ByteSamples getSamples() {
				return delegate.getSamples();
			}

			@Override
			public ByteIterable order(ByteList insertionOrder) {
				ByteList list = new ByteArrayList();
				delegate.order(insertionOrder).forEach(list::add);
				ByteLists.reverse(list);
				return list;
			}

			@Override
			public Iterable<Byte> order(List<Byte> insertionOrder) {
				ByteList list = new ByteArrayList();
				for(Byte entry : delegate.order(insertionOrder))
				{
					list.add(entry.byteValue());
				}
				ByteLists.reverse(list);
				return list;
			}

			@Override
			public ByteNavigableSet create(byte... elements) {
				return delegate.create(elements).descendingSet();
			}

			@Override
			public ByteNavigableSet create(Object... elements) {
				return delegate.create(elements).descendingSet();
			}
		}).named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}
}