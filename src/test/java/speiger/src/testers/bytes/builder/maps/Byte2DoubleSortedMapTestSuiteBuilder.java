package speiger.src.testers.bytes.builder.maps;

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
import speiger.src.testers.bytes.generators.maps.TestByte2DoubleSortedMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2DoubleMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleSortedMapNavigationTester;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.builder.ByteSortedSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSortedSetGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Byte2DoubleSortedMapTestSuiteBuilder extends Byte2DoubleMapTestSuiteBuilder {
	public static Byte2DoubleSortedMapTestSuiteBuilder using(TestByte2DoubleSortedMapGenerator generator) {
		return (Byte2DoubleSortedMapTestSuiteBuilder) new Byte2DoubleSortedMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Byte2DoubleSortedMapNavigationTester.class);
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
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
		if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMAP)) {
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
		}
		return derivedSuites;
	}
	
	@Override
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator keySetGenerator) {
		return keySetGenerator instanceof TestByteSortedSetGenerator ? ByteSortedSetTestSuiteBuilder.using((TestByteSortedSetGenerator) keySetGenerator) : ByteSetTestSuiteBuilder.using(keySetGenerator);
	}

	TestSuite createSubmapSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>>> parentBuilder, Bound from, Bound to) {
		TestByte2DoubleSortedMapGenerator delegate = (TestByte2DoubleSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();
		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.SUBMAP);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);

		return newBuilderUsing(delegate, to, from).named(parentBuilder.getName() + " subMap " + from + "-" + to)
				.withFeatures(features).suppressing(parentBuilder.getSuppressedTests())
				.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown()).createTestSuite();
	}

	Byte2DoubleSortedMapTestSuiteBuilder newBuilderUsing(TestByte2DoubleSortedMapGenerator delegate, Bound to, Bound from) {
		return using(new DerivedByte2DoubleMapGenerators.SortedMapGenerator(delegate, to, from));
	}

	enum NoRecurse implements Feature<Void> {
		SUBMAP, DESCENDING;

		@Override
		public Set<Feature<? super Void>> getImpliedFeatures() {
			return Collections.emptySet();
		}
	}
}