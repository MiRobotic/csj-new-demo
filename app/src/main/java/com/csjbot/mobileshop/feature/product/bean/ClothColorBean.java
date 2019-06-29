package com.csjbot.mobileshop.feature.product.bean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/12/5 15:36
 * @email 714081644@qq.com
 */

public class ClothColorBean {

    /**
     * message : ok
     * result : ["香芋","金黄","亮银","灰驼","灰白","粉色","兰色","黑条彩条","兰灰","豹纹彩花","灰色","黄条","锖色","浅黄","绿格","彩条","黑花","黑点","红色","深绿","咖绿","浅灰银色","彩色","米格","兰格彩格","银灰","灰咖","银色","粉红","红咖","黄色","卡其","红花","黑花彩花","米条","黄咖","兰点","砖红","灰点","粉花","兰花","绿色彩花","绿条","灰花","米色","白格","米灰","枣红","驼格","绿色迷彩","浅绿","深紫","红点","玫红","咖绿迷彩","兰条彩条","绿色","墨绿","棕红","彩花","驼色","米白","银花","咖格","灰黑","红花彩花","豹纹","驼条","黄花","桔条","咖条","灰绿","白条","浅紫","米花","深灰","桔色","绿点","咖色","黑格","紫条","白色","绿花","黑条","深驼","驼花","紫色","黄绿","黑灰","灰紫","桔粉","米黄","灰格","兰格","浅兰","浅灰","深咖","黄色银色","桔花","咖花","蓝绿","黑色","白点","金色","红格","湖兰","紫红","金驼","浅驼","白花","深红","粉格","黑白","咖条彩条","无","粉花彩色","紫花","咖点","咖花彩花","浅粉","白花彩花","红条","Y0","粉条","彩格","金色银色","兰条","浅咖","藏蓝","未定义1","灰条"]
     * status : 200
     */

    private String message;
    private String status;
    private List<String> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
