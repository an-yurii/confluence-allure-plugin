package ru.yuriian;

public class AllureResponse {
    private String pageLink;
    private String shortDescription;

    public AllureResponse(String pageLink, String shortDescription) {
        this.pageLink = pageLink;
        this.shortDescription = shortDescription;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
