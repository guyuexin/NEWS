package edu.com.hzy.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
@BindView(R.id.textview)
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_click)
    public void onclick1(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String s= getherDataByPost();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(s);
                    }
                });
            }
        }).start();
    }

    private String getherDataByPost(){
        String str = "http://118.244.212.82:9092/newsClient/news_list?";


        try {
            URL url=new URL(str);
            String outStr="ver=0&subid=1&dir=1&nid=0&stamp=20140321&cnt=20";
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream out=conn.getOutputStream();
            out.write(outStr.getBytes());
            if (conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream in=conn.getInputStream();
                return getStrFromStream(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    private String getStrFromStream(InputStream in){
        int len=0;
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
       byte[] buff=new byte[1024];
        try {
            while ((len=in.read(buff))!=-1){
                bos.write(buff,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String(bos.toByteArray()
        );
    }

}

