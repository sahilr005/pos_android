package com.example.pos1;

public class Category {
    private int catId;
    private String catName;
    private int is_active;
    private int is_deal;
    private int is_maintain_stock;
    private int server_check;
    private int allow_stock_qty;
    private int web_catid;
    private boolean is_combo;
    private String description;
    private String order_no;
    private String sztxt;
    private String bstxt;
    private String cotxt;
    private String extxt;
    private String anytxt;
    private String nbtxt;
    private String slug;

    public Category() {
        this.catId = catId;
        this.catName = catName;
        this.is_active = is_active;
        this.is_deal = is_deal;
        this.is_maintain_stock = is_maintain_stock;
        this.server_check = server_check;
        this.allow_stock_qty = allow_stock_qty;
        this.web_catid = web_catid;
        this.is_combo = is_combo;
        this.description = description;
        this.order_no = order_no;
        this.sztxt = sztxt;
        this.bstxt = bstxt;
        this.cotxt = cotxt;
        this.extxt = extxt;
        this.anytxt = anytxt;
        this.nbtxt = nbtxt;
        this.slug = slug;
        this.catnmtxt = catnmtxt;
        this.photo = photo;
        this.contains = contains;
        this.extras = extras;
    }

    private String catnmtxt;
    private String photo;

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_deal() {
        return is_deal;
    }

    public void setIs_deal(int is_deal) {
        this.is_deal = is_deal;
    }

    public int getIs_maintain_stock() {
        return is_maintain_stock;
    }

    public void setIs_maintain_stock(int is_maintain_stock) {
        this.is_maintain_stock = is_maintain_stock;
    }

    public int getServer_check() {
        return server_check;
    }

    public void setServer_check(int server_check) {
        this.server_check = server_check;
    }

    public int getAllow_stock_qty() {
        return allow_stock_qty;
    }

    public void setAllow_stock_qty(int allow_stock_qty) {
        this.allow_stock_qty = allow_stock_qty;
    }

    public int getWeb_catid() {
        return web_catid;
    }

    public void setWeb_catid(int web_catid) {
        this.web_catid = web_catid;
    }

    public boolean isIs_combo() {
        return is_combo;
    }

    public void setIs_combo(boolean is_combo) {
        this.is_combo = is_combo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getSztxt() {
        return sztxt;
    }

    public void setSztxt(String sztxt) {
        this.sztxt = sztxt;
    }

    public String getBstxt() {
        return bstxt;
    }

    public void setBstxt(String bstxt) {
        this.bstxt = bstxt;
    }

    public String getCotxt() {
        return cotxt;
    }

    public void setCotxt(String cotxt) {
        this.cotxt = cotxt;
    }

    public String getExtxt() {
        return extxt;
    }

    public void setExtxt(String extxt) {
        this.extxt = extxt;
    }

    public String getAnytxt() {
        return anytxt;
    }

    public void setAnytxt(String anytxt) {
        this.anytxt = anytxt;
    }

    public String getNbtxt() {
        return nbtxt;
    }

    public void setNbtxt(String nbtxt) {
        this.nbtxt = nbtxt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCatnmtxt() {
        return catnmtxt;
    }

    public void setCatnmtxt(String catnmtxt) {
        this.catnmtxt = catnmtxt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    private String contains;
    private String extras;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    // Add getters and setters for other columns
}
