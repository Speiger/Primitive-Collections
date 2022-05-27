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
import speiger.src.testers.chars.generators.maps.TestChar2LongSortedMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2LongMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2LongNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Char2LongNavigableMapTestSuiteBuilder extends Char2LongSortedMapTestSuiteBuilder
{
	public static Char2LongNavigableMapTestSuiteBuilder using(TestChar2LongSortedMapGenerator generator) {
		return (Char2LongNavigableMapTestSuiteBuilder)new Char2LongNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Char2LongNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>>> parentBuilder) {
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
	Char2LongNavigableMapTestSuiteBuilder newBuilderUsing(TestChar2LongSortedMapGenerator delegate, Bound to, Bound from) {
		return Char2LongNavigableMapTestSuiteBuilder.using(new DerivedChar2LongMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>>> parentBuilder) {
		TestChar2LongSortedMapGenerator delegate = (TestChar2LongSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Char2LongNavigableMapTestSuiteBuilder.using(new DerivedChar2LongMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected CharNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator keySetGenerator) {
		return CharNavigableSetTestSuiteBuilder.using((TestCharNavigableSetGenerator) keySetGenerator);
	}
}