package speiger.src.testers.shorts.builder;

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
import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.utils.ShortLists;
import speiger.src.testers.shorts.generators.TestShortNavigableSetGenerator;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.TestShortSortedSetGenerator;
import speiger.src.testers.shorts.impl.ShortSortedSetSubsetTestSetGenerator.ShortNavigableSetSubsetTestSetGenerator;
import speiger.src.testers.shorts.tests.set.ShortNavigableSetNavigationTester;
import speiger.src.testers.shorts.utils.ShortSamples;

public class ShortNavigableSetTestSuiteBuilder extends ShortSortedSetTestSuiteBuilder {
	public static ShortNavigableSetTestSuiteBuilder using(TestShortNavigableSetGenerator generator) {
		return (ShortNavigableSetTestSuiteBuilder) new ShortNavigableSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(NavigableSetNavigationTester.class);
		testers.add(ShortNavigableSetNavigationTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Short>, Short>> parentBuilder) {
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
	ShortSortedSetTestSuiteBuilder newBuilderUsing(TestShortSortedSetGenerator delegate, Bound to, Bound from) {
	    return using(new ShortNavigableSetSubsetTestSetGenerator(delegate, to, from));
	}
	
	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Short>, Short>> parentBuilder) {
		TestShortNavigableSetGenerator delegate = (TestShortNavigableSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(DESCENDING_VIEW);
		features.addAll(parentBuilder.getFeatures());

		return ShortNavigableSetTestSuiteBuilder.using(new TestShortSetGenerator() {

			@Override
			public SampleElements<Short> samples() {
				return delegate.samples();
			}
			
			@Override
			public ShortSamples getSamples() {
				return delegate.getSamples();
			}

			@Override
			public ShortIterable order(ShortList insertionOrder) {
				ShortList list = new ShortArrayList();
				delegate.order(insertionOrder).forEach(list::add);
				ShortLists.reverse(list);
				return list;
			}

			@Override
			public Iterable<Short> order(List<Short> insertionOrder) {
				ShortList list = new ShortArrayList();
				for(Short entry : delegate.order(insertionOrder))
				{
					list.add(entry.shortValue());
				}
				ShortLists.reverse(list);
				return list;
			}

			@Override
			public ShortNavigableSet create(short... elements) {
				return delegate.create(elements).descendingSet();
			}

			@Override
			public ShortNavigableSet create(Object... elements) {
				return delegate.create(elements).descendingSet();
			}
		}).named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}
}