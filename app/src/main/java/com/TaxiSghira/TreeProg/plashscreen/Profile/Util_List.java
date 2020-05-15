package com.TaxiSghira.TreeProg.plashscreen.Profile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.TaxiSghira.TreeProg.plashscreen.API.FireBaseClient;
import com.TaxiSghira.TreeProg.plashscreen.Both.Auth;
import com.TaxiSghira.TreeProg.plashscreen.Both.PersonalInfo;
import com.TaxiSghira.TreeProg.plashscreen.Client.FavorDrivers;
import com.TaxiSghira.TreeProg.plashscreen.Client.Map;
import com.TaxiSghira.TreeProg.plashscreen.R;
import com.bumptech.glide.Glide;

public class Util_List extends AppCompatActivity {

    TextView textViewName;
    ImageView circleImageViewClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util__list);
        circleImageViewClient = findViewById(R.id.circleImageViewClient);
        textViewName = findViewById(R.id.textViewName);

        findViewById(R.id.imageView2back).setOnClickListener(v-> super.onBackPressed());
        findViewById(R.id.button3PN).setOnClickListener(v->startActivity(new Intent(getApplicationContext(), PersonalInfo.class)));
        findViewById(R.id.button6Stting).setOnClickListener(v->startActivity(new Intent(getApplicationContext(), ActivitySetting.class)));
        findViewById(R.id.buttonmap).setOnClickListener(v->startActivity(new Intent(getApplicationContext(), Map.class)));
        findViewById(R.id.button4TopDriver).setOnClickListener(v->startActivity(new Intent(getApplicationContext(), FavorDrivers.class)));
        findViewById(R.id.button8LogOut).setOnClickListener(v -> {
            FireBaseClient.getFireBaseClient().getmGoogleSignInClient()
                    .signOut()
                    .addOnCompleteListener(task ->
                            Toast.makeText(getApplicationContext(), "لقد تم تسجيل خروجك من التطبيق", Toast.LENGTH_SHORT).show());
            startActivity(new Intent(getApplicationContext(), Auth.class));
        });
        findViewById(R.id.button7RuDriver).setOnClickListener(v->{
            try {
                //  getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
            } catch (ActivityNotFoundException e) {
                //   getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("" + getApplicationContext().getPackageName())));
            }});

        try {
            textViewName.setText(FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getDisplayName());
            Glide.with(getApplicationContext()).load(FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getPhotoUrl()).centerCrop().into(circleImageViewClient);
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() { }
}
