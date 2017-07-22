package com.example.uitest.autoaddcontact;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {
    private Button start;
    private CheckBox chinese;
    private CheckBox english;
    private CheckBox add;
    private CheckBox delete;
    private CheckBox check;
    private EditText count_number;
    private TextView test_msg;
    private ContactManager contactManager;
    private ContactBuilder contactBuilder;
    private int COUNT = 0;
    File logfile;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    start.setEnabled(true);
                    break;
                case 2:
                    test_msg.setText(msg.getData().getString("log"));
                    break;
                case 3:
                    Toast.makeText(MainActivity.this,"请选择添加联系人",Toast.LENGTH_SHORT).show();
                    break;
                default:

            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        start = (Button) findViewById(R.id.start);
        chinese = (CheckBox) findViewById(R.id.chinese);
        english = (CheckBox) findViewById(R.id.english);
        add = (CheckBox) findViewById(R.id.add_contact);
        delete = (CheckBox) findViewById(R.id.delete_contact);
        check = (CheckBox) findViewById(R.id.check_contact);
        count_number = (EditText) findViewById(R.id.editText);
        test_msg = (TextView) findViewById(R.id.test_msg);

        contactBuilder = new ContactBuilder();

        logfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/autoaddcontact.txt");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT<23){
                    startTest();
                }
                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    Log.e("GRANTED","申请权限！");
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.WRITE_CONTACTS,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    Log.e("GRANTED","有权限直接测试！");
                    startTest();
                }

            }
        });

        test_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });
    }

    public void startTest(){
        contactManager = new ContactManager(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                testContact();
                Message msg = new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
        }).start();
        printLog();
        start.setEnabled(false);
    }

    public void printLog(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Process process = Runtime.getRuntime().exec("logcat AddContact:I");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String log ;
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile)));
                    Bundle bundle = new Bundle();
                    while ((log=bufferedReader.readLine())!=null&&!start.isEnabled()){
                        bufferedWriter.write(log+"\n");
                        Message message = new Message();
                        message.what = 2;
                        bundle.putString("log",log);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void testContact(){

        if(!add.isChecked()){
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        }else {
            COUNT = Integer.parseInt(count_number.getText().toString());
            for (int i=0;i<COUNT;i++){
                if(chinese.isChecked()&&english.isChecked()){

                    contactManager.add(contactBuilder.getChContact());

                    contactManager.add(contactBuilder.getEngContact());
                }
                else if(english.isChecked()){
                    contactManager.add(contactBuilder.getEngContact());
                }
                else if(chinese.isChecked()){
                    contactManager.add(contactBuilder.getChContact());
                }else{
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                    break;
                }
                if(delete.isChecked()){
                    if(i%2==0){
                        contactManager.delete();
                    }
                }
                if (check.isChecked()){
                    if(i%10==0){
                        contactManager.checkContact();
                    }
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
               startTest();
            }else {
                Toast.makeText(MainActivity.this,"拒绝权限将无法添加联系人",Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
