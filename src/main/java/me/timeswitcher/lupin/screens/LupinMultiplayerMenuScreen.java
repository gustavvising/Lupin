package me.timeswitcher.lupin.screens;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.renderings.ServerSelectionListScreen;
import me.timeswitcher.lupin.utility.RenderUtil;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ServerListScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LupinMultiplayerMenuScreen extends Screen {

	private final ServerPinger oldServerPinger = new ServerPinger();
	private final Screen parentScreen;
	public ServerSelectionListScreen serverListSelector;
	private ServerList savedServerList;
	private Button btnEditServer;
	private Button btnSelectServer;
	private Button btnDeleteServer;
	private String hoveringText;
	private ServerData selectedServer;
	private LanServerDetector.LanServerList lanServerList;
	private LanServerDetector.LanServerFindThread lanServerDetector;
	private boolean initialized;

	public LupinMultiplayerMenuScreen(Screen parentScreen) {
		super(new TranslationTextComponent("multiplayer.title"));
		this.parentScreen = parentScreen;
	}

	private boolean isAltHovered(final double mouseX, final double mouseY) {
		return mouseX >= 2 && mouseX <= 2 + 70 && mouseY >= 2 && mouseY <= 22;
	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {

		if (isAltHovered(p_mouseClicked_1_, p_mouseClicked_3_)) {
			Lupin.mc.displayGuiScreen(new LupinAltLoginScreen(this));
		}
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	protected void init() {
		super.init();
		this.minecraft.keyboardListener.enableRepeatEvents(true);
		if (this.initialized) {
			this.serverListSelector.updateSize(this.width, this.height, 32, this.height - 64);
		} else {
			this.initialized = true;
			this.savedServerList = new ServerList(this.minecraft);
			this.savedServerList.loadServerList();
			this.lanServerList = new LanServerDetector.LanServerList();

			try {
				this.lanServerDetector = new LanServerDetector.LanServerFindThread(this.lanServerList);
				this.lanServerDetector.start();
			} catch (Exception ignored) {
			}

			this.serverListSelector = new ServerSelectionListScreen(this, this.minecraft, this.width, this.height, 32, this.height - 64, 36);
			this.serverListSelector.updateOnlineServers(this.savedServerList);
		}

		this.children.add(this.serverListSelector);
		this.btnSelectServer = this.addButton(new Button(this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select"), (p_214293_1_) -> {
			this.connectToSelected();
		}));
		this.addButton(new Button(this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct"), (p_214286_1_) -> {
			this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
			this.minecraft.displayGuiScreen(new ServerListScreen(this, this::func_214290_d, this.selectedServer));
		}));
		this.addButton(new Button(this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add"), (p_214288_1_) -> {
			this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
			this.minecraft.displayGuiScreen(new AddServerScreen(this, this::func_214284_c, this.selectedServer));
		}));
		this.btnEditServer = this.addButton(new Button(this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit"), (p_214283_1_) -> {
			ServerSelectionListScreen.Entry serverselectionlist$entry = this.serverListSelector.getSelected();
			if (serverselectionlist$entry instanceof ServerSelectionListScreen.NormalEntry) {
				ServerData serverdata = ((ServerSelectionListScreen.NormalEntry)serverselectionlist$entry).getServerData();
				this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
				this.selectedServer.copyFrom(serverdata);
				this.minecraft.displayGuiScreen(new AddServerScreen(this, this::func_214292_b, this.selectedServer));
			}

		}));
		this.btnDeleteServer = this.addButton(new Button(this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete"), (p_214294_1_) -> {
			ServerSelectionListScreen.Entry serverselectionlist$entry = this.serverListSelector.getSelected();
			if (serverselectionlist$entry instanceof ServerSelectionListScreen.NormalEntry) {
				String s = ((ServerSelectionListScreen.NormalEntry)serverselectionlist$entry).getServerData().serverName;
				if (s != null) {
					ITextComponent itextcomponent = new TranslationTextComponent("selectServer.deleteQuestion");
					ITextComponent itextcomponent1 = new TranslationTextComponent("selectServer.deleteWarning", s);
					String s1 = I18n.format("selectServer.deleteButton");
					String s2 = I18n.format("gui.cancel");
					this.minecraft.displayGuiScreen(new ConfirmScreen(this::func_214285_a, itextcomponent, itextcomponent1, s1, s2));
				}
			}

		}));
		this.addButton(new Button(this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh"), (p_214291_1_) -> {
			this.refreshServerList();
		}));
		this.addButton(new Button(this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel"), (p_214289_1_) -> {
			this.minecraft.displayGuiScreen(this.parentScreen);
		}));
		this.func_214295_b();
	}

	public void tick() {
		super.tick();
		if (this.lanServerList.getWasUpdated()) {
			List<LanServerInfo> list = this.lanServerList.getLanServers();
			this.lanServerList.setWasNotUpdated();
			this.serverListSelector.updateNetworkServers(list);
		}
		this.oldServerPinger.pingPendingNetworks();
	}

	public void removed() {
		this.minecraft.keyboardListener.enableRepeatEvents(false);
		if (this.lanServerDetector != null) {
			this.lanServerDetector.interrupt();
			this.lanServerDetector = null;
		}
		this.oldServerPinger.clearPendingNetworks();
	}

	private void refreshServerList() {
		this.minecraft.displayGuiScreen(new LupinMultiplayerMenuScreen(this.parentScreen));
	}

	private void func_214285_a(boolean p_214285_1_) {
		ServerSelectionListScreen.Entry serverselectionlist$entry = this.serverListSelector.getSelected();
		if (p_214285_1_ && serverselectionlist$entry instanceof ServerSelectionListScreen.NormalEntry) {
			this.savedServerList.func_217506_a(((ServerSelectionListScreen.NormalEntry)serverselectionlist$entry).getServerData());
			this.savedServerList.saveServerList();
			this.serverListSelector.setSelected((ServerSelectionListScreen.Entry)null);
			this.serverListSelector.updateOnlineServers(this.savedServerList);
		}

		this.minecraft.displayGuiScreen(this);
	}

	private void func_214292_b(boolean p_214292_1_) {
		ServerSelectionListScreen.Entry serverselectionlist$entry = this.serverListSelector.getSelected();
		if (p_214292_1_ && serverselectionlist$entry instanceof ServerSelectionListScreen.NormalEntry) {
			ServerData serverdata = ((ServerSelectionListScreen.NormalEntry)serverselectionlist$entry).getServerData();
			serverdata.serverName = this.selectedServer.serverName;
			serverdata.serverIP = this.selectedServer.serverIP;
			serverdata.copyFrom(this.selectedServer);
			this.savedServerList.saveServerList();
			this.serverListSelector.updateOnlineServers(this.savedServerList);
		}

		this.minecraft.displayGuiScreen(this);
	}

	private void func_214284_c(boolean p_214284_1_) {
		if (p_214284_1_) {
			this.savedServerList.addServerData(this.selectedServer);
			this.savedServerList.saveServerList();
			this.serverListSelector.setSelected((ServerSelectionListScreen.Entry)null);
			this.serverListSelector.updateOnlineServers(this.savedServerList);
		}

		this.minecraft.displayGuiScreen(this);
	}

	private void func_214290_d(boolean p_214290_1_) {
		if (p_214290_1_) {
			this.connectToServer(this.selectedServer);
		} else {
			this.minecraft.displayGuiScreen(this);
		}
	}

	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)) {
			return true;
		} else if (p_keyPressed_1_ == 294) {
			this.refreshServerList();
			return true;
		} else if (this.serverListSelector.getSelected() != null) {
			if (p_keyPressed_1_ != 257 && p_keyPressed_1_ != 335) {
				return this.serverListSelector.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
			} else {
				this.connectToSelected();
				return true;
			}
		} else {
			return false;
		}
	}

	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.hoveringText = null;
		this.renderBackground();
		this.serverListSelector.render(p_render_1_, p_render_2_, p_render_3_);
		this.drawCenteredString(this.font, this.title.getFormattedText(), this.width / 2, 20, 16777215);
		RenderUtil.drawRect(2, 2, 72, 22, 0, 0, 0, 0.9F);
		Lupin.instance.getFontManager().verdana.drawString("Alt login", 18, 6, Color.WHITE.getRGB(), -1);

		Color color;

		if (Lupin.mc.getSession().getToken().length() <= 32) {

			color = Color.RED;

		} else {

			color = Color.GREEN;
		}
		String loggedInAs = "Logged in as: " + Lupin.mc.getSession().getUsername();
		Lupin.instance.getFontManager().verdana.drawString(loggedInAs, 90, 6, color.getRGB(), -1);

		if (this.hoveringText != null) {
			this.renderTooltip(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), p_render_1_, p_render_2_);
		}
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}

	public void connectToSelected() {
		ServerSelectionListScreen.Entry serverselectionlist$entry = this.serverListSelector.getSelected();
		if (serverselectionlist$entry instanceof ServerSelectionListScreen.NormalEntry) {
			this.connectToServer(((ServerSelectionListScreen.NormalEntry)serverselectionlist$entry).getServerData());
		} else if (serverselectionlist$entry instanceof ServerSelectionListScreen.LanDetectedEntry) {
			LanServerInfo lanserverinfo = ((ServerSelectionListScreen.LanDetectedEntry)serverselectionlist$entry).getServerData();
			this.connectToServer(new ServerData(lanserverinfo.getServerMotd(), lanserverinfo.getServerIpPort(), true));
		}
	}

	private void connectToServer(ServerData server) {
		this.minecraft.displayGuiScreen(new ConnectingScreen(this, this.minecraft, server));
	}

	public void func_214287_a(ServerSelectionListScreen.Entry p_214287_1_) {
		this.serverListSelector.setSelected(p_214287_1_);
		this.func_214295_b();
	}

	public void func_214295_b() {
		this.btnSelectServer.active = false;
		this.btnEditServer.active = false;
		this.btnDeleteServer.active = false;
		ServerSelectionListScreen.Entry serverselectionlist$entry = this.serverListSelector.getSelected();
		if (serverselectionlist$entry != null && !(serverselectionlist$entry instanceof ServerSelectionListScreen.LanScanEntry)) {
			this.btnSelectServer.active = true;
			if (serverselectionlist$entry instanceof ServerSelectionListScreen.NormalEntry) {
				this.btnEditServer.active = true;
				this.btnDeleteServer.active = true;
			}
		}

	}

	public ServerPinger getOldServerPinger() {
		return this.oldServerPinger;
	}

	public void setHoveringText(String p_146793_1_) {
		this.hoveringText = p_146793_1_;
	}

	public ServerList getServerList() {
		return this.savedServerList;
	}

}