package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;

public class Manager extends AppCompatActivity {

    AppCompatButton categoryMaster,toppingMaster,uploadItems,UploadToppings,ItemMaster,SizeBase,CategorySizeSettings,SizeBaseSettingBtn,CategoryToppingSettings,comboMasterBtn,happyHoursBtn,toppingTradePrice,staffScreenBtn,securitySettingsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        categoryMaster = findViewById(R.id.categoryMasterBtn);
        toppingMaster = findViewById(R.id.toppingMasterBtn);
        uploadItems = findViewById(R.id.uploadItems);
        UploadToppings = findViewById(R.id.UploadToppingsBtn);
        ItemMaster = findViewById(R.id.ItemMasterBtn);
        SizeBase = findViewById(R.id.SizeBaseBtn);
        CategorySizeSettings = findViewById(R.id.categorySizeSettingBtn);
        SizeBaseSettingBtn = findViewById(R.id.SizeBaseSettingBtn);
        CategoryToppingSettings = findViewById(R.id.CategoryToppingSettings);
        comboMasterBtn = findViewById(R.id.comboMasterBtn);
        happyHoursBtn = findViewById(R.id.happyHoursBtn);
        toppingTradePrice = findViewById(R.id.ToppingTradePriceBtn);
        staffScreenBtn = findViewById(R.id.staffScreenBtn);
        securitySettingsBtn = findViewById(R.id.securitySettingsBtn);

        modifyButtonDrawable(categoryMaster, R.color.cold, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(ItemMaster, R.color.deep_sky_blue, R.drawable.ic_baseline_notes_24);
        modifyButtonDrawable(CategorySizeSettings, R.color.teal_200, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(SizeBase, R.color.cold, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(SizeBaseSettingBtn, R.color.deep_sky_blue, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(CategoryToppingSettings, R.color.deep_pink, R.drawable.ic_baseline_category_24);

        modifyButtonDrawable(toppingMaster, R.color.cold, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(comboMasterBtn, R.color.deep_sky_blue, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(happyHoursBtn, R.color.deep_pink, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(staffScreenBtn, R.color.deep_sky_blue, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(securitySettingsBtn, R.color.cold, R.drawable.ic_baseline_category_24);
        modifyButtonDrawable(toppingTradePrice, R.color.cold, R.drawable.ic_baseline_category_24);

        categoryMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,CategoryMaster.class);
                startActivity(intent);

            }
        });
        toppingMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,ToppingMaster.class);
                startActivity(intent);
            }
        });

        uploadItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,UploadItems.class);
                startActivity(intent);
            }
        });
        UploadToppings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,UploadToppings.class);
                startActivity(intent);
            }
        });
        ItemMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,ItemMaster.class);
                startActivity(intent);
            }
        });
        SizeBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,SizeBase.class);
                startActivity(intent);
            }
        });
        CategorySizeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,CategorySizeSettings.class);
                startActivity(intent);
            }
        });
        SizeBaseSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,SizeBaseSettings.class);
                startActivity(intent);
            }
        });
        CategoryToppingSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,CategoryToppingSettings.class);
                startActivity(intent);
            }
        });
        comboMasterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,ComboMaster.class);
                startActivity(intent);
            }
        });
        happyHoursBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,HappyHours.class);
                startActivity(intent);
            }
        });
        toppingTradePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,ToppingTradePrice.class);
                startActivity(intent);
            }
        });
        staffScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,StaffScreen.class);
                startActivity(intent);
            }
        });
        securitySettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manager.this,SecuritySettings.class);
                startActivity(intent);
            }
        });
    }

    private void modifyButtonDrawable(AppCompatButton button, int newColor, int newIconResource) {
        Drawable[] drawables = button.getCompoundDrawables();
        Drawable rightDrawable = drawables[2]; // Assuming the drawableRight is at index 2
        Drawable newIcon = getResources().getDrawable(newIconResource);

        if (rightDrawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) rightDrawable;
            GradientDrawable circleBackground = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.background);

            // Change color here
            circleBackground.setColor(ContextCompat.getColor(this, newColor));

            // Set the new icon with the modified LayerDrawable
            layerDrawable.setDrawableByLayerId(R.id.icon, newIcon);

            // Set the modified LayerDrawable back to the button
            button.setCompoundDrawablesWithIntrinsicBounds(null, null, layerDrawable, null);
        }
    }
}