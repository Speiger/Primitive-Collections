package speiger.src.testers.ints.builder.maps;

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
import speiger.src.testers.ints.generators.maps.TestInt2ByteSortedMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2ByteMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2ByteSortedMapNavigationTester;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.builder.IntSortedSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.TestIntSortedSetGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Int2ByteSortedMapTestSuiteBuilder extends Int2ByteMapTestSuiteBuilder {
	public static Int2ByteSortedMapTestSuiteBuilder using(TestInt2ByteSortedMapGenerator generator) {
		return (Int2ByteSortedMapTestSuiteBuilder) new Int2ByteSortedMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Int2ByteSortedMapNavigationTester.class);
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
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
		if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMAP)) {
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
		}
		return derivedSuites;
	}
	
	@Override
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator keySetGenerator) {
		return keySetGenerator instanceof TestIntSortedSetGenerator ? IntSortedSetTestSuiteBuilder.using((TestIntSortedSetGenerator) keySetGenerator) : IntSetTestSuiteBuilder.using(keySetGenerator);
	}

	TestSuite createSubmapSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>>> parentBuilder, Bound from, Bound to) {
		TestInt2ByteSortedMapGenerator delegate = (TestInt2ByteSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();
		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.SUBMAP);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);

		return newBuilderUsing(delegate, to, from).named(parentBuilder.getName() + " subMap " + from + "-" + to)
				.withFeatures(features).suppressing(parentBuilder.getSuppressedTests())
				.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown()).createTestSuite();
	}

	Int2ByteSortedMapTestSuiteBuilder newBuilderUsing(TestInt2ByteSortedMapGenerator delegate, Bound to, Bound from) {
		return using(new DerivedInt2ByteMapGenerators.SortedMapGenerator(delegate, to, from));
	}

	enum NoRecurse implements Feature<Void> {
		SUBMAP, DESCENDING;

		@Override
		public Set<Feature<? super Void>> getImpliedFeatures() {
			return Collections.emptySet();
		}
	}
}