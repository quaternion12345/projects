package com.ssafy.utf.common.util;

import java.util.Random;

public class RandomCodeGenerator {
    public String getCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rand = new Random().nextInt(62);
            if (0 <= rand && rand < 10) {
                stringBuilder.append((char) (rand + '0'));
            } else if (10 <= rand && rand < 36) {
                stringBuilder.append((char) (rand - 10 + 'A'));
            } else if (36 <= rand && rand < 62) {
                stringBuilder.append((char) (rand - 36 + 'a'));
            }
        }
        return stringBuilder.toString();
    }
}
