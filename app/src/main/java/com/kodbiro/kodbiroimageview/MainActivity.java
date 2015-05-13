package com.kodbiro.kodbiroimageview;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {

    private KodbiroImageView kodbiroImageView;
    private KodbiroImageView kodbiroImageView2;
    private KodbiroImageView kodbiroImageView3;
    private KodbiroImageView kodbiroImageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kodbiroImageView = (KodbiroImageView) findViewById(R.id.kodbiroImageView);
        kodbiroImageView2 = (KodbiroImageView) findViewById(R.id.kodbiroImageView2);
        kodbiroImageView3 = (KodbiroImageView) findViewById(R.id.kodbiroImageView3);
        kodbiroImageView4 = (KodbiroImageView) findViewById(R.id.kodbiroImageView4);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    //builden pattern
    @Override
    protected void onResume() {
        super.onResume();
        kodbiroImageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ronaldo));
        kodbiroImageView.showShadow(false);
        kodbiroImageView.setCircularImageView(true);
        kodbiroImageView.showBorder(true);
        kodbiroImageView.setBorderWidth(10);
        kodbiroImageView.setBorderTextures(BitmapFactory.decodeResource(getResources(), R.drawable.ronaldo));
        kodbiroImageView.setShadowColor(Color.CYAN);

        kodbiroImageView3.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
        kodbiroImageView3.showShadow(true);
        kodbiroImageView3.setCircularImageView(true);
        kodbiroImageView3.setShadowColor(Color.RED);

        kodbiroImageView4.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.test));
        kodbiroImageView4.setCircularImageView(true);

        kodbiroImageView2.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ronaldo));
        kodbiroImageView2.setBorderWidth(4);
        kodbiroImageView2.showBorder(true);
        kodbiroImageView2.showShadow(true);
    }
}
