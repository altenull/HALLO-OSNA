package com.altenull.hallo_osna;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;


public class DataHandler {
    private static volatile DataHandler uniqueInstance = null;

    protected int screenSize = 1;
    protected float screenDensity;
    protected double density;
    protected double imageScale;

    private DataGetterSetters data = null;
    private ArrayList<DataGetterSetters> dataList = new ArrayList();


    public static DataHandler getInstance() {
        if ( uniqueInstance == null ) {}
        try {
            if ( uniqueInstance == null ) {
                uniqueInstance = new DataHandler();
            }
            return uniqueInstance;
        }
        finally {}
    }


    public void addData(Element paramElement) {
        this.data = new DataGetterSetters();

        Element localElement1 = (Element)paramElement.getElementsByTagName("id").item(0);

        String localStr1 = localElement1.getFirstChild().getNodeValue();
        this.data.setID(localStr1);

        String localStr2 = localElement1.getAttribute("no");
        this.data.setNo(localStr2);

        String localStr3 = ((Element)paramElement.getElementsByTagName("representativePhoto").item(0)).getFirstChild().getNodeValue();
        this.data.setRepresentativePhoto(localStr3);

        String localStr4 = ((Element)paramElement.getElementsByTagName("name").item(0)).getFirstChild().getNodeValue();
        this.data.setName(localStr4);

        String localStr5 = ((Element)paramElement.getElementsByTagName("englishName").item(0)).getFirstChild().getNodeValue();
        this.data.setEnglishName(localStr5);

        String localStr6 = ((Element)paramElement.getElementsByTagName("major").item(0)).getFirstChild().getNodeValue();
        this.data.setMajor(localStr6);

        String localStr7 = ((Element)paramElement.getElementsByTagName("period").item(0)).getFirstChild().getNodeValue();
        this.data.setPeriod(localStr7);

        String localStr8 = ((Element)paramElement.getElementsByTagName("email").item(0)).getFirstChild().getNodeValue();
        this.data.setEmail(localStr8);

        NodeList photoNodeList = paramElement.getElementsByTagName("photo");
        for ( int i = 0;  i < photoNodeList.getLength();  i++ ) {
            String localStr9 = ((Element)photoNodeList.item(i)).getFirstChild().getNodeValue();
            this.data.setPhoto(localStr9);
        }

        NodeList photoTitleNodeList = paramElement.getElementsByTagName("photoTitle");
        for ( int i = 0;  i < photoTitleNodeList.getLength();  i++ ) {
            String localStr10 = ((Element)photoTitleNodeList.item(i)).getFirstChild().getNodeValue();
            this.data.setPhotoTitle(localStr10);
        }

        NodeList photoCommentNodeList = paramElement.getElementsByTagName("photoComment");
        for ( int i = 0;  i < photoCommentNodeList.getLength();  i++ ) {
            String localStr11 = ((Element)photoCommentNodeList.item(i)).getFirstChild().getNodeValue();
            this.data.setPhotoComment(localStr11);
        }

        NodeList photoMapNodeList = paramElement.getElementsByTagName("photoMap");
        for ( int i = 0;  i < photoMapNodeList.getLength();  i++ ) {
            String localStr12 = ((Element)photoMapNodeList.item(i)).getFirstChild().getNodeValue();
            this.data.setPhotoMap(localStr12);
        }

        NodeList photoCategoryNodeList = paramElement.getElementsByTagName("photoCategory");
        for ( int i = 0;  i < photoCategoryNodeList.getLength();  i++ ) {
            String categoryTag = ((Element)photoCategoryNodeList.item(i)).getFirstChild().getNodeValue();
            if ( !categoryTag.equals("EMPTY") ) {
                CategoryDataGetterSetters.setPhoto(((Element)photoNodeList.item(i)).getFirstChild().getNodeValue(), categoryTag);
                CategoryDataGetterSetters.setPhotoTitle(((Element)photoTitleNodeList.item(i)).getFirstChild().getNodeValue(), categoryTag);
                CategoryDataGetterSetters.setPhotoComment(((Element)photoCommentNodeList.item(i)).getFirstChild().getNodeValue(), categoryTag);
            }
        }

        this.dataList.add(this.data);
        this.data = null;

        return;
    }

    public ArrayList<DataGetterSetters> getData() { return this.dataList; }

    public double getDensity() { return this.density; }

    public double getImageScale() { return this.imageScale; }

    public float getScreenDensity() { return this.screenDensity; }

    public int getScreenSize() { return this.screenSize; }

    public void saveDimensions(float paramDensity, double paramDpHeight) {
        this.density = paramDensity;
        this.imageScale = ((paramDpHeight - 116.0D) / paramDpHeight);
    }

    public void setScreenSize(int paramScreenSize, float paramDensity) {
        this.screenSize = paramScreenSize;
        this.screenDensity = paramDensity;
    }


}