package speiger.src.testers.objects.builder.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.objects.builder.ObjectNavigableSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ByteSortedMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2ByteMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2ByteNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Object2ByteNavigableMapTestSuiteBuilder<T> extends Object2ByteSortedMapTestSuiteBuilder<T>
{
	public static <T> Object2ByteNavigableMapTestSuiteBuilder<T> using(TestObject2ByteSortedMapGenerator<T> generator) {
		return (Object2ByteNavigableMapTestSuiteBuilder<T>)new Object2ByteNavigableMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Object2ByteNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Byte>, Map.Entry<T, Byte>>> parentBuilder) {
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
	Object2ByteNavigableMapTestSuiteBuilder<T> newBuilderUsing(TestObject2ByteSortedMapGenerator<T> delegate, Bound to, Bound from) {
		return Object2ByteNavigableMapTestSuiteBuilder.using(new DerivedObject2ByteMapGenerators.NavigableMapGenerator<>(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Byte>, Map.Entry<T, Byte>>> parentBuilder) {
		TestObject2ByteSortedMapGenerator<T> delegate = (TestObject2ByteSortedMapGenerator<T>) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Object2ByteNavigableMapTestSuiteBuilder.using(new DerivedObject2ByteMapGenerators.DescendingTestMapGenerator<>(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected ObjectNavigableSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> keySetGenerator) {
		return ObjectNavigableSetTestSuiteBuilder.using((TestObjectNavigableSetGenerator<T>) keySetGenerator);
	}
}