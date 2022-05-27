package speiger.src.testers.ints.builder;

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
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.utils.IntLists;
import speiger.src.testers.ints.generators.TestIntNavigableSetGenerator;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.TestIntSortedSetGenerator;
import speiger.src.testers.ints.impl.IntSortedSetSubsetTestSetGenerator.IntNavigableSetSubsetTestSetGenerator;
import speiger.src.testers.ints.tests.set.IntNavigableSetNavigationTester;
import speiger.src.testers.ints.utils.IntSamples;

public class IntNavigableSetTestSuiteBuilder extends IntSortedSetTestSuiteBuilder {
	public static IntNavigableSetTestSuiteBuilder using(TestIntNavigableSetGenerator generator) {
		return (IntNavigableSetTestSuiteBuilder) new IntNavigableSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(NavigableSetNavigationTester.class);
		testers.add(IntNavigableSetNavigationTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Integer>, Integer>> parentBuilder) {
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
	IntSortedSetTestSuiteBuilder newBuilderUsing(TestIntSortedSetGenerator delegate, Bound to, Bound from) {
	    return using(new IntNavigableSetSubsetTestSetGenerator (delegate, to, from));
	}
	
	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Integer>, Integer>> parentBuilder) {
		TestIntNavigableSetGenerator delegate = (TestIntNavigableSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(DESCENDING_VIEW);
		features.addAll(parentBuilder.getFeatures());

		return IntNavigableSetTestSuiteBuilder.using(new TestIntSetGenerator() {

			@Override
			public SampleElements<Integer> samples() {
				return delegate.samples();
			}
			
			@Override
			public IntSamples getSamples() {
				return delegate.getSamples();
			}

			@Override
			public IntIterable order(IntList insertionOrder) {
				IntList list = new IntArrayList();
				delegate.order(insertionOrder).forEach(list::add);
				IntLists.reverse(list);
				return list;
			}

			@Override
			public Iterable<Integer> order(List<Integer> insertionOrder) {
				IntList list = new IntArrayList();
				for(Integer entry : delegate.order(insertionOrder))
				{
					list.add(entry.intValue());
				}
				IntLists.reverse(list);
				return list;
			}
			
			@Override
			public IntNavigableSet create(int... elements) {
				return delegate.create(elements).descendingSet();
			}

			@Override
			public IntNavigableSet create(Object... elements) {
				return delegate.create(elements).descendingSet();
			}
		}).named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}
}