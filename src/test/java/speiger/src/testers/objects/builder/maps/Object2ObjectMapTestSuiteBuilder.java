package speiger.src.testers.objects.builder.maps;

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
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.testers.objects.builder.ObjectCollectionTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2ObjectMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2ObjectMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2ObjectMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Object2ObjectMapTestSuiteBuilder<T, V> extends MapTestSuiteBuilder<T, V> {
	public static <T, V> Object2ObjectMapTestSuiteBuilder<T, V> using(TestObject2ObjectMapGenerator<T, V> generator) {
		return (Object2ObjectMapTestSuiteBuilder<T, V>) new Object2ObjectMapTestSuiteBuilder<T, V>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2ObjectMapClearTester.class);
		testers.add(Object2ObjectMapComputeTester.class);
		testers.add(Object2ObjectMapComputeIfAbsentTester.class);
		testers.add(Object2ObjectMapComputeIfPresentTester.class);
		testers.add(Object2ObjectMapSupplyIfAbsentTester.class);
		testers.add(Object2ObjectMapContainsKeyTester.class);
		testers.add(Object2ObjectMapContainsValueTester.class);
		testers.add(Object2ObjectMapEntrySetTester.class);
		testers.add(Object2ObjectMapEqualsTester.class);
		testers.add(Object2ObjectMapForEachTester.class);
		testers.add(Object2ObjectMapGetTester.class);
		testers.add(Object2ObjectMapGetOrDefaultTester.class);
		testers.add(Object2ObjectMapHashCodeTester.class);
		testers.add(Object2ObjectMapIsEmptyTester.class);
		testers.add(Object2ObjectMapMergeTester.class);
		testers.add(Object2ObjectMapPutTester.class);
		testers.add(Object2ObjectMapPutAllTester.class);
		testers.add(Object2ObjectMapPutAllArrayTester.class);
		testers.add(Object2ObjectMapPutIfAbsentTester.class);
		testers.add(Object2ObjectMapRemoveTester.class);
		testers.add(Object2ObjectMapRemoveEntryTester.class);
		testers.add(Object2ObjectMapRemoveOrDefaultTester.class);
		testers.add(Object2ObjectMapReplaceTester.class);
		testers.add(Object2ObjectMapReplaceAllTester.class);
		testers.add(Object2ObjectMapReplaceEntryTester.class);
		testers.add(Object2ObjectMapSizeTester.class);
		testers.add(Object2ObjectMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, V>, Map.Entry<T, V>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2ObjectMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2ObjectMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedObject2ObjectMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2ObjectMap.Entry<T, V>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2ObjectMap.Entry<T, V>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
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