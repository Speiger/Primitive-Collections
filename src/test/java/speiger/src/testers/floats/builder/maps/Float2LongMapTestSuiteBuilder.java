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
import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.testers.longs.builder.LongCollectionTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2LongMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2LongMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2LongMapAddToTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2LongMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Float2LongMapTestSuiteBuilder extends MapTestSuiteBuilder<Float, Long> {
	public static Float2LongMapTestSuiteBuilder using(TestFloat2LongMapGenerator generator) {
		return (Float2LongMapTestSuiteBuilder) new Float2LongMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2LongMapClearTester.class);
		testers.add(Float2LongMapComputeTester.class);
		testers.add(Float2LongMapComputeIfAbsentTester.class);
		testers.add(Float2LongMapComputeIfPresentTester.class);
		testers.add(Float2LongMapSupplyIfAbsentTester.class);
		testers.add(Float2LongMapContainsKeyTester.class);
		testers.add(Float2LongMapContainsValueTester.class);
		testers.add(Float2LongMapEntrySetTester.class);
		testers.add(Float2LongMapEqualsTester.class);
		testers.add(Float2LongMapForEachTester.class);
		testers.add(Float2LongMapGetTester.class);
		testers.add(Float2LongMapGetOrDefaultTester.class);
		testers.add(Float2LongMapHashCodeTester.class);
		testers.add(Float2LongMapIsEmptyTester.class);
		testers.add(Float2LongMapMergeTester.class);
		testers.add(Float2LongMapPutTester.class);
		testers.add(Float2LongMapAddToTester.class);
		testers.add(Float2LongMapPutAllTester.class);
		testers.add(Float2LongMapPutAllArrayTester.class);
		testers.add(Float2LongMapPutIfAbsentTester.class);
		testers.add(Float2LongMapRemoveTester.class);
		testers.add(Float2LongMapRemoveEntryTester.class);
		testers.add(Float2LongMapRemoveOrDefaultTester.class);
		testers.add(Float2LongMapReplaceTester.class);
		testers.add(Float2LongMapReplaceAllTester.class);
		testers.add(Float2LongMapReplaceEntryTester.class);
		testers.add(Float2LongMapSizeTester.class);
		testers.add(Float2LongMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Long>, Map.Entry<Float, Long>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2LongMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2LongMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedFloat2LongMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2LongMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2LongMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
	}
	
	protected LongCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestLongCollectionGenerator generator) {
		return LongCollectionTestSuiteBuilder.using(generator);
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