import net.runelite.mapping.ObfuscatedName;
import net.runelite.mapping.ObfuscatedSignature;
import net.runelite.mapping.ObfuscatedGetter;
import net.runelite.mapping.Export;
@ObfuscatedName("dn")
public enum class116 implements MouseWheel {

	@ObfuscatedName("c")
	@ObfuscatedSignature(descriptor = "Ldn;")
	field1455(0, 0),
	@ObfuscatedName("v")
	@ObfuscatedSignature(descriptor = "Ldn;")
	field1451(1, 1),
	@ObfuscatedName("q")
	@ObfuscatedSignature(descriptor = "Ldn;")
	field1450(2, 2),
	@ObfuscatedName("f")
	@ObfuscatedSignature(descriptor = "Ldn;")
	field1453(3, 3),
	@ObfuscatedName("j")
	@ObfuscatedSignature(descriptor = "Ldn;")
	field1454(4, 4);
	@ObfuscatedName("e")
	@ObfuscatedGetter(intValue = -121286099)
	final int field1452;

	@ObfuscatedName("g")
	@ObfuscatedGetter(intValue = -1382484373)
	final int field1457;

	class116(int var3, int var4) {
		this.field1452 = var3;
		this.field1457 = var4;
	}

	@ObfuscatedName("c")
	@ObfuscatedSignature(descriptor = "(B)I", garbageValue = "-100")
	@Export("rsOrdinal")
	public int rsOrdinal() {
		return this.field1457;
	}

	@ObfuscatedName("c")
	@ObfuscatedSignature(descriptor = "(Llh;S)V", garbageValue = "22259")
	public static void method2700(AbstractArchive var0) {
		DbRowType.field4675 = var0;
	}

	@ObfuscatedName("v")
	@ObfuscatedSignature(descriptor = "(Lqe;IIII)V", garbageValue = "1518396143")
	static void method2699(SpritePixels var0, int var1, int var2, int var3) {
		DemotingHashTable var4 = WorldMapRegion.WorldMapRegion_cachedSprites;
		long var6 = ((long) (var3 << 16 | var1 << 8 | var2));
		var4.put(var0, var6, var0.pixels.length * 4);
	}
}