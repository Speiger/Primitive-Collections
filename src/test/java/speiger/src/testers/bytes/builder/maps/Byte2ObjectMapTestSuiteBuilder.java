package speiger.src.testers.bytes.builder.maps;

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
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.testers.objects.builder.ObjectCollectionTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ObjectMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2ObjectMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ObjectMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Byte2ObjectMapTestSuiteBuilder<V> extends MapTestSuiteBuilder<Byte, V> {
	public static <V> Byte2ObjectMapTestSuiteBuilder<V> using(TestByte2ObjectMapGenerator<V> generator) {
		return (Byte2ObjectMapTestSuiteBuilder<V>) new Byte2ObjectMapTestSuiteBuilder<V>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2ObjectMapClearTester.class);
		testers.add(Byte2ObjectMapComputeTester.class);
		testers.add(Byte2ObjectMapComputeIfAbsentTester.class);
		testers.add(Byte2ObjectMapComputeIfPresentTester.class);
		testers.add(Byte2ObjectMapSupplyIfAbsentTester.class);
		testers.add(Byte2ObjectMapContainsKeyTester.class);
		testers.add(Byte2ObjectMapContainsValueTester.class);
		testers.add(Byte2ObjectMapEntrySetTester.class);
		testers.add(Byte2ObjectMapEqualsTester.class);
		testers.add(Byte2ObjectMapForEachTester.class);
		testers.add(Byte2ObjectMapGetTester.class);
		testers.add(Byte2ObjectMapGetOrDefaultTester.class);
		testers.add(Byte2ObjectMapHashCodeTester.class);
		testers.add(Byte2ObjectMapIsEmptyTester.class);
		testers.add(Byte2ObjectMapMergeTester.class);
		testers.add(Byte2ObjectMapPutTester.class);
		testers.add(Byte2ObjectMapPutAllTester.class);
		testers.add(Byte2ObjectMapPutAllArrayTester.class);
		testers.add(Byte2ObjectMapPutIfAbsentTester.class);
		testers.add(Byte2ObjectMapRemoveTester.class);
		testers.add(Byte2ObjectMapRemoveEntryTester.class);
		testers.add(Byte2ObjectMapRemoveOrDefaultTester.class);
		testers.add(Byte2ObjectMapReplaceTester.class);
		testers.add(Byte2ObjectMapReplaceAllTester.class);
		testers.add(Byte2ObjectMapReplaceEntryTester.class);
		testers.add(Byte2ObjectMapSizeTester.class);
		testers.add(Byte2ObjectMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, V>, Map.Entry<Byte, V>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2ObjectMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2ObjectMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedByte2ObjectMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2ObjectMap.Entry<V>> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2ObjectMap.Entry<V>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
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