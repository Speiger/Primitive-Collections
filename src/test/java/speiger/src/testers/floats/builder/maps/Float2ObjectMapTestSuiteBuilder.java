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
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.testers.objects.builder.ObjectCollectionTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2ObjectMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2ObjectMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ObjectMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Float2ObjectMapTestSuiteBuilder<V> extends MapTestSuiteBuilder<Float, V> {
	public static <V> Float2ObjectMapTestSuiteBuilder<V> using(TestFloat2ObjectMapGenerator<V> generator) {
		return (Float2ObjectMapTestSuiteBuilder<V>) new Float2ObjectMapTestSuiteBuilder<V>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2ObjectMapClearTester.class);
		testers.add(Float2ObjectMapComputeTester.class);
		testers.add(Float2ObjectMapComputeIfAbsentTester.class);
		testers.add(Float2ObjectMapComputeIfPresentTester.class);
		testers.add(Float2ObjectMapSupplyIfAbsentTester.class);
		testers.add(Float2ObjectMapContainsKeyTester.class);
		testers.add(Float2ObjectMapContainsValueTester.class);
		testers.add(Float2ObjectMapEntrySetTester.class);
		testers.add(Float2ObjectMapEqualsTester.class);
		testers.add(Float2ObjectMapForEachTester.class);
		testers.add(Float2ObjectMapGetTester.class);
		testers.add(Float2ObjectMapGetOrDefaultTester.class);
		testers.add(Float2ObjectMapHashCodeTester.class);
		testers.add(Float2ObjectMapIsEmptyTester.class);
		testers.add(Float2ObjectMapMergeTester.class);
		testers.add(Float2ObjectMapPutTester.class);
		testers.add(Float2ObjectMapPutAllTester.class);
		testers.add(Float2ObjectMapPutAllArrayTester.class);
		testers.add(Float2ObjectMapPutIfAbsentTester.class);
		testers.add(Float2ObjectMapRemoveTester.class);
		testers.add(Float2ObjectMapRemoveEntryTester.class);
		testers.add(Float2ObjectMapRemoveOrDefaultTester.class);
		testers.add(Float2ObjectMapReplaceTester.class);
		testers.add(Float2ObjectMapReplaceAllTester.class);
		testers.add(Float2ObjectMapReplaceEntryTester.class);
		testers.add(Float2ObjectMapSizeTester.class);
		testers.add(Float2ObjectMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, V>, Map.Entry<Float, V>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2ObjectMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2ObjectMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedFloat2ObjectMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2ObjectMap.Entry<V>> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2ObjectMap.Entry<V>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
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
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		return suppressing;
	}
}