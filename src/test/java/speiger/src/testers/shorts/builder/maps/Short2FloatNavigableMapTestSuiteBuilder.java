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
import speiger.src.testers.shorts.generators.maps.TestShort2FloatSortedMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2FloatMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2FloatNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Short2FloatNavigableMapTestSuiteBuilder extends Short2FloatSortedMapTestSuiteBuilder
{
	public static Short2FloatNavigableMapTestSuiteBuilder using(TestShort2FloatSortedMapGenerator generator) {
		return (Short2FloatNavigableMapTestSuiteBuilder)new Short2FloatNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Short2FloatNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Float>, Map.Entry<Short, Float>>> parentBuilder) {
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
	Short2FloatNavigableMapTestSuiteBuilder newBuilderUsing(TestShort2FloatSortedMapGenerator delegate, Bound to, Bound from) {
		return Short2FloatNavigableMapTestSuiteBuilder.using(new DerivedShort2FloatMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Float>, Map.Entry<Short, Float>>> parentBuilder) {
		TestShort2FloatSortedMapGenerator delegate = (TestShort2FloatSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Short2FloatNavigableMapTestSuiteBuilder.using(new DerivedShort2FloatMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected ShortNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator keySetGenerator) {
		return ShortNavigableSetTestSuiteBuilder.using((TestShortNavigableSetGenerator) keySetGenerator);
	}
}