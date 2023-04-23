package me.iowa.perspective.config;

import lombok.Getter;
import me.iowa.perspective.Perspective;
import net.kyori.adventure.text.Component;

@Getter
public class MiscConfig {

    private final String webhookUrl;
    private final Component cancelMessage;

    private final boolean openAiEnabled;
    private final String openAiKey;
    private final double openAiThreshold;

    private final boolean perspectiveEnabled;
    private final String perspectiveKey;
    private final double perspectiveThreshold;

    public MiscConfig(Perspective perspective) {
        perspective.saveDefaultConfig();

        this.webhookUrl = perspective.getConfig().getString("webhook-url", "");
        this.cancelMessage = Component.text(perspective.getConfig().getString("cancel-message", ""));

        this.openAiEnabled = perspective.getConfig().getBoolean("openai.enabled", false);
        this.openAiKey = perspective.getConfig().getString("openai.api-key", "");
        this.openAiThreshold = perspective.getConfig().getDouble("openai.threshold", 0.5);

        this.perspectiveEnabled = perspective.getConfig().getBoolean("perspective.enabled", false);
        this.perspectiveKey = perspective.getConfig().getString("perspective.api-key", "");
        this.perspectiveThreshold = perspective.getConfig().getDouble("perspective.threshold", 0.5);
    }
}
