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
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.testers.booleans.builder.BooleanCollectionTestSuiteBuilder;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2BooleanMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2BooleanMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2BooleanMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Byte2BooleanMapTestSuiteBuilder extends MapTestSuiteBuilder<Byte, Boolean> {
	public static Byte2BooleanMapTestSuiteBuilder using(TestByte2BooleanMapGenerator generator) {
		return (Byte2BooleanMapTestSuiteBuilder) new Byte2BooleanMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2BooleanMapClearTester.class);
		testers.add(Byte2BooleanMapComputeTester.class);
		testers.add(Byte2BooleanMapComputeIfAbsentTester.class);
		testers.add(Byte2BooleanMapComputeIfPresentTester.class);
		testers.add(Byte2BooleanMapSupplyIfAbsentTester.class);
		testers.add(Byte2BooleanMapContainsKeyTester.class);
		testers.add(Byte2BooleanMapContainsValueTester.class);
		testers.add(Byte2BooleanMapEntrySetTester.class);
		testers.add(Byte2BooleanMapEqualsTester.class);
		testers.add(Byte2BooleanMapForEachTester.class);
		testers.add(Byte2BooleanMapGetTester.class);
		testers.add(Byte2BooleanMapGetOrDefaultTester.class);
		testers.add(Byte2BooleanMapHashCodeTester.class);
		testers.add(Byte2BooleanMapIsEmptyTester.class);
		testers.add(Byte2BooleanMapMergeTester.class);
		testers.add(Byte2BooleanMapPutTester.class);
		testers.add(Byte2BooleanMapPutAllTester.class);
		testers.add(Byte2BooleanMapPutAllArrayTester.class);
		testers.add(Byte2BooleanMapPutIfAbsentTester.class);
		testers.add(Byte2BooleanMapRemoveTester.class);
		testers.add(Byte2BooleanMapRemoveEntryTester.class);
		testers.add(Byte2BooleanMapRemoveOrDefaultTester.class);
		testers.add(Byte2BooleanMapReplaceTester.class);
		testers.add(Byte2BooleanMapReplaceAllTester.class);
		testers.add(Byte2BooleanMapReplaceEntryTester.class);
		testers.add(Byte2BooleanMapSizeTester.class);
		testers.add(Byte2BooleanMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Boolean>, Map.Entry<Byte, Boolean>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2BooleanMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2BooleanMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2BooleanMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2BooleanMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
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