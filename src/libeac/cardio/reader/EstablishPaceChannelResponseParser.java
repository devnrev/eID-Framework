/*
 * EstablishPaceChannelResponseParser.java
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

package libeac.cardio.reader;

import common.CConverter;
import common.exceptions.ConverterException;
import common.exceptions.ParsingException;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 01.08.12
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */

/**
 * With this class the establish PACE channel response can be parsed
 */
public class EstablishPaceChannelResponseParser {
    private int status_;
    private int cardAccessLength_;
    private byte[] efCardAccess_;
    private int carLength_;
    private byte[] car_;
    private int carPrevLength_;
    private byte[] carPev_;
    private int iDiccLength_;
    private byte[] iDicc_;

    /**
     * Constructor
     * @param data Card reader response data
     * @throws ParsingException
     */
    public EstablishPaceChannelResponseParser(byte[] data) throws ParsingException {
        if (data.length > 3) {
            try {
                status_ = CConverter.convertByteArrayToIntLittleEndian(Arrays.copyOf(data, 2));
                int cursor = 4;
                cardAccessLength_ = CConverter.convertByteArrayToIntBigEndian(Arrays.copyOfRange(data, 2,
                        cursor));
                if (cardAccessLength_ > 0) {
                    efCardAccess_ = Arrays.copyOfRange(data, cursor, cursor + cardAccessLength_);
                }
                cursor += cardAccessLength_;
                if (data.length > cursor) {
                    carLength_ = data[cursor++];
                    if (carLength_ > 0) {
                        car_ = Arrays.copyOfRange(data, cursor, cursor + carLength_);
                    }
                    cursor += carLength_;
                    carPrevLength_ = data[cursor++];
                    if (carPrevLength_ > 0) {
                        carPev_ = Arrays.copyOfRange(data, cursor, cursor + carPrevLength_);
                    }
                    cursor += carPrevLength_;
                    iDiccLength_ = CConverter.convertByteArrayToIntBigEndian(Arrays.copyOfRange(data, cursor, cursor + 2));
                    cursor += 2;
                    if (iDiccLength_ > 0) {
                        iDicc_ = Arrays.copyOfRange(data, cursor, cursor + iDiccLength_);
                    }
                }
                return;
            } catch (ConverterException e) {
                e.printStackTrace();
            }
        }
        throw new ParsingException("could not parse response");
    }

    /**
     * Get the status code
     * @return Status code
     */
    public int getStatus() {
        return status_;
    }

    /**
     * Get the length oif the EF.CardAccess file
     * @return EF.CardAccess length
     */
    public int getCardAccessLength() {
        return cardAccessLength_;
    }

    /**
     * Get the EF.CardAccess file
     * @return EF.CardAccess file
     */
    public byte[] getEfCardAccess() {
        return efCardAccess_;
    }

    /**
     * Get the Certificate Authority Reference length
     * @return Certificate Authority Reference length
     */
    public int getCarLength() {
        return carLength_;
    }

    /**
     * Get the Certificate Authority Reference
     * @return Certificate Authority Reference
     */
    public byte[] getCar() {
        return car_;
    }

    /**
     * Get the length of the previous Certificate Authority Reference
     * @return Previous Certificate Authority Reference length
     */
    public int getCarPrevLength() {
        return carPrevLength_;
    }

    /**
     * Get the previous Certificate Authority Reference
     * @return previous Certificate Authority Reference
     */
    public byte[] getCarPev() {
        return carPev_;
    }

    /**
     * Get the chip identifier length
     * @return Chip identifier
     */
    public int getIdPiccLength() {
        return iDiccLength_;
    }

    /**
     * Get the chip identifier
     * @return Chip identifier
     */
    public byte[] getIdPicc() {
        return iDicc_;
    }
}
