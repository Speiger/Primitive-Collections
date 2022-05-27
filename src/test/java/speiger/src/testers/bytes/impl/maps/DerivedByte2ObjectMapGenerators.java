package speiger.src.testers.bytes.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectSortedMap;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSortedSetGenerator;
import speiger.src.testers.bytes.generators.TestByteNavigableSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ObjectMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ObjectSortedMapGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class DerivedByte2ObjectMapGenerators {
	public static class NavigableMapGenerator<V> extends SortedMapGenerator<V> {
		public NavigableMapGenerator(TestByte2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Byte2ObjectNavigableMap<V> createSubMap(Byte2ObjectSortedMap<V> sortedMap, byte firstExclusive, byte lastExclusive) {
	    	Byte2ObjectNavigableMap<V> map = (Byte2ObjectNavigableMap<V>) sortedMap;
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
	        return (Byte2ObjectNavigableMap<V>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<V> extends MapGenerator<V> implements TestByte2ObjectSortedMapGenerator<V> {
		TestByte2ObjectSortedMapGenerator<V> parent;
		Bound to;
		Bound from;
		byte firstInclusive;
		byte lastInclusive;
		Comparator<Entry<V>> entryComparator;

		public SortedMapGenerator(TestByte2ObjectSortedMapGenerator<V> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Byte2ObjectSortedMap<V> map = parent.create();
			entryComparator = DerivedByte2ObjectMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<V>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getByteKey();
			lastInclusive = samples.get(samples.size() - 1).getByteKey();
		}
		
		@Override
		public Byte2ObjectSortedMap<V> create(Entry<V>... elements) {
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
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
		
		Byte2ObjectSortedMap<V> createSubMap(Byte2ObjectSortedMap<V> map, byte firstExclusive, byte lastExclusive) {
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
		public Entry<V> belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<V> belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<V> aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<V> aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator<V> extends MapGenerator<V> implements TestByte2ObjectSortedMapGenerator<V>
	{
		TestByte2ObjectSortedMapGenerator<V> parent;
		
		public DescendingTestMapGenerator(TestByte2ObjectSortedMapGenerator<V> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Byte2ObjectNavigableMap<V> create(Entry<V>... elements) {
			return ((Byte2ObjectNavigableMap<V>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Byte, V>> order(List<Map.Entry<Byte, V>> insertionOrder) {
			ObjectList<Map.Entry<Byte, V>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry<V>> order(ObjectList<Entry<V>> insertionOrder) {
			ObjectList<Entry<V>> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry<V> belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry<V> belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry<V> aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry<V> aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator<V> implements TestByte2ObjectMapGenerator<V>
	{
		TestByte2ObjectMapGenerator<V> parent;
		
		public MapGenerator(TestByte2ObjectMapGenerator<V> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Byte, V>> order(List<Map.Entry<Byte, V>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry<V>> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry<V>> order(ObjectList<Entry<V>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Byte2ObjectMap<V> create(Entry<V>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<V> implements TestObjectSetGenerator<Byte2ObjectMap.Entry<V>> {
		TestByte2ObjectMapGenerator<V> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Byte, V>, Map.Entry<Byte, V>> inner) {
			generator = (TestByte2ObjectMapGenerator<V>) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry<V>> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry<V>> order(ObjectList<Entry<V>> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry<V>> order(List<Entry<V>> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry<V>>(insertionOrder));
		}

		@Override
		public Entry<V>[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry<V>> create(Object... elements) {
			return generator.create(elements).byte2ObjectEntrySet();
		}
	}

	public static <V> TestByteSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Byte, V>, Map.Entry<Byte, V>> inner) {
		if (inner.getInnerGenerator() instanceof TestByte2ObjectSortedMapGenerator) {
			ByteSet set = ((TestByte2ObjectSortedMapGenerator<V>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ByteNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof ByteSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<V> extends MapSortedKeySetGenerator<V> implements TestByteNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, V>, Map.Entry<Byte, V>> inner) {
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
	
	public static class MapSortedKeySetGenerator<V> extends MapKeySetGenerator<V> implements TestByteSortedSetGenerator {
		TestByte2ObjectSortedMapGenerator<V> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, V>, Map.Entry<Byte, V>> inner) {
			super(inner);
			generator = (TestByte2ObjectSortedMapGenerator<V>) inner.getInnerGenerator();
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

	public static class MapKeySetGenerator<V> implements TestByteSetGenerator {
		TestByte2ObjectMapGenerator<V> generator;
		ByteSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, V>, Map.Entry<Byte, V>> inner) {
			generator = (TestByte2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Byte2ObjectMap.Entry<V>> samples = generator.getSamples();
			this.samples = new ByteSamples(samples.e0().getByteKey(), samples.e1().getByteKey(), samples.e2().getByteKey(), samples.e3().getByteKey(), samples.e4().getByteKey());
		}

		@Override
		public ByteSamples getSamples() {
			return samples;
		}

		@Override
		public ByteIterable order(ByteList insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (byte key : insertionOrder) {
				entries.add(new AbstractByte2ObjectMap.BasicEntry<>(key, value));
			}
			ByteList list = new ByteArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getByteKey());
			}
			return list;
		}

		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			V value = generator.getSamples().e0().getValue();
			ObjectList<Entry<V>> entries = new ObjectArrayList<>();
			for (byte key : insertionOrder) {
				entries.add(new AbstractByte2ObjectMap.BasicEntry<>(key, value));
			}
			ByteList list = new ByteArrayList();
			for (Entry<V> entry : generator.order(entries)) {
				list.add(entry.getByteKey());
			}
			return list;
		}
		
		@Override
		public ByteSet create(byte... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2ObjectMap.BasicEntry<>(elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public ByteSet create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2ObjectMap.BasicEntry<>((Byte) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<V> implements TestObjectCollectionGenerator<V> {
		TestByte2ObjectMapGenerator<V> generator;
		ObjectSamples<V> samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Byte, V>, Map.Entry<Byte, V>> inner) {
			generator = (TestByte2ObjectMapGenerator<V>) inner.getInnerGenerator();
			ObjectSamples<Byte2ObjectMap.Entry<V>> samples = generator.getSamples();
			this.samples = new ObjectSamples<>(samples.e0().getValue(), samples.e1().getValue(), samples.e2().getValue(), samples.e3().getValue(), samples.e4().getValue());
		}
		
		@Override
		public ObjectSamples<V> getSamples() {
			return samples;
		}

		@Override
		public ObjectIterable<V> order(ObjectList<V> insertionOrder) {
			ObjectList<Entry<V>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<V>() {
				@Override
				public int compare(V key, V value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(V entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Objects.equals(list.get(i).getValue(), entry)) return i;
					}
					throw new IllegalArgumentException("Byte2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<V> order(List<V> insertionOrder) {
			ObjectList<Entry<V>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<V>() {
				@Override
				public int compare(V key, V value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(V entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Objects.equals(list.get(i).getValue(), entry)) return i;
					}
					throw new IllegalArgumentException("Byte2ObjectMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ObjectCollection<V> create(Object... elements) {
			Entry<V>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<V> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2ObjectMap.BasicEntry<>(entry.getKey(), (V)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<V> Comparator<Map.Entry<Byte, V>> entryObjectComparator(Comparator<Byte> keyComparator) {
		return new Comparator<Map.Entry<Byte, V>>() {
			@Override
			public int compare(Map.Entry<Byte, V> a, Map.Entry<Byte, V> b) {
				if(keyComparator == null) {
					return Byte.compare(a.getKey().byteValue(), b.getKey().byteValue());
				}
				return keyComparator.compare(a.getKey().byteValue(), b.getKey().byteValue());
			}
		};
	}
	
	public static<V> Comparator<Entry<V>> entryComparator(ByteComparator keyComparator) {
		return new Comparator<Entry<V>>() {
			@Override
			public int compare(Entry<V> a, Entry<V> b) {
				if(keyComparator == null) {
					return Byte.compare(a.getByteKey(), b.getByteKey());
				}
				return keyComparator.compare(a.getByteKey(), b.getByteKey());
			}
		};
	}
}