package com.jenlern.fileoperation2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private int[] operateRes = {R.id.main_btn_1, R.id.main_btn_2, R.id.main_btn_3,
            R.id.main_btn_4, R.id.main_btn_5};
    private Button[] operateBnt = new Button [operateRes.length];
    private EditText editText;

    private String TAG_WRITE_READ_FILE = "TAG_WRITE_READ_FILE"; //for log to use
    private String FileName = "userEmail.txt";
    private String cacheFileName = "customCache.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //以下為追加
        setTitle("錦能修改過 - 檔案存取示範 (dev2qa.com)");
        editText = (EditText) findViewById(R.id.edit_text);
        for (int i = 0; i < operateRes.length; i++) {
            operateBnt[i] = findViewById(operateRes[i]);
        }
    }

    public void onSeletOperate(View view) {
        for (int i = 0; i < operateBnt.length; i++) {
            if (view == operateBnt[i]) {
                //bDone = true; // done
                Context ctx = getApplicationContext();
                switch (i) {
                    case 0: //Write To internal File
                        String textContent = editText.getText().toString();
                        if (TextUtils.isEmpty(textContent)) {
                            //Toast 是一個彈出的顯示窗，用來顯示警示的文字
                            Toast.makeText(ctx, "Input data can not be empty.", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            /*
                            This comments code can also write data to android internal file.
                            File file = new File(getFilesDir(), userEmalFileName);
                            writeDataToFile(file, userEmail);
                            */
                            try {
                                FileOutputStream fileOutputStream = ctx.openFileOutput(FileName, Context.MODE_PRIVATE);
                                writeDataToFile(fileOutputStream, textContent);
                            } catch (FileNotFoundException ex) {
                                Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
                            }
                            Toast.makeText(ctx, "Data has been written to file " + FileName, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1: // Read from internal file.
                        try {
                            FileInputStream fileInputStream = ctx.openFileInput(FileName);
                            String fileData = readFromFileInputStream(fileInputStream);
                            if(fileData.length()>0) {
                                editText.setText(fileData);
                                editText.setSelection(fileData.length());
                                Toast.makeText(ctx, "Load saved data complete.", Toast.LENGTH_SHORT).show();
                            }else   {
                                Toast.makeText(ctx, "Not load any data.", Toast.LENGTH_SHORT).show();
                            }
                        }catch(FileNotFoundException ex)    {
                            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
                        }
                        break;
                    case 2: // Write to internal cache file
                        String userEmail = editText.getText().toString();
                        if(TextUtils.isEmpty(userEmail))    {
                            Toast.makeText(ctx, "Input data can not be empty.", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            File file = new File(getCacheDir(), cacheFileName);
                            writeDataToFile(file, userEmail);
                            Toast.makeText(ctx, "Cached file is created in file " + cacheFileName, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 3: // Read from cache file.
                        try {
                            File cacheFileDir = new File(getCacheDir(), cacheFileName);
                            FileInputStream fileInputStream = new FileInputStream(cacheFileDir);
                            String fileData = readFromFileInputStream(fileInputStream);
                            if(fileData.length()>0) {
                                editText.setText(fileData);
                                editText.setSelection(fileData.length());
                                Toast.makeText(ctx, "Load saved cache data complete.", Toast.LENGTH_SHORT).show();
                            }else   {
                                Toast.makeText(ctx, "Not load any cache data.", Toast.LENGTH_SHORT).show();
                            }
                        }catch(FileNotFoundException ex)    {
                            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
                        }
                        break;
                    case 4: // Write to internal temp file button.
                        try {
                            userEmail = editText.getText().toString();
                            if(TextUtils.isEmpty(userEmail))     {
                                Toast.makeText(getApplicationContext(), "Input data can not be empty.", Toast.LENGTH_LONG).show();
                                return;
                            }else {
                                // This method will create a temp file in android cache folder,
                                // temp file prefix is temp, suffix is .txt, each temp file name is unique.
                                File tempFile = File.createTempFile("temp", ".txt", getCacheDir());
                                String tempFileName = tempFile.getAbsolutePath();
                                writeDataToFile(tempFile, userEmail);
                                Toast.makeText(getApplicationContext(), "Temp file is created, file name is " + tempFileName, Toast.LENGTH_LONG).show();
                            }
                        }catch(IOException ex)  {
                            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
                        }
                        break;
                }
                break;  //跳出迴圈
            }
        }
    }

    // This method will read data from FileInputStream.
    private String readFromFileInputStream(FileInputStream fileInputStream) {
        StringBuffer retBuf = new StringBuffer();
        try {
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String lineData = bufferedReader.readLine();
                while (lineData != null) {
                    retBuf.append(lineData);
                    lineData = bufferedReader.readLine();
                }
            }
        }catch(IOException ex)  {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }finally    {
            return retBuf.toString();
        }
    }

    // This method will write data to file.
    private void writeDataToFile(File file, String data)
    {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            this.writeDataToFile(fileOutputStream, data);
            fileOutputStream.close();
        }catch(FileNotFoundException ex)
        {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }catch(IOException ex)
        {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }
    }


    // This method will write data to FileOutputStream.
    private void writeDataToFile(FileOutputStream fileOutputStream, String textContent) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(textContent);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();
        } catch(FileNotFoundException ex) {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }catch(IOException ex)  {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }
    }

}   /////////////////////////////

