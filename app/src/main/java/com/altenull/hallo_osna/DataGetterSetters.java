package com.altenull.hallo_osna;

import java.util.ArrayList;


public class DataGetterSetters {
    private String mRepresentativePhoto;
    private String mID;
    private String mName;
    private String mEnglishName;
    private String mMajor;
    private String mPeriod;
    private String mEmail;
    private String mNo;
    private ArrayList<String> mPhoto = new ArrayList();
    private ArrayList<String> mPhotoTitle = new ArrayList();
    private ArrayList<String> mPhotoComment = new ArrayList();
    private ArrayList<String> mPhotoMap = new ArrayList();


    public void setRepresentativePhoto(String paramRepresentativePhoto) { this.mRepresentativePhoto = paramRepresentativePhoto; }
    public void setID(String paramID)
    {
        this.mID = paramID;
    }
    public void setName(String paramName) { this.mName = paramName; }
    public void setEnglishName(String paramEnglishName) { this.mEnglishName = paramEnglishName; }
    public void setMajor(String paramMajor) {this.mMajor = paramMajor; }
    public void setPeriod(String paramPeriod) { this.mPeriod = paramPeriod; }
    public void setEmail(String paramEmail) { this.mEmail = paramEmail; }
    public void setNo(String paramNo)
    {
        this.mNo = paramNo;
    }
    public void setPhoto(String paramPhoto) { this.mPhoto.add(paramPhoto); }
    public void setPhotoTitle(String paramPhotoTitle) { this.mPhotoTitle.add(paramPhotoTitle); }
    public void setPhotoComment(String paramPhotoComment) { this.mPhotoComment.add(paramPhotoComment); }
    public void setPhotoMap(String paramPhotoMap) { this.mPhotoMap.add(paramPhotoMap); }


    public String getRepresentativePhoto()
    {
        return this.mRepresentativePhoto;
    }
    public String getID() { return this.mID; }
    public String getName() { return this.mName; }
    public String getEnglishName() { return this.mEnglishName; }
    public String getMajor() { return this.mMajor; }
    public String getPeriod() { return this.mPeriod; }
    public String getEmail() { return this.mEmail; }
    public String getNo()
    {
        return this.mNo;
    }
    public ArrayList<String> getPhoto() { return this.mPhoto; }
    public ArrayList<String> getPhotoTitle() { return this.mPhotoTitle; }
    public ArrayList<String> getPhotoComment() { return this.mPhotoComment; }
    public ArrayList<String> getPhotoMap() { return this.mPhotoMap; }


}