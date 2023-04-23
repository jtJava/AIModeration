package me.iowa.perspective;

import club.minnced.discord.webhook.WebhookClient;
import lombok.Getter;
import me.iowa.perspective.config.MiscConfig;
import me.iowa.perspective.openai.OpenAIListener;
import me.iowa.perspective.perspective.PerspectiveListener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Perspective extends JavaPlugin {

    @Getter
    private static Perspective instance;

    private WebhookClient webhookClient;
    private MiscConfig miscConfig;

    @Override
    public void onEnable() {
        instance = this;

        this.miscConfig = new MiscConfig(this);

        this.webhookClient = WebhookClient.withUrl(this.miscConfig.getWebhookUrl());

        if (miscConfig.isPerspectiveEnabled()) {
            this.getServer().getPluginManager().registerEvents(new PerspectiveListener(this), this);
        }

        if (miscConfig.isOpenAiEnabled()) {
            this.getServer().getPluginManager().registerEvents(new OpenAIListener(this), this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
