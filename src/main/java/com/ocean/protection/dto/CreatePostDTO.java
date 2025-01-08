package com.ocean.protection.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CreatePostDTO {
    private String title;
    private String content;
    private String tag;
    private List<String> images;
} 