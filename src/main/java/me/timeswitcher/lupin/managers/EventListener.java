package me.timeswitcher.lupin.managers;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.main.LupinUser;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mods.Aura;
import me.timeswitcher.lupin.mods.FancyChat;
import me.timeswitcher.lupin.mods.FastBreak;
import me.timeswitcher.lupin.mods.HighJump;
import me.timeswitcher.lupin.mods.InvMove;
import me.timeswitcher.lupin.mods.ItemESP;
import me.timeswitcher.lupin.mods.LivingESP;
import me.timeswitcher.lupin.mods.MassMSG;
import me.timeswitcher.lupin.mods.Nametags;
import me.timeswitcher.lupin.mods.NoScoreboard;
import me.timeswitcher.lupin.mods.NoSlowdown;
import me.timeswitcher.lupin.mods.Step;
import me.timeswitcher.lupin.mods.TPAura;
import me.timeswitcher.lupin.mods.VClip;
import me.timeswitcher.lupin.mods.Wallhax;
import me.timeswitcher.lupin.renderings.Cape;
import me.timeswitcher.lupin.renderings.Deadmau5;
import me.timeswitcher.lupin.screens.LupinClickGuiScreen;
import me.timeswitcher.lupin.screens.LupinMainMenuScreen;
import me.timeswitcher.lupin.screens.LupinMultiplayerMenuScreen;
import me.timeswitcher.lupin.utility.LupinUtil;
import me.timeswitcher.lupin.utility.EntityUtil;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.RenderUtil;
import me.timeswitcher.lupin.utility.ChatUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerPacket.PositionRotationPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.EnderChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TrappedChestTileEntity;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventListener {

	private Mod keybindMod = null;

	private boolean say = false;

	private static boolean fontsInitialized = false;

	@SubscribeEvent
	public void openGui(GuiOpenEvent e) {

		if (e.getGui() instanceof MainMenuScreen) {

			e.setCanceled(true);

			if (!fontsInitialized) {
				fontsInitialized = true;
				Lupin.instance.getFontManager().init();
			}

				if (LupinUtil.getMainMenu() == null) {
					LupinUtil.setMainMenu(new LupinMainMenuScreen());
				}
				if (LupinUtil.getMainMenu() != null) {
					Lupin.mc.displayGuiScreen(LupinUtil.getMainMenu());
				}
		}
		if (e.getGui() instanceof MultiplayerScreen) {
			e.setCanceled(true);
			Lupin.mc.displayGuiScreen(new LupinMultiplayerMenuScreen(null));
		}
	}

	@SubscribeEvent
	public void onGuiScreen(GuiScreenEvent e) {
		if (!(e.getGui() instanceof SettingsScreen)) {
			if (Lupin.mc.gameSettings.guiScale > 2 || Lupin.mc.gameSettings.guiScale == 0) {
				Lupin.mc.gameSettings.guiScale = LupinUtil.getGuiScale() != 0 ? LupinUtil.getGuiScale() : 2;
				Lupin.mc.updateWindowSize();
			}
		}
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent e) {

		if (!LupinUtil.isDidSetupRendererLayer()) {

			for (PlayerRenderer playerRenderer : Lupin.mc.getRenderManager().getSkinMap().values()) {
				playerRenderer.addLayer(new Cape(playerRenderer));
				playerRenderer.addLayer(new Deadmau5(playerRenderer));
			}
			LupinUtil.setDidSetupRendererLayer(true);
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
		if (Lupin.instance.getModHandler().getModByName("Aura").isToggled()) {
			Lupin.instance.getModHandler().getModByName("Aura").toggleMod();
		}
		if (Lupin.instance.getModHandler().getModByName("TP Aura").isToggled()) {
			Lupin.instance.getModHandler().getModByName("TP Aura").toggleMod();
		}
		if (Lupin.instance.getModHandler().getModByName("Chest Stealer").isToggled()) {
			Lupin.instance.getModHandler().getModByName("Chest Stealer").toggleMod();
		}
		if (Lupin.instance.getModHandler().getModByName("No Scoreboard").isToggled()) {
			NoScoreboard.lastScoreObjective = null;
		}
		if (LivingESP.addedGlow) {

			if (!LivingESP.glowingEntities.isEmpty()) {

				for (Entity glowingEntity: LivingESP.glowingEntities) {

					if (glowingEntity != null) {
						glowingEntity.setGlowing(false);
					}
				}
				LivingESP.glowingEntities.clear();
			}
			LivingESP.addedGlow = false;
		}
		Step.STEP_TIMER.reset();
	}

	@SubscribeEvent
	public void onEntity(EntityEvent e) {

		if (!GameUtil.isGameNull()) {

			if (e.getEntity() != null) {

				if (Lupin.instance.getModHandler().getModByName("Living ESP").isToggled() && Lupin.instance.getModHandler().getModByName("Living ESP").getCurrentMode().equals(LivingESP.GLOW)) {

					if (e.getEntity().isLiving()) {

						LivingEntity le = (LivingEntity)e.getEntity();

						if (le != null) {

							if (!le.getEntity().isGlowing()) {
								if (EntityUtil.isEntityValidESP(le)) {
									le.setGlowing(true);
									LivingESP.glowingEntities.add(le);
									LivingESP.addedGlow = true;
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPreRenderLiving(@SuppressWarnings("rawtypes") RenderLivingEvent.Pre e) {
		if (Lupin.instance.getModHandler().getModByName("Wallhax").isToggled()) {
			if (e.getEntity() != null) {
				if (!(e.getEntity() instanceof ClientPlayerEntity) && (e.getEntity() instanceof PlayerEntity && Wallhax.ONLY_PLAYERS.isChecked() || !Wallhax.ONLY_PLAYERS.isChecked())) {
					GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
					RenderSystem.enablePolygonOffset();
					RenderSystem.polygonOffset(1.0F, -1000000);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPostRenderLiving(@SuppressWarnings("rawtypes") RenderLivingEvent.Post e) {
		if (Lupin.instance.getModHandler().getModByName("Wallhax").isToggled()) {
			if (e.getEntity() != null) {
				if (!(e.getEntity() instanceof ClientPlayerEntity) && (e.getEntity() instanceof PlayerEntity && Wallhax.ONLY_PLAYERS.isChecked() || !Wallhax.ONLY_PLAYERS.isChecked())) {
					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					RenderSystem.polygonOffset(1.0F, 1000000);
					RenderSystem.disablePolygonOffset();
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderName(RenderNameplateEvent e) {

		if (e.getEntity() != null) {

			if (!e.getEntity().getName().getFormattedText().equals("")) {

				if (e.getOriginalContent().contains(LupinUser.SPECIALIGN)) {
					e.setContent("\u00A7a\u00A7l\u00A7k: " + "\u00A7f" + LupinUser.SPECIALIGN + " \u00A7a\u00A7l\u00A7k:");
				}

				if (Lupin.instance.getModHandler().getModByName("Item ESP").isToggled() && ItemESP.NAMETAG.isChecked() && e.getEntity() instanceof ItemEntity) {

					ItemEntity itemEntity = ((ItemEntity)e.getEntity());
					String text = itemEntity.getName().getFormattedText() + (itemEntity.getItem().isStackable() ? (" x" + itemEntity.getItem().getCount()) : "");

					RenderUtil.renderItemEntityNametag(e.getEntity(), text, e.getMatrixStack(), e.getRenderTypeBuffer(), 15728880);

					e.setResult(Result.DENY);

				} else if (Lupin.instance.getModHandler().getModByName("Nametags").isToggled()) {

					if (!GameUtil.isGameNull()) {

						if (e.getEntity() instanceof LivingEntity && (e.getEntity() instanceof PlayerEntity && Nametags.ONLY_PLAYERS.isChecked() || !Nametags.ONLY_PLAYERS.isChecked()) && !(e.getEntity() instanceof ClientPlayerEntity)) {

							if (Nametags.HEALTH.isChecked()) {

								LivingEntity le = (LivingEntity) e.getEntity();

								float entityHealth = le.getHealth();
								float maxHealth = le.getMaxHealth();

								String HPCOLOR = "";

								if (entityHealth == maxHealth) {

									HPCOLOR = "\u00A72";

								} else if (entityHealth >= 0.75f * maxHealth) {

									HPCOLOR = "\u00A72";

								} else if (entityHealth >= 0.6f * maxHealth && entityHealth < 0.75f * maxHealth) {

									HPCOLOR = "\u00A7a";

								} else if (entityHealth >= 0.3f * maxHealth && entityHealth < 0.6f * maxHealth) {

									HPCOLOR = "\u00A7e";

								} else if (entityHealth >= 0.2f * maxHealth && entityHealth < 0.3f * maxHealth) {

									HPCOLOR = "\u00A7c";

								} else {

									HPCOLOR = "\u00A74";
								}

								boolean addFriendTag = false;

								if (e.getEntity() instanceof PlayerEntity) {

									for (String friendName : LupinUtil.getFriends()) {

										if (e.getOriginalContent().toLowerCase().contains(friendName.toLowerCase())) {

											addFriendTag = true;
										}
									}
								}
								e.setContent(e.getOriginalContent() + HPCOLOR + " HP " + entityHealth + (addFriendTag ? " \u00A7f[\u00A79\u00A7lFRIEND\u00A7f]" : ""));
								if (e.getOriginalContent().contains(LupinUser.SPECIALIGN)) {
									e.setContent("\u00A7a\u00A7l\u00A7k: " + "\u00A7f" + LupinUser.SPECIALIGN + (addFriendTag ? " \u00A7f[\u00A79\u00A7lFRIEND\u00A7f]" : "") + " \u00A7a\u00A7l\u00A7k:");
								}
							}
							RenderUtil.renderEntityNametag(e.getEntity(), e.getContent(), e.getMatrixStack(), e.getRenderTypeBuffer(), 15728880);
						}
					}
					e.setResult(Result.DENY);
				}
				if (e.getEntity() instanceof PlayerEntity) {

					for (String friendName : LupinUtil.getFriends()) {

						if (e.getOriginalContent().toLowerCase().contains(friendName.toLowerCase())) {
							e.setContent(e.getOriginalContent() + " \u00A7f[\u00A79\u00A7lFRIEND\u00A7f]");
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent e) {
		if (!GameUtil.isGameNull()) {
			Lupin.instance.getModHandler().runEnabledMods();
		}
	}

	@SubscribeEvent
	public void onPlayer(PlayerEvent e) {

		if (!GameUtil.isGameNull()) {

			if (e.getEntity() == Lupin.mc.player) {

				if (Lupin.instance.getModHandler().getModByName("No Slowdown").isToggled()) {

					if (ModsUtil.canNoSlow()) {

						NoSlowdown.noSlowdown();
					}
				}
				if (Lupin.instance.getModHandler().getModByName("Inv Move").isToggled()) {

					if (ModsUtil.canInvMove()) {

						float speed = InvMove.sneak ? 0.3f : 1.0f;
						float noSpeed = 0.0f;

						Lupin.mc.player.movementInput.moveForward = InvMove.forward ? speed : noSpeed;
						Lupin.mc.player.movementInput.moveForward = InvMove.backward ? -speed : InvMove.forward ? speed : noSpeed;

						Lupin.mc.player.movementInput.moveStrafe = InvMove.left ? speed : noSpeed;
						Lupin.mc.player.movementInput.moveStrafe = InvMove.right ? -speed : InvMove.left ? speed : noSpeed;

						Lupin.mc.player.movementInput.jump = InvMove.jump ? true : false;
						Lupin.mc.player.movementInput.sneaking = InvMove.sneak ? true : false;

						if (InvMove.perspective) {

							int currentPerspective = Lupin.mc.gameSettings.thirdPersonView;

							if (currentPerspective == 0) {
								Lupin.mc.gameSettings.thirdPersonView = 1;
								InvMove.perspective = false;
								return;
							}
							if (currentPerspective == 1) {
								Lupin.mc.gameSettings.thirdPersonView = 2;
								InvMove.perspective = false;
								return;
							}
							if (currentPerspective == 2) {
								Lupin.mc.gameSettings.thirdPersonView = 0;
								InvMove.perspective = false;
								return;
							}
						}
						if (InvMove.sprint) {

							if (ModsUtil.canSprint()) {
								Lupin.mc.player.setSprinting(true);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onJump(LivingJumpEvent e) {

		if (Lupin.instance.getModHandler().getModByName("High Jump").isToggled() && !Lupin.instance.getModHandler().getModByName("Fly").isToggled()) {

			if (HighJump.GROUND_TIMER.isDelayComplete(100f)) {

				if (GameUtil.isKeyDown(Lupin.mc.gameSettings.keyBindJump)) {

					if (e.getEntity() instanceof ClientPlayerEntity) {

						if (Lupin.mc.player.isSprinting()) {
							Lupin.mc.player.setSprinting(false);
						}
						PlayerUtil.setMotion(0, 0, 0);

						ModsUtil.setTimerSpeed(2200f);

						double y = PlayerUtil.posY();

						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));

						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.41999998688698d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.00133597911214d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.16610926093821d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));

						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.41999998688698d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.00133597911214d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.16610926093821d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));

						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.41999998688698d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.00133597911214d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.16610926093821d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));

						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.41999998688698d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.00133597911214d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.16610926093821d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));

						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.41999998688698d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 6.00133597911214d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));
						PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 6.16610926093821d, PlayerUtil.posZ(), Lupin.mc.player.rotationYaw, Lupin.mc.player.rotationPitch, false));

						PlayerUtil.setPos(PlayerUtil.posX(), y + 7, PlayerUtil.posZ());

						HighJump.jumped = true;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onClientChat(ClientChatEvent e) {

		if (!GameUtil.isGameNull()) {

			if (e.getMessage() != null && e.getOriginalMessage() != null) {

				if (e.getMessage().startsWith("./")) {
					e.setCanceled(true);
					return;
				}

				if ((!e.getMessage().toLowerCase().startsWith(".say"))) {

					if (e.getMessage().startsWith(".")) {

						e.setCanceled(true);

						if (e.getMessage().toLowerCase().startsWith(".toggler")) {

							for (Mod mod : Lupin.instance.getModHandler().getAllMods()) {

								if (mod.isToggled()) {

									mod.toggleMod();
								}
							}

						} else if (e.getMessage().toLowerCase().startsWith(".targetinfo")) {

							EntityUtil.showTargetInfo.setChecked(EntityUtil.showTargetInfo.isChecked() ^ true);

						} else if (e.getMessage().toLowerCase().startsWith(".t ")) {

							boolean found = false;

							for (Mod mod : Lupin.instance.getModHandler().getAllMods()) {

								if (e.getMessage().equalsIgnoreCase(".t " + mod.getName())) {

									mod.toggleMod();
									found = true;

									ChatUtil.sendClientMessage("Toggled: " + mod.getName());
								}
							}
							if (found == false) {
								ChatUtil.sendClientMessage("Targeted Mod was not found! Syntax: .t <mod>");
							}

						} else if (e.getMessage().toLowerCase().startsWith(".help")) {

							final String row = "\u00A78-------------------------------------------------";

							ChatUtil.sendClientMessageNoPrefix(row);
							ChatUtil.sendClientMessage(".friend <name> - adds the name to friend list");
							ChatUtil.sendClientMessage(".friend list - show friends");
							ChatUtil.sendClientMessage(".friend clear - clears friend list");
							ChatUtil.sendClientMessage(".say <message> - send a chat, ignores dots");
							ChatUtil.sendClientMessage(".logo - toggle the logo");
							ChatUtil.sendClientMessage(".infohud - toggle the infohud");
							ChatUtil.sendClientMessage(".rendermods - toggle the active mod rendering");
							ChatUtil.sendClientMessage(".targetinfo - toggle the aura attack target info");
							ChatUtil.sendClientMessage(".toggler - Disable all mods");
							ChatUtil.sendClientMessage(".t <mod> - toggle a mod");
							ChatUtil.sendClientMessage(".bind <mod> - keybind a mod. Press any key afterwards");
							ChatUtil.sendClientMessage(".bind del <mod> - delete a keybind");
							ChatUtil.sendClientMessage(".bind clear - remove all keybinds");
							ChatUtil.sendClientMessage(".clearchat - remove everything from chat");
							ChatUtil.sendClientMessage(".massmsg <message> - sets message for MassMSG");
							ChatUtil.sendClientMessage(".massmsg clear - clear MassMSG recipient list");
							ChatUtil.sendClientMessage(".vclip <number> - teleport up and down");
							ChatUtil.sendClientMessage(".hclip <number> - teleport forward and backward");
							ChatUtil.sendClientMessageNoPrefix(row);

						} else if (e.getMessage().toLowerCase().startsWith(".clearchat")) {

							GameUtil.clearChat();

						} else if (e.getMessage().toLowerCase().startsWith(".logo")) {

							LupinUtil.setShowLogo(LupinUtil.isShowLogo() ^ true);

						} else if (e.getMessage().toLowerCase().startsWith(".infohud")) {

							LupinUtil.setShowInfoHud(LupinUtil.isShowInfoHud() ^ true);

						} else if (e.getMessage().toLowerCase().startsWith(".rendermods")) {

							LupinUtil.setRenderModList(LupinUtil.isRenderModList() ^ true);

						} else if (e.getMessage().toLowerCase().startsWith(".massmsg clear")) {

							MassMSG.recipients.clear();

							ChatUtil.sendClientMessage("MassMSG recipients cleared");

						} else if (e.getMessage().toLowerCase().startsWith(".massmsg ")) {

							String message = e.getMessage().replaceFirst(".massmsg ", "");

							MassMSG.message = message;

							ChatUtil.sendClientMessage("MassMSG message set to: " + message);

						} else if (e.getMessage().toLowerCase().startsWith(".friend list")) {

							String allFriends = "";

							for (String friend : LupinUtil.getFriends()) {

								allFriends += friend + ", ";
							}
							ChatUtil.sendClientMessage("Friends: " + allFriends);

						} else if (e.getMessage().toLowerCase().startsWith(".friend clear")) {

							LupinUtil.getFriends().clear();

							ChatUtil.sendClientMessage("Cleared the friend list");

						} else if (e.getMessage().toLowerCase().startsWith(".friend ")) {

							String name = e.getMessage().replaceFirst(".friend ", "").toLowerCase();

							if (!LupinUtil.getFriends().contains(name.toLowerCase())) {

								LupinUtil.getFriends().add(name);
								ChatUtil.sendClientMessage("Added " + name + " to friend list");

							} else {

								LupinUtil.getFriends().remove(name);
								ChatUtil.sendClientMessage("Removed " + name + " from friend list");
							}

						} else if (e.getMessage().toLowerCase().startsWith(".bind del")) {

							boolean found = false;

							for (Mod mod : Lupin.instance.getModHandler().getAllMods()) {

								if (e.getMessage().equalsIgnoreCase(".bind del " + mod.getName())) {

									if (mod.getToggleKey() != GLFW.GLFW_KEY_UNKNOWN) {

										mod.setToggleKey(GLFW.GLFW_KEY_UNKNOWN);

										ChatUtil.sendClientMessage("Deleted " + mod.getName() + " keybind");

										found = true;
									}
								}
							}
							if (found == false) {
								ChatUtil.sendClientMessage("Targeted Mod was not found! Syntax: .bind del <mod>");
							}

						} else if (e.getMessage().toLowerCase().startsWith(".bind clear")) {

							for (Mod mod : Lupin.instance.getModHandler().getAllMods()) {

								if (mod.getToggleKey() != GLFW.GLFW_KEY_UNKNOWN) {

									mod.setToggleKey(GLFW.GLFW_KEY_UNKNOWN);
								}
							}
							ChatUtil.sendClientMessage("Cleared all keybinds");

						} else if (e.getMessage().toLowerCase().startsWith(".bind ")) {

							boolean found = false;

							for (Mod mod : Lupin.instance.getModHandler().getAllMods()) {

								if (e.getMessage().contains(".bind " + mod.getName().toLowerCase())) {

									keybindMod = mod;
									found = true;
								}
							}
							if (found == false) {
								ChatUtil.sendClientMessage("Targeted Mod was not found! Syntax: .bind <mod>");
							}

						} else if (e.getMessage().toLowerCase().startsWith(".vclip ")) {

							String vclipString = e.getMessage().toLowerCase().replaceFirst(".vclip ", "");

							try {

								double vclipValue = Double.parseDouble(vclipString);

								PlayerUtil.setPos(PlayerUtil.posX(), PlayerUtil.posY() + vclipValue, PlayerUtil.posZ());

								double posY = ((int)(PlayerUtil.posY() * 10D)) / 10.0D;

								String yRound = "";

								if (posY != PlayerUtil.posY()) {
									yRound = "approximately ";
								}
								ChatUtil.sendClientMessage("vclipped to Y: " + yRound + posY);

							} catch (Exception exception) {

								ChatUtil.sendClientMessage("Invalid vclip input.");
							}

						} else if (e.getMessage().toLowerCase().startsWith(".hclip ")) { 

							String hclipString = e.getMessage().toLowerCase().replaceFirst(".hclip ", "");

							try {

								double hclipValue = Double.parseDouble(hclipString);

								double x = PlayerUtil.getHorizontalX(hclipValue);
								double y = PlayerUtil.posY();
								double z = PlayerUtil.getHorizontalZ(hclipValue);

								PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.getHorizontalX(hclipValue), PlayerUtil.posY(), PlayerUtil.getHorizontalZ(hclipValue), true));
								PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.getHorizontalX(hclipValue), -0, PlayerUtil.getHorizontalZ(hclipValue), true));
								PlayerUtil.setPos(x, y, z);

								double posX = ((int)(PlayerUtil.posX() * 10D)) / 10.0D;
								double posZ = ((int)(PlayerUtil.posZ() * 10D)) / 10.0D;

								String xRound = "";

								if (posX != PlayerUtil.posX()) {
									xRound = "approx ";
								}
								String zRound = "";

								if (posZ != PlayerUtil.posZ()) {
									zRound = "approx ";
								}
								ChatUtil.sendClientMessage("hclipped to X: " + xRound + posX + " Z: " + zRound + posZ);

							} catch (Exception exception) {

								ChatUtil.sendClientMessage("Invalid hclip input.");
							}
						}

					} else {

						if (Lupin.instance.getModHandler().getModByName("Fancy Chat").isToggled()) {

							String message = e.getOriginalMessage();

							if (message.startsWith("/")) {

								return;
							}
							String newMessage = FancyChat.convertStringFancyChat(message);
							e.setMessage(newMessage);
						}
					}
				} else {

					if (say) { 
						e.setMessage(e.getMessage().replaceFirst(".say ", ""));
						say = false;
					}
				}
		}
			}
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent e) {

		if (!GameUtil.isGameNull() && !GameUtil.isRenderNull()) {

			if (Lupin.instance.getModHandler().getModByName("TP Aura").isToggled() && TPAura.target != null) {

				if (e.getType() == ElementType.VIGNETTE) {

					e.setCanceled(true);
				}
			}
			if (Lupin.instance.getModHandler().getModByName("No Scoreboard").isToggled()) {
				if (Lupin.mc.world.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
					NoScoreboard.lastScoreObjective = Lupin.mc.world.getScoreboard().getObjectiveInDisplaySlot(1);
					Lupin.mc.world.getScoreboard().setObjectiveInDisplaySlot(1, (ScoreObjective)null);
				}
			}
			if (Lupin.instance.getModHandler().getModByName("Anti Blind").isToggled()) {

				if (e.getType() == ElementType.HELMET) {

					ItemStack itemstack = Lupin.mc.player.inventory.armorItemInSlot(3);

					if (Lupin.mc.gameSettings.thirdPersonView == 0 && !itemstack.isEmpty())
					{
						Item item = itemstack.getItem();
						if (item == Blocks.CARVED_PUMPKIN.asItem())
						{
							e.setCanceled(true);
						}
					}
				}
			}
			int width;
			int height;

			try {

				width = e.getWindow().getWidth() / Lupin.mc.gameSettings.guiScale;
				height = e.getWindow().getHeight() / Lupin.mc.gameSettings.guiScale;

			} catch (Exception exception) {

				width = e.getWindow().getWidth() / 4;
				height = e.getWindow().getHeight() / 4;
			}

			if (e.getType() == ElementType.HOTBAR) {

				if (keybindMod != null) {
					String bindText = "Press any key to bind " + keybindMod.getName();
					RenderUtil.drawRect(0, 0, e.getWindow().getWidth(), e.getWindow().getHeight(), 0f, 0f, 0f, 0.4f);
					Lupin.instance.getFontManager().verdana.drawStringWithShadow(bindText, width / 2 - (Lupin.mc.fontRenderer.getStringWidth(bindText) / 2), height / 2 - 30, Color.WHITE.getRGB(), -1);
				}
				if (!GameUtil.isChatOpen() && GameUtil.isFullScreen() && LupinUtil.isShowInfoHud()) {

					double posX = ((int)(PlayerUtil.posX() * 10D)) / 10.0D;
					double posY = ((int)(PlayerUtil.posY() * 10D)) / 10.0D;
					double posZ = ((int)(PlayerUtil.posZ() * 10D)) / 10.0D;

					String direction = Lupin.mc.player.getHorizontalFacing().getAxisDirection() == AxisDirection.POSITIVE ? "+" : "-";

					String info = "XYZ: \u00A77" + posX + ", " + posY + ", " + posZ + "\u00A78 - \u00A7f" + "Facing:\u00A77 " + Lupin.mc.player.getHorizontalFacing() + " \u00A7f[" + "\u00A77" + direction + Lupin.mc.player.getHorizontalFacing().getAxis() + "\u00A7f]\u00A78 - " + PlayerUtil.getMoveSpeedString();

					Lupin.instance.getFontManager().verdana.drawStringWithShadow(info, 2, height - 13, Color.WHITE.getRGB(), -1);

					if (!Lupin.mc.isSingleplayer()) {

						if (Lupin.mc.getCurrentServerData() != null) {

							String version = Lupin.mc.getCurrentServerData().gameVersion;
							Lupin.instance.getFontManager().verdana.drawStringWithShadow(version, (width - 4) - Lupin.instance.getFontManager().verdana.getStringWidth(version), height - 13, Color.LIGHT_GRAY.getRGB(), -1);
						}
					}
				}
				if (Lupin.instance.getModHandler().getModByName("Me").isToggled() && !GameUtil.isDebugInfo()) {

					int TwoDPlayerHeight;

					if (Lupin.mc.player.getName().getFormattedText().equalsIgnoreCase(LupinUser.SPECIALIGN)) {

						TwoDPlayerHeight = 140;
					} else {

						TwoDPlayerHeight = 130;
					}
					RenderUtil.drawEntityOnScreen(Lupin.mc.player, 20, TwoDPlayerHeight, 0, 0, false);
				}

				if (!GameUtil.isDebugInfo()) {

					if (LupinUtil.isShowLogo()) {
						RenderUtil.drawImage(LupinUtil.LUPINLOGO, -55, -65, 200, 200, 1.0f);
						Lupin.instance.getFontManager().verdana.drawStringWithShadow(Lupin.VERSION, 80, 23, Color.LIGHT_GRAY.getRGB(), -1);
					}
				}

			} else if (e.getType() == ElementType.TEXT) {

				if (LupinUtil.isRenderModList()) {

					int y;
					int modCounter = 0;
					int lastModLength = 0;

					if (Lupin.mc.player.getActivePotionEffects().isEmpty()) {

						y = 0;

					} else {

						boolean row1 = false;
						boolean row2 = false;

						for (EffectInstance effect : Lupin.mc.player.getActivePotionEffects()) {
							if (effect.isShowIcon() && effect.getPotion().getEffectType() == EffectType.BENEFICIAL) {
								row1 = true;
							}
							if (effect.isShowIcon() && (effect.getPotion().getEffectType() == EffectType.NEUTRAL || effect.getPotion().getEffectType() == EffectType.HARMFUL)) {
								row2 = true;
							}
						}
						y = row2 ? 55 : row1 ? 30 : 0;
					}
					int colorFadeCounter = 0;

					for (Mod mod : Lupin.instance.getModHandler().getEnabledMods()) {

						if (mod.getAnimation() != Lupin.mc.fontRenderer.getStringWidth(mod.getNameAndMode())) {
							mod.setAnimation(mod.getAnimation() + 1);
						}
						RenderUtil.drawRect((width - 4) - mod.getAnimation(), y, (width - 1) + mod.getAnimation(), y + 12, 0, 0, 0, 0.7F);

						int modListColor = RenderUtil.getRainbowLight(5000, 300 * colorFadeCounter);

						RenderUtil.drawRect(width - 1, y, width, y + 12, modListColor);
						RenderUtil.drawRect(width - mod.getAnimation() - 5, y, width - mod.getAnimation() - 4, y + 12, modListColor);
						if (lastModLength != 0) {
							RenderUtil.drawRect(width - lastModLength - 5, y - 1, width - mod.getAnimation() - 4, y, modListColor);
						} else {
							RenderUtil.drawRect(width - mod.getAnimation() - 5, y, width, y + 1, modListColor);
						}
						if (modCounter == Lupin.instance.getModHandler().getEnabledMods().size() - 1) {
							RenderUtil.drawRect(width - mod.getAnimation() - 5, y + 12, width + (width - mod.getAnimation() - 5), y + 13, modListColor);
						}
						Lupin.mc.fontRenderer.drawString(mod.getNameAndMode(), (width - 2) - mod.getAnimation(), y + 2, modListColor);

						y += 12;
						colorFadeCounter++;
						modCounter++;
						lastModLength = mod.getAnimation();
					}
				}

				if (VClip.up != 0) {
					double y = VClip.up;
					Lupin.instance.getFontManager().verdana.drawStringWithShadow("VClip position \u00A77" + y + "\u00A7f blocks up found. \u00A77Jump\u00A7f to vclip.", 2, 150, Color.WHITE.getRGB(), -1);
					RenderSystem.popMatrix();
					VClip.up = 0;
				}
				if (VClip.down != 0) {
					double y = VClip.down;
					Lupin.instance.getFontManager().verdana.drawStringWithShadow("VClip position \u00A77" + y + "\u00A7f blocks down found. \u00A77Sneak\u00A7f to vclip.", 2, 160, Color.WHITE.getRGB(), -1);
					VClip.down = 0;
				}
				if (Aura.target != null) {

					try {

						if (GameUtil.isFullScreen() && EntityUtil.showTargetInfo.isChecked()) {

							float entityHealth = Aura.target.getHealth();
							float maxHealth = Aura.target.getMaxHealth();

							float hRed = 0;
							float hGreen = 0;
							float length = 110;
							float maxLength = 110;

							if (entityHealth == maxHealth) {
								hRed = 0;
								hGreen = 255;
								length = maxLength;

							} else {

								hRed = 255 - (255 * (entityHealth / maxHealth));
								hGreen = 255 * (entityHealth / maxHealth);
								length = maxLength * (entityHealth / maxHealth);
							}
							hRed= hRed / 255.0F;
							hGreen= hGreen / 255.0F;

							RenderUtil.drawRect(width / 2 - 130, height / 2 + 60, width / 2 + 130, height / 2 + 160, 0, 0, 0, 0.2F);
							RenderUtil.drawRect(width / 2 - 120, height / 2 + 70, width / 2 - 5, height / 2 + 150, 0, 0, 0, 0.7F);

							RenderUtil.drawRect(width / 2 - 131, height / 2 + 60, width / 2 - 130, height / 2 + 160, 0, 0, 0, 0.7F);
							RenderUtil.drawRect(width / 2 - 131, height / 2 + 61, width / 2 + 130, height / 2 + 60, 0, 0, 0, 0.7F);
							RenderUtil.drawRect(width / 2 - 131, height / 2 + 160, width / 2 + 130, height / 2 + 161, 0, 0, 0, 0.7F);
							RenderUtil.drawRect(width / 2 + 131, height / 2 + 60, width / 2 + 130, height / 2 + 161, 0, 0, 0, 0.7F);

							Lupin.mc.fontRenderer.drawString(Aura.target.getDisplayName().getFormattedText(), width / 2 + 10, height / 2 + 70, Color.WHITE.getRGB());
							Lupin.mc.fontRenderer.drawString("\u00A7fHP \u00A7r" + Aura.target.getHealth() + "\u00A77/\u00A72" + Aura.target.getMaxHealth(), width / 2 + 10, height / 2 + 115, new Color(hRed, hGreen, 0).getRGB());
							Lupin.mc.fontRenderer.drawString("Distance \u00A77" + (int)(Lupin.mc.player.getDistance(Aura.target) * 100.0F) / 100.0F +"m", width / 2 + 10, height / 2 + 127, Color.WHITE.getRGB());

							RenderUtil.drawRect(width / 2 + 11, height / 2 + 94, width / 2 + 123, height / 2 + 106, 0, 0, 0, 1.0F);
							RenderUtil.drawRect(width / 2 + 12, height / 2 + 95, width / 2 + 12 + length, height / 2 + 105, hRed, hGreen, 0, 1.0F);

							RenderUtil.drawEntityOnScreen(Aura.target, -63, 145, width / 2, height / 2, false);
						}
					} catch (Exception exception) {

					}
				}
				if (TPAura.target != null) {

					try {

						if (GameUtil.isFullScreen() && EntityUtil.showTargetInfo.isChecked()) {

							float entityHealth = TPAura.target.getHealth();
							float maxHealth = TPAura.target.getMaxHealth();

							float hRed = 0;
							float hGreen = 0;
							float length = 110;
							float maxLength = 110;

							if (entityHealth == maxHealth) {
								hRed = 0;
								hGreen = 255;
								length = maxLength;

							} else {

								hRed = 255 - (255 * (entityHealth / maxHealth));
								hGreen = 255 * (entityHealth / maxHealth);
								length = maxLength * (entityHealth / maxHealth);
							}
							hRed= hRed / 255.0F;
							hGreen= hGreen / 255.0F;

							RenderUtil.drawRect(width / 2 - 130, height / 2 + 60, width / 2 + 130, height / 2 + 160, 0, 0, 0, 0.2F);
							RenderUtil.drawRect(width / 2 - 120, height / 2 + 70, width / 2 - 5, height / 2 + 150, 0, 0, 0, 0.7F);

							RenderUtil.drawRect(width / 2 - 131, height / 2 + 60, width / 2 - 130, height / 2 + 160, 0, 0, 0, 0.7F);
							RenderUtil.drawRect(width / 2 - 131, height / 2 + 61, width / 2 + 130, height / 2 + 60, 0, 0, 0, 0.7F);
							RenderUtil.drawRect(width / 2 - 131, height / 2 + 160, width / 2 + 130, height / 2 + 161, 0, 0, 0, 0.7F);
							RenderUtil.drawRect(width / 2 + 131, height / 2 + 60, width / 2 + 130, height / 2 + 161, 0, 0, 0, 0.7F);

							Lupin.mc.fontRenderer.drawString(TPAura.target.getDisplayName().getFormattedText(), width / 2 + 10, height / 2 + 70, Color.WHITE.getRGB());
							Lupin.mc.fontRenderer.drawString("\u00A7fHP \u00A7r" + TPAura.target.getHealth() + "\u00A77/\u00A72" + TPAura.target.getMaxHealth(), width / 2 + 10, height / 2 + 115, new Color(hRed, hGreen, 0).getRGB());
							Lupin.mc.fontRenderer.drawString("Distance \u00A77" + (int)(Lupin.mc.player.getDistance(TPAura.target) * 100.0F) / 100.0F +"m", width / 2 + 10, height / 2 + 127, Color.WHITE.getRGB());

							RenderUtil.drawRect(width / 2 + 11, height / 2 + 94, width / 2 + 123, height / 2 + 106, 0, 0, 0, 1.0F);
							RenderUtil.drawRect(width / 2 + 12, height / 2 + 95, width / 2 + 12 + length, height / 2 + 105, hRed, hGreen, 0, 1.0F);

							RenderUtil.drawEntityOnScreen(TPAura.target, -63, 145, width / 2, height / 2, true);
						}
					} catch (Exception exception) {

					}
				} 
			}
		}
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent e) {

		if (!GameUtil.isGameNull()) {

			try {

				int key = e.getKey();

				if (keybindMod != null) {

					if (e.getAction() == GLFW.GLFW_RELEASE) {

						if (key != GLFW.GLFW_KEY_ENTER) {
							keybindMod.setToggleKey(key);
							ChatUtil.sendClientMessage(keybindMod.getName() + " bound to " + GameUtil.getKeyName(e));
							keybindMod = null;
							return;
						}
					}
				}
				if (Lupin.mc.currentScreen == null) {

					if (e.getAction() == GLFW.GLFW_RELEASE) {

						if (key == LupinUtil.getCLICKGUIKEY()) {
							if (LupinUtil.getClickGui() == null) {
								LupinUtil.setClickGui(new LupinClickGuiScreen());
							}
							Lupin.mc.displayGuiScreen(LupinUtil.getClickGui());	
							return;
						}
						for (Mod mod : Lupin.instance.getModHandler().getAllMods()) {

							if (mod.getToggleKey() == key) {
								mod.toggleMod();
							}
						}
					}
				} else {

					if (!GameUtil.isChatOpen()) {

						say = false;

						if (Lupin.instance.getModHandler().getModByName("Inv Move").isToggled()) {

							if (e.getAction() == GLFW.GLFW_PRESS) {

								if (key == Lupin.mc.gameSettings.keyBindForward.getKey().getKeyCode()) {

									InvMove.forward = true;
								}
								if (key == Lupin.mc.gameSettings.keyBindBack.getKey().getKeyCode()) {

									InvMove.backward = true;
								}
								if (key == Lupin.mc.gameSettings.keyBindLeft.getKey().getKeyCode()) {

									InvMove.left = true;
								}
								if (key == Lupin.mc.gameSettings.keyBindRight.getKey().getKeyCode()) {

									InvMove.right = true;
								}
								if (key == Lupin.mc.gameSettings.keyBindJump.getKey().getKeyCode()) {

									InvMove.jump = true;
								}
								if (key == Lupin.mc.gameSettings.keyBindSneak.getKey().getKeyCode()) {

									InvMove.sneak = true;
								}
								if (key == Lupin.mc.gameSettings.keyBindSprint.getKey().getKeyCode()) {

									InvMove.sprint = true;
								}
								if (key == Lupin.mc.gameSettings.keyBindTogglePerspective.getKey().getKeyCode()) {

									InvMove.perspective = true;
								}
							}
							if (e.getAction() == GLFW.GLFW_RELEASE) {

								if (key == Lupin.mc.gameSettings.keyBindForward.getKey().getKeyCode()) {

									InvMove.forward = false;
								}
								if (key == Lupin.mc.gameSettings.keyBindBack.getKey().getKeyCode()) {

									InvMove.backward = false;
								}
								if (key == Lupin.mc.gameSettings.keyBindLeft.getKey().getKeyCode()) {

									InvMove.left = false;
								}
								if (key == Lupin.mc.gameSettings.keyBindRight.getKey().getKeyCode()) {

									InvMove.right = false;
								}
								if (key == Lupin.mc.gameSettings.keyBindJump.getKey().getKeyCode()) {

									InvMove.jump = false;
								}
								if (key == Lupin.mc.gameSettings.keyBindSneak.getKey().getKeyCode()) {

									InvMove.sneak = false;
								}
								if (key == Lupin.mc.gameSettings.keyBindSprint.getKey().getKeyCode()) {

									InvMove.sprint = false;
								}
								if (key == Lupin.mc.gameSettings.keyBindTogglePerspective.getKey().getKeyCode()) {

									InvMove.perspective = false;
								}
							}
						}
					} else {

						if (GameUtil.getKeyName(e).equals(".")) {
							say = true;
						}
					}
				}
			} catch (Exception ignored) {

			}
		}
	}

	@SubscribeEvent
	public void onBreakSpeed(BreakSpeed e) {
		if (Lupin.instance.getModHandler().getModByName("Fast Break").isToggled()) {

			if (e.getEntity() == Lupin.mc.player) {

				e.setNewSpeed(e.getOriginalSpeed() + FastBreak.speed);
			} 
		} else {

			if (e.getEntity() == Lupin.mc.player) {

				e.setNewSpeed(e.getOriginalSpeed());
			}
		}
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent e) {

		if (!GameUtil.isGameNull() && !GameUtil.isRenderNull()) {

			try {

				if (Lupin.instance.getModHandler().getModByName("Living ESP").isToggled()) {

					if (!Lupin.instance.getModHandler().getModByName("Living ESP").getCurrentMode().equals(LivingESP.GLOW)) {

						for (Entity entity : Lupin.mc.world.getAllEntities()) {

							if (entity != null) {

								if (entity instanceof LivingEntity) {

									if (EntityUtil.isEntityValidESP(entity)) {

										ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
										Vec3d view = Lupin.mc.gameRenderer.getActiveRenderInfo().getProjectedView();

										RenderSystem.pushMatrix();

										RenderSystem.rotatef(renderInfo.getPitch(), 1, 0, 0);
										RenderSystem.rotatef(renderInfo.getYaw() + 180, 0, 1, 0);
										RenderSystem.translated(-view.x, -view.y, -view.z);

										int color = Color.LIGHT_GRAY.getRGB();
										float f = (float) (color >> 16 & 255) / 255.0F;
										float f1 = (float) (color >> 8 & 255) / 255.0F;
										float f2 = (float) (color & 255) / 255.0F;

										if (Lupin.instance.getModHandler().getModByName("Living ESP").getCurrentMode().equals(LivingESP.LINES)) {
											RenderUtil.drawESP(entity.getRenderBoundingBox(), (LivingEntity)entity, f, f1, f2, 0.9f);
										} else if (Lupin.instance.getModHandler().getModByName("Living ESP").getCurrentMode().equals(LivingESP.BOX)) {
											RenderUtil.drawOutlinedBox(entity.getRenderBoundingBox(), f, f1, f2, 0.9f);
										}
										RenderSystem.enableTexture();
										RenderSystem.popMatrix();
									}
								}
							}
						}
					}

				}
				if (Lupin.instance.getModHandler().getModByName("Chest ESP").isToggled()) {

					if (Lupin.mc.world.loadedTileEntityList != null) {

						for (TileEntity tileEntity : Lupin.mc.world.loadedTileEntityList) {

							if (tileEntity != null) {

								if (tileEntity instanceof ChestTileEntity || tileEntity instanceof TrappedChestTileEntity || tileEntity instanceof EnderChestTileEntity) {

                                    ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
									Vec3d view = Lupin.mc.gameRenderer.getActiveRenderInfo().getProjectedView();

									RenderSystem.pushMatrix();

									RenderSystem.rotatef(renderInfo.getPitch(), 1, 0, 0);
									RenderSystem.rotatef(renderInfo.getYaw() + 180, 0, 1, 0);
									RenderSystem.translated(-view.x, -view.y, -view.z);

									BlockPos b = tileEntity.getPos();

									float red = 1.0f;
									float green = 1.0f;
									float blue = 1.0f;
									float alpha = 0.4f;

									if (tileEntity instanceof ChestTileEntity) {
										red = 1f;
										green = 0.6f;
										blue = 0f;
									}
									if (tileEntity instanceof TrappedChestTileEntity) {
										red = 0.8f;
										green = 0.2f;
										blue = 0f;
									}
									if (tileEntity instanceof EnderChestTileEntity) {
										red = 0f;
										green = 1f;
										blue = 1f;
									}
									RenderUtil.drawBox(b, red, green, blue, alpha);

									RenderSystem.enableTexture();
									RenderSystem.popMatrix();
								}
							}
						}
					}
                    Lupin.mc.world.getAllEntities();
                    for (Entity entity : Lupin.mc.world.getAllEntities()) {

                        if (entity != null) {

                            if (entity instanceof ChestMinecartEntity) {

                                ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
                                Vec3d view = Lupin.mc.gameRenderer.getActiveRenderInfo().getProjectedView();

                                RenderSystem.pushMatrix();

                                RenderSystem.rotatef(renderInfo.getPitch(), 1, 0, 0);
                                RenderSystem.rotatef(renderInfo.getYaw() + 180, 0, 1, 0);
                                RenderSystem.translated(-view.x, -view.y, -view.z);

                                BlockPos b = entity.getPosition();

                                float red = 1;
                                float green = 0.6f;
                                float blue = 0f;
                                float alpha = 0.4f;

                                RenderUtil.drawBox(b, red, green, blue, alpha);

                                RenderSystem.enableTexture();
                                RenderSystem.popMatrix();
                            }
                        }
                    }
                }
				if (Lupin.instance.getModHandler().getModByName("Item ESP").isToggled()) {

					for (Entity entity : Lupin.mc.world.getAllEntities()) {

						if (entity != null) {

							if (entity instanceof ItemEntity) {

								ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
								Vec3d view = Lupin.mc.gameRenderer.getActiveRenderInfo().getProjectedView();

								RenderSystem.pushMatrix();

								RenderSystem.rotatef(renderInfo.getPitch(), 1, 0, 0);
								RenderSystem.rotatef(renderInfo.getYaw() + 180, 0, 1, 0);
								RenderSystem.translated(-view.x, -view.y, -view.z);

								RenderUtil.drawOutlinedBox(entity.getBoundingBox(), 1.0f, 1.0f, 1.0f, 0.9f);

								RenderSystem.enableTexture();
								RenderSystem.popMatrix();
							}
						}
					}
				}
				if (Lupin.instance.getModHandler().getModByName("TP Aura").isToggled()) {

					if (TPAura.target != null && TPAura.lastTPPos != null) {

						ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
						Vec3d view = Lupin.mc.gameRenderer.getActiveRenderInfo().getProjectedView();

						RenderSystem.pushMatrix();

						RenderSystem.rotatef(renderInfo.getPitch(), 1, 0, 0);
						RenderSystem.rotatef(renderInfo.getYaw() + 180, 0, 1, 0);
						RenderSystem.translated(-view.x, -view.y, -view.z);

						float red = 1.0f;
						float green = 1.0f;
						float blue = 1.0f;
						float alpha = 0.4f;

						RenderUtil.drawBox(TPAura.lastTPPos, red, green, blue, alpha);
						RenderUtil.drawBox(TPAura.lastTPPos.up(), red, green, blue, alpha);

						RenderSystem.enableTexture();
						RenderSystem.popMatrix();
					}
				}
			} catch (Exception ignored) {

			}
		}
	}
}