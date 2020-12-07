package com.example.myapplication;


public class Song {

    public String song;//歌曲名
    public String singer;//歌手
    public long size;//歌曲所占空间大小
    public int duration;//歌曲时间长度
    public String path;//歌曲地址
    public String album;//专辑名

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Song() {
    }

    public Song(String song, String singer, long size, int duration, String path, String album) {
        this.song = song;
        this.singer = singer;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.album = album;
    }

    public String getPath(){
        return path;
    }
    public int getDuration(){
        return duration;
    }
}
