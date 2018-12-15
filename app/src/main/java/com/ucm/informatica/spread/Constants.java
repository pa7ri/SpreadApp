package com.ucm.informatica.spread;

import android.os.Environment;

import java.math.BigInteger;

import static android.os.Environment.getExternalStorageState;

public final class Constants {

    public static final String INFURA_PATH = "https://rinkeby.infura.io/";
    public static final String INFURA_PUBLIC_PROYECT_ADDRESS = "2dde3de7006043b88fb4e35189745853";

    public static final class Contract {
        public static final BigInteger GAS_PRICE = BigInteger.valueOf(10);
        public static final BigInteger GAS_LIMIT = BigInteger.valueOf(100000);
        public static final String CONTRACT_ADDRESS = "0xd95091325d005d64c843352f54c5a275acdee5a5";
    }

    public static final class Map {
        public static final String MAP_TOKEN ="pk.eyJ1IjoiaGltb2NoaSIsImEiOiJjam9pYTh6encwNmV5M3BwZnRqdjZmZXQzIn0.6nLIwyqEDZ9o5KFquUHaeA";
    }

    public static final class Wallet {
        public static final String WALLET_FILE = "personalWallet";
        public static final String WALLET_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
