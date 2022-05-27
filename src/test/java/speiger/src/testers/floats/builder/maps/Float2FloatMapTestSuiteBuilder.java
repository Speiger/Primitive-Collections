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
import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.testers.floats.builder.FloatCollectionTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2FloatMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2FloatMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2FloatMapAddToTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2FloatMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Float2FloatMapTestSuiteBuilder extends MapTestSuiteBuilder<Float, Float> {
	public static Float2FloatMapTestSuiteBuilder using(TestFloat2FloatMapGenerator generator) {
		return (Float2FloatMapTestSuiteBuilder) new Float2FloatMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2FloatMapClearTester.class);
		testers.add(Float2FloatMapComputeTester.class);
		testers.add(Float2FloatMapComputeIfAbsentTester.class);
		testers.add(Float2FloatMapComputeIfPresentTester.class);
		testers.add(Float2FloatMapSupplyIfAbsentTester.class);
		testers.add(Float2FloatMapContainsKeyTester.class);
		testers.add(Float2FloatMapContainsValueTester.class);
		testers.add(Float2FloatMapEntrySetTester.class);
		testers.add(Float2FloatMapEqualsTester.class);
		testers.add(Float2FloatMapForEachTester.class);
		testers.add(Float2FloatMapGetTester.class);
		testers.add(Float2FloatMapGetOrDefaultTester.class);
		testers.add(Float2FloatMapHashCodeTester.class);
		testers.add(Float2FloatMapIsEmptyTester.class);
		testers.add(Float2FloatMapMergeTester.class);
		testers.add(Float2FloatMapPutTester.class);
		testers.add(Float2FloatMapAddToTester.class);
		testers.add(Float2FloatMapPutAllTester.class);
		testers.add(Float2FloatMapPutAllArrayTester.class);
		testers.add(Float2FloatMapPutIfAbsentTester.class);
		testers.add(Float2FloatMapRemoveTester.class);
		testers.add(Float2FloatMapRemoveEntryTester.class);
		testers.add(Float2FloatMapRemoveOrDefaultTester.class);
		testers.add(Float2FloatMapReplaceTester.class);
		testers.add(Float2FloatMapReplaceAllTester.class);
		testers.add(Float2FloatMapReplaceEntryTester.class);
		testers.add(Float2FloatMapSizeTester.class);
		testers.add(Float2FloatMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Float>, Map.Entry<Float, Float>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2FloatMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2FloatMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedFloat2FloatMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2FloatMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2FloatMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
	}
	
	protected FloatCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestFloatCollectionGenerator generator) {
		return FloatCollectionTestSuiteBuilder.using(generator);
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