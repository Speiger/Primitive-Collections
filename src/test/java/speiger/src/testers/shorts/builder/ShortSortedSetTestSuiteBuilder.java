package speiger.src.testers.shorts.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.testers.SortedSetNavigationTester;

import junit.framework.TestSuite;
import speiger.src.testers.shorts.generators.TestShortSortedSetGenerator;
import speiger.src.testers.shorts.impl.ShortSortedSetSubsetTestSetGenerator;
import speiger.src.testers.shorts.tests.set.ShortSortedSetNaviationTester;

@SuppressWarnings("javadoc")
public class ShortSortedSetTestSuiteBuilder extends ShortSetTestSuiteBuilder {
	public static ShortSortedSetTestSuiteBuilder using(TestShortSortedSetGenerator generator) {
		return (ShortSortedSetTestSuiteBuilder) new ShortSortedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(SortedSetNavigationTester.class);
		testers.add(ShortSortedSetNaviationTester.class);
		return testers;
	}
	
	@Override
	public TestSuite createTestSuite() {
		if (!getFeatures().contains(CollectionFeature.KNOWN_ORDER)) {
			List<Feature<?>> features = Helpers.copyToList(getFeatures());
			features.add(CollectionFeature.KNOWN_ORDER);
			withFeatures(features);
		}
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(
			FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Short>, Short>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);

		if (!parentBuilder.getFeatures().contains(CollectionFeature.SUBSET_VIEW)) {
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
		}

		return derivedSuites;
	}

	final TestSuite createSubsetSuite(
			FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Short>, Short>> parentBuilder,
			Bound from, Bound to) {
		TestShortSortedSetGenerator delegate = (TestShortSortedSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>(parentBuilder.getFeatures());
		features.remove(CollectionFeature.ALLOWS_NULL_VALUES);
		features.add(CollectionFeature.SUBSET_VIEW);

		return newBuilderUsing(delegate, to, from).named(parentBuilder.getName() + " subSet " + from + "-" + to)
				.withFeatures(features).suppressing(parentBuilder.getSuppressedTests())
				.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown()).createTestSuite();
	}

	ShortSortedSetTestSuiteBuilder newBuilderUsing(TestShortSortedSetGenerator delegate, Bound to, Bound from) {
		return using(new ShortSortedSetSubsetTestSetGenerator(delegate, to, from));
	}
}