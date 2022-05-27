package speiger.src.testers.objects.builder;

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
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.objects.impl.ObjectSortedSetSubsetTestSetGenerator.ObjectNavigableSetSubsetTestSetGenerator;
import speiger.src.testers.objects.tests.set.ObjectNavigableSetNavigationTester;
import speiger.src.testers.objects.utils.ObjectSamples;

public class ObjectNavigableSetTestSuiteBuilder<T> extends ObjectSortedSetTestSuiteBuilder<T> {
	public static <T> ObjectNavigableSetTestSuiteBuilder<T> using(TestObjectNavigableSetGenerator<T> generator) {
		return (ObjectNavigableSetTestSuiteBuilder<T>) new ObjectNavigableSetTestSuiteBuilder<T>().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(NavigableSetNavigationTester.class);
		testers.add(ObjectNavigableSetNavigationTester.class);
		return testers;
	}
	
	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<T>, T>> parentBuilder) {
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
	ObjectSortedSetTestSuiteBuilder<T> newBuilderUsing(TestObjectSortedSetGenerator<T> delegate, Bound to, Bound from) {
	    return using(new ObjectNavigableSetSubsetTestSetGenerator <>(delegate, to, from));
	}
	
	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<T>, T>> parentBuilder) {
		TestObjectNavigableSetGenerator<T> delegate = (TestObjectNavigableSetGenerator<T>) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(DESCENDING_VIEW);
		features.addAll(parentBuilder.getFeatures());

		return ObjectNavigableSetTestSuiteBuilder.using(new TestObjectSetGenerator<T>() {

			@Override
			public SampleElements<T> samples() {
				return delegate.samples();
			}
			
			@Override
			public ObjectSamples<T> getSamples() {
				return delegate.getSamples();
			}

			@Override
			public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
				ObjectList<T> list = new ObjectArrayList<>();
				delegate.order(insertionOrder).forEach(list::add);
				ObjectLists.reverse(list);
				return list;
			}

			@Override
			public Iterable<T> order(List<T> insertionOrder) {
				ObjectList<T> list = new ObjectArrayList<>();
				for(T entry : delegate.order(insertionOrder))
				{
					list.add(entry);
				}
				ObjectLists.reverse(list);
				return list;
			}
			
			@Override
			public ObjectNavigableSet<T> create(Object... elements) {
				return delegate.create(elements).descendingSet();
			}
		}).named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}
}