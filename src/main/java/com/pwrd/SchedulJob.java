package com.pwrd;


import org.springframework.stereotype.Component;


/**
 * User: ylwys
 * Date: 2017/1/23
 * Time: 15:05
 */

/**
 * 定时队列
 */
@Component
public class SchedulJob {
//    @Autowired
//    private MoodDao moodDao;
//    @Autowired
//    private AppConfig appConfig;

//    /**
//     * 清空热闻排行榜(每周一凌晨)
//     */
//    @Scheduled(cron = "0 59 23 ? * SUN")
//    public void clearMoodRank() {
//
//        //只masterTomcat去做热闻衰弱
//        if (!appConfig.masterTomcat) {
//            return;
//        }
//
//        //清空每个服务器排行榜
//        Set<Integer> serverIdSet = Server.servers.keySet();
//        for (Integer serverId : serverIdSet) {
//            moodDao.recedeMoodRank(serverId);
//        }
//    }
}
