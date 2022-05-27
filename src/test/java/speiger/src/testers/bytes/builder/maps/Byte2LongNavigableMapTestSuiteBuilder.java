package speiger.src.testers.bytes.builder.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.bytes.builder.ByteNavigableSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.TestByteNavigableSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2LongSortedMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2LongMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2LongNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Byte2LongNavigableMapTestSuiteBuilder extends Byte2LongSortedMapTestSuiteBuilder
{
	public static Byte2LongNavigableMapTestSuiteBuilder using(TestByte2LongSortedMapGenerator generator) {
		return (Byte2LongNavigableMapTestSuiteBuilder)new Byte2LongNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Byte2LongNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Long>, Map.Entry<Byte, Long>>> parentBuilder) {
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
	Byte2LongNavigableMapTestSuiteBuilder newBuilderUsing(TestByte2LongSortedMapGenerator delegate, Bound to, Bound from) {
		return Byte2LongNavigableMapTestSuiteBuilder.using(new DerivedByte2LongMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Long>, Map.Entry<Byte, Long>>> parentBuilder) {
		TestByte2LongSortedMapGenerator delegate = (TestByte2LongSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Byte2LongNavigableMapTestSuiteBuilder.using(new DerivedByte2LongMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected ByteNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator keySetGenerator) {
		return ByteNavigableSetTestSuiteBuilder.using((TestByteNavigableSetGenerator) keySetGenerator);
	}
}