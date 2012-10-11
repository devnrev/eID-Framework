/*
 * CPACECore.java
 *
 * Copyright (C) 2012, Axel
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 * have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package libeac;

import common.CConverter;
import common.CFactoryHelper;
import common.crypto.*;
import common.exceptions.ParameterException;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 14.12.11
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */

/**
 * Core implementation of the actual PACE protocol steps
 */
public class PACECore {
    private byte[] kPassword_;
    private byte[] nonce_S_;
    private BigInteger sk_x_;
    private IECPoint pk_X_;
    private BigInteger sk_PCD_;
    private IECPoint pk_PCD_;
    private IECPoint base_G_;
    private IECPoint base_G_Dash_;
    private IECPointFP H_;
    private IECPointFP pk_PICC_;
    private BigInteger sk_K_;
    private IECCurveFP curve_;
    private byte[] t_PCD_;
    private byte[] t_PICC_Dash_;
    private byte[] k_MAC_;
    private byte[] k_ENC_;
    private PACEInfos paceInfos_;
    private KeyDerivation kdf_;
    private  byte[] random_;
    private boolean testingMode_;
    private IX9ECParameters curveSpec_;

    /**
     * Constructor
     * @param paceInfos Initialization parameters for the protocol
     * @throws ParameterException
     */
    public PACECore(PACEInfos paceInfos) throws ParameterException {
        paceInfos_ = paceInfos;
        ICryptoProvider cryptoProvider = CFactoryHelper.getCryptoProvider();
        switch (paceInfos_.getParameterId()){
            case 8:
                curveSpec_ = cryptoProvider.getX9ECParametersFromSECCurve("secp192r1");
                break;
            case 9:
                curveSpec_ = cryptoProvider.getX9ECParametersFromTeleTrusTCurve("brainpoolp192r1");
                break;
            case 10:
                curveSpec_ = cryptoProvider.getX9ECParametersFromSECCurve("secp224r1");
                break;
            case 11:
                curveSpec_ = cryptoProvider.getX9ECParametersFromTeleTrusTCurve("brainpoolp224r1");
                break;
            case 12:
                curveSpec_ = cryptoProvider.getX9ECParametersFromSECCurve("secp256r1");
                break;
            case 13:
                curveSpec_ = cryptoProvider.getX9ECParametersFromTeleTrusTCurve("brainpoolp256r1");
                break;
            case 14:
                curveSpec_ = cryptoProvider.getX9ECParametersFromTeleTrusTCurve("brainpoolp320r1");
                break;
            case 15:
                curveSpec_ = cryptoProvider.getX9ECParametersFromSECCurve("secp384r1");
                break;
            case 16:
                curveSpec_ = cryptoProvider.getX9ECParametersFromTeleTrusTCurve("brainpoolp384r1");
                break;
            case 17:
                curveSpec_ = cryptoProvider.getX9ECParametersFromTeleTrusTCurve("brainpoolp512r1");
                break;
            case 18:
                curveSpec_ = cryptoProvider.getX9ECParametersFromSECCurve("secp521r1");
                break;
            default:
                throw new ParameterException("PACE info parameter Id does not match any curve. paramId: "
                        + paceInfos_.getParameterId());
        }
        base_G_ = curveSpec_.getG();
        curve_ = curveSpec_.getCurveFP();
        kdf_ = new KeyDerivation(CryptoTypes.EKeyLength.LEN_128); //TODO: make dependent of pace domain parameters

        //Testing things
        random_ = new byte[32];
        testingMode_ = false;


    }

    /**
     * Wrap the password used in the protocol
     * @param szPassword Password
     */
    public void calcKPassword(String szPassword){
       byte[] byaPassword = szPassword.getBytes();
       kPassword_ = kdf_.derivateForPassword(byaPassword);
    }

    /**
     * Decrypt the nonce received from the card
     * @param byaData Nonce data
     */
    public void decryptNonce(byte[] byaData){
        ISymmetricBlockCipher aesProc = CFactoryHelper.getCryptoProvider().createAESProcessor();
        byte[] byaIV = new byte[CryptoTypes.getKeyLen(CryptoTypes.EKeyLength.LEN_128)/8]; //TODO: make dependend of pace domain params
        aesProc.initialize(byaIV, kPassword_, CryptoTypes.EBlockMode.CBC, CryptoTypes.EKeyLength.LEN_128);
        nonce_S_ = aesProc.decrypt(byaData) ;
    }

    /**
     * Return the current random number
     * @return Random number
     */
    public  byte[] getRand(){
         return random_;

    }

    /**
     * Calculate the ephemeral key pair SK_x and PK_X
     * @return PK_X
     */
    public IECPoint calcX(){
        if(!testingMode_){
            //byte[] random_ = new byte[32];
            random_ = new byte[32];
            CFactoryHelper.getCryptoProvider().getRandomNumberGenerator().nextBytes(random_);
            sk_x_ = new BigInteger(1, random_);
        }
        pk_X_ = base_G_.multiply(sk_x_);
        return pk_X_;
    }

    /**
     * Calculate the new generator G'
     * @param Y Public key received from the card
     * @return G'
     */
    private IECPoint calcGDash(IECPointFP Y){
        H_ = Y.multiply(sk_x_);
        BigInteger ns = new BigInteger(1, nonce_S_);
        return base_G_.multiply(ns).add(H_);
    }

    /**
     * Calculate the ephemeral key pair SK_PCD and PK_PCD
     * @param Y Public key received from the card
     * @return PK_PCD
     */
    public IECPoint calcPkPcd(IECPointFP Y){
        base_G_Dash_ = calcGDash(Y);
        if(!testingMode_){
        //  byte[] random_ = new byte[32];
            random_ = new byte[32];
            CFactoryHelper.getCryptoProvider().getRandomNumberGenerator().nextBytes(random_);
            sk_PCD_ = new BigInteger(1, random_);
        }
        pk_PCD_ = base_G_Dash_.multiply(sk_PCD_);
        return pk_PCD_;
    }

    /**
     * Calculate the authenticated token T_PCD
     * @param PK_PICC Public key received from the card
     * @return T_PCD
     */
    public byte[] calcTPcd(IECPointFP PK_PICC){
        pk_PICC_ = PK_PICC;
        IECPointFP K = PK_PICC.multiply(sk_PCD_);
        sk_K_ = K.getX();
        k_MAC_ = kdf_.derivateForMAC(CConverter.convertBigIntegerToByteArray(sk_K_));
        k_ENC_ = kdf_.derivateForEnc(CConverter.convertBigIntegerToByteArray(sk_K_));
        AuthenticationToken auth = new AuthenticationToken(paceInfos_.getProtocol());
        t_PCD_ = auth.calcAuthToken(k_MAC_,PK_PICC);
        return t_PCD_;
    }

    /**
     * Verify the authenticated token T_PICC
     * @param T_PICC Token received from the card
     * @return true if verified
     */
    public boolean Verify(byte[] T_PICC){
        AuthenticationToken auth = new AuthenticationToken(paceInfos_.getProtocol());
        t_PICC_Dash_ = auth.calcAuthToken(k_MAC_, pk_PCD_);
        return Arrays.equals(T_PICC, t_PICC_Dash_);
    }

    /**
     * Get the decrypted nonce
     * @return decrypted nonce
     */
    public byte[] getDecryptedNonce(){
        return nonce_S_;
    }

    /**
     * Get the wrapped password
     * @return wrapped password
     */
    public byte[] getKPassword(){
        return kPassword_;

    }

    /**
     * Get the curve
     * @return curve
     */
    public IECCurveFP getCurve(){
        return curve_;
    }

    /**
     * Get the encryption key
     * @return encryption key
     */
    public byte[] getKEnc(){
        return k_ENC_;
    }

    /**
     * Get the authentication key
     * @return authentication key
     */
    public byte[] getKMac(){
        return k_MAC_;
    }

    /**
     * Get the secret key SK_K
     * @return SK_K
     */
    public BigInteger getK(){
        return sk_K_;

    }

    /**
     * Get the public key of the card
     * @return PK_PICC
     */
    public IECPointFP getPK_PICC(){
        return pk_PICC_;
    }

    /**
     * Get the curve spec
     * @return Curve spec
     */
    public IX9ECParameters getCurveSpec() {
        return curveSpec_;
    }

    /*  Functions for testing purpose

     */


    public void setKPassword(byte[] byaKPw){
        kPassword_ = byaKPw;
    }

    public void setSK_PCD(BigInteger byaSK_PCD){
        sk_PCD_ = byaSK_PCD;
    }

    public void setSK_x(BigInteger byaSK_x){
        sk_x_ = byaSK_x;
    }

    public void setTestingMode(boolean bTesting){
        testingMode_ = bTesting;
    }

    public BigInteger getSK_PCD(){
        return sk_PCD_;
    }

    public BigInteger getSK_x(){
        return sk_x_;
    }

    public IECPoint getGDash(){
        return base_G_Dash_;
    }

    public IECPoint getH(){
        return H_;
    }

    public byte[] getTP_PICC_DASH(){
        return t_PICC_Dash_;
    }


}
