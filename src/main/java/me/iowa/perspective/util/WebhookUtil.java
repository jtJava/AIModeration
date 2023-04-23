package me.iowa.perspective.util;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import lombok.experimental.UtilityClass;
import me.iowa.perspective.Perspective;

import java.util.List;

@UtilityClass
public class WebhookUtil {
    private final WebhookEmbedBuilder BUILDER = new WebhookEmbedBuilder();

    public void sendPerspectiveWebhook(String api, String message, String player, List<WebhookEmbed.EmbedField> scores) {
        BUILDER.setTitle(new WebhookEmbed.EmbedTitle(api + " Moderation", null));
        BUILDER.setDescription("The message below was flagged by " + api + "'s moderation API.");
        BUILDER.addField(new WebhookEmbed.EmbedField(false, "Flagged by", player));
        BUILDER.addField(new WebhookEmbed.EmbedField(false, "Message", message));

        for (WebhookEmbed.EmbedField score : scores) {
            BUILDER.addField(score);
        }

        BUILDER.setColor(0x00a767);
        BUILDER.setThumbnailUrl("https://centraloregondaily.com/wp-content/uploads/2023/01/FEATURED-IMAGE-TEMPLATE-38-1.png");

        Perspective.getInstance().getWebhookClient().send(BUILDER.build());

        BUILDER.reset();
    }
}
