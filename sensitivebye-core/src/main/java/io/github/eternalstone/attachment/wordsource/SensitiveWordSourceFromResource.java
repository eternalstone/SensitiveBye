package io.github.eternalstone.attachment.wordsource;

import io.github.eternalstone.exception.SensitiveByeException;
import io.github.eternalstone.tools.StringUitl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 从resource目录下加载指定的词源文件
 *
 * @author Justzone on 2022/8/30 19:16
 */
public class SensitiveWordSourceFromResource implements ISensitiveWordSource {

    private String resourceFile = "sensitive.txt";

    public SensitiveWordSourceFromResource() {

    }

    public SensitiveWordSourceFromResource(String filename) {
        this.resourceFile = filename;
    }

    @Override
    public List<String> loadSource() {
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceFile);
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(reader)) {
            String line;
            List<String> list = new ArrayList<>();
            while (null != (line = br.readLine())) {
                if (StringUitl.isNotBlank(line)) {
                    list.add(line.trim());
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SensitiveByeException("the resource file read failed");
        }
    }

    public void renameFile(String filename) {
        resourceFile = filename;
    }

}
