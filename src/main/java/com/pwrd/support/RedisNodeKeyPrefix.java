package com.pwrd.support;

/**
 * User: ylwys
 * Date: 2016/11/24
 * Time: 14:09
 */

/**
 * redis节点key前缀
 */
public interface RedisNodeKeyPrefix {
    String HUMAN_DATA_KEY_PRE = "human_data_";                                  //玩家数据nodeKey
    String HUMAN_OWN_WISH_KEY_PRE = "human_own_wish_";                          //玩家全部祝福列表nodeKey
    String HUMAN_OWN_MOOD_KEY_PRE = "human_own_mood_";                          //玩家全部心情列表nodeKey
    String HUMAN_OWN_MESSAGE_KEY_PRE = "human_own_message_";                    //玩家全部留言列表nodeKey

    String MOOD_DATA_KEY_PRE = "mood_data_";                                    //心情数据nodeKey
    String MOOD_OWN_DISCUSS_KEY_PRE = "mood_own_discuss_";                      //心情全部评论列表nodeKey
    String MOOD_OWN_PRAISE_KEY_PRE = "mood_own_praise_";                        //心情全部点赞列表nodeKey
    String MOOD_ALL_PRAISE_HUMAN_ID_KEY_PRE = "mood_all_praise_human_id_";      //心情全部点赞的humanId列表nodeKey
    String MOOD_HOT_RANGE_KEY_PRE = "mood_hot_range_";                         //心情热闻排行榜nodeKey


    String DISCUSS_DATA_KEY_PRE = "discuss_data_";                              //评论数据nodeKey

    String PRAISE_DATA_KEY_PRE = "praise_data_";                                //点赞数据nodeKey

    String MESSAGE_DATA_KEY_PRE = "message_data_";                              //留言数据nodeKey

    String WISH_DATA_KEY_PRE = "wish_data_";                                    //祝福数据nodeKey


}
