/*
 * CCertificateTest.java
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

import common.util.Logger;
import junit.framework.TestCase;
import libeac.Certificate;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 05.01.12
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
public class CCertificateTest extends TestCase {
    public void testCertificate() throws Exception {
        String szDVCert =   "7F4E81DE5F290100420D444543564341415430303030317F49" +
                            "8190060A04007F00070202020205868181045B6EC028FABAA388531E46DA2D1E" +
                            "4DF2DA21903AA2FF00BD22325C25D7CD4624C5524EE1EC35F3151845EF29AC40" +
                            "BF71F4576B827F79EBECBB5460E22C2F38720EB6A05ACA694388885F53D46230" +
                            "6F98CECC5DDCB6434F81BE0153FC1E4E6BA27116F7FB06FE5CB6F4A3A287A49B" +
                            "F4A4DAF9DBBD8AB638EE8E66AC72FA7698AF5F200D4445544553544456444530" +
                            "31397F4C12060904007F0007030102025305801FFFFF105F2506010000090300" +
                            "5F24060100010003005F37818034EA282825231BF9EF84DCD15AF772A926B0A4" +
                            "78CA965A7F8E9E0D90CD7DE61830530FD915478E6856399FE78AB2D6F3BDE50D" +
                            "9A5625ECB651D758773477136F70A59081E530D82928C734AF02BAA9E303E96B" +
                            "BDA606DC6EF99A402862DD727E964E1964DA0949B5846020C3267AEA6CF026E5" +
                            "4C7E9E837367DF171CC7B2C891";
        Certificate cert;
        try{
            cert = new Certificate(DatatypeConverter.parseHexBinary(szDVCert));
        }catch(IOException e){
            Logger.log("Could initialize certificate object with give certificate. Formated as DER sequence ?");
            return;
        }
        assertNotNull(cert);
        byte[] byaCar = cert.getCar();
        assertNotNull(byaCar);
        String szDVCar = "44454356434141543030303031";
        assertTrue(Arrays.equals(byaCar,DatatypeConverter.parseHexBinary(szDVCar)));
        byte[] byaOriginalData = cert.getBytes();

        assertTrue(Arrays.equals(byaOriginalData, DatatypeConverter.parseHexBinary(szDVCert)));

        String cert2 = "7F218201447F4E81FD5F290100420F444544567449446D744730303030397F494F060A04007F0007020202020386410429DE2CA270B7F1CD4A121D182F84E1B01F123D021699B427C81D8E02DD7D0D7A6FBF8F9882F3DD12916A41F320831A0E9C4AF76A42CE98F0ECAE8EBB341292A55F200C444541546D744730303030347F4C12060904007F0007030102025305000501FB075F25060102000700045F2406010201000001655E732D060904007F00070301030180203D481284343970B32B336BF6F9316AC990342D275D273CBE3855C1C08F12CECC732D060904007F0007030103028020E0BFAAA425C6673920F25F40C8DCE16086FC9C37F723D6198CFBDFA98FDA2F0C5F374082F5C7985B73C4A46976EB3CC4BC07C6377090FDAB9134BC329A5BA97665EE23564632A9C529009437975A40205E8D5DEF6C0F0621006F0C6C6D404E46ED7616";

        Certificate dvcert = new Certificate(DatatypeConverter.parseHexBinary(cert2));
        Logger.log(new String(dvcert.getChr()));

        



    }
}
