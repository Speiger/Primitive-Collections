package speiger.src.collections.bytes.functions;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class OptionalByte {
	private static final OptionalByte EMPTY = new OptionalByte();
	
	private final boolean isPresent;
	private final byte value;
	
	private OptionalByte() {
		this.isPresent = false;
		this.value = (byte)0;
	}
	
	private OptionalByte(byte value) {
		this.isPresent = true;
		this.value = value;
	}
	
	public static OptionalByte empty() {
		return EMPTY;
	}
	
	public static OptionalByte of(byte value) {
		return new OptionalByte(value);
	}
	
	public byte getAsByte() {
		if(!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public boolean isEmpty() {
		return !isPresent;
	}
	
	public void ifPresent(ByteConsumer consumer) {
		if(isPresent) consumer.accept(value);
	}
	
    public void ifPresentOrElse(ByteConsumer action, Runnable emptyAction) {
        if (isPresent) action.accept(value);
        else emptyAction.run();
    }
	
	public byte orElse(byte other) {
		return isPresent ? value : other;
	}
	
	public byte orElseGet(ByteSupplier other) {
		return isPresent ? value : other.getAsByte();
	}
	
	public byte orElseThrow() {
        if (!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
    public <X extends Throwable> byte orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    	if(isPresent) return value;
    	else throw exceptionSupplier.get();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(obj == this) return true;
    	if(obj instanceof OptionalByte) {
    		OptionalByte other = (OptionalByte)obj;
    		return (isPresent && other.isPresent ? value == other.value : isPresent == other.isPresent);
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return isPresent ? Byte.hashCode(value) : 0;
    }
    
    @Override
    public String toString() {
    	return isPresent ? "OptionalByte["+value+"]" : "OptionalByte.empty";
    }
}