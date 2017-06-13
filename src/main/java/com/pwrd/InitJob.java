package com.pwrd;


import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * User: ylwys
 * Date: 2016/12/21
 * Time: 14:09
 */

/**
 * 初始化工作
 */
@Component
@Order(value = 1)
public class InitJob implements CommandLineRunner {
//    @Autowired
//    private MoodDao moodDao;
//    @Autowired
//    private AppConfig appConfig;

    @Override
    public void run(String... strings) throws Exception {
//        //每分钟同步一次服务器列表
//        new Timer().schedule(new Server.ServerListTask(), 0, 60 * 1000);
//        //定时衰减排行榜
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//                //只masterTomcat去做热闻衰弱
//                if (!appConfig.masterTomcat) {
//                    return;
//                }
//
//                //清空每个服务器排行榜
//                Set<Integer> serverIdSet = Server.servers.keySet();
//                for (Integer serverId : serverIdSet) {
//                    moodDao.recedeMoodRank(serverId);
//                }
//            }
//        }, 0, appConfig.moodRankRecedePeriod);


    }
}
