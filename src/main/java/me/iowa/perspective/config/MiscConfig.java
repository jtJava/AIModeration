package me.iowa.perspective.config;

import lombok.Getter;
import me.iowa.perspective.Perspective;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@Getter
public class MiscConfig {
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().character('&').hexColors().build();

    private final String webhookUrl;
    private final Component cancelMessage;

    private final boolean openAiEnabled;
    private final String openAiKey;
    private final double openAiThreshold;
    private final boolean openAiCancel;

    private final boolean perspectiveEnabled;
    private final String perspectiveKey;
    private final double perspectiveThreshold;
    private final boolean perspectiveCancel;

    public MiscConfig(Perspective perspective) {
        perspective.saveDefaultConfig();

        this.webhookUrl = perspective.getConfig().getString("webhook-url", "");
        this.cancelMessage = this.serializer.deserialize(perspective.getConfig().getString("cancel-message", ""));

        this.openAiEnabled = perspective.getConfig().getBoolean("openai.enabled", false);
        this.openAiKey = perspective.getConfig().getString("openai.api-key", "");
        this.openAiThreshold = perspective.getConfig().getDouble("openai.threshold", 0.5);
        this.openAiCancel = perspective.getConfig().getBoolean("openai.cancel", false);

        this.perspectiveEnabled = perspective.getConfig().getBoolean("perspective.enabled", false);
        this.perspectiveKey = perspective.getConfig().getString("perspective.api-key", "");
        this.perspectiveThreshold = perspective.getConfig().getDouble("perspective.threshold", 0.5);
        this.perspectiveCancel = perspective.getConfig().getBoolean("perspective.cancel", false);
    }
}
