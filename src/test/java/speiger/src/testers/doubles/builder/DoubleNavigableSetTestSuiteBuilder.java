package speiger.src.testers.doubles.builder;

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
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.utils.DoubleLists;
import speiger.src.testers.doubles.generators.TestDoubleNavigableSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleSortedSetGenerator;
import speiger.src.testers.doubles.impl.DoubleSortedSetSubsetTestSetGenerator.DoubleNavigableSetSubsetTestSetGenerator;
import speiger.src.testers.doubles.tests.set.DoubleNavigableSetNavigationTester;
import speiger.src.testers.doubles.utils.DoubleSamples;

@SuppressWarnings("javadoc")
public class DoubleNavigableSetTestSuiteBuilder extends DoubleSortedSetTestSuiteBuilder {
	public static DoubleNavigableSetTestSuiteBuilder using(TestDoubleNavigableSetGenerator generator) {
		return (DoubleNavigableSetTestSuiteBuilder) new DoubleNavigableSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(NavigableSetNavigationTester.class);
		testers.add(DoubleNavigableSetNavigationTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Double>, Double>> parentBuilder) {
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
	DoubleSortedSetTestSuiteBuilder newBuilderUsing(TestDoubleSortedSetGenerator delegate, Bound to, Bound from) {
	    return using(new DoubleNavigableSetSubsetTestSetGenerator (delegate, to, from));
	}
	
	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Double>, Double>> parentBuilder) {
		TestDoubleNavigableSetGenerator delegate = (TestDoubleNavigableSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(DESCENDING_VIEW);
		features.addAll(parentBuilder.getFeatures());

		return DoubleNavigableSetTestSuiteBuilder.using(new TestDoubleSetGenerator() {

			@Override
			public SampleElements<Double> samples() {
				return delegate.samples();
			}
			
			@Override
			public DoubleSamples getSamples() {
				return delegate.getSamples();
			}

			@Override
			public DoubleIterable order(DoubleList insertionOrder) {
				DoubleList list = new DoubleArrayList();
				delegate.order(insertionOrder).forEach(list::add);
				DoubleLists.reverse(list);
				return list;
			}

			@Override
			public Iterable<Double> order(List<Double> insertionOrder) {
				DoubleList list = new DoubleArrayList();
				for(Double entry : delegate.order(insertionOrder))
				{
					list.add(entry.doubleValue());
				}
				DoubleLists.reverse(list);
				return list;
			}
			
			@Override
			public DoubleNavigableSet create(double... elements) {
				return delegate.create(elements).descendingSet();
			}

			@Override
			public DoubleNavigableSet create(Object... elements) {
				return delegate.create(elements).descendingSet();
			}
		}).named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}
}