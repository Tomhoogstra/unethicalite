import net.runelite.mapping.Export;
import net.runelite.mapping.Implements;
import net.runelite.mapping.ObfuscatedName;

@ObfuscatedName("bj")
@Implements("AbstractSound")
public abstract class AbstractSound extends Node {
	@ObfuscatedName("z")
	@Export("position")
	int position;

	AbstractSound() {
	} // L: 10
}
