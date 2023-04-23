package me.iowa.perspective.openai.json;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class ModerationCategories {
    private boolean hate;
    @SerializedName(value = "hateOrthreateaning", alternate = {"hate/threatening"})
    private boolean hateOrThreatening;
    @SerializedName(value = "selfharm", alternate = {"self-harm"})
    private boolean selfHarm;
    private boolean sexual;
    @SerializedName(value = "sexualOrminors", alternate = {"sexual/minors"})
    private boolean sexualOrMinors;
    private boolean violence;
    @SerializedName(value = "violenceOrgraphic", alternate = {"violence/graphic"})
    private boolean violenceOrGraphic;
}
