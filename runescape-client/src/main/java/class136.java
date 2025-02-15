import java.net.URL;
import net.runelite.mapping.ObfuscatedName;
import net.runelite.mapping.ObfuscatedSignature;
import net.runelite.mapping.ObfuscatedGetter;
import net.runelite.mapping.Export;
@ObfuscatedName("ed")
public class class136 extends class144 {
	@ObfuscatedName("z")
	@ObfuscatedGetter(intValue = 108140015)
	static int field1612;

	@ObfuscatedName("m")
	@Export("BZip2Decompressor_block")
	static int[] BZip2Decompressor_block;

	@ObfuscatedName("c")
	@ObfuscatedGetter(intValue = 1682759161)
	int field1615;

	@ObfuscatedSignature(descriptor = "Leu;")
	final class145 this$0;

	@ObfuscatedSignature(descriptor = "(Leu;)V")
	class136(class145 var1) {
		this.this$0 = var1;
		this.field1615 = -1;
	}

	@ObfuscatedName("c")
	@ObfuscatedSignature(descriptor = "(Lqt;I)V", garbageValue = "355261812")
	void vmethod3137(Buffer var1) {
		this.field1615 = var1.readUnsignedShort();
		var1.readUnsignedByte();
		if (var1.readUnsignedByte() != 255) {
			--var1.offset;
			var1.readLong();
		}
	}

	@ObfuscatedName("v")
	@ObfuscatedSignature(descriptor = "(Lex;I)V", garbageValue = "1368590037")
	void vmethod3138(ClanChannel var1) {
		var1.removeMember(this.field1615);
	}

	@ObfuscatedName("c")
	@ObfuscatedSignature(descriptor = "(I)Z", garbageValue = "1973466779")
	@Export("loadWorlds")
	static boolean loadWorlds() {
		try {
			if (class174.World_request == null) {
				class174.World_request = class138.urlRequester.request(new URL(Client.field481));
			} else if (class174.World_request.isDone()) {
				byte[] var0 = class174.World_request.getResponse();
				Buffer var1 = new Buffer(var0);
				var1.readInt();
				World.World_count = var1.readUnsignedShort();
				class362.World_worlds = new World[World.World_count];
				World var3;
				for (int var2 = 0; var2 < World.World_count; var3.index = var2++) {
					var3 = class362.World_worlds[var2] = new World();
					var3.id = var1.readUnsignedShort();
					var3.properties = var1.readInt();
					var3.host = var1.readStringCp1252NullTerminated();
					var3.activity = var1.readStringCp1252NullTerminated();
					var3.location = var1.readUnsignedByte();
					var3.population = var1.readShort();
				}
				MouseRecorder.sortWorlds(class362.World_worlds, 0, class362.World_worlds.length - 1, World.World_sortOption1, World.World_sortOption2);
				class174.World_request = null;
				return true;
			}
		} catch (Exception var4) {
			var4.printStackTrace();
			class174.World_request = null;
		}
		return false;
	}

	@ObfuscatedName("c")
	@ObfuscatedSignature(descriptor = "(III)I", garbageValue = "695321125")
	public static int method2923(int var0, int var1) {
		return ((int) (Math.round(Math.atan2(((double) (var0)), ((double) (var1))) * 2607.5945876176133))) & 16383;
	}
}