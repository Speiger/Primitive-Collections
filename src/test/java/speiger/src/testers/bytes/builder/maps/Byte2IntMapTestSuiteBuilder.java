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
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.testers.ints.builder.IntCollectionTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2IntMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2IntMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapAddToTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2IntMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Byte2IntMapTestSuiteBuilder extends MapTestSuiteBuilder<Byte, Integer> {
	public static Byte2IntMapTestSuiteBuilder using(TestByte2IntMapGenerator generator) {
		return (Byte2IntMapTestSuiteBuilder) new Byte2IntMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2IntMapClearTester.class);
		testers.add(Byte2IntMapComputeTester.class);
		testers.add(Byte2IntMapComputeIfAbsentTester.class);
		testers.add(Byte2IntMapComputeIfPresentTester.class);
		testers.add(Byte2IntMapSupplyIfAbsentTester.class);
		testers.add(Byte2IntMapContainsKeyTester.class);
		testers.add(Byte2IntMapContainsValueTester.class);
		testers.add(Byte2IntMapEntrySetTester.class);
		testers.add(Byte2IntMapEqualsTester.class);
		testers.add(Byte2IntMapForEachTester.class);
		testers.add(Byte2IntMapGetTester.class);
		testers.add(Byte2IntMapGetOrDefaultTester.class);
		testers.add(Byte2IntMapHashCodeTester.class);
		testers.add(Byte2IntMapIsEmptyTester.class);
		testers.add(Byte2IntMapMergeTester.class);
		testers.add(Byte2IntMapPutTester.class);
		testers.add(Byte2IntMapAddToTester.class);
		testers.add(Byte2IntMapPutAllTester.class);
		testers.add(Byte2IntMapPutAllArrayTester.class);
		testers.add(Byte2IntMapPutIfAbsentTester.class);
		testers.add(Byte2IntMapRemoveTester.class);
		testers.add(Byte2IntMapRemoveEntryTester.class);
		testers.add(Byte2IntMapRemoveOrDefaultTester.class);
		testers.add(Byte2IntMapReplaceTester.class);
		testers.add(Byte2IntMapReplaceAllTester.class);
		testers.add(Byte2IntMapReplaceEntryTester.class);
		testers.add(Byte2IntMapSizeTester.class);
		testers.add(Byte2IntMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Integer>, Map.Entry<Byte, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2IntMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2IntMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedByte2IntMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2IntMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2IntMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
	}
	
	protected IntCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestIntCollectionGenerator generator) {
		return IntCollectionTestSuiteBuilder.using(generator);
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