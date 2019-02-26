package com.ucm.informatica.spread.Utils;

import java.math.BigInteger;

public final class Constants {

    public static final int NUMBER_TABS = 5;
    public static final int REQUEST_IMAGE_POSTER = 0;
    public static final int REQUEST_IMAGE_PROFILE = 1;
    public static final int REQUEST_IMAGE_POSTER_CAMERA = 2;
    public static final int REQUEST_IMAGE_POSTER_GALLERY = 3;



    public static final String INFURA_PATH = "https://rinkeby.infura.io/";
    public static final String INFURA_PUBLIC_PROYECT_ADDRESS = "2dde3de7006043b88fb4e35189745853";

    public static final class Contract {
        public static final BigInteger GAS_PRICE = BigInteger.valueOf(1500000000); //1,5 GWEI
        public static final BigInteger GAS_LIMIT = BigInteger.valueOf(176600);
        public static final String CONTRACT_ADDRESS_ALERT = "0x715b16b2e6d68ac3c4f270621e11e3a2798346a4";
        public static final String CONTRACT_ADDRESS_POSTER = "0xf3aa1f5b39301173e637db0425eb4ee55ad51cc6";
    }

    public static final class Map {
        public static final String MAP_TOKEN ="pk.eyJ1IjoiaGltb2NoaSIsImEiOiJjam9pYTh6encwNmV5M3BwZnRqdjZmZXQzIn0.6nLIwyqEDZ9o5KFquUHaeA";
        public static final String MAP_STYLE ="mapbox://styles/himochi/cjois77wl020w2sk7muy2htur";
        public static final String POLYGON_LAYER ="polygonLayer";
        public static final String POINT_LAYER ="pointLayer";

        public static final String UPDATE_MAP = "UPDATE_MAP";
        public static final String IMAGE_POSTER = "IMAGE_POSTER";
    }

    public static final class Wallet {
        public static final String WALLET_FILE = "personalWallet";

    }
}
