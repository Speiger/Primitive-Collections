package speiger.src.testers.doubles.builder.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.doubles.builder.DoubleNavigableSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.TestDoubleNavigableSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2IntSortedMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2IntMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2IntNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Double2IntNavigableMapTestSuiteBuilder extends Double2IntSortedMapTestSuiteBuilder
{
	public static Double2IntNavigableMapTestSuiteBuilder using(TestDouble2IntSortedMapGenerator generator) {
		return (Double2IntNavigableMapTestSuiteBuilder)new Double2IntNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Double2IntNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>>> parentBuilder) {
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
	Double2IntNavigableMapTestSuiteBuilder newBuilderUsing(TestDouble2IntSortedMapGenerator delegate, Bound to, Bound from) {
		return Double2IntNavigableMapTestSuiteBuilder.using(new DerivedDouble2IntMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>>> parentBuilder) {
		TestDouble2IntSortedMapGenerator delegate = (TestDouble2IntSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Double2IntNavigableMapTestSuiteBuilder.using(new DerivedDouble2IntMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected DoubleNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator keySetGenerator) {
		return DoubleNavigableSetTestSuiteBuilder.using((TestDoubleNavigableSetGenerator) keySetGenerator);
	}
}