package kware.apps.asp.contents.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HomeData {

    private Long uid;
    private List<String> category;
    private String title;
    private String description;
    private String sources;
    private List<String> dataType;
    private List<String> tags;
    private String date;
    private String image;
    private String url;
}
