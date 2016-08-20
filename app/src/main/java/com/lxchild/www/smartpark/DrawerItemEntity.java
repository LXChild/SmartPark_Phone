package com.lxchild.www.smartpark;

/**
 * Created by LXChild on 2015/6/3.
 */
public class DrawerItemEntity {

    private int img_res;
    private String title;

    public int getImg() {
        return img_res;
    }

    public void setImg(int img_res) {
        switch (img_res) {
            case 0:
                this.img_res = R.mipmap.ic_launcher;
                break;
            case 1:
                this.img_res = R.mipmap.ic_launcher;
                break;
            case 2:
                this.img_res = R.mipmap.ic_launcher;
                break;
            case 3:
                this.img_res = R.mipmap.ic_launcher;
                break;
            case 4:
                this.img_res = R.mipmap.ic_launcher;
                break;
            case 5:
                this.img_res = R.mipmap.ic_launcher;
                break;
            default:
                break;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
