package speiger.src.testers.floats.builder.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.floats.builder.FloatNavigableSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.TestFloatNavigableSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2IntSortedMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2IntMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2IntNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Float2IntNavigableMapTestSuiteBuilder extends Float2IntSortedMapTestSuiteBuilder
{
	public static Float2IntNavigableMapTestSuiteBuilder using(TestFloat2IntSortedMapGenerator generator) {
		return (Float2IntNavigableMapTestSuiteBuilder)new Float2IntNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Float2IntNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Integer>, Map.Entry<Float, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
		if (!parentBuilder.getFeatures().contains(NoRecurse.DESCENDING)) {
			derivedSuites.add(createDescendingSuite(parentBuilder));
		}
		if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMAP)) {
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.NO_BOUND, Bound.INCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.EXCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.EXCLUSIVE, Bound.EXCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.EXCLUSIVE, Bound.INCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.INCLUSIVE));
		}
		return derivedSuites;
	}

	@Override
	Float2IntNavigableMapTestSuiteBuilder newBuilderUsing(TestFloat2IntSortedMapGenerator delegate, Bound to, Bound from) {
		return Float2IntNavigableMapTestSuiteBuilder.using(new DerivedFloat2IntMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Integer>, Map.Entry<Float, Integer>>> parentBuilder) {
		TestFloat2IntSortedMapGenerator delegate = (TestFloat2IntSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Float2IntNavigableMapTestSuiteBuilder.using(new DerivedFloat2IntMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected FloatNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator keySetGenerator) {
		return FloatNavigableSetTestSuiteBuilder.using((TestFloatNavigableSetGenerator) keySetGenerator);
	}
}