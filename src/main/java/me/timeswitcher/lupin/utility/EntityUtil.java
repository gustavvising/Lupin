package me.timeswitcher.lupin.utility;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mods.LivingESP;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class EntityUtil {

	public static CheckBox showTargetInfo = new CheckBox("ShowTargetInfo", true);
	public static CheckBox players = new CheckBox("Players", true);
	public static CheckBox monsters = new CheckBox("Monsters", true);
	public static CheckBox animals = new CheckBox("Animals", true);
	public static CheckBox tameables = new CheckBox("Tameables", false);
	public static CheckBox villagers = new CheckBox("Villagers", false);
	public static CheckBox teams = new CheckBox("Teams", false);
	public static CheckBox friends = new CheckBox("Friends", false);
	public static CheckBox invisibles = new CheckBox("Invisibles", false);
	public static CheckBox deads = new CheckBox("Deads", false);

	public static boolean shouldNotTarget(LivingEntity e) {	
		return (!players.isChecked() && e instanceof PlayerEntity) 
				|| (!monsters.isChecked() && e instanceof MonsterEntity)
				|| (!animals.isChecked() && e instanceof AnimalEntity) 
				|| (!tameables.isChecked() && e instanceof TameableEntity)
				|| (!villagers.isChecked() && e instanceof VillagerEntity)
				|| (!teams.isChecked() && e.isOnSameTeam(Lupin.mc.player))
				|| (!friends.isChecked() && LupinUtil.getFriends().contains(e.getName().getFormattedText()))
				|| (!invisibles.isChecked() && e.isInvisible())
				|| (!deads.isChecked() && !e.isLiving());
	}

	public static LivingEntity getEntityInRange(double range) {
		if (Lupin.mc.world == null || Lupin.mc.player == null) {
			return null;
		}

		double distance = range;
		LivingEntity tempEntity = null;

		for (Entity entity : Lupin.mc.world.getAllEntities()) {
			if (!(entity instanceof LivingEntity)) {
				continue;
			}

			if (entity == Lupin.mc.player) {
				continue;
			}

			if (!isEntityValid(entity)) {
				continue;
			}

			double curDistance = Lupin.mc.player.getDistance(entity);

			if (curDistance <= distance) {
				distance = curDistance;
				tempEntity = (LivingEntity) entity;
			}
		}

		return tempEntity;
	}

	public static float getDistance(double xStart, double yStart, double zStart, double xEnd, double yEnd, double zEnd) {
		float f = (float)(xStart - xEnd);
		float f1 = (float)(yStart - yEnd);
		float f2 = (float)(zStart - zEnd);
		return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
	}

	public static boolean isEntityValid(Entity entity) {
		if (entity == null || Lupin.mc.player == null) {
			return false;
		}

		if (!(entity instanceof LivingEntity)) {
			return false;
		}

		LivingEntity livingEntity = (LivingEntity) entity;

		return entity != Lupin.mc.player
				&& entity.isLiving()
				&& entity.isAlive()
				&& entity.canBeAttackedWithItem()
				&& entity.ticksExisted > 20
				&& !shouldNotTarget(livingEntity)
				&& Lupin.mc.player.canEntityBeSeen(entity);
	}

	public static boolean isEntityValidESP(Entity entity) {
		return entity.isLiving() && (!LivingESP.onlyPlayers.isChecked() || entity instanceof PlayerEntity) && !(entity instanceof ArmorStandEntity) && !(entity instanceof ClientPlayerEntity) && entity.isAlive();
	}

}