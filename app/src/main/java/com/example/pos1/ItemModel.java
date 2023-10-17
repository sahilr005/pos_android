package com.example.pos1;

public class ItemModel {
    private int tid;
    private String catId;
    private String code;
    private String name;
    private int status;
    private String pickupPrice;
    private String deliveryPrice;
    private String eatInPrice;
    private String costPrice;
    private String stock;
    private String minStock;
    private String itemImg;
    private String barCode;
    private String description;
    private String contain;
    private String extra;
    private String prices;
    private String createdAt;
    private int serverCheck;
    private String deactivationDate;
    private boolean isCombo;
    private String itemText;
    private String buttonBgColor;
    private String buttonFontColor;
    private int isDeleted;

    // Constructors
    public ItemModel() {
        // Default constructor
    }

    public ItemModel(int tid, String catId, String code, String name, int status, String pickupPrice, String deliveryPrice,
                String eatInPrice, String costPrice, String stock, String minStock, String itemImg, String barCode,
                String description, String contain, String extra, String prices, String createdAt, int serverCheck,
                String deactivationDate, boolean isCombo, String itemText, String buttonBgColor, String buttonFontColor,
                int isDeleted) {
        this.tid = tid;
        this.catId = catId;
        this.code = code;
        this.name = name;
        this.status = status;
        this.pickupPrice = pickupPrice;
        this.deliveryPrice = deliveryPrice;
        this.eatInPrice = eatInPrice;
        this.costPrice = costPrice;
        this.stock = stock;
        this.minStock = minStock;
        this.itemImg = itemImg;
        this.barCode = barCode;
        this.description = description;
        this.contain = contain;
        this.extra = extra;
        this.prices = prices;
        this.createdAt = createdAt;
        this.serverCheck = serverCheck;
        this.deactivationDate = deactivationDate;
        this.isCombo = isCombo;
        this.itemText = itemText;
        this.buttonBgColor = buttonBgColor;
        this.buttonFontColor = buttonFontColor;
        this.isDeleted = isDeleted;
    }

    // Getters and setters
    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPickupPrice() {
        return pickupPrice;
    }

    public void setPickupPrice(String pickupPrice) {
        this.pickupPrice = pickupPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getEatInPrice() {
        return eatInPrice;
    }

    public void setEatInPrice(String eatInPrice) {
        this.eatInPrice = eatInPrice;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMinStock() {
        return minStock;
    }

    public void setMinStock(String minStock) {
        this.minStock = minStock;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getContain() {
        return contain;
    }
    public void setContain(String contain) {
        this.contain = contain;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getServerCheck() {
        return serverCheck;
    }

    public void setServerCheck(int serverCheck) {
        this.serverCheck = serverCheck;
    }

    public String getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(String deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public boolean isCombo() {
        return isCombo;
    }

    public void setCombo(boolean combo) {
        isCombo = combo;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public String getButtonBgColor() {
        return buttonBgColor;
    }

    public void setButtonBgColor(String buttonBgColor) {
        this.buttonBgColor = buttonBgColor;
    }

    public String getButtonFontColor() {
        return buttonFontColor;
    }

    public void setButtonFontColor(String buttonFontColor) {
        this.buttonFontColor = buttonFontColor;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
