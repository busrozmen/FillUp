package com.example.ben.tezfillup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Pc on 4.03.2018.
 */

public class ClassForm extends Activity {

    String gelenVeri;
    String js;
    private Context mContext;
    private Activity mActivity;

    private LinearLayout mRootLayout;
    private WebView mWebView;
    String TCNo = "";
    String Name = "";
    String Surname = "";
    String FatherName = "";
    String MotherName = "";
    String DOB = "";
    String City = "";
    String Gender = "";

    String day = "";
    String month = "";
    String year = "";

    String ActiveUserEmail = "";
    ArrayList<String> citiesSoundex;
    ArrayList<String> seperatedSoundex;
    ArrayList<String> seperatedText;

    String SavedInfolu = null;

    String[] seperatedtext;
    String text;

    SqliteDatabase sqliteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        mContext = getApplicationContext();
        mActivity = ClassForm.this;

        mRootLayout = (LinearLayout) findViewById(R.id.root_layout);
        mWebView = (WebView) findViewById(R.id.web_view);

        sqliteDatabase = new SqliteDatabase(this);

        SharedPreferences SP = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        ActiveUserEmail = SP.getString("ActiveUserEmail", null);

        List<String> cities = Arrays.asList(getResources().getStringArray(R.array.cities));
        citiesSoundex = new ArrayList<>();
        for (int index = 0; index<cities.size(); index++){
            citiesSoundex.add(getGode(cities.get(index)));
        }

        Bundle extras = getIntent().getExtras();
        SavedInfolu = extras.getString("infolusayfa");
        if (SavedInfolu != null){
            List<String> list = sqliteDatabase.AccountList(ActiveUserEmail);
            String user = list.get(list.size()-1);
            String [] userlist = user.split("-",7);
            TCNo = userlist[0];
            Surname = userlist[1];
            Name = userlist[2];
            FatherName = userlist[3];
            MotherName = userlist[4];
            City = userlist[5];
            DOB = userlist[6];

            if (SavedInfolu.contains("btnRandevu")) {
                String url = "https://m.hastanerandevu.gov.tr/yeni_uye.html";
                mWebView.loadUrl(url);
                mWebView.getSettings().setJavaScriptEnabled(true);
                js = "javascript:" +
                        "document.getElementById('ad').value='" + Name + "';" +
                        "document.getElementById('soyad').value='" + Surname + "';" +
                        "document.getElementById('tcNo').value='" + TCNo + "';" +
                        "document.getElementById('baba').value='" + FatherName + "';" +
                        "document.getElementById('anne').value='" + MotherName + "';" +
                        "document.getElementById('tarih1').value='" + DOB + "';" +
                        "document.getElementById('dogum_yer').value='" + City + "';" +
                        "document.getElementById('eposta').value='" + ActiveUserEmail + "';" +
                        "document.getElementById('epostaTekrar').value='" + ActiveUserEmail + "';";
            }
            else if (SavedInfolu.contains("btnAnasis")) {
                String url = "https://anasis.anadolu.edu.tr/#/";
                mWebView.loadUrl(url);
                mWebView.getSettings().setJavaScriptEnabled(true);
                js = "javascript:document.getElementById('txtAccount').value='" + TCNo + "';";
            }
            else if (SavedInfolu.contains("btnMavi")) {
                String url = "https://www.mavi.com";
                mWebView.loadUrl(url);
                mWebView.getSettings().setJavaScriptEnabled(true);
                js = "javascript:" +
                        "document.getElementById('register-firstName').value='" + Name + "';" +
                        "document.getElementById('register-lastName').value='" + Surname + "';" +
                        "document.getElementById('register-day').value='" + day + "';" +
                        "document.getElementById('register-month').value='" + month + "';" +
                        "document.getElementById('register-year').value='" + year + "';" +
                        "document.getElementById('register-email').value='" + ActiveUserEmail + "';";
            }
            mWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        view.evaluateJavascript(js, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                            }
                        });
                    }
                }
            });
        }
        else {
            TCNo = "";
            Name = "";
            Surname = "";
            FatherName = "";
            MotherName = "";
            DOB = "";
            City = "";
            Gender = "";
            day = "";
            month = "";
            year = "";

            Intent i = getIntent();
            this.gelenVeri = i.getStringExtra("gidenVeri");

            text = gelenVeri.replace("\n\n\n", "\n").replace("\n\n", "\n").replace("\n", "-");
            seperatedtext = text.split("-");

            seperatedText = new ArrayList<String>();
            for (int v = 0; v<seperatedtext.length; v++){
                seperatedText.add(seperatedtext[v]);
            }

            seperatedSoundex = new ArrayList<>();
            for (int index = 0; index < seperatedText.size(); index++) {
                if ((seperatedText.get(index).toString().matches("[0-9]+") && seperatedText.get(index).toString().length() == 11)) {
                    TCNo = (seperatedText.get(index));
                    Surname = (seperatedText.get(index + 1));
                    Name = (seperatedText.get(index + 2));
                    FatherName = (seperatedText.get(index + 3));
                    MotherName = (seperatedText.get(index + 4));
                }

                else if (seperatedText.get(index).toString().contains(".19")) {
                    DOB = (seperatedText.get(index));
                }
            }
            for (int k = 0; k < seperatedText.size(); k++){
                seperatedSoundex.add(getGode(seperatedText.get(k)));
            }
            for (int x = 0; x<citiesSoundex.size(); x++){
                for (int y = 0; y<seperatedSoundex.size(); y++){
                    if(cities.get(x).length()==seperatedText.get(y).length()){
                        if (citiesSoundex.get(x).equals(seperatedSoundex.get(y))){
                            City = cities.get(x);
                        }
                    }
                }
            }

            if (DOB != "") {
                day = DOB.split(Pattern.quote("."))[0];
                month = DOB.split(Pattern.quote("."))[1];
                year = DOB.split(Pattern.quote("."))[2];
            }

            sqliteDatabase.InsertAccount(ActiveUserEmail, TCNo, Surname, Name, FatherName, MotherName, City, DOB);

            if (gelenVeri.contains("btnRandevu")) {
                String url = "https://m.hastanerandevu.gov.tr/yeni_uye.html";
                mWebView.loadUrl(url);
                mWebView.getSettings().setJavaScriptEnabled(true);
                js = "javascript:" +
                        "document.getElementById('ad').value='" + Name + "';" +
                        "document.getElementById('soyad').value='" + Surname + "';" +
                        "document.getElementById('tcNo').value='" + TCNo + "';" +
                        "document.getElementById('baba').value='" + FatherName + "';" +
                        "document.getElementById('anne').value='" + MotherName + "';" +
                        "document.getElementById('tarih1').value='" + DOB + "';" +
                        "document.getElementById('dogum_yer').value='" + City + "';" +
                        "document.getElementById('eposta').value='" + ActiveUserEmail + "';" +
                        "document.getElementById('epostaTekrar').value='" + ActiveUserEmail + "';";
            }
            else if (gelenVeri.contains("btnAnasis")) {
                String url = "https://anasis.anadolu.edu.tr/#/";
                mWebView.loadUrl(url);
                mWebView.getSettings().setJavaScriptEnabled(true);
                js = "javascript:document.getElementById('txtAccount').value='" + TCNo + "';";
            }
            else if (gelenVeri.contains("btnMavi")) {
                String url = "https://www.mavi.com";
                mWebView.loadUrl(url);
                mWebView.getSettings().setJavaScriptEnabled(true);
                js = "javascript:" +
                        "document.getElementById('register-firstName').value='" + Name + "';" +
                        "document.getElementById('register-lastName').value='" + Surname + "';" +
                        "document.getElementById('register-day').value='" + day + "';" +
                        "document.getElementById('register-month').value='" + month + "';" +
                        "document.getElementById('register-year').value='" + year + "';" +
                        "document.getElementById('register-email').value='" + ActiveUserEmail + "';";
            }

            mWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        view.evaluateJavascript(js, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                            }
                        });
                    }
                }
            });
        }
    }

    public static String getGode(String city){
        city=city.toUpperCase();

        city = city.replace("Ğ", "G");
        city = city.replace("Ü", "U");
        city = city.replace("Ş", "S");
        city = city.replace("Ö", "O");
        city = city.replace("İ", "I");
        city = city.replace("Ç", "C");


        char[] x = city.toCharArray();

        char firstLetter = x[0];

        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {
                case 'G':
                {
                    x[i] = '1';
                    break;
                }

                case 'U':
                {
                    x[i] = '2';
                    break;
                }

                case 'S':
                {
                    x[i] = '3';
                    break;
                }

                case 'O': {
                    x[i] = '4';
                    break;
                }

                case 'I':
                {
                    x[i] = '5';
                    break;
                }

                case 'C': {
                    x[i] = '6';
                    break;
                }
                default: {
                    x[i]=x[i];
                    break;
                }
            }
        }

        String output = "" + firstLetter;

        for (int i = 1; i < x.length; i++)
            output += x[i];


        return output;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ClassForm.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}