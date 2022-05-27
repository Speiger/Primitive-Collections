package speiger.src.testers.floats.builder;

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
import speiger.src.testers.floats.generators.TestFloatSortedSetGenerator;
import speiger.src.testers.floats.impl.FloatSortedSetSubsetTestSetGenerator;
import speiger.src.testers.floats.tests.set.FloatSortedSetNaviationTester;

@SuppressWarnings("javadoc")
public class FloatSortedSetTestSuiteBuilder extends FloatSetTestSuiteBuilder {
	public static FloatSortedSetTestSuiteBuilder using(TestFloatSortedSetGenerator generator) {
		return (FloatSortedSetTestSuiteBuilder) new FloatSortedSetTestSuiteBuilder().usingGenerator(generator);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(SortedSetNavigationTester.class);
		testers.add(FloatSortedSetNaviationTester.class);
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
			FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Float>, Float>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);

		if (!parentBuilder.getFeatures().contains(CollectionFeature.SUBSET_VIEW)) {
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubsetSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
		}

		return derivedSuites;
	}

	final TestSuite createSubsetSuite(
			FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<Float>, Float>> parentBuilder,
			Bound from, Bound to) {
		TestFloatSortedSetGenerator delegate = (TestFloatSortedSetGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>(parentBuilder.getFeatures());
		features.remove(CollectionFeature.ALLOWS_NULL_VALUES);
		features.add(CollectionFeature.SUBSET_VIEW);

		return newBuilderUsing(delegate, to, from).named(parentBuilder.getName() + " subSet " + from + "-" + to)
				.withFeatures(features).suppressing(parentBuilder.getSuppressedTests())
				.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown()).createTestSuite();
	}

	FloatSortedSetTestSuiteBuilder newBuilderUsing(TestFloatSortedSetGenerator delegate, Bound to, Bound from) {
		return using(new FloatSortedSetSubsetTestSetGenerator(delegate, to, from));
	}
}