package com.ucm.informatica.spread;

import java.math.BigInteger;

import static android.os.Environment.getExternalStorageState;

public final class Constants {

    public static final String INFURA_PATH = "https://rinkeby.infura.io/";
    public static final String INFURA_PUBLIC_PROYECT_ADDRESS = "2dde3de7006043b88fb4e35189745853";

    public static final class Contract {
        public static final BigInteger GAS_PRICE = BigInteger.valueOf(1);
        public static final BigInteger GAS_LIMIT = BigInteger.valueOf(100000);
    }

    public static final class Wallet {
        public static final String WALLET_FILE = "personalWallet";
        public static final String WALLET_FILE_PATH = getExternalStorageState();
    }
}
