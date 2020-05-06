package ru.csu.ttpapp.common;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private String title;
    @NonNull
    private String link;
    private Date date = new Date();
    private boolean isUpdate = false;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSimpleDateFormat() {
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        String format = formatForDateNow.format(this.date);
        return format;
    }
}