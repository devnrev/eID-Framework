/*
 * ControlCommand.java
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

package common.cardio;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 27.07.12
 * Time: 15:34
 */

/**
 * This class defines a basic structure for terminal control commands
 */
public class ControlCommand {
    protected int controlCode_;
    protected byte[] control_;

    protected ControlCommand(){
        control_ = new byte[]{};
    }

    public ControlCommand(int controlCode) {
        this.controlCode_ = controlCode;
        control_ = new byte[]{};
    }

    public ControlCommand(int controlCode, byte[] control) {
        this.controlCode_ = controlCode;
        this.control_ = control;
    }

    public int getControlCode() {
        return controlCode_;
    }

    public byte[] getControl() {
        return control_;
    }

    public void setControl(byte[] control) {
        this.control_ = control;
    }
}
