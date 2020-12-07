package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.myapplication.Utils.list;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int UPDATE_SEEKBAR = 0;//更新seekbar控制字
    MediaPlayer mediaPlayer=new MediaPlayer();//避免空指针异常
    private int currentPosition=-1;//记录当前音频位置
    private ListAdapter adapter=null;
    SeekBar seekBar;
    TextView end;//当前歌曲时间
    TextView now;//当前进度时间
    // 判断seekbar是否正在滑动
    private boolean ischanging = false;
    private Thread thread;

    //进度条线程状态更新，自动
    class SeekBarThread implements Runnable{

        @Override
        public void run() {
            while(!ischanging&&mediaPlayer.isPlaying()){
                try{
                    //每500毫秒更新一次位置
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Message message=new Message();
                message.what=UPDATE_SEEKBAR;
                handler.sendMessage(message);

            }

        }

}
//更新seekbar，进度条的走动
    private  Handler handler=new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_SEEKBAR:
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    end.setText(""+Utils.formatTime(mediaPlayer.getCurrentPosition()));
                    break;
                default:
                    break;
            }
        }
    };
    // 下一曲
    private void nextMusic() {
        currentPosition++;
        if (currentPosition > list.size() - 1) {
            currentPosition = 0;
        }
        playmusic(currentPosition);
        adapter.notifyDataSetChanged();
    }

    // 上一曲
    private void frontMusic() {
        currentPosition--;
        if (currentPosition < 0) {
            currentPosition = list.size() - 1;
        }
        playmusic(currentPosition);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer=new MediaPlayer();
        ImageView pre=findViewById(R.id.bt_previous);
        pre.setOnClickListener(this);
        ImageView play=findViewById(R.id.bt_play);
        play.setOnClickListener(this);
        ImageView next=findViewById(R.id.bt_next);
        next.setOnClickListener(this);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        end=(TextView)findViewById(R.id.time_now);
        now=(TextView)findViewById(R.id.time_end);


        //设置权限
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String []{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }else{ }
        list = new ArrayList<>();
        list = Utils.getmusic(this);//获取音乐列表
        adapter=new ListAdapter(MainActivity.this,list);
        final ListView listView=(ListView)findViewById(R.id.list_song_rv);
        listView.setAdapter(adapter);


        //点击条目播放
        //设置每一项的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取位置
                currentPosition=position;
                playmusic(currentPosition);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                    currentPosition++;
                    if(currentPosition>list.size()-1){
                        currentPosition=0;
                    }playmusic(currentPosition);
                }
        });
        //拖动进度条进行控制
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser==true){
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();

            }
        });
    }


    //播放音乐
    private void playmusic(int currentPosition){
        seekBar.setMax(list.get(currentPosition).getDuration());
        //开始播放时，获取当前的时间
        now.setText(""+Utils.formatTime(list.get(currentPosition).getDuration()));
        try{
            mediaPlayer.reset();
            //获取音乐路径
            String url=list.get(currentPosition).getPath();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        thread = new Thread(new SeekBarThread());
        thread.start();
    }

    //点击下方按钮事件
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_previous:
                frontMusic();
                break;
            case R.id.bt_play:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }else{
                mediaPlayer.start();}
                break;
            case R.id.bt_next:
                nextMusic();
                break;
            default:
                break;
        }

    // 设置发生错误时调用
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            // TODO Auto-generated method stub
            mp.reset();
            // Toast.makeText(MainActivity.this, "未发现音乐", 1500).show();
            return false;
        }
    });
}

    //释放空间
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
