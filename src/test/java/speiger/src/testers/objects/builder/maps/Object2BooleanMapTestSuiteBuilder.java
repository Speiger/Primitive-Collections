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
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.testers.booleans.builder.BooleanCollectionTestSuiteBuilder;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2BooleanMapGenerator;
import speiger.src.testers.objects.impl.maps.DerivedObject2BooleanMapGenerators;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapClearTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapComputeIfPresentTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapComputeTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapContainsKeyTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapContainsValueTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapEntrySetTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapEqualsTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapForEachTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapGetOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapGetTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapHashCodeTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapIsEmptyTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapMergeTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapPutAllArrayTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapPutAllTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapPutIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapPutTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapRemoveEntryTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapRemoveOrDefaultTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapRemoveTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapReplaceAllTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapReplaceEntryTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapReplaceTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapSizeTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.objects.tests.maps.Object2BooleanMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Object2BooleanMapTestSuiteBuilder<T> extends MapTestSuiteBuilder<T, Boolean> {
	public static <T> Object2BooleanMapTestSuiteBuilder<T> using(TestObject2BooleanMapGenerator<T> generator) {
		return (Object2BooleanMapTestSuiteBuilder<T>) new Object2BooleanMapTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Object2BooleanMapClearTester.class);
		testers.add(Object2BooleanMapComputeTester.class);
		testers.add(Object2BooleanMapComputeIfAbsentTester.class);
		testers.add(Object2BooleanMapComputeIfPresentTester.class);
		testers.add(Object2BooleanMapSupplyIfAbsentTester.class);
		testers.add(Object2BooleanMapContainsKeyTester.class);
		testers.add(Object2BooleanMapContainsValueTester.class);
		testers.add(Object2BooleanMapEntrySetTester.class);
		testers.add(Object2BooleanMapEqualsTester.class);
		testers.add(Object2BooleanMapForEachTester.class);
		testers.add(Object2BooleanMapGetTester.class);
		testers.add(Object2BooleanMapGetOrDefaultTester.class);
		testers.add(Object2BooleanMapHashCodeTester.class);
		testers.add(Object2BooleanMapIsEmptyTester.class);
		testers.add(Object2BooleanMapMergeTester.class);
		testers.add(Object2BooleanMapPutTester.class);
		testers.add(Object2BooleanMapPutAllTester.class);
		testers.add(Object2BooleanMapPutAllArrayTester.class);
		testers.add(Object2BooleanMapPutIfAbsentTester.class);
		testers.add(Object2BooleanMapRemoveTester.class);
		testers.add(Object2BooleanMapRemoveEntryTester.class);
		testers.add(Object2BooleanMapRemoveOrDefaultTester.class);
		testers.add(Object2BooleanMapReplaceTester.class);
		testers.add(Object2BooleanMapReplaceAllTester.class);
		testers.add(Object2BooleanMapReplaceEntryTester.class);
		testers.add(Object2BooleanMapSizeTester.class);
		testers.add(Object2BooleanMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<T, Boolean>, Map.Entry<T, Boolean>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedObject2BooleanMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedObject2BooleanMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Object2BooleanMap.Entry<T>> createDerivedEntrySetSuite(TestObjectSetGenerator<Object2BooleanMap.Entry<T>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ObjectSetTestSuiteBuilder<T> createDerivedKeySetSuite(TestObjectSetGenerator<T> generator) {
		return ObjectSetTestSuiteBuilder.using(generator);
	}
	
	protected BooleanCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestBooleanCollectionGenerator generator) {
		return BooleanCollectionTestSuiteBuilder.using(generator);
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

	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}