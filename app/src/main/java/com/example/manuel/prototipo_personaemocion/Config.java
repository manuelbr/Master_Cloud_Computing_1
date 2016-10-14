package com.example.manuel.prototipo_personaemocion;

/**
 * Created by Manuel on 17/12/2015.
 */
public class Config {
    // Google Console APIs developer key
    private static final String DEVELOPER_KEY = "AIzaSyDzWT2c5dt8A80qdNJs0qjRgGdmZjcVk0w";
    private String YOUTUBE_VIDEO_CODE = "_oEA18Y8gM0";

    public Config(String videoID){
        YOUTUBE_VIDEO_CODE = videoID;
    }

    public String getDeveloperKey(){
        return DEVELOPER_KEY;
    }

    public String getVideoCode(){
        return YOUTUBE_VIDEO_CODE;
    }
}
