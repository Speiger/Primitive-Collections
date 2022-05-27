package speiger.src.testers.chars.builder.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.chars.builder.CharNavigableSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.TestCharNavigableSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2FloatSortedMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2FloatMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2FloatNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Char2FloatNavigableMapTestSuiteBuilder extends Char2FloatSortedMapTestSuiteBuilder
{
	public static Char2FloatNavigableMapTestSuiteBuilder using(TestChar2FloatSortedMapGenerator generator) {
		return (Char2FloatNavigableMapTestSuiteBuilder)new Char2FloatNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Char2FloatNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>>> parentBuilder) {
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
	Char2FloatNavigableMapTestSuiteBuilder newBuilderUsing(TestChar2FloatSortedMapGenerator delegate, Bound to, Bound from) {
		return Char2FloatNavigableMapTestSuiteBuilder.using(new DerivedChar2FloatMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Float>, Map.Entry<Character, Float>>> parentBuilder) {
		TestChar2FloatSortedMapGenerator delegate = (TestChar2FloatSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Char2FloatNavigableMapTestSuiteBuilder.using(new DerivedChar2FloatMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected CharNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator keySetGenerator) {
		return CharNavigableSetTestSuiteBuilder.using((TestCharNavigableSetGenerator) keySetGenerator);
	}
}