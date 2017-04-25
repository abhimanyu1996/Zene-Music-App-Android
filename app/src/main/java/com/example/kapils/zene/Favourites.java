package com.example.kapils.zene;

public class Favourites {
    private int _id;
    private String _songname;

    public Favourites(){
    }

    public Favourites(String songname) {
        this._songname = songname;
    }

    public void set_songname(String _songname) {
        this._songname = _songname;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public String get_songname() {
        return _songname;
    }
}
