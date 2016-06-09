package com.advisorapp.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steeve on 02/06/2016.
 */
public class Token implements Parcelable{
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
    }

    public static final Parcelable.Creator<Token> CREATOR
            = new Parcelable.Creator<Token>() {
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    private Token(Parcel in) {
        this.token = in.readString();
    }

}
