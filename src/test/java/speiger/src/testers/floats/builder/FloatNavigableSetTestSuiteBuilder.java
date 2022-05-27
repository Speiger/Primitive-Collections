package speiger.src.testers.floats.builder;

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
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.utils.FloatLists;
import speiger.src.testers.floats.generators.TestFloatNavigableSetGenerator;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.TestFloatSortedSetGenerator;
import speiger.src.testers.floats.impl.FloatSortedSetSubsetTestSetGenerator.FloatNavigableSetSubsetTestSetGenerator;
import speiger.src.testers.floats.tests.set.FloatNavigableSetNavigationTester;
import speiger.src.testers.floats.utils.FloatSamples;

@SuppressWarnings("javadoc")
public class FloatNavigableSetTestSuiteBuilder extends FloatSortedSetTestSuiteBuilder {
	public static FloatNavigableSetTestSuiteBuilder using(TestFloatNavigableSetGenerator generator) {
		return (FloatNavigableSetTestSuiteBuilder) new FloatNavigableSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(NavigableSetNavigationTester.class);
		testers.add(FloatNavigableSetNavigationTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Float>, Float>> parentBuilder) {
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
	FloatSortedSetTestSuiteBuilder newBuilderUsing(TestFloatSortedSetGenerator delegate, Bound to, Bound from) {
	    return using(new FloatNavigableSetSubsetTestSetGenerator (delegate, to, from));
	}
	
	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Float>, Float>> parentBuilder) {
		TestFloatNavigableSetGenerator delegate = (TestFloatNavigableSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(DESCENDING_VIEW);
		features.addAll(parentBuilder.getFeatures());

		return FloatNavigableSetTestSuiteBuilder.using(new TestFloatSetGenerator() {

			@Override
			public SampleElements<Float> samples() {
				return delegate.samples();
			}
			
			@Override
			public FloatSamples getSamples() {
				return delegate.getSamples();
			}

			@Override
			public FloatIterable order(FloatList insertionOrder) {
				FloatList list = new FloatArrayList();
				delegate.order(insertionOrder).forEach(list::add);
				FloatLists.reverse(list);
				return list;
			}

			@Override
			public Iterable<Float> order(List<Float> insertionOrder) {
				FloatList list = new FloatArrayList();
				for(Float entry : delegate.order(insertionOrder))
				{
					list.add(entry.floatValue());
				}
				FloatLists.reverse(list);
				return list;
			}
			
			@Override
			public FloatNavigableSet create(float... elements) {
				return delegate.create(elements).descendingSet();
			}

			@Override
			public FloatNavigableSet create(Object... elements) {
				return delegate.create(elements).descendingSet();
			}
		}).named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}
}