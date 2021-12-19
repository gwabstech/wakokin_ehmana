package com.gwabs.hausamusicplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutAlamin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element element = new Element();
        element.setTitle("Contact on Phone");
        element.setValue("+2347068131818");
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
                .setImage(R.drawable.playerimg)
                .setDescription("AL-AMIN is a Hausa musician based in Kaduna State Nigeria")
                .addItem(element)
                .addEmail("ehmana@gmail.com")
                .addTwitter("al_ehmana")
                .addInstagram("al_amin_ehmana")
                .create();
        setContentView(aboutPage);
        /*
        setContentView(new AboutPage(this).isRTL(false).enableDarkMode(false).setImage(C0766R.C0768drawable.playerimg).setDescription("AL-AMIN is a Hausa musician based in Kaduna State Nigeria").addItem(new Element().setTitle("Version 1")).addGroup("Contact Us").addItem(element).addItem(element2).addEmail("ehmana@gmail.com").addTwitter("al_ehmana").addInstagram("al_amin_ehmana").create());


         */
    }
}