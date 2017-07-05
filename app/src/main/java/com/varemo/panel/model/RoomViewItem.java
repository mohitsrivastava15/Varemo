package com.varemo.panel.model;

/**
 * Created by mohit on 24/2/17.
 */
public class RoomViewItem {

    private String imgUrl;
    private String imgDescription;
    private String xmlContent;

    public RoomViewItem(String imgUrl, String imgDescription) {
        this.imgUrl = imgUrl;
        this.imgDescription = imgDescription;
        this.xmlContent = "activity_default_room";
    }

    public RoomViewItem(String imgUrl, String imgDescription, String xmlContent) {
        this.imgUrl = imgUrl;
        this.imgDescription = imgDescription;
        this.xmlContent = xmlContent;
    }

    public String getImgUrl() {return imgUrl;}
    public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}

    public String getImgDescription() {return imgDescription;}
    public void setImgDescription(String imgDescription) {this.imgDescription = imgDescription;}

    public String getXmlContent() {return xmlContent;}
    public void setXmlContent(String xmlContent) {this.xmlContent = xmlContent;}

}
