package speiger.src.testers.objects.builder.maps;

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
import speiger.src.testers.objects.generators.maps.TestObject2DoubleSortedMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2DoubleMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2DoubleSortedMapNavigationTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.builder.ObjectSortedSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Object2DoubleSortedMapTestSuiteBuilder<T> extends Object2DoubleMapTestSuiteBuilder<T> {
	public static <T> Object2DoubleSortedMapTestSuiteBuilder<T> using(TestObject2DoubleSortedMapGenerator<T> generator) {
		return (Object2DoubleSortedMapTestSuiteBuilder<T>) new Object2DoubleSortedMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Object2DoubleSortedMapNavigationTester.class);
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
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Double>, Map.Entry<T, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
		if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMAP)) {
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
		}
		return derivedSuites;
	}
	
	@Override
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> keySetGenerator) {
		return keySetGenerator instanceof TestObjectSortedSetGenerator ? ObjectSortedSetTestSuiteBuilder.using((TestObjectSortedSetGenerator<T>) keySetGenerator) : ObjectSetTestSuiteBuilder.using(keySetGenerator);
	}

	TestSuite createSubmapSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Double>, Map.Entry<T, Double>>> parentBuilder, Bound from, Bound to) {
		TestObject2DoubleSortedMapGenerator<T> delegate = (TestObject2DoubleSortedMapGenerator<T>) parentBuilder.getSubjectGenerator().getInnerGenerator();
		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.SUBMAP);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);

		return newBuilderUsing(delegate, to, from).named(parentBuilder.getName() + " subMap " + from + "-" + to)
				.withFeatures(features).suppressing(parentBuilder.getSuppressedTests())
				.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown()).createTestSuite();
	}

	Object2DoubleSortedMapTestSuiteBuilder<T> newBuilderUsing(TestObject2DoubleSortedMapGenerator<T> delegate, Bound to, Bound from) {
		return using(new DerivedObject2DoubleMapGenerators.SortedMapGenerator<>(delegate, to, from));
	}

	enum NoRecurse implements Feature<Void> {
		SUBMAP, DESCENDING;

		@Override
		public Set<Feature<? super Void>> getImpliedFeatures() {
			return Collections.emptySet();
		}
	}
}