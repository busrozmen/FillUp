package com.example.ben.tezfillup;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.Context.MODE_PRIVATE;

public class FragmentPdf extends Fragment {
    private static final String SEARCH_ENABLED_PARAM_NAME = "searchEnabledParam";
    private static final String TAG = "FragmentPdf";

    EditText editTitle;
    EditText editText;
    String title;
    private boolean searchEnabled;
    View content;
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    public FragmentPdf() {
        // Required empty public constructor
    }
    public static FragmentPdf newInstance(Boolean searchEnabled) {
        FragmentPdf fragment = new FragmentPdf();
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
        View view = inflater.inflate(R.layout.pdf_layout, container, false);
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE).edit();
        editor.putString("PageName", "FragmentPdf");
        editor.commit();
        editTitle = (EditText) view.findViewById(R.id.editbaslik);
        editText = (EditText)  view.findViewById(R.id.editText);
        Button btnPdf = (Button) view.findViewById(R.id.pdfButton);
        Button btnCancel = (Button)  view.findViewById(R.id.cancelButton);
        content = view.findViewById(R.id.editText);


        String texts=getArguments().getString("texts");
        texts = texts.replace("\n\n", "--");
        texts = texts.replace("\n", " ");
        texts = texts.replace("--","\n");
        editText.setText(texts);
        title = editTitle.getText().toString();

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Please enter text to generate Pdf");
                }
                else if (editTitle.getText().toString().isEmpty()) {
                    editTitle.setError("Please enter a title");
                }
                else {
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                editTitle.setText("");
                FragmentOcr fragmentOcr = FragmentOcr.newInstance(true);
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction ft2 = fragmentManager2.beginTransaction();
                ft2.replace(R.id.container, fragmentOcr);
                ft2.commit();
            }
        });

        return view;
    }


    private void createPdfWrapper() throws FileNotFoundException,DocumentException{

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/FillUpPdf");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        pdfFile = new File(docsFolder.getAbsolutePath(), editTitle.getText().toString() + ".pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(BaseFont.COURIER, "CP1254", BaseFont.NOT_EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font titleFont = new Font(bf, 22, Font.BOLD
                | Font.NORMAL, BaseColor.BLACK);
        Font textFont = new Font(bf, 18, Font.NORMAL
                | Font.NORMAL, BaseColor.BLACK);
        Paragraph prHead = new Paragraph();
        Paragraph prText = new Paragraph();

        prHead.setFont(titleFont);
        prText.setFont(textFont);
        prHead.setAlignment(Element.ALIGN_CENTER);
        prHead.add(editTitle.getText().toString().toUpperCase()+"\n\n");
        document.add(prHead);
        prText.add(new Paragraph(editText.getText().toString(),textFont));
        document.add(prText);
        document.close();
        previewPdf();

    }
    private void previewPdf() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(pdfFile);
        intent.setDataAndType(uri, "application/pdf");

        startActivity(intent);
    }

}