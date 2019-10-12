package com.example.thirukural;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mayuonline.tamilandroidunicodeutil.TamilUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    Button search,copy,share,gmail,strava;

    LinearLayout layoutMaster;
    TextView paal,kural,transliteration,meaning,amma,urai,date,riD,ruD,rrT;
    EditText kNo;

    ProgressBar pb;

    GetDataService service = null;

    String[] explanationArray;

    float pbX=0, pbY=0, rideDistance = 0, runDistance=0, rrTime=0;

    Response<com.example.thirukural.kural> res;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        search= (Button)findViewById(R.id.search);
        copy= (Button)findViewById(R.id.copy);
        share= (Button)findViewById(R.id.share);
        gmail= (Button)findViewById(R.id.gmail);
        strava= (Button)findViewById(R.id.strava);

        pb= (ProgressBar) findViewById(R.id.progressBar);

        paal= (TextView) findViewById(R.id.paal);
        kural= (TextView) findViewById(R.id.kural);
        transliteration= (TextView) findViewById(R.id.transliteration);
        meaning= (TextView) findViewById(R.id.meaning);
        urai= (TextView) findViewById(R.id.urai);
        amma= (TextView) findViewById(R.id.amma);
        date= (TextView) findViewById(R.id.date);
        kNo= (EditText) findViewById(R.id.kEditText);
        riD= (TextView) findViewById(R.id.ride);
        ruD= (TextView) findViewById(R.id.run);
        rrT= (TextView) findViewById(R.id.rrTime);


        layoutMaster = (LinearLayout)findViewById(R.id.layoutMaster);

        //Typeface tf = Typeface.createFromAsset(getAssets(),"Bamini.ttf");
        explanationArray = new String[3];

        //Set date..
        long time = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(time);
        date.setText(dateString);
        kNo.setText("" + 16);


        //service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        //Search onClick....
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                //Clear all the previous values...
                kural.setText("");
                transliteration.setText("");
                meaning.setText("");
                amma.setText("");
                urai.setText("");


                Toast.makeText(getApplicationContext(),"Loading, please wait...",Toast.LENGTH_LONG).show();
                int kuralNo = Integer.parseInt(kNo.getText().toString());
                setKuralDetails(kuralNo);

                pbX = pb.getX();
                pbY = pb.getY();

                pb.setX(kNo.getX());
                pb.setY(kNo.getY());
                kNo.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
            }
        });

        //Share onClick....
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Hiding the footer
                kNo.setVisibility(View.INVISIBLE);
                search.setVisibility(View.INVISIBLE);
                copy.setVisibility(View.INVISIBLE);
                share.setVisibility(View.INVISIBLE);
                gmail.setVisibility(View.INVISIBLE);

                Bitmap bitmap = Bitmap.createBitmap(1080, 1280, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Drawable bgDrawable = layoutMaster.getBackground();
                if (bgDrawable != null) {
                    bgDrawable.draw(canvas);
                } else {
                    canvas.drawColor(Color.WHITE);
                }
                layoutMaster.draw(canvas);

                String mPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/"  + "amma.jpeg";
                File imageFile = new File(mPath);
                try {
                    imageFile.createNewFile();
                    Log.d("Amma","Image saved");
                } catch (IOException e) {
                    Log.d("Amma","" + e.toString());
                }

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    Toast.makeText(getApplicationContext(),"Shared to " + mPath,Toast.LENGTH_SHORT).show();
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
                    getApplicationContext().startActivity(shareIntent);

                    kNo.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    copy.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                    gmail.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Log.d("KURAL","" + e);
                }

            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                int kuralNo = Integer.parseInt(kNo.getText().toString());
                Call<kural> call = service.getKuralDetails(kuralNo);
                call.enqueue(new Callback<com.example.thirukural.kural>() {
                     @Override
                     public void onResponse(Call<com.example.thirukural.kural> call, Response<com.example.thirukural.kural> response) {

                         //Copying kural..
                         String copyKural = "";
                         copyKural = "";
                         copyKural += "*" + response.body().getPaal() + " - " + response.body().getIyal() + " - " + response.body().getAgaradhi() + " - " + response.body().getNumber() + "*";
                         copyKural += "\n__________________________________\n";
                         copyKural += "\n*" + response.body().getLine1() + "*";
                         copyKural += "\n*" + response.body().getLine2() + "*";
                         copyKural += "\n__________________________________\n";
                         copyKural += "\n*" + response.body().getTransliteration1() + "*";
                         copyKural += "\n*" + response.body().getTransliteration2() + "*";
                         copyKural += "\n__________________________________\n";
                         copyKural += "\n*Translation:* " + response.body().getTranslation();
                         copyKural += "\n__________________________________\n";
                         copyKural += "\n*Meaning:* " + response.body().getExplanation();
                         copyKural += "\n__________________________________\n";
                         copyKural += "\n*தமிழ் அம்மா உரை:* " + response.body().getAmma();
                         copyKural += "\n__________________________________\n";
                         copyKural += "\n*மு.வ உரை:* " + response.body().getMv();
                         copyKural += "\n__________________________________\n";
                         copyKural += "\n*சாலமன் பாப்பையா உரை:* " + response.body().getSp();
                         copyKural += "\n__________________________________\n";
                         copyKural += "\n*மு.கருணாநிதி உரை:* " + response.body().getMk();

                         ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                         ClipData clip = ClipData.newPlainText("stravaKural", copyKural);
                         clipboard.setPrimaryClip(clip);

                         Log.d("Amma", copyKural);
                         Toast.makeText(getApplicationContext(),"Copied திருக்குறள் to Whatsapp...",Toast.LENGTH_SHORT).show();

                     }

                     @Override
                     public void onFailure(Call<com.example.thirukural.kural> call, Throwable t) {
                         Log.d("Amma","" + t.toString());
                     }
                });

            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                int kuralNo = Integer.parseInt(kNo.getText().toString());
                Call<kural> call = service.getKuralDetails(kuralNo);
                call.enqueue(new Callback<com.example.thirukural.kural>() {
                    @Override
                    public void onResponse(Call<com.example.thirukural.kural> call, Response<com.example.thirukural.kural> response) {

                        //Copying kural..
                        String copyKural = "";
                        copyKural = "";
                        copyKural += "" + response.body().getPaal() + " - " + response.body().getIyal() + " - " + response.body().getAgaradhi() + " - " + response.body().getNumber();
                        copyKural += "\n__________________________________\n";
                        copyKural += "\n" + response.body().getLine1();
                        copyKural += "\n" + response.body().getLine2();
                        copyKural += "\n__________________________________\n";
                        copyKural += "\n" + response.body().getTransliteration1();
                        copyKural += "\n" + response.body().getTransliteration2();
                        copyKural += "\n__________________________________\n";
                        copyKural += "\nTranslation: " + response.body().getTranslation();
                        copyKural += "\n__________________________________\n";
                        copyKural += "\nMeaning: " + response.body().getExplanation();
                        copyKural += "\n__________________________________\n";
                        copyKural += "\nதமிழ் அம்மா உரை: " + response.body().getAmma();
                        copyKural += "\n__________________________________\n";
                        copyKural += "\nமு.வ உரை: " + response.body().getMv();
                        copyKural += "\n__________________________________\n";
                        copyKural += "\nசாலமன் பாப்பையா உரை: " + response.body().getSp();
                        copyKural += "\n__________________________________\n";
                        copyKural += "\nமு.கருணாநிதி உரை: " + response.body().getMk();

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("stravaKural", copyKural);
                        clipboard.setPrimaryClip(clip);

                        Log.d("Amma", copyKural);
                        Toast.makeText(getApplicationContext(),"Copied திருக்குறள் to mail...",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<com.example.thirukural.kural> call, Throwable t) {
                        Log.d("Amma","" + t.toString());
                    }
                });

            }
        });

        strava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check at expires...
                SharedPreferences prefs = getSharedPreferences("kuralAmma", MODE_PRIVATE);
                final String[] at = {prefs.getString("at", "dbs")};//"No name defined" is the default value.
                long et = prefs.getLong("et", 0); //0 is the default value.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String t  = sdf.format(now);
                long currentTime = 0;
                try {
                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(t).getTime();
                    String xx = String.valueOf(currentTime);
                    Log.d("Strava","" + et);
                    xx = xx.substring(0,xx.length() - 3);
                    Log.d("Strava","" + xx);
                    currentTime = Long.parseLong(xx);
                } catch (ParseException e) {
                    Log.d("Strava",e.toString());
                }

                if(currentTime > et || et == 0){
                    //expired at..
                    service = RetrofitStravaAuth.getRetrofitInstance().create(GetDataService.class);
                    Call <authDetails> call = service.getAuthDetails();
                    Log.d("Strava","  Expiredddddd.." + et + " Current time " + currentTime);
                    call.enqueue(new Callback<authDetails>() {

                        @Override
                        public void onResponse(Call<authDetails> call, Response<authDetails> response) {
                            Log.d("Strava"," Accessing...");
                            authDetails res = response.body();
                            SharedPreferences.Editor editor = getSharedPreferences("kuralAmma", MODE_PRIVATE).edit();
                            editor.putString("at", res.getAccess_token());
                            editor.putLong("et", res.getExpires_at());
                            editor.apply();
                            at[0] = res.getAccess_token();
                            Log.d("Strava"," " + at[0]);
                            Toast.makeText(getApplicationContext(),"Got new access token....",Toast.LENGTH_SHORT).show();

                            getStravaDetails(at[0]);
                        }

                        @Override
                        public void onFailure(Call<authDetails> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"New access tokenb fetch failure...",Toast.LENGTH_SHORT).show();
                            Log.d("Strava","Errrrorrr " + t.toString());
                        }
                    });
                }else{
                    getStravaDetails(at[0]);
                }


            }
        });

    }

    public void getStravaDetails(String at){

        service = RetrofitStrava.getRetrofitInstance().create(GetDataService.class);

        Call <List<strava>> gaCall = service.getStravaStats(at);

        Log.d("Strava","" + gaCall.request().url().toString());

        gaCall.enqueue(new Callback<List<strava>>() {

            @Override
            public void onResponse(Call<List<com.example.thirukural.strava>> call, Response<List<com.example.thirukural.strava>> response) {
                List<strava> rs = response.body();
                rs = rs.subList(0,2);
                setStravaDetails(rs);
                Log.d("Strava","" + rs.get(0).getDistance());
                Toast.makeText(getApplicationContext(),"Got strava details....",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<com.example.thirukural.strava>> call, Throwable t) {
                Log.d("Strava"," " + t.toString());
                Toast.makeText(getApplicationContext(),"Auth error strava.....",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setStravaDetails(List<strava> rs){
        rrTime = 0;
        for(int i=0; i<=1; i++){
            strava activity = rs.get(i);
            if (activity.getType().equals("Ride")){
                rideDistance = activity.getDistance();

            }else{
                runDistance = activity.getDistance();
            }
            rrTime += activity.getElapsed_time();
        }
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);

        rideDistance = rideDistance / 1000;
        riD.setText("" + df.format(rideDistance) + " km");
        runDistance = runDistance / 1000;
        ruD.setText("" + df.format(runDistance) + " km");
        rrTime = rrTime / 60;
        //rrTime = 40;
        float hr=0, min=0;
        hr =  rrTime / 60;
        min = rrTime % 60;
        if((int)hr == 0){
            rrT.setText("" + (int)min + "m");
        }else{
            rrT.setText((int)hr + "h " + (int)min + "m");
        }
        Log.d("Strava"," " + rideDistance + " " + runDistance + " " + rrTime);
    }

    public void setKuralDetails(int kNoo){

        Call<kural> call = service.getKuralDetails(kNoo);
        call.enqueue(new Callback<com.example.thirukural.kural>() {

            @Override
            public void onResponse(Call<com.example.thirukural.kural> call, Response<com.example.thirukural.kural> response) {
                String line1 = response.body().getLine1().toString();
                String line2 = response.body().getLine2().toString();

                String trans1 = response.body().getTransliteration1().toString();
                String trans2 = response.body().getTransliteration2().toString();

                Log.d("Amma","" + line1 + "\n" + line2);

                String resIyal = response.body().getIyal();
                String resAgaradhi = response.body().getAgaradhi();
                int resNumber = response.body().getNumber();

                //kural.setText(Html.fromHtml("<b><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + resIyal + " - " + resAgaradhi + " - " + resNumber + "</i></b>"));
                kural.append(line1 + "\n" + line2);
                kural.append(Html.fromHtml("<b><i><font color='#FFFFFF'>&nbsp;&nbsp;<u>(" + resIyal + ")" + "</font></i></b></u>"));

                //transliteration.append(Html.fromHtml("<b>Transliteration: </b>"));
                transliteration.append("\n" + trans1 + "\n" + trans2);
                transliteration.append(Html.fromHtml("<b><i><font color='#FFFFFF'>&nbsp;&nbsp;<u>(" + resAgaradhi + ")" + "(" + resNumber + ")</font></i></b></u>"));

                paal.setText(response.body().getPaal());
                meaning.append(Html.fromHtml("<b><u>Explanation:</u></b> " + response.body().getTranslation() + "."));
                amma.append(Html.fromHtml("<b><u>தமிழ் அம்மா உரை:</u></b> " + response.body().getAmma() + ""));

                int ammaHeight = amma.getLineCount()* 43;
                Log.d("Amma","Amma height is : " + amma.getLineCount() + " + " + 43 + " + " + ammaHeight);

                //Dynamically set textsize of amma urai..
                if(ammaHeight > 172){
                    amma.setTextSize(13);
                    Log.d("Amma","Setting 13");
                }else{
                    amma.setTextSize(14);
                    Log.d("Amma","Setting 14");
                }


                explanationArray[0] = response.body().getMv();
                explanationArray[1] = response.body().getSp();
                explanationArray[2] = response.body().getMk();

                Random rn = new Random();
                int rInt =  rn.nextInt(3);

                Log.d("Amma","" + rInt);
                String explanation = explanationArray[rInt];

                switch (rInt){
                    case 0 :
                        urai.append(Html.fromHtml("<b><u>மு.வ உரை :</u></b> " + explanation + "."));
                        break;

                    case 1 :
                        urai.append(Html.fromHtml("<b><u>சாலமன் பாப்பையா உரை :</u></b> " + explanation + "."));
                        break;

                    case 2 :
                        urai.append(Html.fromHtml("<b><u>மு.கருணாநிதி உரை :</u></b> " + explanation + "."));
                        break;
                }

                pb.setX(pbX);
                pb.setY(pbY);
                pb.setVisibility(View.INVISIBLE);
                kNo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<com.example.thirukural.kural> call, Throwable t) {
                Log.d("Amma","" + t.toString());
            }
        });

    }

}


