package com.github.eternalstone.attachment.wordsource;

import com.github.eternalstone.exception.SensitiveByeException;
import com.github.eternalstone.tools.StringUitl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * to do something
 *
 * @author Justzone on 2022/8/30 19:59
 */
public class SensitiveWordSourceFromUrl implements ISensitiveWordSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveWordSourceFromUrl.class);

    private String url;

    public SensitiveWordSourceFromUrl(String url) {
        this.url = url;
    }

    @Override
    public List<String> loadSource() {
        try {
            URL url = new URL(this.url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            List<String> list = new ArrayList<>();
            while (null != (line = reader.readLine())) {
                if (StringUitl.isNotBlank(line)) {
                    list.add(line.trim());
                }
            }
            LOGGER.info("Successfully downloaded from {}", this.url);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SensitiveByeException("the url resource download failed");
        }
    }

}
