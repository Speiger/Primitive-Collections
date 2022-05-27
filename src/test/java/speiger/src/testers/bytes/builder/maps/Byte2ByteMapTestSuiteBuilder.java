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
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.testers.bytes.builder.ByteCollectionTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ByteMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2ByteMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapAddToTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2ByteMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Byte2ByteMapTestSuiteBuilder extends MapTestSuiteBuilder<Byte, Byte> {
	public static Byte2ByteMapTestSuiteBuilder using(TestByte2ByteMapGenerator generator) {
		return (Byte2ByteMapTestSuiteBuilder) new Byte2ByteMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2ByteMapClearTester.class);
		testers.add(Byte2ByteMapComputeTester.class);
		testers.add(Byte2ByteMapComputeIfAbsentTester.class);
		testers.add(Byte2ByteMapComputeIfPresentTester.class);
		testers.add(Byte2ByteMapSupplyIfAbsentTester.class);
		testers.add(Byte2ByteMapContainsKeyTester.class);
		testers.add(Byte2ByteMapContainsValueTester.class);
		testers.add(Byte2ByteMapEntrySetTester.class);
		testers.add(Byte2ByteMapEqualsTester.class);
		testers.add(Byte2ByteMapForEachTester.class);
		testers.add(Byte2ByteMapGetTester.class);
		testers.add(Byte2ByteMapGetOrDefaultTester.class);
		testers.add(Byte2ByteMapHashCodeTester.class);
		testers.add(Byte2ByteMapIsEmptyTester.class);
		testers.add(Byte2ByteMapMergeTester.class);
		testers.add(Byte2ByteMapPutTester.class);
		testers.add(Byte2ByteMapAddToTester.class);
		testers.add(Byte2ByteMapPutAllTester.class);
		testers.add(Byte2ByteMapPutAllArrayTester.class);
		testers.add(Byte2ByteMapPutIfAbsentTester.class);
		testers.add(Byte2ByteMapRemoveTester.class);
		testers.add(Byte2ByteMapRemoveEntryTester.class);
		testers.add(Byte2ByteMapRemoveOrDefaultTester.class);
		testers.add(Byte2ByteMapReplaceTester.class);
		testers.add(Byte2ByteMapReplaceAllTester.class);
		testers.add(Byte2ByteMapReplaceEntryTester.class);
		testers.add(Byte2ByteMapSizeTester.class);
		testers.add(Byte2ByteMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Byte>, Map.Entry<Byte, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2ByteMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2ByteMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedByte2ByteMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2ByteMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2ByteMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
	}
	
	protected ByteCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestByteCollectionGenerator generator) {
		return ByteCollectionTestSuiteBuilder.using(generator);
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