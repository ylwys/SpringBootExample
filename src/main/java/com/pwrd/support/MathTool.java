package com.pwrd.support;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ylwys
 * Date: 12-1-7
 * Time: 下午3:48
 */
public final class MathTool {

    /**
     * 返回[n,m]中的一个随机数
     *
     * @param n 起始点
     * @param m 结束点
     * @return int
     */
    public static int getRandomInScope(int n, int m) {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(m - n + 1) + n;
    }

    /**
     * 获取概率
     *
     * @param f f%就是概率
     * @return boolean
     */
    public static boolean getProbability(float f) {
        if (f <= 0) {
            return false;
        }
        boolean b = false;
        int i = (int) (f * 100);
        int randomNum = getRandomInScope(1, 10000);
        if (randomNum <= i) {
            b = true;
        }
        return b;
    }

    /**
     * 四舍五入
     *
     * @param f Float
     * @return int
     */
    public static int getRoundInt(float f) {
        return new BigDecimal(String.valueOf(f)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * 获取随机权重的区间
     *
     * @param weightList 权重列表
     * @return int
     */
    public static int getIndexWeight(List<Integer> weightList) {
        int total = 0;
        for (int weight : weightList) {
            total += weight;
        }

        int random = getRandomInScope(1, total);
        int limit = 0;
        for (int i = 0; i < weightList.size(); i++) {
            limit += weightList.get(i);
            if (random <= limit) {
                return i;
            }
        }
        return getRandomInScope(0, weightList.size() - 1);
    }

    /**
     * 获取i属于哪个区间（大于等于当前区间，小于后区间）
     */
    public static int getLocal(List<Integer> list, int a) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (a >= list.get(i)) {
                return i;
            }
        }
        return -1;
    }
}
