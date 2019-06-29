package com.csjbot.mobileshop.model.http.bean.rsp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 *
 * @author ShenBen
 * @date 2018/12/20 18:13
 * @email 714081644@qq.com
 */

public class GoodsDetailBean implements Comparable<GoodsDetailBean>, Parcelable {

    /**
     * id : 7
     * createById : 154
     * createByDate : null
     * modifyById : 154
     * modifyByDate : 2018-12-17 16:53:40
     * deleted : 0
     * level : 1154,
     * language : zh
     * name : 安踏春季鞋
     * coverUrl : www.安踏.com
     * typeId : 22
     * typeName : 鞋子
     * costPrice : 409
     * nowPrice : 385
     * usingWay : 展示
     * sort : 4
     * moneyType : 人民币
     * remarks : 昆山111安踏专卖店
     * weight : 1056
     * snList : null
     * imageUrls : [{"id":153,"createById":154,"createByDate":"2018-12-17 16:53:40","modifyById":null,"modifyByDate":"2018-12-17 16:53:40","deleted":0,"level":"1154,","language":"zh","resTypeName":"RobotGoodsInfoEntity","resTypeId":7,"name":"www.安踏.com","fileUploadName":null,"fileType":"图片","url":"www.安踏.com"}]
     * paramTypeInfoModels : [{"attId":33,"attName":"季节","labelId":34,"labelName":"春季"}]
     */

    private int id;
    private int createById;
    private Object createByDate;
    private int modifyById;
    private String modifyByDate;
    private int deleted;
    private String level;
    private String language;
    private String name;
    private String coverUrl;
    private int typeId;
    private String typeName;
    private double costPrice;
    private double nowPrice;
    private String usingWay;
    private int sort;
    private String moneyType;
    private String remarks;
    private int weight;
    private Object snList;
    private List<ImageUrlsBean> imageUrls;
    private List<ParamTypeInfoModelsBean> paramTypeInfoModels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreateById() {
        return createById;
    }

    public void setCreateById(int createById) {
        this.createById = createById;
    }

    public Object getCreateByDate() {
        return createByDate;
    }

    public void setCreateByDate(Object createByDate) {
        this.createByDate = createByDate;
    }

    public int getModifyById() {
        return modifyById;
    }

    public void setModifyById(int modifyById) {
        this.modifyById = modifyById;
    }

    public String getModifyByDate() {
        return modifyByDate;
    }

    public void setModifyByDate(String modifyByDate) {
        this.modifyByDate = modifyByDate;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(double nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getUsingWay() {
        return usingWay;
    }

    public void setUsingWay(String usingWay) {
        this.usingWay = usingWay;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Object getSnList() {
        return snList;
    }

    public void setSnList(Object snList) {
        this.snList = snList;
    }

    public List<ImageUrlsBean> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ImageUrlsBean> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<ParamTypeInfoModelsBean> getParamTypeInfoModels() {
        return paramTypeInfoModels;
    }

    public void setParamTypeInfoModels(List<ParamTypeInfoModelsBean> paramTypeInfoModels) {
        this.paramTypeInfoModels = paramTypeInfoModels;
    }

    @Override
    public int compareTo(@NonNull GoodsDetailBean o) {
        return getSort() - o.getSort();
    }

    public static class ImageUrlsBean implements Parcelable {
        /**
         * id : 153
         * createById : 154
         * createByDate : 2018-12-17 16:53:40
         * modifyById : null
         * modifyByDate : 2018-12-17 16:53:40
         * deleted : 0
         * level : 1154,
         * language : zh
         * resTypeName : RobotGoodsInfoEntity
         * resTypeId : 7
         * name : www.安踏.com
         * fileUploadName : null
         * fileType : 图片
         * url : www.安踏.com
         */

        private int id;
        private int createById;
        private String createByDate;
        private Object modifyById;
        private String modifyByDate;
        private int deleted;
        private String level;
        private String language;
        private String resTypeName;
        private int resTypeId;
        private String name;
        private Object fileUploadName;
        private String fileType;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCreateById() {
            return createById;
        }

        public void setCreateById(int createById) {
            this.createById = createById;
        }

        public String getCreateByDate() {
            return createByDate;
        }

        public void setCreateByDate(String createByDate) {
            this.createByDate = createByDate;
        }

        public Object getModifyById() {
            return modifyById;
        }

        public void setModifyById(Object modifyById) {
            this.modifyById = modifyById;
        }

        public String getModifyByDate() {
            return modifyByDate;
        }

        public void setModifyByDate(String modifyByDate) {
            this.modifyByDate = modifyByDate;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getResTypeName() {
            return resTypeName;
        }

        public void setResTypeName(String resTypeName) {
            this.resTypeName = resTypeName;
        }

        public int getResTypeId() {
            return resTypeId;
        }

        public void setResTypeId(int resTypeId) {
            this.resTypeId = resTypeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getFileUploadName() {
            return fileUploadName;
        }

        public void setFileUploadName(Object fileUploadName) {
            this.fileUploadName = fileUploadName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.createById);
            dest.writeString(this.createByDate);
//            dest.writeParcelable(this.modifyById, flags);
            dest.writeString(this.modifyByDate);
            dest.writeInt(this.deleted);
            dest.writeString(this.level);
            dest.writeString(this.language);
            dest.writeString(this.resTypeName);
            dest.writeInt(this.resTypeId);
            dest.writeString(this.name);
//            dest.writeParcelable(this.fileUploadName, flags);
            dest.writeString(this.fileType);
            dest.writeString(this.url);
        }

        public ImageUrlsBean() {
        }

        protected ImageUrlsBean(Parcel in) {
            this.id = in.readInt();
            this.createById = in.readInt();
            this.createByDate = in.readString();
//            this.modifyById = in.readParcelable(Object.class.getClassLoader());
            this.modifyByDate = in.readString();
            this.deleted = in.readInt();
            this.level = in.readString();
            this.language = in.readString();
            this.resTypeName = in.readString();
            this.resTypeId = in.readInt();
            this.name = in.readString();
//            this.fileUploadName = in.readParcelable(Object.class.getClassLoader());
            this.fileType = in.readString();
            this.url = in.readString();
        }

        public static final Creator<ImageUrlsBean> CREATOR = new Creator<ImageUrlsBean>() {
            @Override
            public ImageUrlsBean createFromParcel(Parcel source) {
                return new ImageUrlsBean(source);
            }

            @Override
            public ImageUrlsBean[] newArray(int size) {
                return new ImageUrlsBean[size];
            }
        };
    }

    public static class ParamTypeInfoModelsBean implements Parcelable {
        /**
         * attId : 33
         * attName : 季节
         * labelId : 34
         * labelName : 春季
         */

        private int attId;
        private String attName;
        private int labelId;
        private String labelName;

        public int getAttId() {
            return attId;
        }

        public void setAttId(int attId) {
            this.attId = attId;
        }

        public String getAttName() {
            return attName;
        }

        public void setAttName(String attName) {
            this.attName = attName;
        }

        public int getLabelId() {
            return labelId;
        }

        public void setLabelId(int labelId) {
            this.labelId = labelId;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.attId);
            dest.writeString(this.attName);
            dest.writeInt(this.labelId);
            dest.writeString(this.labelName);
        }

        public ParamTypeInfoModelsBean() {
        }

        protected ParamTypeInfoModelsBean(Parcel in) {
            this.attId = in.readInt();
            this.attName = in.readString();
            this.labelId = in.readInt();
            this.labelName = in.readString();
        }

        public static final Creator<ParamTypeInfoModelsBean> CREATOR = new Creator<ParamTypeInfoModelsBean>() {
            @Override
            public ParamTypeInfoModelsBean createFromParcel(Parcel source) {
                return new ParamTypeInfoModelsBean(source);
            }

            @Override
            public ParamTypeInfoModelsBean[] newArray(int size) {
                return new ParamTypeInfoModelsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.createById);
//        dest.writeParcelable(this.createByDate, flags);
        dest.writeInt(this.modifyById);
        dest.writeString(this.modifyByDate);
        dest.writeInt(this.deleted);
        dest.writeString(this.level);
        dest.writeString(this.language);
        dest.writeString(this.name);
        dest.writeString(this.coverUrl);
        dest.writeInt(this.typeId);
        dest.writeString(this.typeName);
        dest.writeDouble(this.costPrice);
        dest.writeDouble(this.nowPrice);
        dest.writeString(this.usingWay);
        dest.writeInt(this.sort);
        dest.writeString(this.moneyType);
        dest.writeString(this.remarks);
        dest.writeInt(this.weight);
//        dest.writeParcelable(this.snList, flags);
        dest.writeList(this.imageUrls);
        dest.writeList(this.paramTypeInfoModels);
    }

    public GoodsDetailBean() {
    }

    protected GoodsDetailBean(Parcel in) {
        this.id = in.readInt();
        this.createById = in.readInt();
//        this.createByDate = in.readParcelable(Object.class.getClassLoader());
        this.modifyById = in.readInt();
        this.modifyByDate = in.readString();
        this.deleted = in.readInt();
        this.level = in.readString();
        this.language = in.readString();
        this.name = in.readString();
        this.coverUrl = in.readString();
        this.typeId = in.readInt();
        this.typeName = in.readString();
        this.costPrice = in.readDouble();
        this.nowPrice = in.readDouble();
        this.usingWay = in.readString();
        this.sort = in.readInt();
        this.moneyType = in.readString();
        this.remarks = in.readString();
        this.weight = in.readInt();
//        this.snList = in.readParcelable(Object.class.getClassLoader());
        this.imageUrls = new ArrayList<>();
        in.readList(this.imageUrls, ImageUrlsBean.class.getClassLoader());
        this.paramTypeInfoModels = new ArrayList<>();
        in.readList(this.paramTypeInfoModels, ParamTypeInfoModelsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<GoodsDetailBean> CREATOR = new Parcelable.Creator<GoodsDetailBean>() {
        @Override
        public GoodsDetailBean createFromParcel(Parcel source) {
            return new GoodsDetailBean(source);
        }

        @Override
        public GoodsDetailBean[] newArray(int size) {
            return new GoodsDetailBean[size];
        }
    };
}
