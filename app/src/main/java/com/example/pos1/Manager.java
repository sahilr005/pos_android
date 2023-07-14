package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Manager extends AppCompatActivity {

    AppCompatButton categoryMaster,toppingMaster,uploadItems,UploadToppings,ItemMaster,SizeBase,CategorySizeSettings,SizeBaseSettingBtn,CategoryToppingSettings,comboMasterBtn,happyHoursBtn,toppingTradePrice,staffScreenBtn;
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
    }
}