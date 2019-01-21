package com.example.elasticsearch.esentity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import lombok.Data;

@Document(indexName = "demo", type = "store")
@Data
public class ESStore {
    @Id
    private String id;
    @Field(index = FieldIndex.not_analyzed)
    private String storeId;//返回前端使用
    @Field(index = FieldIndex.not_analyzed)
    private Double distance;//计算后返回前端使用
    @Field(index = FieldIndex.not_analyzed)
    private String appId;
    @Field(index = FieldIndex.not_analyzed)
    private String storeLogo;
    @Field(index = FieldIndex.not_analyzed)
    private Integer storeCredit = 0;
    @Field(index = FieldIndex.not_analyzed)
    private BigDecimal storeDesccredit = new BigDecimal("0");
    @Field(index = FieldIndex.not_analyzed)
    private BigDecimal storeServicecredit = new BigDecimal("0");
    @Field(index = FieldIndex.not_analyzed)
    private String cityId;
    @Field(index = FieldIndex.not_analyzed)
    private String provinceId;
    @Field(index = FieldIndex.not_analyzed)
    private Integer isOpen = 0;
    @Field(index = FieldIndex.not_analyzed)
    private String areaId;
    @Field(index = FieldIndex.not_analyzed)
    private String areaInfo;
    @Field(index = FieldIndex.not_analyzed)
    private String extendInfo;
    @Field(index = FieldIndex.not_analyzed)
    private String storeCloseInfo;
    @Field(index = FieldIndex.not_analyzed)
    private BigDecimal storeDeliverycredit = new BigDecimal("0");
    @Field(index = FieldIndex.not_analyzed)
    private Integer storeRecommend = 0;
    @Field(index = FieldIndex.not_analyzed)
    private String position;
    @Field(index = FieldIndex.not_analyzed)
    private Integer storeSales = 0;
    @Field(index = FieldIndex.not_analyzed)
    private String isDel = "N";
    @Field(index = FieldIndex.not_analyzed)
    private Integer storeState = 2;
    @Field(index = FieldIndex.not_analyzed)
    private Date createTime;
    @Field(index = FieldIndex.not_analyzed)
    private String storeWorkingtime;
    @Field(index = FieldIndex.not_analyzed)
    private Integer storeCollect = 0;
    @Field(index = FieldIndex.not_analyzed)
    private String memberId;
    @Field(index = FieldIndex.not_analyzed)
    private Integer isPlatformStore = 0;
    @Field(index = FieldIndex.not_analyzed)
    private Integer isSettlementAccount = 1;
    @Field(index = FieldIndex.not_analyzed)
    private String joininMessage;
    @Field(index = FieldIndex.not_analyzed)
    private String memberName;
    @Field(index = FieldIndex.not_analyzed)
    private Date storeAddtime;
    @Field(index = FieldIndex.not_analyzed)
    private String storeAvatar;
    @Field(index = FieldIndex.not_analyzed)
    private String storeBanner;
    @Field(index = FieldIndex.not_analyzed)
    private BigDecimal storeFreePrice = new BigDecimal("0");
    private Integer storeHuodaofk = 0;
    @Field(index = FieldIndex.not_analyzed)
    private String storePhone;
    @Field(index = FieldIndex.not_analyzed)
    private String storeQq;
    @Field(index = FieldIndex.not_analyzed)
    private String storeTheme = "default";
    @Field(index = FieldIndex.not_analyzed)
    private String storeWw;
    @Field(index = FieldIndex.not_analyzed)
    private String joininState = "10";
    @Field(index = FieldIndex.not_analyzed)
    private String contactsName;
    @Field(index = FieldIndex.not_analyzed)
    private String contactsPhone;
    @Field(index = FieldIndex.not_analyzed)
    private String contactsEmail;
    @Field(index = FieldIndex.not_analyzed)
    private String businessLicence;
    @Field(index = FieldIndex.not_analyzed)
    private String businessLicenceFile;
    @Field(index = FieldIndex.not_analyzed)
    private String identityCardFront;
    @Field(index = FieldIndex.not_analyzed)
    private String identityCardBack;
    @Field(index = FieldIndex.not_analyzed)
    private String storeJoininId;
    @Field(index = FieldIndex.not_analyzed)
    private String companyAreaId;

    @GeoPointField
    private GeoPoint location;

    @Field(index = FieldIndex.analyzed, store = true, type = FieldType.String, searchAnalyzer = "ik", analyzer = "ik")
    private String storeName;

    @Field(index = FieldIndex.analyzed, store = true, type = FieldType.String, searchAnalyzer = "ik", analyzer = "ik")
    private String storeCompanyName;

    @Field(index = FieldIndex.analyzed, store = true, type = FieldType.String, searchAnalyzer = "ik", analyzer = "ik")
    private String storeAddress;

    @Field(index = FieldIndex.analyzed, store = true, type = FieldType.String, searchAnalyzer = "ik", analyzer = "ik")
    private String storeKeywords;

    @Field(index = FieldIndex.analyzed, store = true, type = FieldType.String, searchAnalyzer = "ik", analyzer = "ik")
    private String storeMainbusiness;

    @Field(index = FieldIndex.analyzed, store = true, type = FieldType.String, searchAnalyzer = "ik", analyzer = "ik")
    private String companyName;

    @Field(index = FieldIndex.analyzed, store = true, type = FieldType.String, searchAnalyzer = "ik", analyzer = "ik")
    private String companyAddress;
}
