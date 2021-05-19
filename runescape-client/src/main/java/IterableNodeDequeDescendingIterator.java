import java.util.Iterator;
import net.runelite.mapping.Export;
import net.runelite.mapping.Implements;
import net.runelite.mapping.ObfuscatedName;
import net.runelite.mapping.ObfuscatedSignature;

@ObfuscatedName("ku")
@Implements("IterableNodeDequeDescendingIterator")
public class IterableNodeDequeDescendingIterator implements Iterator {
	@ObfuscatedName("h")
	@ObfuscatedSignature(
		descriptor = "Lko;"
	)
	@Export("deque")
	IterableNodeDeque deque;
	@ObfuscatedName("c")
	@ObfuscatedSignature(
		descriptor = "Lmd;"
	)
	Node field3730;
	@ObfuscatedName("o")
	@ObfuscatedSignature(
		descriptor = "Lmd;"
	)
	@Export("last")
	Node last;

	@ObfuscatedSignature(
		descriptor = "(Lko;)V"
	)
	IterableNodeDequeDescendingIterator(IterableNodeDeque var1) {
		this.last = null; // L: 9
		this.setDeque(var1); // L: 12
	} // L: 13

	@ObfuscatedName("h")
	@ObfuscatedSignature(
		descriptor = "(Lko;)V"
	)
	@Export("setDeque")
	void setDeque(IterableNodeDeque var1) {
		this.deque = var1; // L: 16
		this.start(); // L: 17
	} // L: 18

	@ObfuscatedName("c")
	@Export("start")
	void start() {
		this.field3730 = this.deque != null ? this.deque.sentinel.previous : null; // L: 21
		this.last = null; // L: 22
	} // L: 23

	public Object next() {
		Node var1 = this.field3730; // L: 26
		if (var1 == this.deque.sentinel) { // L: 27
			var1 = null; // L: 28
			this.field3730 = null; // L: 29
		} else {
			this.field3730 = var1.previous; // L: 31
		}

		this.last = var1; // L: 32
		return var1; // L: 33
	}

	public boolean hasNext() {
		return this.deque.sentinel != this.field3730; // L: 37
	}

	public void remove() {
		if (this.last == null) { // L: 41
			throw new IllegalStateException();
		} else {
			this.last.remove(); // L: 42
			this.last = null; // L: 43
		}
	} // L: 44
}
