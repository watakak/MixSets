package net.watakak;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.client.gui.DrawContext;

public class MixSets implements ModInitializer {
	public static final String MOD_ID = "mixsets";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final MixSetsConfig config = new MixSetsConfig();
	private static boolean hasDisplayedGreeting = false;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing MixSets 0.0.56");

		// Greetings - отображаем только один раз за сессию
		if (config.isGreetingsEnabled()) {
			ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
				if (!hasDisplayedGreeting) { // Проверка внутри обработчика
					for (String message : config.getGreetingMessages()) {
						MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(message));
					}
					hasDisplayedGreeting = true; // Устанавливаем флаг после первого отображения
				}
			});
		}

		// Отображение FPS
		if (config.isFpsDisplayEnabled()) {
			HudRenderCallback.EVENT.register(this::renderFps);
		}
	}

	private void renderFps(DrawContext context, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();

		// Проверка настройки OnlyInWorld
		if (client != null) {
			// Проверка, что F3 (режим отладки) не активен
			if (!client.options.debugEnabled) {
				int fps = client.getCurrentFps();
				String fpsText = "FPS: " + fps;

				// Установка позиции отображения
				int x = "right".equals(config.getFpsPosition())
						? client.getWindow().getScaledWidth() - client.textRenderer.getWidth(fpsText) - 6
						: 6;
				int y = 6; // Можно изменить на другой отступ по вертикали

				context.drawTextWithShadow(client.textRenderer, fpsText, x, y, 0xFFFFFF);
			}
		}
	}


	public static MixSetsConfig getConfig() {
		return config;
	}
}
