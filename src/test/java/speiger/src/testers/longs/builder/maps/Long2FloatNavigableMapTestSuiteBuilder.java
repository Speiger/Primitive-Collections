package speiger.src.testers.longs.builder.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.longs.builder.LongNavigableSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongNavigableSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2FloatSortedMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2FloatMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2FloatNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Long2FloatNavigableMapTestSuiteBuilder extends Long2FloatSortedMapTestSuiteBuilder
{
	public static Long2FloatNavigableMapTestSuiteBuilder using(TestLong2FloatSortedMapGenerator generator) {
		return (Long2FloatNavigableMapTestSuiteBuilder)new Long2FloatNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Long2FloatNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Float>, Map.Entry<Long, Float>>> parentBuilder) {
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
	Long2FloatNavigableMapTestSuiteBuilder newBuilderUsing(TestLong2FloatSortedMapGenerator delegate, Bound to, Bound from) {
		return Long2FloatNavigableMapTestSuiteBuilder.using(new DerivedLong2FloatMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Float>, Map.Entry<Long, Float>>> parentBuilder) {
		TestLong2FloatSortedMapGenerator delegate = (TestLong2FloatSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Long2FloatNavigableMapTestSuiteBuilder.using(new DerivedLong2FloatMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected LongNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator keySetGenerator) {
		return LongNavigableSetTestSuiteBuilder.using((TestLongNavigableSetGenerator) keySetGenerator);
	}
}