package speiger.src.testers.floats.builder.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.floats.generators.maps.TestFloat2CharSortedMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2CharMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2CharSortedMapNavigationTester;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.builder.FloatSortedSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.TestFloatSortedSetGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Float2CharSortedMapTestSuiteBuilder extends Float2CharMapTestSuiteBuilder {
	public static Float2CharSortedMapTestSuiteBuilder using(TestFloat2CharSortedMapGenerator generator) {
		return (Float2CharSortedMapTestSuiteBuilder) new Float2CharSortedMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Float2CharSortedMapNavigationTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		if (!getFeatures().contains(KNOWN_ORDER)) {
			List<Feature<?>> features = Helpers.copyToList(getFeatures());
			features.add(KNOWN_ORDER);
			withFeatures(features);
		}
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Character>, Map.Entry<Float, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
		if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMAP)) {
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
		}
		return derivedSuites;
	}
	
	@Override
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator keySetGenerator) {
		return keySetGenerator instanceof TestFloatSortedSetGenerator ? FloatSortedSetTestSuiteBuilder.using((TestFloatSortedSetGenerator) keySetGenerator) : FloatSetTestSuiteBuilder.using(keySetGenerator);
	}

	TestSuite createSubmapSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Character>, Map.Entry<Float, Character>>> parentBuilder, Bound from, Bound to) {
		TestFloat2CharSortedMapGenerator delegate = (TestFloat2CharSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();
		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.SUBMAP);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);

		return newBuilderUsing(delegate, to, from).named(parentBuilder.getName() + " subMap " + from + "-" + to)
				.withFeatures(features).suppressing(parentBuilder.getSuppressedTests())
				.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown()).createTestSuite();
	}

	Float2CharSortedMapTestSuiteBuilder newBuilderUsing(TestFloat2CharSortedMapGenerator delegate, Bound to, Bound from) {
		return using(new DerivedFloat2CharMapGenerators.SortedMapGenerator(delegate, to, from));
	}

	enum NoRecurse implements Feature<Void> {
		SUBMAP, DESCENDING;

		@Override
		public Set<Feature<? super Void>> getImpliedFeatures() {
			return Collections.emptySet();
		}
	}
}