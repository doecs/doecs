package com.doecs.core.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaUtils {
    public static Map<String, Tag> readMetaTags(File file) {
//        File file = new File("C:\\temp\\photo_test\\IMG_20170722_132453.jpg");
//        File file = new File("C:\\JAY MI5 BAK\\Camera\\VID_20150612_184930.3gp");
//        File file = new File("C:\\JAY MI5 BAK\\Camera\\VID_20180924_142731.mp4");
//        File file = new File("C:\\JAY MI5 BAK\\Camera\\IMG_20180217_064728.jpg");
        Metadata metadata;
        Map<String, Tag> tagMap = new HashMap<>();

        try {
            metadata = ImageMetadataReader.readMetadata(file);
            Iterator<Directory> it = metadata.getDirectories().iterator();
            while (it.hasNext()) {
                Directory mediaDir = it.next();
                Iterator<Tag> mediaTags = mediaDir.getTags().iterator();
                while (mediaTags.hasNext()) {
                    Tag tag = (Tag) mediaTags.next();
                    System.out.println(tag);
                    tagMap.put(tag.getTagName(), tag);
                }
            }
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        }
        return tagMap;
    }

    public static BigDecimal duFenMiaoToLat(String latlng) {
        latlng = latlng.replaceAll(" ", "");
        Pattern patt = Pattern.compile("(?<du>[.0-9]+)°(?<fen>[.0-9]+)'(?<miao>[.0-9]+)\"");
        Matcher match = patt.matcher(latlng);
        if (match.find()) {
            String duStr = match.group("du");
            String fenStr = match.group("fen");
            String miaoStr = match.group("miao");
            double du = Double.parseDouble(duStr);
            double fen = Double.parseDouble(fenStr);
            double miao = Double.parseDouble(miaoStr);

            if (du < 0) {
                return BigDecimal.valueOf(-(Math.abs(du) + (fen + (miao / 60)) / 60));
            }
            return BigDecimal.valueOf(du + (fen + (miao / 60)) / 60);
        }else {
            return null;
        }
    }

    public static BigDecimal duFenMiaoToLon(String latlng) {
        latlng = latlng.replaceAll(" ", "");
        Pattern patt = Pattern.compile("(?<du>[.0-9]+)°(?<fen>[.0-9]+)'(?<miao>[.0-9]+)\"");
        Matcher match = patt.matcher(latlng);
        if (match.find()) {
            String duStr = match.group("du");
            String fenStr = match.group("fen");
            String miaoStr = match.group("miao");
            double du = Double.parseDouble(duStr);
            double fen = Double.parseDouble(fenStr);
            double miao = Double.parseDouble(miaoStr);
            if (du < 0) {
                return BigDecimal.valueOf(-(Math.abs(du) + (fen + (miao / 60)) / 60));
            }else{
                return BigDecimal.valueOf(du + (fen + (miao / 60)) / 60);
            }
        } else {
            return null;
        }
    }
}
