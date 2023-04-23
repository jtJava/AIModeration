package me.iowa.perspective.perspective;

import au.com.origma.perspectiveapi.v1alpha1.PerspectiveAPI;
import au.com.origma.perspectiveapi.v1alpha1.models.*;
import club.minnced.discord.webhook.send.WebhookEmbed;
import com.google.common.collect.Maps;
import me.iowa.perspective.Perspective;
import me.iowa.perspective.config.MiscConfig;
import me.iowa.perspective.util.WebhookUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class PerspectiveListener implements Listener {
    private final MiscConfig config;
    private final PerspectiveAPI perspectiveAPI;
    private final Map<AttributeType, RequestedAttribute> attributeMap = new HashMap<>();

    public PerspectiveListener(Perspective perspective) {
        this.perspectiveAPI = PerspectiveAPI.create(perspective.getMiscConfig().getPerspectiveKey());
        this.config = perspective.getMiscConfig();

        attributeMap.put(AttributeType.TOXICITY, new RequestedAttribute.Builder().build());
        attributeMap.put(AttributeType.SEVERE_TOXICITY, new RequestedAttribute.Builder().build());
        attributeMap.put(AttributeType.IDENTITY_ATTACK, new RequestedAttribute.Builder().build());
        attributeMap.put(AttributeType.INSULT, new RequestedAttribute.Builder().build());
        attributeMap.put(AttributeType.THREAT, new RequestedAttribute.Builder().build());
        attributeMap.put(AttributeType.PROFANITY, new RequestedAttribute.Builder().build());
        attributeMap.put(AttributeType.SEXUALLY_EXPLICIT, new RequestedAttribute.Builder().build());
        attributeMap.put(AttributeType.FLIRTATION, new RequestedAttribute.Builder().build());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        AnalyzeCommentResponse response = perspectiveAPI.analyze(new AnalyzeCommentRequest.Builder()
                .requestedAttributes(attributeMap)
                .comment(new Entry.Builder()
                        .type(ContentType.PLAIN_TEXT)
                        .text(message)
                        .build())
                .build());

        if (response == null) {
            return;
        }

        Map<AttributeType, Float> sortedScores = new LinkedHashMap<>();
        // Sort by value (scores)
        response.getAttributeScores().entrySet()
                .stream()
                .map(e -> Maps.immutableEntry(e.getKey(), e.getValue().getSummaryScore().getValue()))
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> sortedScores.put(e.getKey(), e.getValue()));

        List<WebhookEmbed.EmbedField> embeds = new ArrayList<>();

        Optional<AttributeType> mostLikelyType = sortedScores.keySet().stream().findFirst();
        mostLikelyType.ifPresent(type -> {
            float score = sortedScores.get(type);

            if (score > this.config.getPerspectiveThreshold()) {
                embeds.add(new WebhookEmbed.EmbedField(false, "Most Likely Type", type + ": " + String.format("%.0f%%", score * 100)));
            }
        });

        sortedScores.forEach((attributeType, attributeScore) -> {
            if (attributeScore > this.config.getPerspectiveThreshold()) {
                embeds.add(new WebhookEmbed.EmbedField(false, attributeType.name().replace("_", " "), String.format("%.0f%%", attributeScore * 100)));
            }
        });

        if (embeds.isEmpty()) {
            return;
        }

        if (player.hasPermission("perspective.bypass")) {
            return;
        }

        event.setCancelled(true);

        player.sendMessage(this.config.getCancelMessage());
        WebhookUtil.sendPerspectiveWebhook("Perspective", message, player.getName(), embeds);
    }
}
