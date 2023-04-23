package me.iowa.perspective.openai.json;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class ModerationCategoryScores {
    private double hate;
    @SerializedName(value = "hateOrthreatening", alternate = {"hate/threatening"})
    private double hateOrThreatening;
    @SerializedName(value = "selfharm", alternate = {"self-harm"})
    private double selfHarm;
    private double sexual;
    @SerializedName(value = "sexualOrminors", alternate = {"sexual/minors"})
    private double sexualOrMinors;
    private double violence;
    @SerializedName(value = "violenceOrgraphic", alternate = {"violence/graphic"})
    private double violenceOrGraphic;
}
