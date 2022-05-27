package speiger.src.testers.shorts.builder.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.shorts.builder.ShortNavigableSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.TestShortNavigableSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2DoubleSortedMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2DoubleMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2DoubleNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2DoubleNavigableMapTestSuiteBuilder extends Short2DoubleSortedMapTestSuiteBuilder
{
	public static Short2DoubleNavigableMapTestSuiteBuilder using(TestShort2DoubleSortedMapGenerator generator) {
		return (Short2DoubleNavigableMapTestSuiteBuilder)new Short2DoubleNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Short2DoubleNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Double>, Map.Entry<Short, Double>>> parentBuilder) {
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
	Short2DoubleNavigableMapTestSuiteBuilder newBuilderUsing(TestShort2DoubleSortedMapGenerator delegate, Bound to, Bound from) {
		return Short2DoubleNavigableMapTestSuiteBuilder.using(new DerivedShort2DoubleMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Double>, Map.Entry<Short, Double>>> parentBuilder) {
		TestShort2DoubleSortedMapGenerator delegate = (TestShort2DoubleSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Short2DoubleNavigableMapTestSuiteBuilder.using(new DerivedShort2DoubleMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected ShortNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator keySetGenerator) {
		return ShortNavigableSetTestSuiteBuilder.using((TestShortNavigableSetGenerator) keySetGenerator);
	}
}