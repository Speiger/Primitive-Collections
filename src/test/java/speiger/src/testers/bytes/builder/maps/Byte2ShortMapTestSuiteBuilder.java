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
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.testers.shorts.builder.ShortCollectionTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ShortMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2ShortMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapAddToTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ShortMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Byte2ShortMapTestSuiteBuilder extends MapTestSuiteBuilder<Byte, Short> {
	public static Byte2ShortMapTestSuiteBuilder using(TestByte2ShortMapGenerator generator) {
		return (Byte2ShortMapTestSuiteBuilder) new Byte2ShortMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2ShortMapClearTester.class);
		testers.add(Byte2ShortMapComputeTester.class);
		testers.add(Byte2ShortMapComputeIfAbsentTester.class);
		testers.add(Byte2ShortMapComputeIfPresentTester.class);
		testers.add(Byte2ShortMapSupplyIfAbsentTester.class);
		testers.add(Byte2ShortMapContainsKeyTester.class);
		testers.add(Byte2ShortMapContainsValueTester.class);
		testers.add(Byte2ShortMapEntrySetTester.class);
		testers.add(Byte2ShortMapEqualsTester.class);
		testers.add(Byte2ShortMapForEachTester.class);
		testers.add(Byte2ShortMapGetTester.class);
		testers.add(Byte2ShortMapGetOrDefaultTester.class);
		testers.add(Byte2ShortMapHashCodeTester.class);
		testers.add(Byte2ShortMapIsEmptyTester.class);
		testers.add(Byte2ShortMapMergeTester.class);
		testers.add(Byte2ShortMapPutTester.class);
		testers.add(Byte2ShortMapAddToTester.class);
		testers.add(Byte2ShortMapPutAllTester.class);
		testers.add(Byte2ShortMapPutAllArrayTester.class);
		testers.add(Byte2ShortMapPutIfAbsentTester.class);
		testers.add(Byte2ShortMapRemoveTester.class);
		testers.add(Byte2ShortMapRemoveEntryTester.class);
		testers.add(Byte2ShortMapRemoveOrDefaultTester.class);
		testers.add(Byte2ShortMapReplaceTester.class);
		testers.add(Byte2ShortMapReplaceAllTester.class);
		testers.add(Byte2ShortMapReplaceEntryTester.class);
		testers.add(Byte2ShortMapSizeTester.class);
		testers.add(Byte2ShortMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Short>, Map.Entry<Byte, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2ShortMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2ShortMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedByte2ShortMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2ShortMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2ShortMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
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