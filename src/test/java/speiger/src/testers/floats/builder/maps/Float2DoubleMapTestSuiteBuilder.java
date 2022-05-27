package speiger.src.testers.floats.builder.maps;

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
import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.testers.doubles.builder.DoubleCollectionTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2DoubleMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2DoubleMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapAddToTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2DoubleMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Float2DoubleMapTestSuiteBuilder extends MapTestSuiteBuilder<Float, Double> {
	public static Float2DoubleMapTestSuiteBuilder using(TestFloat2DoubleMapGenerator generator) {
		return (Float2DoubleMapTestSuiteBuilder) new Float2DoubleMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2DoubleMapClearTester.class);
		testers.add(Float2DoubleMapComputeTester.class);
		testers.add(Float2DoubleMapComputeIfAbsentTester.class);
		testers.add(Float2DoubleMapComputeIfPresentTester.class);
		testers.add(Float2DoubleMapSupplyIfAbsentTester.class);
		testers.add(Float2DoubleMapContainsKeyTester.class);
		testers.add(Float2DoubleMapContainsValueTester.class);
		testers.add(Float2DoubleMapEntrySetTester.class);
		testers.add(Float2DoubleMapEqualsTester.class);
		testers.add(Float2DoubleMapForEachTester.class);
		testers.add(Float2DoubleMapGetTester.class);
		testers.add(Float2DoubleMapGetOrDefaultTester.class);
		testers.add(Float2DoubleMapHashCodeTester.class);
		testers.add(Float2DoubleMapIsEmptyTester.class);
		testers.add(Float2DoubleMapMergeTester.class);
		testers.add(Float2DoubleMapPutTester.class);
		testers.add(Float2DoubleMapAddToTester.class);
		testers.add(Float2DoubleMapPutAllTester.class);
		testers.add(Float2DoubleMapPutAllArrayTester.class);
		testers.add(Float2DoubleMapPutIfAbsentTester.class);
		testers.add(Float2DoubleMapRemoveTester.class);
		testers.add(Float2DoubleMapRemoveEntryTester.class);
		testers.add(Float2DoubleMapRemoveOrDefaultTester.class);
		testers.add(Float2DoubleMapReplaceTester.class);
		testers.add(Float2DoubleMapReplaceAllTester.class);
		testers.add(Float2DoubleMapReplaceEntryTester.class);
		testers.add(Float2DoubleMapSizeTester.class);
		testers.add(Float2DoubleMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Double>, Map.Entry<Float, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2DoubleMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2DoubleMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedFloat2DoubleMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2DoubleMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2DoubleMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
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