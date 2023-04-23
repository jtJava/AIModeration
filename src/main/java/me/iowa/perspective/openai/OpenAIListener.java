package me.iowa.perspective.openai;

import club.minnced.discord.webhook.send.WebhookEmbed;
import me.iowa.perspective.Perspective;
import me.iowa.perspective.config.MiscConfig;
import me.iowa.perspective.openai.json.ModerationResponse;
import me.iowa.perspective.openai.json.ModerationResults;
import me.iowa.perspective.util.WebhookUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAIListener implements Listener {
    private final MiscConfig config;

    public OpenAIListener(Perspective perspective) {
        this.config = perspective.getMiscConfig();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        ModerationResponse response = new ModerationRequest(message).send();
        if (response == null || response.getResults().length == 0) {
            return;
        }

        ModerationResults result = response.getResults()[0];
        if (result.isFlagged()) {
            Map<String, Double> scores = new HashMap<>();
            scores.put("Sexual", result.getCategoryScores().getSexual());
            scores.put("Hate", result.getCategoryScores().getHate());
            scores.put("Violence", result.getCategoryScores().getViolence());
            scores.put("Self Harm", result.getCategoryScores().getSelfHarm());
            scores.put("Sexual/Minors", result.getCategoryScores().getSexualOrMinors());
            scores.put("Hate/Threatening", result.getCategoryScores().getHateOrThreatening());
            scores.put("Violence/Graphic", result.getCategoryScores().getViolenceOrGraphic());

            List<WebhookEmbed.EmbedField> embeds = new ArrayList<>();
            scores.entrySet().stream().filter(entry -> entry.getValue() > this.config.getOpenAiThreshold()).forEach(entry -> {
                embeds.add(new WebhookEmbed.EmbedField(true, entry.getKey(), String.format("%.0f%%", entry.getValue() * 100)));
            });

            if (embeds.isEmpty()) {
                return;
            }

            if (player.hasPermission("openai.bypass")) {
                return;
            }

            event.setCancelled(true);

            player.sendMessage(this.config.getCancelMessage());
            WebhookUtil.sendPerspectiveWebhook("OpenAI", message, player.getName(), embeds);

        }
    }
}
