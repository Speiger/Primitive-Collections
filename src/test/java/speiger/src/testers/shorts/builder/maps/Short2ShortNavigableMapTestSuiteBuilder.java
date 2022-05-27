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
import speiger.src.testers.shorts.generators.maps.TestShort2ShortSortedMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2ShortMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2ShortNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

public class Short2ShortNavigableMapTestSuiteBuilder extends Short2ShortSortedMapTestSuiteBuilder
{
	public static Short2ShortNavigableMapTestSuiteBuilder using(TestShort2ShortSortedMapGenerator generator) {
		return (Short2ShortNavigableMapTestSuiteBuilder)new Short2ShortNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Short2ShortNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Short>, Map.Entry<Short, Short>>> parentBuilder) {
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
	Short2ShortNavigableMapTestSuiteBuilder newBuilderUsing(TestShort2ShortSortedMapGenerator delegate, Bound to, Bound from) {
		return Short2ShortNavigableMapTestSuiteBuilder.using(new DerivedShort2ShortMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Short>, Map.Entry<Short, Short>>> parentBuilder) {
		TestShort2ShortSortedMapGenerator delegate = (TestShort2ShortSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Short2ShortNavigableMapTestSuiteBuilder.using(new DerivedShort2ShortMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected ShortNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator keySetGenerator) {
		return ShortNavigableSetTestSuiteBuilder.using((TestShortNavigableSetGenerator) keySetGenerator);
	}
}