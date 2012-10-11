/*
 * CPACECoreTest.java
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

package libeac.tests;

import common.CConverter;
import common.CFactoryHelper;
import common.crypto.IECPoint;
import common.util.Logger;
import junit.framework.TestCase;
import libeac.*;
import libeac.cardio.CGeneralAuthenticateAPDUCommand;
import libeac.cardio.CGetNonceADPUCommand;
import libeac.cardio.CMSEATAPDUCommand;
import libeac.cardio.CPACEFinalAPDUCommand;
import libeac.definitions.PACETypes;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.prng.VMPCRandomGenerator;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 21.12.11
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
public class CPACECoreTest extends TestCase {
    PACECore m_pace;
    final String m_szPw = "hgoiewrhgowh";

    @Override
    public void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
        m_pace = new PACECore(new PACEInfos(
                CFactoryHelper.getCryptoProvider().createDERObjectIdentifier("0.4.0.127.0.7.2.2.4.2.2"),
                2,
                13));
        assertNotNull(m_pace);
    }


    public void testCalcKPassword() throws Exception {

        m_pace.calcKPassword(m_szPw);
        assertTrue(Arrays.equals(m_pace.getKPassword(),
                DatatypeConverter.parseHexBinary("e81bff55075ffd49d6915f2a789ddc35")));
    }

    public void testDecryptNonce() throws Exception {
        m_pace.calcKPassword(m_szPw);
        m_pace.decryptNonce(DatatypeConverter.parseHexBinary("4dd3ea7e1d014d9f6c66940d4125496b"));
        assertTrue(Arrays.equals(m_pace.getDecryptedNonce(),
                DatatypeConverter.parseHexBinary("12236554766534425356246513647687")));
    }

    public void testCalcX() throws Exception {
        m_pace.calcKPassword(m_szPw);
        m_pace.decryptNonce(DatatypeConverter.parseHexBinary("4dd3ea7e1d014d9f6c66940d4125496b"));
        IECPoint X = m_pace.calcX();

        byte[] byaRnd = m_pace.getRand();
        VMPCRandomGenerator rndGen = new VMPCRandomGenerator();
        rndGen.nextBytes(byaRnd);
        X9ECParameters params = TeleTrusTNamedCurves.getByName("brainpoolp256r1");
        ECCurve.Fp curve = (ECCurve.Fp)params.getCurve();
        ECPoint base = params.getG();
        ECPoint.Fp Xbc = (ECPoint.Fp)base.multiply(new BigInteger(1,byaRnd));

        assertTrue(Arrays.equals(Xbc.getEncoded(),X.getEncoded()));


    }

    public void testCalcPkPcd() throws Exception {
        m_pace.calcKPassword(m_szPw);
        m_pace.decryptNonce(DatatypeConverter.parseHexBinary("4dd3ea7e1d014d9f6c66940d4125496b"));
        IECPoint X = m_pace.calcX();
    }

    public void testCalcTPcd() throws Exception {

    }


    public void testPACEWorkingExample(){

        // check MSE AT cmd
        String szMSEATCmd = "0022C1A427800A04007F000702020402028301037F4C12060904007F0007030102025305000000011084010D";
        CMSEATAPDUCommand mseCMd= new CMSEATAPDUCommand(PACETypes.AuthenticationType.PACE);
        mseCMd.setPassword(PACETypes.Password.PIN);
        mseCMd.setProtocol(CFactoryHelper.getCryptoProvider().createDERObjectIdentifier("0.4.0.127.0.7.2.2.4.2.2"));
        CertHolderAuthTemplate chat = new CertHolderAuthTemplate(PACETypes.ERoleType.AT);
        chat.setAuthorization(new byte[]{0x00,0x00,0x00,0x01,0x10});
        mseCMd.setCHAT(chat);
        mseCMd.setPrivateKeyReference(0xD);
        assertTrue(Arrays.equals(mseCMd.getBytes(), DatatypeConverter.parseHexBinary(szMSEATCmd)));
                
        m_pace.setTestingMode(true);

        // Decrcrypt nonce
        String szPin = "123456";
        String szCardresponse_Nonce = "7C128010CE834CDE69FFBB1D1EB21585CD709F189000";
        String szEncNonceFromChip = "CE834CDE69FFBB1D1EB21585CD709F18";
        DynamicAuthenticationData dynNon = new DynamicAuthenticationData();
        dynNon.initialize(DatatypeConverter.parseHexBinary(szCardresponse_Nonce));
        assertTrue(Arrays.equals(dynNon.getEncryptedNonce(),DatatypeConverter.parseHexBinary(szEncNonceFromChip)));

        String szKPassword = "591468CDA83D65219CCCB8560233600F";
        m_pace.calcKPassword(szPin);
        assertTrue(Arrays.equals(m_pace.getKPassword(),DatatypeConverter.parseHexBinary(szKPassword)));

        String szDecNonce =  "7D98C00FC6C9E9543BBF94A87073A123";
        m_pace.setKPassword(DatatypeConverter.parseHexBinary(szKPassword));
        assertTrue(Arrays.equals(DatatypeConverter.parseHexBinary(szKPassword), m_pace.getKPassword()));


        m_pace.decryptNonce(DatatypeConverter.parseHexBinary(szEncNonceFromChip));
        assertTrue(Arrays.equals(m_pace.getDecryptedNonce(),DatatypeConverter.parseHexBinary(szDecNonce)));

        //check get nonce cmd
        String szNonce = "10860000027C0000";
        CGetNonceADPUCommand nonceCmd = new CGetNonceADPUCommand();
        assertTrue(Arrays.equals(nonceCmd.getBytes(),DatatypeConverter.parseHexBinary(szNonce)));

        // Set sk_x manually for testing purpose
        String szSK_x = "752287F5B02DE3C4BC3E17945118C51B23C97278E4CD748048AC56BA5BDC3D46";
        String szPK_X = "043DD29BBE5907FD21A152ADA4895FAAE7ACC55F5E50EFBFDE5AB0C6EB54F198D615913635F0FDF5BEB383E00355F82D3C41ED0DF2E28363433DFB73856A15DC9F";

        m_pace.setSK_x(new BigInteger(1, DatatypeConverter.parseHexBinary(szSK_x)));
        assertTrue(Arrays.equals(m_pace.getSK_x().toByteArray(),DatatypeConverter.parseHexBinary(szSK_x)));

        // calculate X
        IECPoint X = m_pace.calcX();
        assertTrue(Arrays.equals(X.getEncoded(),DatatypeConverter.parseHexBinary(szPK_X)));

        // check X cmd
        String szGaMapping = "10860000457C438141043DD29BBE5907FD21A152ADA4895FAAE7ACC55F5E50EFBFDE5AB0C6EB54F198D615913635F0FDF5BEB383E00355F82D3C41ED0DF2E28363433DFB73856A15DC9F00";
        CGeneralAuthenticateAPDUCommand gaCmd = new CGeneralAuthenticateAPDUCommand();
        DynamicAuthenticationData dynAuth = new DynamicAuthenticationData();
        dynAuth.setTerminalMapping(X.getEncoded());
        gaCmd.appendData(dynAuth.getDERTerminalAuthenticationData());
        assertTrue(Arrays.equals(gaCmd.getBytes(),DatatypeConverter.parseHexBinary(szGaMapping)));

        //Card responce with Y
        String szCardResp_PK_Y = "7C438241049CFCF7582AC986D0DD52FA53123414C3E1B96B4D00ABA8E574679B70EFB5BC3B45D2F13729CC2AE178E7E241B443213533B77DBB44649A815DDC4A2384BA422A9000";
        String szPK_Y = "049CFCF7582AC986D0DD52FA53123414C3E1B96B4D00ABA8E574679B70EFB5BC3B45D2F13729CC2AE178E7E241B443213533B77DBB44649A815DDC4A2384BA422A";
        DynamicAuthenticationData dynDat2 = new DynamicAuthenticationData();
        dynDat2.initialize(DatatypeConverter.parseHexBinary(szCardResp_PK_Y));
        assertTrue(Arrays.equals(DatatypeConverter.parseHexBinary(szPK_Y), dynDat2.getChipMapping()));



        String szH = "0471850CFD80FB475947E5B1AF10FE8E6663967C2D264935B31951F763A4B03A5749167388F88F52A109167E3E6592CA0820468D1157A8E781D2F7049179B1D114";
        String szGDash = "043929D28BA1E5339D6C5DADE5E33BD3C2F0BD14DD77C7521532261659C918FA6014DD48FA84E62BDE438EDB4C9771D042CDB24B7788BDBAB2031C45751E777F66";
        String szSK_PCD = "009D9A32DF93A57CCE33CA3CDD3457E33A976F293546C73550F397259C93BE0120";
        String szPK_PCD = "04518BC4E532AD2A9BD6527804D5D665ABD51041037A0CC8AA922804EB501C222B3427388599AFAAE9FBACE2DF93E13C3C4979CD12F0AE3E3C0126028391554582";

        // calculate PK_PCD
        m_pace.setSK_PCD(new BigInteger(1,DatatypeConverter.parseHexBinary(szSK_PCD)));
        assertTrue(Arrays.equals(m_pace.getSK_PCD().toByteArray(),DatatypeConverter.parseHexBinary(szSK_PCD)));
        IECPoint pkPCD;
        try {
            pkPCD=m_pace.calcPkPcd(CConverter.getECPointFPFromByteArray(DatatypeConverter.parseHexBinary(szPK_Y),m_pace.getCurve()));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return;
        }
        assertTrue(Arrays.equals(m_pace.getGDash().getEncoded(),DatatypeConverter.parseHexBinary(szGDash)));
        assertTrue(Arrays.equals(pkPCD.getEncoded(),DatatypeConverter.parseHexBinary(szPK_PCD)));
        assertTrue(Arrays.equals(m_pace.getH().getEncoded(), DatatypeConverter.parseHexBinary(szH)));

        // check PK_PCD cmd
        String szGaEphKey = "10860000457C43834104518BC4E532AD2A9BD6527804D5D665ABD51041037A0CC8AA922804EB501C222B3427388599AFAAE9FBACE2DF93E13C3C4979CD12F0AE3E3C012602839155458200";
        gaCmd = new CGeneralAuthenticateAPDUCommand();
        dynAuth.reset();
        dynAuth.setTerminalEphemeralPK(pkPCD.getEncoded());
        gaCmd.appendData(dynAuth.getDERTerminalAuthenticationData());
        assertTrue(Arrays.equals(gaCmd.getBytes(), DatatypeConverter.parseHexBinary(szGaEphKey)));

        //Card responds with PK_PICC
        String szCardResponse_PK_PICC = "7C43844104282CF38073036AFAC216AF135BD994DA0C357F10BD4C34AFEA1042B2EB0FD6804DF3658B835AC2E7133F13691184542BB50B109963A4662ABDC08B9763AF4B5B9000";
        String szPK_PICC = "04282CF38073036AFAC216AF135BD994DA0C357F10BD4C34AFEA1042B2EB0FD6804DF3658B835AC2E7133F13691184542BB50B109963A4662ABDC08B9763AF4B5B";
        dynAuth.reset();
        dynAuth.initialize(DatatypeConverter.parseHexBinary(szCardResponse_PK_PICC));
        assertTrue(Arrays.equals(dynAuth.getChipEphemeralPK(),DatatypeConverter.parseHexBinary(szPK_PICC)));

        String szK = "6E7D077CCD367C2EAA683F1E8EC534302E2D00B6ADAF8A87A6EDA78740F17606";
        //values taken from openPACE source code since BSI working example document contains wrong values for K_ENC, K_MAC.  <-- corrected in vers. 1.02
        // Also T_PCD and T_PICC need to be exchanged
        String szTPCD = "A27AE7B36573C1D9";
        String szKENC = "68406B4162100563D9C901A6154D2901";
        String szKMAC = "73FF268784F72AF833FDC9464049AFC9";

        //calculate T_PCD
        byte[] T_PCD;
        try {
            T_PCD = m_pace.calcTPcd(CConverter.getECPointFPFromByteArray(DatatypeConverter.parseHexBinary(szPK_PICC), m_pace.getCurve()));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return;
        }

        assertTrue(Arrays.equals(m_pace.getK().toByteArray(), DatatypeConverter.parseHexBinary(szK)));
        assertTrue(Arrays.equals(m_pace.getKEnc(), DatatypeConverter.parseHexBinary(szKENC)));
        assertTrue(Arrays.equals(m_pace.getKMac(), DatatypeConverter.parseHexBinary(szKMAC)));
        assertTrue(Arrays.equals(T_PCD, DatatypeConverter.parseHexBinary(szTPCD)));

        // check T_PCD cmd
        String szGaAuthToken = "008600000C7C0A8508A27AE7B36573C1D900";
        CPACEFinalAPDUCommand fpaCmd = new CPACEFinalAPDUCommand();
        dynAuth.reset();
        dynAuth.setTerminalAuthToken(T_PCD);
        fpaCmd.appendData(dynAuth.getDERTerminalAuthenticationData());
        assertTrue(Arrays.equals(fpaCmd.getBytes(), DatatypeConverter.parseHexBinary(szGaAuthToken)));

        String szTPICC = "A2658C2F38600B0F";
        boolean bVerify = m_pace.Verify(DatatypeConverter.parseHexBinary(szTPICC));
        assertTrue(bVerify);

        if(bVerify)
            Logger.log("BSI working example pace check was successful");

    }

}
