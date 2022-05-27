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
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.testers.objects.builder.ObjectCollectionTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ObjectMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2ObjectMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ObjectMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Double2ObjectMapTestSuiteBuilder<V> extends MapTestSuiteBuilder<Double, V> {
	public static <V> Double2ObjectMapTestSuiteBuilder<V> using(TestDouble2ObjectMapGenerator<V> generator) {
		return (Double2ObjectMapTestSuiteBuilder<V>) new Double2ObjectMapTestSuiteBuilder<V>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2ObjectMapClearTester.class);
		testers.add(Double2ObjectMapComputeTester.class);
		testers.add(Double2ObjectMapComputeIfAbsentTester.class);
		testers.add(Double2ObjectMapComputeIfPresentTester.class);
		testers.add(Double2ObjectMapSupplyIfAbsentTester.class);
		testers.add(Double2ObjectMapContainsKeyTester.class);
		testers.add(Double2ObjectMapContainsValueTester.class);
		testers.add(Double2ObjectMapEntrySetTester.class);
		testers.add(Double2ObjectMapEqualsTester.class);
		testers.add(Double2ObjectMapForEachTester.class);
		testers.add(Double2ObjectMapGetTester.class);
		testers.add(Double2ObjectMapGetOrDefaultTester.class);
		testers.add(Double2ObjectMapHashCodeTester.class);
		testers.add(Double2ObjectMapIsEmptyTester.class);
		testers.add(Double2ObjectMapMergeTester.class);
		testers.add(Double2ObjectMapPutTester.class);
		testers.add(Double2ObjectMapPutAllTester.class);
		testers.add(Double2ObjectMapPutAllArrayTester.class);
		testers.add(Double2ObjectMapPutIfAbsentTester.class);
		testers.add(Double2ObjectMapRemoveTester.class);
		testers.add(Double2ObjectMapRemoveEntryTester.class);
		testers.add(Double2ObjectMapRemoveOrDefaultTester.class);
		testers.add(Double2ObjectMapReplaceTester.class);
		testers.add(Double2ObjectMapReplaceAllTester.class);
		testers.add(Double2ObjectMapReplaceEntryTester.class);
		testers.add(Double2ObjectMapSizeTester.class);
		testers.add(Double2ObjectMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, V>, Map.Entry<Double, V>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2ObjectMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2ObjectMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedDouble2ObjectMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2ObjectMap.Entry<V>> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2ObjectMap.Entry<V>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
	}
	
	protected ObjectCollectionTestSuiteBuilder<V> createDerivedValueCollectionSuite(TestObjectCollectionGenerator<V> generator) {
		return ObjectCollectionTestSuiteBuilder.using(generator);
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