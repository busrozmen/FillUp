package com.example.ben.tezfillup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


public class SliderAdapter extends PagerAdapter {

    private ArrayList<Integer> images;
    private LayoutInflater inflater;
    private Context context;
    SqliteDatabase sqliteDatabase;
    String ActiveUserEmail;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;

    public SliderAdapter(Context context, ArrayList<Integer> images,String email) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
        this.ActiveUserEmail = email;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public Object instantiateItem(final ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_show, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        myImage.setImageResource(images.get(position));
        final int image = images.get(position);



        sqliteDatabase = new SqliteDatabase(context);


        myImage.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                if(ActiveUserEmail!="") {
                    if (sqliteDatabase.isUserAccountSaved(ActiveUserEmail)) {
                        new AlertDialog.Builder(context).setTitle("Make Your Choice")
                                .setMessage("Where do you want to receive your information?")
                                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, OcrCaptureActivity.class);
                                        Bundle bundle = new Bundle();
                                        if (image == R.drawable.image1) {
                                            intent.putExtra("sayfa", "btnAnasis");
                                        } else if (image == R.drawable.image2) {
                                            intent.putExtra("sayfa", "btnRandevu");
                                        } else if (image == R.drawable.image3) {
                                            intent.putExtra("sayfa", "btnMavi");
                                        }
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);
                                    }
                                })
                                .setNeutralButton("Regıstered Info", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, ClassForm.class);
                                        Bundle bundle = new Bundle();
                                        if (image == R.drawable.image1) {
                                            intent.putExtra("infolusayfa", "btnAnasis");
                                        } else if (image == R.drawable.image2) {
                                            intent.putExtra("infolusayfa", "btnRandevu");
                                        } else if (image == R.drawable.image3) {
                                            intent.putExtra("infolusayfa", "btnMavi");
                                        }
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);
                                    }
                                })
                                .create().show();
                    } else {
                        Intent intent = new Intent(v.getContext(), OcrCaptureActivity.class);
                        Bundle bundle = new Bundle();
                        if (image == R.drawable.image1) {
                            intent.putExtra("sayfa", "btnAnasis");
                        } else if (image == R.drawable.image2) {
                            intent.putExtra("sayfa", "btnRandevu");
                        } else if (image == R.drawable.image3) {
                            intent.putExtra("sayfa", "btnMavi");
                        }
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    }
                }
            }
        });



        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}