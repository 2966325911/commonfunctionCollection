package com.cloudoc.share.yybpg.customview.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author : Vic
 * time   : 2018/06/25
 * desc   : 读取温度计的值
 */
public class ThermalStatusJ {

    public final double temperature;
    public ThermalStatusJ(){
        File thermalDir = new File("/sys/class/thermal");
        File[]  thermalZoneFiles = thermalDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && pathname.getName().startsWith("thermal_zone");
            }
        });

        int sum = 0;
        int count = 0;
        for(File thermalZoneFile : thermalZoneFiles) {
            BufferedReader reader  = null;
            try {
                reader = new BufferedReader(new FileReader(new File(thermalZoneFile,"temp")));

                String line = reader.readLine();
                sum += Integer.valueOf(line);
                count ++;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }

        if(count > 0) {
            temperature = sum / 1000.0 / count;
        } else {
            temperature = 0;
        }
    }
}
