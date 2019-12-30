package com.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * DESC
 *
 * @author cc zhao
 * @date 2019/10/12
 */
public class IOUtils {

    public static void main(String[] args) throws Exception {
        bagSize();
    }

    public static void bagSize() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/admin/Downloads/箱包尺寸.txt")));
        String line = null;
        Set<String> sizeSet = Sets.newHashSet();
        while ((line = reader.readLine()) != null) {
            if (Assert.isNotEmpty(line)) {
                sizeSet.add(line + ",");
            }
        }

        System.out.println(JSON.toJSONString(sizeSet));
    }

    public static void fileOperation() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/admin/Downloads/waitAccounted.txt")));
        int count = 0;
        // 统计已经签收，但未结清的订单
        List<HashMap<String, String>> oidList = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            count++;

            String oid = line.substring(0, line.indexOf(",")).replace("\"", "").trim();
            if (Objects.equals(oid, "oid")) {
                continue;
            }

            String extraData = line.substring(line.indexOf(",") + 2, line.length() - 1).trim().replace("\\", "");
            JSONObject json = JSONObject.parseObject(extraData);

            boolean existBuyerSignTime = json.containsKey("buyerSignTime");

            if (json.containsKey("buyerSignTime")) {
                String buyerSignTime = json.getString("buyerSignTime");
                if (json.containsKey("soldTime")) {
                    long soldTime = json.getLong("soldTime");
                    // soldTime <= buyerSignTime
                    LocalDateTime sign = TimeUtils.str2LocalDateTime(buyerSignTime, "yyyy-MM-dd HH:mm:ss");
                    LocalDateTime sold = LocalDateTime.ofEpochSecond(soldTime / 1000, 0, ZoneOffset.ofHours(8));
                    if (sold.isAfter(sign)) {
                        oidList.add(new HashMap<String, String>() {{
                            put(oid, "发货时间 晚于 签收时间");
                        }});
                    }
                } else {
                    // 有签收时间没有发货时间
                    oidList.add(new HashMap<String, String>() {{
                        put(oid, "有签收时间没有发货时间");
                    }});
                }
            }

            if (json.containsKey("soldTime")) {
                // 时间超过7天的
                LocalDateTime sold = LocalDateTime.ofEpochSecond(json.getLong("soldTime") / 1000, 0, ZoneOffset.ofHours(8));
                if (Duration.between(sold, LocalDateTime.now()).toDays() >= 7L) {
                    oidList.add(new HashMap<String, String>() {{
                        put(oid, "没有签收时间 有发货时间，且超过7天的");
                    }});
                }
            }
        }
        System.out.println(JSONObject.toJSONString(oidList));
        System.out.println(count);
    }

}
