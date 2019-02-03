package com.ucm.informatica.spread;

import java.math.BigInteger;

public final class Constants {

    public static final int NUMBER_TABS = 5;

    public static final String INFURA_PATH = "https://rinkeby.infura.io/";
    public static final String INFURA_PUBLIC_PROYECT_ADDRESS = "2dde3de7006043b88fb4e35189745853";

    public static final class Contract {
        public static final BigInteger GAS_PRICE = BigInteger.valueOf(1500000000); //1,5 GWEI
        public static final BigInteger GAS_LIMIT = BigInteger.valueOf(46600);
        public static final String CONTRACT_ADDRESS = "0xd3fa5cfddee1a22968b998adc4441ea685428f09"; //TODO : change
        /*
            nContract -> 0x23d3a5a22c4c916baf40eda98beae34630e628d5
            nameContract -> 0xd3fa5cfddee1a22968b998adc4441ea685428f09
            CoordContract -> 0xaa0fdda8c7aa2ee7ff2a13f4e5902360182ca4dc
         */
    }

    public static final class Map {
        public static final String MAP_TOKEN ="pk.eyJ1IjoiaGltb2NoaSIsImEiOiJjam9pYTh6encwNmV5M3BwZnRqdjZmZXQzIn0.6nLIwyqEDZ9o5KFquUHaeA";
    }

    public static final class Wallet {
        public static final String WALLET_FILE = "personalWallet";

        public static final String LOCAL_NAME_CONTRACT = "localNameContract";
        public static final String LOCAL_SMART_CONTRACT = "localSmartContract";
    }
}
