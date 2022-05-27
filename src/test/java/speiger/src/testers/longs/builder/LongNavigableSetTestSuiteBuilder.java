package speiger.src.testers.longs.builder;

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
import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.utils.LongLists;
import speiger.src.testers.longs.generators.TestLongNavigableSetGenerator;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.longs.impl.LongSortedSetSubsetTestSetGenerator.LongNavigableSetSubsetTestSetGenerator;
import speiger.src.testers.longs.tests.set.LongNavigableSetNavigationTester;
import speiger.src.testers.longs.utils.LongSamples;

public class LongNavigableSetTestSuiteBuilder extends LongSortedSetTestSuiteBuilder {
	public static LongNavigableSetTestSuiteBuilder using(TestLongNavigableSetGenerator generator) {
		return (LongNavigableSetTestSuiteBuilder) new LongNavigableSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(NavigableSetNavigationTester.class);
		testers.add(LongNavigableSetNavigationTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Long>, Long>> parentBuilder) {
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
	LongSortedSetTestSuiteBuilder newBuilderUsing(TestLongSortedSetGenerator delegate, Bound to, Bound from) {
	    return using(new LongNavigableSetSubsetTestSetGenerator (delegate, to, from));
	}
	
	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Long>, Long>> parentBuilder) {
		TestLongNavigableSetGenerator delegate = (TestLongNavigableSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(DESCENDING_VIEW);
		features.addAll(parentBuilder.getFeatures());

		return LongNavigableSetTestSuiteBuilder.using(new TestLongSetGenerator() {

			@Override
			public SampleElements<Long> samples() {
				return delegate.samples();
			}
			
			@Override
			public LongSamples getSamples() {
				return delegate.getSamples();
			}

			@Override
			public LongIterable order(LongList insertionOrder) {
				LongList list = new LongArrayList();
				delegate.order(insertionOrder).forEach(list::add);
				LongLists.reverse(list);
				return list;
			}

			@Override
			public Iterable<Long> order(List<Long> insertionOrder) {
				LongList list = new LongArrayList();
				for(Long entry : delegate.order(insertionOrder))
				{
					list.add(entry.longValue());
				}
				LongLists.reverse(list);
				return list;
			}
			
			@Override
			public LongNavigableSet create(long... elements) {
				return delegate.create(elements).descendingSet();
			}

			@Override
			public LongNavigableSet create(Object... elements) {
				return delegate.create(elements).descendingSet();
			}
		}).named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}
}