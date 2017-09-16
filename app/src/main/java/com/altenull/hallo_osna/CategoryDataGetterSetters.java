package com.altenull.hallo_osna;


import java.util.ArrayList;


public class CategoryDataGetterSetters {
    private static ArrayList<String> mPhotoFood = new ArrayList();
    private static ArrayList<String> mPhotoTitleFood = new ArrayList();
    private static ArrayList<String> mPhotoCommentFood = new ArrayList();

    private static ArrayList<String> mPhotoLeisure = new ArrayList();
    private static ArrayList<String> mPhotoTitleLeisure = new ArrayList();
    private static ArrayList<String> mPhotoCommentLeisure = new ArrayList();

    private static ArrayList<String> mPhotoDomitory = new ArrayList();
    private static ArrayList<String> mPhotoTitleDomitory = new ArrayList();
    private static ArrayList<String> mPhotoCommentDomitory = new ArrayList();

    private static ArrayList<String> mPhotoUniversity = new ArrayList();
    private static ArrayList<String> mPhotoTitleUniversity = new ArrayList();
    private static ArrayList<String> mPhotoCommentUniversity = new ArrayList();

    private static ArrayList<String> mPhotoTravel = new ArrayList();
    private static ArrayList<String> mPhotoTitleTravel = new ArrayList();
    private static ArrayList<String> mPhotoCommentTravel = new ArrayList();

    private static ArrayList<String> mPhotoDaily = new ArrayList();
    private static ArrayList<String> mPhotoTitleDaily = new ArrayList();
    private static ArrayList<String> mPhotoCommentDaily = new ArrayList();


    public static void setPhoto(String paramPhoto, String paramCategoryTag) {
        switch (paramCategoryTag) {
            case "#FOOD":
                mPhotoFood.add(paramPhoto);
                break;

            case "#LEISURE":
                mPhotoLeisure.add(paramPhoto);
                break;

            case "#DOMITORY":
                mPhotoDomitory.add(paramPhoto);
                break;

            case "#UNIVERSITY":
                mPhotoUniversity.add(paramPhoto);
                break;

            case "#TRAVEL":
                mPhotoTravel.add(paramPhoto);
                break;

            case "#DAILY":
                mPhotoDaily.add(paramPhoto);
                break;

            default:
                break;
        }
    }


    public static void setPhotoTitle(String paramPhotoTitle, String paramCategoryTag) {
        switch (paramCategoryTag) {
            case "#FOOD":
                mPhotoTitleFood.add(paramPhotoTitle);
                break;

            case "#LEISURE":
                mPhotoTitleLeisure.add(paramPhotoTitle);
                break;

            case "#DOMITORY":
                mPhotoTitleDomitory.add(paramPhotoTitle);
                break;

            case "#UNIVERSITY":
                mPhotoTitleUniversity.add(paramPhotoTitle);
                break;

            case "#TRAVEL":
                mPhotoTitleTravel.add(paramPhotoTitle);
                break;

            case "#DAILY":
                mPhotoTitleDaily.add(paramPhotoTitle);
                break;

            default:
                break;
        }
    }


    public static void setPhotoComment(String paramPhotoComment, String paramCategoryTag) {
        switch (paramCategoryTag) {
            case "#FOOD":
                mPhotoCommentFood.add(paramPhotoComment);
                break;

            case "#LEISURE":
                mPhotoCommentLeisure.add(paramPhotoComment);
                break;

            case "#DOMITORY":
                mPhotoCommentDomitory.add(paramPhotoComment);
                break;

            case "#UNIVERSITY":
                mPhotoCommentUniversity.add(paramPhotoComment);
                break;

            case "#TRAVEL":
                mPhotoCommentTravel.add(paramPhotoComment);
                break;

            case "#DAILY":
                mPhotoCommentDaily.add(paramPhotoComment);
                break;

            default:
                break;
        }
    }


    public static ArrayList<String> getPhoto(String paramCategoryTag) {
        switch(paramCategoryTag) {
            case "#FOOD":
                return mPhotoFood;

            case "#LEISURE":
                return mPhotoLeisure;

            case "#DOMITORY":
                return mPhotoDomitory;

            case "#UNIVERSITY":
                return mPhotoUniversity;

            case "#TRAVEL":
                return mPhotoTravel;

            case "#DAILY":
                return mPhotoDaily;

            default:
                return null;
        }
    }

    public static ArrayList<String> getPhotoTitle(String paramCategoryTag) {
        switch(paramCategoryTag) {
            case "#FOOD":
                return mPhotoTitleFood;

            case "#LEISURE":
                return mPhotoTitleLeisure;

            case "#DOMITORY":
                return mPhotoTitleDomitory;

            case "#UNIVERSITY":
                return mPhotoTitleUniversity;

            case "#TRAVEL":
                return mPhotoTitleTravel;

            case "#DAILY":
                return mPhotoTitleDaily;

            default:
                return null;
        }
    }


    public static ArrayList<String> getPhotoComment(String paramCategoryTag) {
        switch(paramCategoryTag) {
            case "#FOOD":
                return mPhotoCommentFood;

            case "#LEISURE":
                return mPhotoCommentLeisure;

            case "#DOMITORY":
                return mPhotoCommentDomitory;

            case "#UNIVERSITY":
                return mPhotoCommentUniversity;

            case "#TRAVEL":
                return mPhotoCommentTravel;

            case "#DAILY":
                return mPhotoCommentDaily;

            default:
                return null;
        }
    }


    public static int getSize(String paramCategoryTag) {
        switch(paramCategoryTag) {
            case "#FOOD":
                return mPhotoFood.size();

            case "#LEISURE":
                return mPhotoLeisure.size();

            case "#DOMITORY":
                return mPhotoDomitory.size();

            case "#UNIVERSITY":
                return mPhotoUniversity.size();

            case "#TRAVEL":
                return mPhotoTravel.size();

            case "#DAILY":
                return mPhotoDaily.size();

            default:
                return 0;
        }
    }


}