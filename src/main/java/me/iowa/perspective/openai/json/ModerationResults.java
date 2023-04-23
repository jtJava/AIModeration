package me.iowa.perspective.openai.json;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class ModerationResults {
    private ModerationCategories categories;
    @SerializedName(value = "categoryscores", alternate = {"category_scores"})
    private ModerationCategoryScores categoryScores;
    private boolean flagged;
}
