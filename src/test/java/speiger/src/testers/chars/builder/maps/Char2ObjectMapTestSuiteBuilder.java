package speiger.src.testers.chars.builder.maps;

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
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.testers.objects.builder.ObjectCollectionTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2ObjectMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2ObjectMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ObjectMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Char2ObjectMapTestSuiteBuilder<V> extends MapTestSuiteBuilder<Character, V> {
	public static <V> Char2ObjectMapTestSuiteBuilder<V> using(TestChar2ObjectMapGenerator<V> generator) {
		return (Char2ObjectMapTestSuiteBuilder<V>) new Char2ObjectMapTestSuiteBuilder<V>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2ObjectMapClearTester.class);
		testers.add(Char2ObjectMapComputeTester.class);
		testers.add(Char2ObjectMapComputeIfAbsentTester.class);
		testers.add(Char2ObjectMapComputeIfPresentTester.class);
		testers.add(Char2ObjectMapSupplyIfAbsentTester.class);
		testers.add(Char2ObjectMapContainsKeyTester.class);
		testers.add(Char2ObjectMapContainsValueTester.class);
		testers.add(Char2ObjectMapEntrySetTester.class);
		testers.add(Char2ObjectMapEqualsTester.class);
		testers.add(Char2ObjectMapForEachTester.class);
		testers.add(Char2ObjectMapGetTester.class);
		testers.add(Char2ObjectMapGetOrDefaultTester.class);
		testers.add(Char2ObjectMapHashCodeTester.class);
		testers.add(Char2ObjectMapIsEmptyTester.class);
		testers.add(Char2ObjectMapMergeTester.class);
		testers.add(Char2ObjectMapPutTester.class);
		testers.add(Char2ObjectMapPutAllTester.class);
		testers.add(Char2ObjectMapPutAllArrayTester.class);
		testers.add(Char2ObjectMapPutIfAbsentTester.class);
		testers.add(Char2ObjectMapRemoveTester.class);
		testers.add(Char2ObjectMapRemoveEntryTester.class);
		testers.add(Char2ObjectMapRemoveOrDefaultTester.class);
		testers.add(Char2ObjectMapReplaceTester.class);
		testers.add(Char2ObjectMapReplaceAllTester.class);
		testers.add(Char2ObjectMapReplaceEntryTester.class);
		testers.add(Char2ObjectMapSizeTester.class);
		testers.add(Char2ObjectMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, V>, Map.Entry<Character, V>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2ObjectMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2ObjectMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedChar2ObjectMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2ObjectMap.Entry<V>> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2ObjectMap.Entry<V>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
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