package com.gwabs.hausamusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutDev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element element = new Element();
        element.setTitle(" Phone");
        element.setValue("+2349030863146");
        element.setIconDrawable(R.drawable.ic_baseline_phone_24);
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = element.getValue();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
                startActivity(intent);
            }
        });

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription("Gwabstech solutions is a software development company based in Kaduna State Nigeria. \n We Deals with Development of Entertainment, Business Software feel free to contact us ")
                .addItem(element)
                .addEmail("Gwabstech@gmail.com","Email")
                .addInstagram("gwabstech")
                .addFacebook("Gwabstech5986")
                .create();
        setContentView(aboutPage);
    }
}