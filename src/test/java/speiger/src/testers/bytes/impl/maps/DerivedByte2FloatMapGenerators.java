package speiger.src.testers.bytes.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatSortedMap;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSortedSetGenerator;
import speiger.src.testers.bytes.generators.TestByteNavigableSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2FloatMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2FloatSortedMapGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedByte2FloatMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestByte2FloatSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Byte2FloatNavigableMap createSubMap(Byte2FloatSortedMap sortedMap, byte firstExclusive, byte lastExclusive) {
	    	Byte2FloatNavigableMap map = (Byte2FloatNavigableMap) sortedMap;
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
	        return (Byte2FloatNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestByte2FloatSortedMapGenerator {
		TestByte2FloatSortedMapGenerator parent;
		Bound to;
		Bound from;
		byte firstInclusive;
		byte lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestByte2FloatSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Byte2FloatSortedMap map = parent.create();
			entryComparator = DerivedByte2FloatMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getByteKey();
			lastInclusive = samples.get(samples.size() - 1).getByteKey();
		}
		
		@Override
		public Byte2FloatSortedMap create(Entry... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getByteKey(), parent.aboveSamplesLesser().getByteKey());
		}
		
		Byte2FloatSortedMap createSubMap(Byte2FloatSortedMap map, byte firstExclusive, byte lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestByte2FloatSortedMapGenerator
	{
		TestByte2FloatSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestByte2FloatSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Byte2FloatNavigableMap create(Entry... elements) {
			return ((Byte2FloatNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Byte, Float>> order(List<Map.Entry<Byte, Float>> insertionOrder) {
			ObjectList<Map.Entry<Byte, Float>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestByte2FloatMapGenerator
	{
		TestByte2FloatMapGenerator parent;
		
		public MapGenerator(TestByte2FloatMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Byte, Float>> order(List<Map.Entry<Byte, Float>> insertionOrder) {
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
		public Byte2FloatMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Byte2FloatMap.Entry> {
		TestByte2FloatMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Float>, Map.Entry<Byte, Float>> inner) {
			generator = (TestByte2FloatMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).byte2FloatEntrySet();
		}
	}

	public static TestByteSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Float>, Map.Entry<Byte, Float>> inner) {
		if (inner.getInnerGenerator() instanceof TestByte2FloatSortedMapGenerator) {
			ByteSet set = ((TestByte2FloatSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ByteNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof ByteSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestByteNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Float>, Map.Entry<Byte, Float>> inner) {
			super(inner);
		}
		
		@Override
		public ByteNavigableSet create(byte... elements) {
			return (ByteNavigableSet) super.create(elements);
		}

		@Override
		public ByteNavigableSet create(Object... elements) {
			return (ByteNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestByteSortedSetGenerator {
		TestByte2FloatSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Float>, Map.Entry<Byte, Float>> inner) {
			super(inner);
			generator = (TestByte2FloatSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ByteSortedSet create(byte... elements) {
			return (ByteSortedSet) super.create(elements);
		}

		@Override
		public ByteSortedSet create(Object... elements) {
			return (ByteSortedSet) super.create(elements);
		}

		@Override
		public byte belowSamplesLesser() {
			return generator.belowSamplesLesser().getByteKey();
		}

		@Override
		public byte belowSamplesGreater() {
			return generator.belowSamplesGreater().getByteKey();
		}

		@Override
		public byte aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getByteKey();
		}

		@Override
		public byte aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getByteKey();
		}
	}

	public static class MapKeySetGenerator implements TestByteSetGenerator {
		TestByte2FloatMapGenerator generator;
		ByteSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Float>, Map.Entry<Byte, Float>> inner) {
			generator = (TestByte2FloatMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Byte2FloatMap.Entry> samples = generator.getSamples();
			this.samples = new ByteSamples(samples.e0().getByteKey(), samples.e1().getByteKey(), samples.e2().getByteKey(), samples.e3().getByteKey(), samples.e4().getByteKey());
		}

		@Override
		public ByteSamples getSamples() {
			return samples;
		}

		@Override
		public ByteIterable order(ByteList insertionOrder) {
			float value = generator.getSamples().e0().getFloatValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (byte key : insertionOrder) {
				entries.add(new AbstractByte2FloatMap.BasicEntry(key, value));
			}
			ByteList list = new ByteArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getByteKey());
			}
			return list;
		}

		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			float value = generator.getSamples().e0().getFloatValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (byte key : insertionOrder) {
				entries.add(new AbstractByte2FloatMap.BasicEntry(key, value));
			}
			ByteList list = new ByteArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getByteKey());
			}
			return list;
		}
		
		@Override
		public ByteSet create(byte... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2FloatMap.BasicEntry(elements[index++], entry.getFloatValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public ByteSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2FloatMap.BasicEntry((Byte) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestFloatCollectionGenerator {
		TestByte2FloatMapGenerator generator;
		FloatSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Byte, Float>, Map.Entry<Byte, Float>> inner) {
			generator = (TestByte2FloatMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Byte2FloatMap.Entry> samples = generator.getSamples();
			this.samples = new FloatSamples(samples.e0().getFloatValue(), samples.e1().getFloatValue(), samples.e2().getFloatValue(), samples.e3().getFloatValue(), samples.e4().getFloatValue());
		}
		
		@Override
		public FloatSamples getSamples() {
			return samples;
		}

		@Override
		public FloatIterable order(FloatList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new FloatComparator() {
				@Override
				public int compare(float key, float value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(float entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Float.floatToIntBits(list.get(i).getFloatValue()) == Float.floatToIntBits(entry)) return i;
					}
					throw new IllegalArgumentException("Byte2FloatMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Float>() {
				@Override
				public int compare(Float key, Float value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Float entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Float.floatToIntBits(list.get(i).getFloatValue()) == Float.floatToIntBits(entry.floatValue())) return i;
					}
					throw new IllegalArgumentException("Byte2FloatMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public FloatCollection create(float... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2FloatMap.BasicEntry(entry.getByteKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public FloatCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2FloatMap.BasicEntry(entry.getKey(), (Float)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Byte, Float>> entryObjectComparator(Comparator<Byte> keyComparator) {
		return new Comparator<Map.Entry<Byte, Float>>() {
			@Override
			public int compare(Map.Entry<Byte, Float> a, Map.Entry<Byte, Float> b) {
				if(keyComparator == null) {
					return Byte.compare(a.getKey().byteValue(), b.getKey().byteValue());
				}
				return keyComparator.compare(a.getKey().byteValue(), b.getKey().byteValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(ByteComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Byte.compare(a.getByteKey(), b.getByteKey());
				}
				return keyComparator.compare(a.getByteKey(), b.getByteKey());
			}
		};
	}
}