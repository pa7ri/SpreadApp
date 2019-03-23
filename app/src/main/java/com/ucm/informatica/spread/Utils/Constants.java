package com.ucm.informatica.spread.Utils;

import java.math.BigInteger;

public final class Constants {

    public static final int NUMBER_TABS = 5;
    public static final int REQUEST_IMAGE_POSTER = 0;
    public static final int REQUEST_IMAGE_POSTER_CAMERA = 1;
    public static final int REQUEST_IMAGE_POSTER_GALLERY = 2;

    public static final String INFURA_PATH = "https://rinkeby.infura.io/";
    public static final String INFURA_PUBLIC_PROYECT_ADDRESS = "2dde3de7006043b88fb4e35189745853";

    public static final class Contract {
        public static final BigInteger GAS_PRICE = BigInteger.valueOf(1500000000); //1,5 GWEI
        public static final BigInteger GAS_LIMIT = BigInteger.valueOf(176600);
        public static final String CONTRACT_ADDRESS_ALERT = "0x715b16b2e6d68ac3c4f270621e11e3a2798346a4";
        public static final String CONTRACT_ADDRESS_POSTER = "0x5a1575f6b1577b5d0817f6f075ff441552fa4259";
    }

    public static final class Map {
        public static final String MAP_TOKEN ="pk.eyJ1IjoiaGltb2NoaSIsImEiOiJjam9pYTh6encwNmV5M3BwZnRqdjZmZXQzIn0.6nLIwyqEDZ9o5KFquUHaeA";
        public static final String MAP_STYLE ="mapbox://styles/himochi/cjois77wl020w2sk7muy2htur";
        public static final String POLYGON_LAYER ="polygonLayer";
        public static final String POINT_LAYER ="pointLayer";


        public static final String CAMERA_BOUND_LATITUDE_START = "CAMERA_BOUND_LATITUDE_START";
        public static final String CAMERA_BOUND_LATITUDE_END = "CAMERA_BOUND_LATITUDE_END";
        public static final String CAMERA_BOUND_LONGITUDE_START = "CAMERA_BOUND_LONGITUDE_START";
        public static final String CAMERA_BOUND_LONGITUDE_END = "CAMERA_BOUND_LONGITUDE_END";

        public static final String NOTIFICATION_TOPIC_PREF = "NOTIFICATION_TOPIC_PREF";

        public static final int ZOOM_MARKER = 15;

    }

    public static final class Wallet {
        public static final String WALLET_FILE = "personalWallet";
    }

    public static final class LocalPreferences {
        public static final String NOTIFICATION_TOPIC_PREF = "NOTIFICATION_TOPIC_PREF";

        public static final String PROFILE_PREF = "PROFILE_PREF";

        public static final String NAME_PREF = "NAME_PREF";
        public static final String AGE_PREF = "AGE_PREF";
        public static final String TSHIRT_PREF = "TSHIRT_PREF";
        public static final String PANTS_PREF = "PANTS_PREF";
        public static final String KEY_PREF = "KEY_PREF";
        public static final String RESPONSE_PREF = "RESPONSE_PREF";
    }

    public static final class Notifications {
        public static final String API_FCM_URL = "https://fcm.googleapis.com/";
        public static final String NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID_FCM";
        public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";
        public static final String TOPICS = "/topics/";

        public static final String NOTIFICATION_DATA="NOTIFICATION_DATA";

        public static final String NOTIFICATION_DATA_UNKNOWN = "Desconocido";

        public static final String NOTIFICATION_DATA_TITLE = "NOTIFICATION_DATA_TITLE";
        public static final String NOTIFICATION_DATA_TITLE_CONTENT = "Petición de ayuda";
        public static final String NOTIFICATION_DATA_SUBTITLE = "NOTIFICATION_DATA_SUBTITLE";
        public static final String NOTIFICATION_DATA_SUBTITLE_CONTENT = "Alguien cerca de ti te necesita";

        public static final String NOTIFICATION_DATA_NAME = "NOTIFICATION_DATA_NAME";
        public static final String NOTIFICATION_DATA_AGE = "NOTIFICATION_DATA_AGE";
        public static final String NOTIFICATION_DATA_LATITUDE = "NOTIFICATION_DATA_LATITUDE";
        public static final String NOTIFICATION_DATA_LONGITUDE = "NOTIFICATION_DATA_LONGITUDE";
        public static final String NOTIFICATION_DATA_TSHIRT_COLOR = "NOTIFICATION_DATA_TSHIRT_COLOR";
        public static final String NOTIFICATION_DATA_PANTS_COLOR = "NOTIFICATION_DATA_PANTS_COLOR";
        public static final String NOTIFICATION_DATA_WATCHWORD_KEY = "NOTIFICATION_DATA_WATCHWORD_KEY";
        public static final String NOTIFICATION_DATA_WATCHWORD_RESPONSE = "NOTIFICATION_DATA_WATCHWORD_RESPONSE";
    }
}
