package me.timeswitcher.lupin.managers;

import java.lang.reflect.Field;
import java.util.UUID;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import me.timeswitcher.lupin.main.LupinUser;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Session;

public class AltManager {

	private UserAuthentication auth;

	public AltManager() {
		AuthenticationService authService = new YggdrasilAuthenticationService(Minecraft.getInstance().getProxy(), UUID.randomUUID().toString());
		setAuth(authService.createUserAuthentication(Agent.MINECRAFT));
		authService.createMinecraftSessionService();
	}

	public void login(String username, String password) {

		try {

			getAuth().logOut();

			getAuth().setUsername((username));
			getAuth().setPassword((password));

			getAuth().logIn();

			Session session = new Session(getAuth().getSelectedProfile().getName(), getAuth().getSelectedProfile().getId().toString(), getAuth().getAuthenticatedToken(), "mojang");

			setSession(session);

		} catch (Exception e) {

		}
	}

	public void loginOffline(String username) {

		try {

			getAuth().logOut();

			if (username.equalsIgnoreCase(LupinUser.SPECIALIGN)) {

				username = "username";
			}
			Session session = new Session(username, PlayerEntity.getOfflineUUID(username).toString(), "-", "legacy");

			setSession(session);

		} catch (Exception e) {

		}
	}

	public void setSession(Session s) throws Exception {

		Class<? extends Minecraft> mc = Minecraft.getInstance().getClass();

		try {

			Field session = null;

			for (Field f : mc.getDeclaredFields()) {

				if (f.getType().isInstance(s)) {

					session = f;
				}
			}

			if (session == null) {
				throw new IllegalStateException("No field of type " + Session.class.getCanonicalName() + " declared.");
			}
			session.setAccessible(true);
			session.set(Minecraft.getInstance(), s);
			session.setAccessible(false);

		} catch (Exception e) {

		}
	}

	public UserAuthentication getAuth() {
		return auth;
	}

	public void setAuth(UserAuthentication auth) {
		this.auth = auth;
	}
}