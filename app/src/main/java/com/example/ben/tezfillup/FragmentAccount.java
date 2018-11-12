package com.example.ben.tezfillup;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Pc on 3.04.2018.
 */

public class FragmentAccount extends Fragment {
    private static final String SEARCH_ENABLED_PARAM_NAME = "searchEnabledParam";
    private static final String TAG = "FragmentAccount";

    private boolean searchEnabled;
    PdfListLoadTask listTask;
    LinearLayout infLinear;
    ArrayAdapter<String> adapter;

    EditText edtTCNo;
    EditText edtName;
    EditText edtSurname;
    EditText edtFatherName;
    EditText edtMotherName;
    EditText edtCity;
    EditText edtDob;
    Button btnEdit;

    String TCNo = "";
    String Name = "";
    String Surname = "";
    String FatherName = "";
    String MotherName = "";
    String City = "";
    String DOB = "";

    File[] filelist;
    ListView pdfList;

    SqliteDatabase sqliteDatabase;
    String ActiveUserEmail;

    public FragmentAccount() {
        // Required empty public constructor
    }
    public static FragmentAccount newInstance(Boolean searchEnabled) {
        FragmentAccount fragment = new FragmentAccount();
        Bundle args = new Bundle();
        args.putString(SEARCH_ENABLED_PARAM_NAME,Boolean.toString(searchEnabled));
        fragment.setArguments(args);
        if(searchEnabled)
            fragment.setHasOptionsMenu(true);
        else
            fragment.setHasOptionsMenu(false);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.searchEnabled = Boolean.parseBoolean(getArguments().getString(SEARCH_ENABLED_PARAM_NAME));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.myaccount_list, container, false);

        sqliteDatabase = new SqliteDatabase(getActivity());

        SharedPreferences SP = this.getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE);
        ActiveUserEmail = SP.getString("ActiveUserEmail", null);
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE).edit();
        editor.putString("PageName", "FragmentAccount");
        editor.commit();
        edtTCNo = (EditText) view.findViewById(R.id.edtTCNo);
        edtName = (EditText) view.findViewById(R.id.edtName);
        edtSurname = (EditText) view.findViewById(R.id.edtSurname);
        edtFatherName = (EditText) view.findViewById(R.id.edtFatherName);
        edtMotherName = (EditText) view.findViewById(R.id.edtMotherName);
        edtCity = (EditText) view.findViewById(R.id.edtCity);
        edtDob = (EditText) view.findViewById(R.id.edtDob);

        btnEdit = (Button) view.findViewById(R.id.inf_edit_button);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDatabase.InsertAccount(ActiveUserEmail, edtTCNo.getText().toString(),
                        edtSurname.getText().toString(), edtName.getText().toString(), edtFatherName.getText().toString(),
                        edtMotherName.getText().toString(), edtCity.getText().toString(), edtDob.getText().toString());
                Snackbar.make(btnEdit, "Succesfully updated!", Snackbar.LENGTH_LONG).show();
            }
        });

        pdfList = (ListView) view.findViewById(R.id.pdfList);
        Button ButtonPdf=(Button) view.findViewById(R.id.pdf_list_button);
        Button ButtonForm=(Button) view.findViewById(R.id.form_list_button);
        infLinear=(LinearLayout) view.findViewById(R.id.inf) ;

        ButtonPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infLinear.setVisibility(View.INVISIBLE);
                listTask = new PdfListLoadTask();
                listTask.execute();

                pdfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        File file = new File(String.valueOf(filelist[position].getPath()));
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    }

                });
            }
        });
        ButtonForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> List = new ArrayList<>();
                adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, List);
                pdfList.setAdapter(adapter);
                infLinear.setVisibility(View.VISIBLE);

                List<String> list = sqliteDatabase.AccountList(ActiveUserEmail);
                if (list.size() > 0){
                    String user = list.get(list.size()-1);
                    String [] userlist = user.split("-",7);
                    TCNo = userlist[0];
                    Surname = userlist[1];
                    Name = userlist[2];
                    FatherName = userlist[3];
                    MotherName = userlist[4];
                    City = userlist[5];
                    DOB = userlist[6];

                    edtTCNo.setText(TCNo);
                    edtName.setText(Name);
                    edtSurname.setText(Surname);
                    edtFatherName.setText(FatherName);
                    edtMotherName.setText(MotherName);
                    edtCity.setText(City);
                    edtDob.setText(DOB);
                }
            }
        });
        return view;
    }

    public boolean onBackPressed() {
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
            return true;
    }


    private class PdfListLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            File files = new File("storage/emulated/0/Download/FillUpPdf/");
            filelist = files.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return ((name.endsWith(".pdf")));
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (filelist != null && filelist.length >= 1) {
                ArrayList<String> fileNameList = new ArrayList<>();
                for (int i = 0; i < filelist.length; i++)
                    fileNameList.add(filelist[i].getPath().toString().split("FillUpPdf/")[1]);
                adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, fileNameList);
                pdfList.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(),
                        "No pdf file found, Please create new Pdf file",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}