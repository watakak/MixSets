package net.watakak;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MixSets implements ModInitializer {
	public static final String MOD_ID = "mixsets";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final MixSetsConfig config = new MixSetsConfig();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing MixSets Alpha-0.0.1");

		if (config.isGreetingsEnabled()) {
			ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
				for (String message : config.getGreetingMessages()) {
					MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(message));
				}
			});
		}
	}

	public static MixSetsConfig getConfig() {
		return config;
	}
}
