package coin.congobuy.com.object;

/**
 * Created by Andymub on 23/06/2018.
 */

public class MyItem {
    private int imageid;
    private String imageheading="";
    public MyItem(int id,String title)
    {
        imageid=id;
        imageheading=title;
    }
    public int getImageid() {
        return imageid;
    }
    public String getImageheading() {
        return imageheading;
    }
    public void setImageheading(String imageheading) {
        this.imageheading = imageheading;
    }
    public void setImageid(int imageid) {
        this.imageid = imageid;
    }
}