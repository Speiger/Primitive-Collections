package speiger.src.builder.misc;

public class Tuple<K, V>
{
	K key;
	V value;
	
	public Tuple(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
	
	public K getKey()
	{
		return key;
	}
	
	public V getValue()
	{
		return value;
	}
}
