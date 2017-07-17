package fr.esiea.ferre.usefuel.UserActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import fr.esiea.ferre.usefuel.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by rob on 7/14/2017.
 */


public class LoadingScreenBookActivity extends AppCompatActivity {

    Button button_cancel;
    TextView text;
    ImageView logo_circle;
    AnimationSet animSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_book_screen);

        button_cancel = (Button)findViewById(R.id.button_cancel);
        text = (TextView)findViewById(R.id.text);

        // Set up of the Calligraphy dependencies, that allows us to use a custom font in a .xml layout file
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        displayAnim();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button_cancel.setVisibility(View.VISIBLE);
                text.setText(getString(R.string.really_no_deliverer));
            }
        },(5)*20000 + (6)*4000);


    }

    public void onCancel(View view){
        finish();
    }

    // Calligraphy dependencies required that
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void displayAnim(){
        // Animation to wait for deliverer
        logo_circle = (ImageView)findViewById(R.id.logo);
        button_cancel = (Button)findViewById(R.id.button_cancel);
        text = (TextView)findViewById(R.id.text);

        animSet = new AnimationSet(true);

        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(0.0f, 360.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(2000);
        animRotate.setFillAfter(true);
        animRotate.setRepeatCount(61);
        animSet.addAnimation(animRotate);
        logo_circle.startAnimation(animSet);

        for (int i=0; i<=4; i++){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button_cancel.setVisibility(View.VISIBLE);
                    text.setText(getString(R.string.no_deliverer));
                }
            }, (i+1)*20000 + i*4000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button_cancel.setVisibility(View.GONE);
                    text.setText(getString(R.string.wait_deliverer));
                }
            }, (i+1)*20000 + (i+1)*4000);
        }
    }


}
