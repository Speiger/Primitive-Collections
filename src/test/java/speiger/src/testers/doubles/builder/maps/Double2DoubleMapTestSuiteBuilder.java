package speiger.src.testers.doubles.builder.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionIteratorTester;

import junit.framework.TestSuite;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.testers.doubles.builder.DoubleCollectionTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2DoubleMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2DoubleMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapAddToTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2DoubleMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Double2DoubleMapTestSuiteBuilder extends MapTestSuiteBuilder<Double, Double> {
	public static Double2DoubleMapTestSuiteBuilder using(TestDouble2DoubleMapGenerator generator) {
		return (Double2DoubleMapTestSuiteBuilder) new Double2DoubleMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2DoubleMapClearTester.class);
		testers.add(Double2DoubleMapComputeTester.class);
		testers.add(Double2DoubleMapComputeIfAbsentTester.class);
		testers.add(Double2DoubleMapComputeIfPresentTester.class);
		testers.add(Double2DoubleMapSupplyIfAbsentTester.class);
		testers.add(Double2DoubleMapContainsKeyTester.class);
		testers.add(Double2DoubleMapContainsValueTester.class);
		testers.add(Double2DoubleMapEntrySetTester.class);
		testers.add(Double2DoubleMapEqualsTester.class);
		testers.add(Double2DoubleMapForEachTester.class);
		testers.add(Double2DoubleMapGetTester.class);
		testers.add(Double2DoubleMapGetOrDefaultTester.class);
		testers.add(Double2DoubleMapHashCodeTester.class);
		testers.add(Double2DoubleMapIsEmptyTester.class);
		testers.add(Double2DoubleMapMergeTester.class);
		testers.add(Double2DoubleMapPutTester.class);
		testers.add(Double2DoubleMapAddToTester.class);
		testers.add(Double2DoubleMapPutAllTester.class);
		testers.add(Double2DoubleMapPutAllArrayTester.class);
		testers.add(Double2DoubleMapPutIfAbsentTester.class);
		testers.add(Double2DoubleMapRemoveTester.class);
		testers.add(Double2DoubleMapRemoveEntryTester.class);
		testers.add(Double2DoubleMapRemoveOrDefaultTester.class);
		testers.add(Double2DoubleMapReplaceTester.class);
		testers.add(Double2DoubleMapReplaceAllTester.class);
		testers.add(Double2DoubleMapReplaceEntryTester.class);
		testers.add(Double2DoubleMapSizeTester.class);
		testers.add(Double2DoubleMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Double>, Map.Entry<Double, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2DoubleMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2DoubleMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedDouble2DoubleMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2DoubleMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2DoubleMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
	}
	
	protected DoubleCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestDoubleCollectionGenerator generator) {
		return DoubleCollectionTestSuiteBuilder.using(generator);
	}
	
	private static Set<Feature<?>> computeEntrySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> entrySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_ENTRY_QUERIES)) {
			entrySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		entrySetFeatures.remove(SpecialFeature.COPYING);
		return entrySetFeatures;
	}

	private static Set<Feature<?>> computeKeySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> keySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		keySetFeatures.add(CollectionFeature.SUBSET_VIEW);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEYS)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		} else if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEY_QUERIES)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		keySetFeatures.remove(SpecialFeature.COPYING);
		return keySetFeatures;
	}

	private static Set<Feature<?>> computeValuesCollectionFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> valuesCollectionFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUE_QUERIES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		}
		valuesCollectionFeatures.remove(SpecialFeature.COPYING);
		return valuesCollectionFeatures;
	}
	
	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");

		return suppressing;
	}
}