package com.pjb.springbootcampusshop.util.weixin.message.resp;

/** 
 * 音乐消息 
 *  
 * @author xiangli
 * @date 2015-02-10
 */  
public class MusicMessage extends BaseMessage {  
    // 音乐  
    private Music Music;  
  
    public Music getMusic() {  
        return Music;  
    }  
  
    public void setMusic(Music music) {  
        Music = music;  
    }  
}  