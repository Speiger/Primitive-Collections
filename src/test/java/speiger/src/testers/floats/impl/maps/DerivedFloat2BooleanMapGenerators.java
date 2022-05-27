package speiger.src.testers.floats.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanSortedMap;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.TestFloatSortedSetGenerator;
import speiger.src.testers.floats.generators.TestFloatNavigableSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2BooleanMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2BooleanSortedMapGenerator;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.booleans.utils.BooleanSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedFloat2BooleanMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestFloat2BooleanSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Float2BooleanNavigableMap createSubMap(Float2BooleanSortedMap sortedMap, float firstExclusive, float lastExclusive) {
	    	Float2BooleanNavigableMap map = (Float2BooleanNavigableMap) sortedMap;
	      if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) {
	        return map.headMap(lastInclusive, true);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) {
	        return map.tailMap(firstExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastInclusive, true);
	      } else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstInclusive, true, lastInclusive, true);
	      } else {
	        return (Float2BooleanNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestFloat2BooleanSortedMapGenerator {
		TestFloat2BooleanSortedMapGenerator parent;
		Bound to;
		Bound from;
		float firstInclusive;
		float lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestFloat2BooleanSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Float2BooleanSortedMap map = parent.create();
			entryComparator = DerivedFloat2BooleanMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getFloatKey();
			lastInclusive = samples.get(samples.size() - 1).getFloatKey();
		}
		
		@Override
		public Float2BooleanSortedMap create(Entry... elements) {
			ObjectList<Entry> entries = new ObjectArrayList<>();
			if (from != Bound.NO_BOUND) {
				entries.add(parent.belowSamplesLesser());
				entries.add(parent.belowSamplesGreater());
			}
			if (to != Bound.NO_BOUND) {
				entries.add(parent.aboveSamplesLesser());
				entries.add(parent.aboveSamplesGreater());
			}
			entries.addAll(elements);
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getFloatKey(), parent.aboveSamplesLesser().getFloatKey());
		}
		
		Float2BooleanSortedMap createSubMap(Float2BooleanSortedMap map, float firstExclusive, float lastExclusive) {
			if (from == Bound.NO_BOUND && to == Bound.EXCLUSIVE) {
				return map.headMap(lastExclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.NO_BOUND) {
				return map.tailMap(firstInclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.EXCLUSIVE) {
				return map.subMap(firstInclusive, lastExclusive);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Entry belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestFloat2BooleanSortedMapGenerator
	{
		TestFloat2BooleanSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestFloat2BooleanSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Float2BooleanNavigableMap create(Entry... elements) {
			return ((Float2BooleanNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Float, Boolean>> order(List<Map.Entry<Float, Boolean>> insertionOrder) {
			ObjectList<Map.Entry<Float, Boolean>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			ObjectList<Entry> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator implements TestFloat2BooleanMapGenerator
	{
		TestFloat2BooleanMapGenerator parent;
		
		public MapGenerator(TestFloat2BooleanMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Float, Boolean>> order(List<Map.Entry<Float, Boolean>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Float2BooleanMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Float2BooleanMap.Entry> {
		TestFloat2BooleanMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Float, Boolean>, Map.Entry<Float, Boolean>> inner) {
			generator = (TestFloat2BooleanMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry> order(List<Entry> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry>(insertionOrder));
		}

		@Override
		public Entry[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry> create(Object... elements) {
			return generator.create(elements).float2BooleanEntrySet();
		}
	}

	public static TestFloatSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Float, Boolean>, Map.Entry<Float, Boolean>> inner) {
		if (inner.getInnerGenerator() instanceof TestFloat2BooleanSortedMapGenerator) {
			FloatSet set = ((TestFloat2BooleanSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof FloatNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof FloatSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestFloatNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, Boolean>, Map.Entry<Float, Boolean>> inner) {
			super(inner);
		}
		
		@Override
		public FloatNavigableSet create(float... elements) {
			return (FloatNavigableSet) super.create(elements);
		}

		@Override
		public FloatNavigableSet create(Object... elements) {
			return (FloatNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestFloatSortedSetGenerator {
		TestFloat2BooleanSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, Boolean>, Map.Entry<Float, Boolean>> inner) {
			super(inner);
			generator = (TestFloat2BooleanSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public FloatSortedSet create(float... elements) {
			return (FloatSortedSet) super.create(elements);
		}

		@Override
		public FloatSortedSet create(Object... elements) {
			return (FloatSortedSet) super.create(elements);
		}

		@Override
		public float belowSamplesLesser() {
			return generator.belowSamplesLesser().getFloatKey();
		}

		@Override
		public float belowSamplesGreater() {
			return generator.belowSamplesGreater().getFloatKey();
		}

		@Override
		public float aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getFloatKey();
		}

		@Override
		public float aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getFloatKey();
		}
	}

	public static class MapKeySetGenerator implements TestFloatSetGenerator {
		TestFloat2BooleanMapGenerator generator;
		FloatSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, Boolean>, Map.Entry<Float, Boolean>> inner) {
			generator = (TestFloat2BooleanMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Float2BooleanMap.Entry> samples = generator.getSamples();
			this.samples = new FloatSamples(samples.e0().getFloatKey(), samples.e1().getFloatKey(), samples.e2().getFloatKey(), samples.e3().getFloatKey(), samples.e4().getFloatKey());
		}

		@Override
		public FloatSamples getSamples() {
			return samples;
		}

		@Override
		public FloatIterable order(FloatList insertionOrder) {
			boolean value = generator.getSamples().e0().getBooleanValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (float key : insertionOrder) {
				entries.add(new AbstractFloat2BooleanMap.BasicEntry(key, value));
			}
			FloatList list = new FloatArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getFloatKey());
			}
			return list;
		}

		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			boolean value = generator.getSamples().e0().getBooleanValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (float key : insertionOrder) {
				entries.add(new AbstractFloat2BooleanMap.BasicEntry(key, value));
			}
			FloatList list = new FloatArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getFloatKey());
			}
			return list;
		}
		
		@Override
		public FloatSet create(float... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2BooleanMap.BasicEntry(elements[index++], entry.getBooleanValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public FloatSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2BooleanMap.BasicEntry((Float) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestBooleanCollectionGenerator {
		TestFloat2BooleanMapGenerator generator;
		BooleanSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Float, Boolean>, Map.Entry<Float, Boolean>> inner) {
			generator = (TestFloat2BooleanMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Float2BooleanMap.Entry> samples = generator.getSamples();
			this.samples = new BooleanSamples(samples.e0().getBooleanValue(), samples.e1().getBooleanValue(), samples.e2().getBooleanValue(), samples.e3().getBooleanValue(), samples.e4().getBooleanValue());
		}
		
		@Override
		public BooleanSamples getSamples() {
			return samples;
		}

		@Override
		public BooleanIterable order(BooleanList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new BooleanComparator() {
				@Override
				public int compare(boolean key, boolean value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(boolean entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getBooleanValue() == entry) return i;
					}
					throw new IllegalArgumentException("Float2BooleanMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Boolean> order(List<Boolean> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Boolean>() {
				@Override
				public int compare(Boolean key, Boolean value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Boolean entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getBooleanValue() == entry.booleanValue()) return i;
					}
					throw new IllegalArgumentException("Float2BooleanMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public BooleanCollection create(boolean... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2BooleanMap.BasicEntry(entry.getFloatKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public BooleanCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2BooleanMap.BasicEntry(entry.getKey(), (Boolean)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Float, Boolean>> entryObjectComparator(Comparator<Float> keyComparator) {
		return new Comparator<Map.Entry<Float, Boolean>>() {
			@Override
			public int compare(Map.Entry<Float, Boolean> a, Map.Entry<Float, Boolean> b) {
				if(keyComparator == null) {
					return Float.compare(a.getKey().floatValue(), b.getKey().floatValue());
				}
				return keyComparator.compare(a.getKey().floatValue(), b.getKey().floatValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(FloatComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Float.compare(a.getFloatKey(), b.getFloatKey());
				}
				return keyComparator.compare(a.getFloatKey(), b.getFloatKey());
			}
		};
	}
}