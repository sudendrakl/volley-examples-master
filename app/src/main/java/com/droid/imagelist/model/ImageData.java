package com.droid.imagelist.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Table(name = "ImageData")
public class ImageData extends Model{

    @Column(name = "albumId")
    @SerializedName("albumId")
    String albumId;

    /*@SerializedName("id")
    String id;
*/
    @Column(name = "title")
    @SerializedName("title")
    String title;

    @Column(name = "url")
    @SerializedName("url")
    String url;

    @Column(name = "thumbnailUrl")
    @SerializedName("thumbnailUrl")
    String thumbnailUrl;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*public String getImageId() {
        return id;
    }

    public void setImageId(String id) {
        this.id = id;
    }
*/
    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public static List<ImageData> getAll() {
        return new Select()
                .from(ImageData.class)
                .execute();
    }
}
