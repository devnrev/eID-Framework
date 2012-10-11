/*
 * CTerminalAuthenticationTest.java
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

import common.CFactoryHelper;
import common.cardio.CAPDUResponse;
import common.cardio.ICardAccess;
import common.cardio.coding.CAPDUSecureMessaging;
import common.cardio.test.CDummyIOAccess;
import common.crypto.CryptoTypes;
import common.crypto.IECCurveFP;
import common.util.Logger;
import junit.framework.TestCase;
import libeac.Certificate;
import libeac.TerminalAuthentication;
import libeac.PACESecurityData;


import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 02.01.12
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class CTerminalAuthenticationTest extends TestCase {
    private final String szKENC = "68406B4162100563D9C901A6154D2901";
    private final String szKMAC = "73FF268784F72AF833FDC9464049AFC9";
    private final String szCar = "44454356434141543030303031";
    public void testPerformTA() throws Exception {
        CAPDUSecureMessaging secMsg = new CAPDUSecureMessaging(DatatypeConverter.parseHexBinary(szKENC),
                                                               DatatypeConverter.parseHexBinary(szKMAC),
                                                               CryptoTypes.ESymmetricBlockCipher.AES);
        PACESecurityData secData = new PACESecurityData();
        secData.KMac = DatatypeConverter.parseHexBinary(szKMAC);
        secData.KEnc = DatatypeConverter.parseHexBinary(szKENC);
        secData.Car = DatatypeConverter.parseHexBinary(szCar);
        ICardAccess dummyIO = new CDummyIOAccess();

        TerminalAuthentication terminalAT = new TerminalAuthentication(dummyIO,secData);

        //test MSE DST car which was transmitted by pace
        String szTestVec1 = "0C2281B61D871101BE90237EEB4BA0FF253EA246AE31C8B88E0892D21C73A1DFE99900";

        CAPDUResponse dummyResponse = terminalAT.sendCAR(secData.Car);
        Logger.log("MSE DST Pace Car\n" + DatatypeConverter.printHexBinary(dummyResponse.getData()) +
                "\nexpected:\n" + szTestVec1);
        assertTrue(Arrays.equals(dummyResponse.getData(), DatatypeConverter.parseHexBinary(szTestVec1)));

        //simulate card response
        String szResp = "990290008E08A89570A68664A7D6";
        CAPDUResponse resp = secMsg.decode(DatatypeConverter.parseHexBinary(szResp));
        assertNotNull(resp);

        assertTrue(resp.getSW() == 0x9000);

        /*


        //DV Certificate encoded with secure messaging taken from BSI Working Example. Actually the data body does not contain
        //the encrypted version of the DV certificate in the previous normal encoded DV APDU command.
        //Instead there the decryption of szDVCerAPDUTest contains szDVCerAPDUDecrypted.
        //Due to this obviously incorrect values in the BSI document, further unit testing against working example is not
        //possible
        // 4.2Command PSO: Verify Certificate
        String szDVCertAPDUTestSM = "0C2A00BE00017F8782017101BB99CB14CA8DAB03808E10A40524A8A65C86FD7B" +
                "674AE43B2AFE419447B1EBAD05D8AE6E347EE181A83628F5C179B49137A2E678" +
                "B9818D867999C6E37EF7F3F68DAB4136A1491C915B616BEB835AFEDEA79261C7" +
                "F5CE5C6387BD720690AB909D13CE3615C9CDA7167026B3D828135B8C54F0F11A" +
                "F8DB65B9F435BB2CCF4171F82C45723A88B5FFA3409F09EDF9A246E84C215F0E" +
                "F1A8BB2718350A4F9632C7068CB9C9FA4E0A7540875D4B73D28AE05C28D523B4" +
                "2C6D4C1A4F6478F66E86EE421BC8DB6834120BECB912B150ABAC9D806D66AC1F" +
                "7BEF14C540F0A253E1C1830327B9375F6BDA79A8B277DDEF63D7EE2552531E2C" +
                "9E44B7152984A577E2E8A5C3213FBCB4BF7F0DF3ECF9EBD50C48EF2D01C3B303" +
                "AF5839264AA534D55876AF81CAB173F7084EBF463D8F0AF16AB97ADB9D4C6568" +
                "8DD432A9EC3B3F8D9A685BA8484B5EA21AF739CDA4D63C2A9874B961F5300697" +
                "7064830590E2761F88AA6E4D65088AE4C7C84DE8AE62B68E557DCDF58E08E38A" +
                "CDA44E503CE50000";

        //Decrypted content
        String szDVCerAPDUDecrypted = "1D9A3D9096A648DD5606AB86D3055954415430303030317F498190060A04007F" +
                "00070202020205868181040A74972E84B7D2C428FBE46D40C92D6CB56AE6A4B0" +
                "AF5B8BC0927E5EF6F73220776D31693E36D685DF6CF3763E41728D967DC1963F" +
                "7CA70F0EA7ADB5EA856D8A133B867B8EF4132E7FFA3E8F32CD0321FC22199B93" +
                "23376E59D84A062491948117178BF4DB6C0EE7A235BFC3D4459183D408232AC7" +
                "81C78CF5C54E2FF35BA5165F200D444554455354445644453031397F4C120609" +
                "04007F0007030102025305801FFFFF105F25060100000903005F240601000100" +
                "03005F37818098C637BEFA63058921902896A605206D5BE3BFF2F7E258EB0BB0" +
                "6EA7E84DFBC98E35276110C684BB23D277350E3862E6A30520808F417B891198" +
                "5880A556E1758C7FE6545B3DA03383BD7B5B51DEC007139CBC44FF320F84F2B6" +
                "AD44C7B28FCAEB2B7B98E3A2BAE8322B115A8F10D48349281B4F4645C9AAE307" +
                "CFAE9AD6B194";
        */

        //Assuming that this certificate taken from openPace was used in BSI worked example
        
        String szDVCert = "7F4E81DE5F290100420D444543564341415430303030317F498190060A04007F" +
                "00070202020205868181040A74972E84B7D2C428FBE46D40C92D6CB56AE6A4B0" +
                "AF5B8BC0927E5EF6F73220776D31693E36D685DF6CF3763E41728D967DC1963F" +
                "7CA70F0EA7ADB5EA856D8A133B867B8EF4132E7FFA3E8F32CD0321FC22199B93" +
                "23376E59D84A062491948117178BF4DB6C0EE7A235BFC3D4459183D408232AC7" +
                "81C78CF5C54E2FF35BA5165F200D444554455354445644453031397F4C120609" +
                "04007F0007030102025305801FFFFF105F25060100000903005F240601000100" +
                "03005F37818098C637BEFA63058921902896A605206D5BE3BFF2F7E258EB0BB0" +
                "6EA7E84DFBC98E35276110C684BB23D277350E3862E6A30520808F417B891198" +
                "5880A556E1758C7FE6545B3DA03383BD7B5B51DEC007139CBC44FF320F84F2B6" +
                "AD44C7B28FCAEB2B7B98E3A2BAE8322B115A8F10D48349281B4F4645C9AAE307" +
                "CFAE9AD6B194";

        Certificate dvCert;
        try{
            dvCert = new Certificate(DatatypeConverter.parseHexBinary(szDVCert));
        }catch(IOException e){
            Logger.log("Could initialize DV certificate object with give certificate. Formatted as DER sequence ?");
            return;
        }
        assertNotNull(dvCert);

        dummyResponse = terminalAT.sendDvCertificate(dvCert);


        String szDVCerAPDUTest =    "002A00BE0001667F4E81DE5F290100420D444543564341415430303030317F498190060A04007F" +
                                    "00070202020205868181040A74972E84B7D2C428FBE46D40C92D6CB56AE6A4B0" +
                                    "AF5B8BC0927E5EF6F73220776D31693E36D685DF6CF3763E41728D967DC1963F" +
                                    "7CA70F0EA7ADB5EA856D8A133B867B8EF4132E7FFA3E8F32CD0321FC22199B93" +
                                    "23376E59D84A062491948117178BF4DB6C0EE7A235BFC3D4459183D408232AC7" +
                                    "81C78CF5C54E2FF35BA5165F200D444554455354445644453031397F4C120609" +
                                    "04007F0007030102025305801FFFFF105F25060100000903005F240601000100" +
                                    "03005F37818098C637BEFA63058921902896A605206D5BE3BFF2F7E258EB0BB0" +
                                    "6EA7E84DFBC98E35276110C684BB23D277350E3862E6A30520808F417B891198" +
                                    "5880A556E1758C7FE6545B3DA03383BD7B5B51DEC007139CBC44FF320F84F2B6" +
                                    "AD44C7B28FCAEB2B7B98E3A2BAE8322B115A8F10D48349281B4F4645C9AAE307" +
                                    "CFAE9AD6B194";


        String szDVCertAPDUTestSM = "0C2A00BE00017F8782017101BB99CB14CA8DAB03808E10A40524A8A65C86FD7B" +
                                    "674AE43B2AFE419447B1EBAD05D8AE6E347EE181A83628F5C179B49137A2E678" +
                                    "B9818D867999C6E37EF7F3F68DAB4136A1491C915B616BEB835AFEDEA79261C7" +
                                    "F5CE5C6387BD720690AB909D13CE3615C9CDA7167026B3D828135B8C54F0F11A" +
                                    "F8DB65B9F435BB2CCF4171F82C45723A88B5FFA3409F09EDF9A246E84C215F0E" +
                                    "F1A8BB2718350A4F9632C7068CB9C9FA4E0A7540875D4B73D28AE05C28D523B4" +
                                    "2C6D4C1A4F6478F66E86EE421BC8DB6834120BECB912B150ABAC9D806D66AC1F" +
                                    "7BEF14C540F0A253E1C1830327B9375F6BDA79A8B277DDEF63D7EE2552531E2C" +
                                    "9E44B7152984A577E2E8A5C3213FBCB4BF7F0DF3ECF9EBD50C48EF2D01C3B303" +
                                    "AF5839264AA534D55876AF81CAB173F7084EBF463D8F0AF16AB97ADB9D4C6568" +
                                    "8DD432A9EC3B3F8D9A685BA8484B5EA21AF739CDA4D63C2A9874B961F5300697" +
                                    "7064830590E2761F88AA6E4D65088AE4C7C84DE8AE62B68E557DCDF58E08E38A" +
                                    "CDA44E503CE50000";

        byte[] byaSmAPDU = dummyResponse.getData();
        Logger.log("send DV certificate\n" + DatatypeConverter.printHexBinary(byaSmAPDU) +
                "\nexpected:\n" + szDVCertAPDUTestSM);
        assertTrue(Arrays.equals(byaSmAPDU, DatatypeConverter.parseHexBinary(szDVCertAPDUTestSM)));

        //simulate chip response
        String szDvResp = "990290008E082B06864AEA1A1013";
        resp = secMsg.decode(DatatypeConverter.parseHexBinary(szDvResp));
        assertNotNull(resp);

        assertTrue(resp.getSW() == 0x9000);

        //BSI worked example specifies that dv CAR is used in next MSE DST command but the binary values reflect
        //the AT certificate CAR

        String szATCertificate  =   "7F4E81DE5F290100420D444554455354445644453031397F498190060A04007F" +
                                    "00070202020205868181043385B484E8E994C93F55ADF4A9C92DA3C063AC7D74" +
                                    "4E85D38EE070BF6FF8D7ABA2DE688724B27D6BAE2B1C8AE074C09BB8808CC830" +
                                    "36B0692898711D3F6FC3213846E14B3154845084DFBC0EB70AE2BDEA8EEBD679" +
                                    "6ACFA4C56F5703CE5A6EFE0A266E43C3F71A1B7EEC60E5C6F0850DC4D3455F53" +
                                    "282C9FBCA85DC947D13BEE5F200D444554455354415444453031397F4C120609" +
                                    "04007F000703010202530500000001105F25060100000903005F240601000100" +
                                    "03005F37818034BB8C280A7A593CF7C7E9F375FFFFBFD92C4E3F6188A4482421" +
                                    "5E11EF47D03428CFC91936A2609C742EF92C5968BCE65BA42AED7AAD70B7B2A3" +
                                    "1201DC152CEC930D7D7954BFBE0021AD1FD2ACAA6C349A6C2FB86514B9F03DFA" +
                                    "B99871FBD990DD8416D3EDA2883EBD0B94401A7DBFE16E5A1DB643EE7C2FC0D5" +
                                    "D77BAE3666AF";
                
        Certificate atCert;
        try{
            atCert = new Certificate(DatatypeConverter.parseHexBinary(szATCertificate));
        }catch(IOException e){
            Logger.log("Could initialize AT certificate object with give certificate. Formatted as DER sequence ?");
            return;
        }
        assertNotNull(atCert);

        String szATCar = "44455445535444564445303139";
        byte[] byaAtCar = atCert.getCar();
        Logger.log("Car from AT certificate\n" + DatatypeConverter.printHexBinary(byaAtCar) + "\nexpected:\n" + szATCar);
        assertTrue(Arrays.equals(byaAtCar,DatatypeConverter.parseHexBinary(szATCar)));

        //test MSE DST car which was transmitted by pace
        String szATCarAPDU = "0C2281B61D871101A7BB8F230FFF9221162AD673B9F319A88E08D8713E9B7A600B4900";
        dummyResponse = terminalAT.sendCAR(byaAtCar);
        Logger.log("MSE DST AT Car\n" + DatatypeConverter.printHexBinary(dummyResponse.getData()) +
                "\nexpected:\n" + szATCarAPDU);
        assertTrue(Arrays.equals(dummyResponse.getData(), DatatypeConverter.parseHexBinary(szATCarAPDU)));

        //simulate card response
        String szAtCarResp = "990290008E08C8488F79FEF386C7";
        resp = secMsg.decode(DatatypeConverter.parseHexBinary(szAtCarResp));
        assertNotNull(resp);
        assertTrue(resp.getSW() == 0x9000);
        
        String szMseDstATCmd =  "0C2A00BE00017F8782017101EF27176D7426F3411023618E47D735CBFCA43550" +
                                "8790DC5E99AE8037F78A0A29BC886C5DFD3B86852AC8C620B5ED8D0EBCCDC7E2" +
                                "79FC60E1AE14250FB8F0EFF2E969DC13431EBF0B6FACD7D91FBF1A0F581D6A6A" +
                                "BEE7ED81015B7EF6B00FEE25B1759BB5E7978437AC09479D6FF2FFD12B16ABE3" +
                                "0C53DA4160F367DB1D7C1D8FD228BAA9D4D3C9F1B02A5F943A8B9CA363475984" +
                                "8586FE351A83BF733958D42949D27DFC853B2370FE417C849BA1B04355729740" +
                                "A160CF0650CB37783E4F8CE4CFF1D9B4118F4676AEF13A8C89198AB8331048B7" +
                                "0C686F943010378224C62C91E4AA957F723F5E5DB3411AC9E7B2AC5EFFD8726A" +
                                "38BE5A1B1E067892E18D6E43C4C29D5C2CF95FF3BD142EE648BD154442696D6D" +
                                "F0F6F25C43C1DAAA410946E55EE16D6FDC1853D8434E6FF7C78415010C8EECC9" +
                                "C2EE3D53AF0DCFC90D0A436AF7C4E36453A559122E5220F5C0994DDF5D6A953C" +
                                "607C4C3987B597E3D4685C8337E9AAB95C2339028BABB9ACADED113D8E080680" +
                                "772FB39D76610000";

        resp = terminalAT.sendAtCertificate(atCert);
        Logger.log("send AT Certificate:\n" + DatatypeConverter.printHexBinary(resp.getData()) +
                "\nexpected:\n" + szMseDstATCmd);
        assertTrue(Arrays.equals(resp.getData(),DatatypeConverter.parseHexBinary(szMseDstATCmd)));

        //simulate card response
        String szAtCertResp = "990290008E08A7F7F042EBD09233";
        resp = secMsg.decode(DatatypeConverter.parseHexBinary(szAtCertResp));
        assertNotNull(resp);
        assertTrue(resp.getSW() == 0x9000);

        //test MSE Set AT
        String szMseSetATCmd =  "0C2281A44D874101FE3F0234E5EFE66B9C56FD23D602EEA59C251E1E8604C46B" +
                                "33FA60C7D09570EE881791C89FB8159E71C63543A9BB546F6F746FA8837BC399" +
                                "0A60CAA58A5A5AB58E08D1736F1E725BC5F800";


        String szPK_PCD =   "045A7A377FC9CAFC03AC7FF45441A8B2909D88EAB8E6B0173847AB49B949DF37" +
                            "99A34EE57EC55268CF8B1C3EC489F8BF4CF4C68D3FD9670E89C0D5D3FFF1AAF8" +
                            "9F";

        IECCurveFP curve = CFactoryHelper.getCryptoProvider().getX9ECParametersFromTeleTrusTCurve("brainpoolp256r1").getCurveFP();
        resp = terminalAT.sendMseSetAT(
                        DatatypeConverter.parseHexBinary(szPK_PCD),atCert.getChr(),
                            CFactoryHelper.getCryptoProvider().createDERObjectIdentifier("0.4.0.127.0.7.2.2.2.2.5"),null);

        Logger.log("send MSE Set AT:\n" + DatatypeConverter.printHexBinary(resp.getData()) + "\nexpected:\n" + szMseSetATCmd);
        assertTrue(Arrays.equals(resp.getData(),DatatypeConverter.parseHexBinary(szMseSetATCmd)));

        //simulate card response
        String szMseSetAtCertResp = "990290008E087D47C45B27DEDB5C";
        resp = secMsg.decode(DatatypeConverter.parseHexBinary(szMseSetAtCertResp));
        assertNotNull(resp);
        assertTrue(resp.getSW() == 0x9000);

        //test challenge request
        String szChallengeRequesCmd = "0C8400000D9701088E081D21EBBE738FF4FD00";

        resp = terminalAT.getChallenge();
        Logger.log("get challenge:\n" + DatatypeConverter.printHexBinary(resp.getData()) +
                "\nexpected:\n" + szChallengeRequesCmd);
        assertTrue(Arrays.equals(resp.getData(),DatatypeConverter.parseHexBinary(szChallengeRequesCmd)));

        //simulate card response
        String szChallengeResp = "871101124EBDD570FE8E05FD048B2A765C5E75990290008E08C9409B3057B1B43F";
        String szChallengeDecoded = "547E4EAB03B235D2";
        resp = secMsg.decode(DatatypeConverter.parseHexBinary(szChallengeResp));
        assertNotNull(resp);
        assertTrue(resp.getSW() == 0x9000);
        Logger.log("decoded challenge:\n" + DatatypeConverter.printHexBinary(resp.getData()) + "\nexpected:\n" + szChallengeDecoded);
        assertTrue(Arrays.equals(resp.getData(),DatatypeConverter.parseHexBinary(szChallengeDecoded)));


    }
}
