package com.example.androidaplikacjapierwsza;

import android.os.Parcel;
import android.os.Parcelable;

public class Grade implements Parcelable {
    private final String subject;
    private int grade;

    public Grade(String subject, int grade) {
        this.subject = subject;
        this.grade = grade;
    }

    protected Grade(Parcel in) {
        subject = in.readString();
        grade = in.readInt();
    }

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };

    public String getSubject() {
        return subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeInt(grade);
    }
}
