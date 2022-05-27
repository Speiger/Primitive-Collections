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
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.testers.shorts.builder.ShortCollectionTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2ShortMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2ShortMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2ShortMapAddToTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ShortMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Float2ShortMapTestSuiteBuilder extends MapTestSuiteBuilder<Float, Short> {
	public static Float2ShortMapTestSuiteBuilder using(TestFloat2ShortMapGenerator generator) {
		return (Float2ShortMapTestSuiteBuilder) new Float2ShortMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2ShortMapClearTester.class);
		testers.add(Float2ShortMapComputeTester.class);
		testers.add(Float2ShortMapComputeIfAbsentTester.class);
		testers.add(Float2ShortMapComputeIfPresentTester.class);
		testers.add(Float2ShortMapSupplyIfAbsentTester.class);
		testers.add(Float2ShortMapContainsKeyTester.class);
		testers.add(Float2ShortMapContainsValueTester.class);
		testers.add(Float2ShortMapEntrySetTester.class);
		testers.add(Float2ShortMapEqualsTester.class);
		testers.add(Float2ShortMapForEachTester.class);
		testers.add(Float2ShortMapGetTester.class);
		testers.add(Float2ShortMapGetOrDefaultTester.class);
		testers.add(Float2ShortMapHashCodeTester.class);
		testers.add(Float2ShortMapIsEmptyTester.class);
		testers.add(Float2ShortMapMergeTester.class);
		testers.add(Float2ShortMapPutTester.class);
		testers.add(Float2ShortMapAddToTester.class);
		testers.add(Float2ShortMapPutAllTester.class);
		testers.add(Float2ShortMapPutAllArrayTester.class);
		testers.add(Float2ShortMapPutIfAbsentTester.class);
		testers.add(Float2ShortMapRemoveTester.class);
		testers.add(Float2ShortMapRemoveEntryTester.class);
		testers.add(Float2ShortMapRemoveOrDefaultTester.class);
		testers.add(Float2ShortMapReplaceTester.class);
		testers.add(Float2ShortMapReplaceAllTester.class);
		testers.add(Float2ShortMapReplaceEntryTester.class);
		testers.add(Float2ShortMapSizeTester.class);
		testers.add(Float2ShortMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Short>, Map.Entry<Float, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2ShortMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2ShortMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedFloat2ShortMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2ShortMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2ShortMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
	}
	
	protected ShortCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestShortCollectionGenerator generator) {
		return ShortCollectionTestSuiteBuilder.using(generator);
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