package me.iowa.perspective.openai.json;

import lombok.Getter;

@Getter
public class ModerationResponse {
    private String id;
    private String model;
    private ModerationResults[] results;
}