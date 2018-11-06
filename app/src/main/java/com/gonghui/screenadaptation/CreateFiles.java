package com.gonghui.screenadaptation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class CreateFiles {
    private static String fileName = "dimens.xml";
    private static String folderName = "screenadaptationbase320/src/main/res/";//输出目录
    private static int[] screenSize = {320, 360, 380, 390, 410};//屏幕size的集合 以320为基础倍率
    private static List<String> adaptationFolders = new ArrayList<>();//与倍率对应的文件夹数组

    /**
     * 添加目录
     */
    private static void createFolder() {
        for (int size : screenSize) {
            adaptationFolders.add("values-sw" + size + "dp/");
        }
    }

    /**
     * 循环创建文件内容
     * @param sizePosition 循环position，用于获取当前屏幕size
     * @return 写入文件的内容
     */
    private static String createContent(int sizePosition) {
        StringBuilder content = new StringBuilder();
        content.append("<resources>\n");
        BigDecimal rate = new BigDecimal((float) screenSize[sizePosition] / (float) screenSize[0]);//换算倍率
        //附加项
        content.append("\t<dimen name=\"dp02\">" + rate.multiply(new BigDecimal(0.2)).floatValue() + "dp</dimen>\n");
        content.append("\t<dimen name=\"dp05\">" + rate.multiply(new BigDecimal(0.5)).floatValue() + "dp</dimen>\n");
        //------
        //------dp
        for (int i = 0; i <= 1000; i++) {
            content.append("\t<dimen name=\"dp");
            content.append(i);
            content.append("\">");
            content.append(rate.multiply(new BigDecimal(i)).floatValue());//进行换算
            content.append("dp</dimen>\n");
        }
        //------sp
        for (int i = 0; i <= 100; i++) {
            content.append("\t<dimen name=\"sp");
            content.append(i);
            content.append("\">");
            content.append(rate.multiply(new BigDecimal(i)).floatValue());//进行换算
            content.append("sp</dimen>\n");
        }
        content.append("</resources>");
        return content.toString();
    }

    private static void write() {
        try {
            for (int i = 0; i < adaptationFolders.size(); i++) {
                File folder = new File(folderName + adaptationFolders.get(i));
                if (!folder.isDirectory())
                    folder.mkdirs();

                File file = new File(folder, fileName);
                if (!file.exists())
                    file.createNewFile();

                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(createContent(i));
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String... args) {
        createFolder();
        write();
    }
}
