/*
 * ResultType.java
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

package eid.network.data;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 22.07.12
 * Time: 23:23
 */

/**
 * The ResultType describes the failure code of an operation. It is divided into 2 different types.
 * The major code defines the the error code in general and minor describes it more in detail. Additionally a
 * failure message can be passed which describes the error.
 */
public class ResultType {

    /**
     * Enum containing major error codes
     */
    public enum Major{
        OK{
            public String toString() {
                return "/resultmajor#ok";
            }
        },
        ERROR{
            public String toString() {
                return "/resultmajor#error";
            }
        },
        WARNING{
            public String toString() {
                return "/resultmajor#warning";
            }
        }
    }

    /**
     * Enum containing minor error codes
     */
    public enum Minor{
        INTERNAL_ERROR{
            public String toString() {
                return "/resultminor/al/common#internalError";
            }
        },
        UNKNOWN_PROTOCOL{
            public String toString() {
                return "/resultminor/dp#unknownProtocol";
            }
        },
        PARAMETER_ERROR{
            public String toString() {
                return "/resultminor/al/common#parameterError";
            }
        },
        COMMUNICATION_ERROR{
            public String toString() {
                return "/resultminor/dp#communicationError";
            }
        }
    }

    private Major resultMajor_;
    private Minor resultMinor_;
    private String message_;

    public ResultType(Major resultMajor, Minor resultMinor) {
        this.resultMajor_ = resultMajor;
        this.resultMinor_ = resultMinor;
        message_ = "";
    }

    public ResultType(Major resultMajor, Minor resultMinor, String message) {
        this.resultMajor_ = resultMajor;
        this.resultMinor_ = resultMinor;
        this.message_ = message;
    }

    public ResultType(Major resultMajor) {
        resultMajor_ = resultMajor;
    }

    public ResultType() {
        resultMajor_ = Major.OK;
    }

    public Major getResultMajor() {
        return resultMajor_;
    }

    public Minor getResultMinor() {
        return resultMinor_;
    }

    public String getMessage() {
        return message_;
    }
}
