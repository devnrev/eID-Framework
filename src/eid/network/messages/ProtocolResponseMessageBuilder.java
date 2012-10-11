/*
 * ProtocolResponseMessageBuilder.java
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

package eid.network.messages;

import common.exceptions.BuildException;
import common.network.paos.WsPaosResponseMessageBuilder;
import eid.network.data.ProtocolResponseType;
import eid.network.data.ResultType;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;


/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 22.07.12
 * Time: 23:02
 */

/**
 * This class acts as an abstract generic class which defines the basic layout of the
 * body content of the PAOS response
 * @param <T> Content data type
 */
public abstract class ProtocolResponseMessageBuilder<T extends ProtocolResponseType>
        extends WsPaosResponseMessageBuilder<T> {

    protected String bodyTag_;
    protected String namespace_;
    protected String namespaceDef_;

    /**
     * Constructor
     * @param bodyTag Name of the message tag
     * @param namespace Message namespace
     * @param namespaceDef Namespace definition
     */
    protected ProtocolResponseMessageBuilder(String bodyTag, String namespace,String namespaceDef){
        bodyTag_ = bodyTag;
        namespace_ = namespace;
        namespaceDef_ = namespaceDef;
    }

    /**
     * Build the body content
     * @param payload Body data
     * @return Body element
     * @throws BuildException
     */
    @Override
    public SOAPElement buildBody(T payload) throws BuildException {
        try {
            SOAPElement content = body_.addChildElement(ProtocolResponseBodyHelper.buildContentTag(bodyTag_,
                    namespace_,
                    namespaceDef_));
            ResultType resultData = payload.getResultType();
            ResultFieldHelper.appendResult(content,resultData);
            if(resultData.getResultMajor() == ResultType.Major.OK){
                buildBodyContent(payload, content);
            }
            return body_;
        } catch (SOAPException e) {
            throw new BuildException("could not build body content");
        }
    }

    /**
     * Pure virtual method which should implement the actual insertion of the content data into the body
     * @param payload Content data
     * @param content Body element
     * @throws BuildException
     * @throws SOAPException
     */
    protected abstract void buildBodyContent(T payload,SOAPElement content) throws BuildException,SOAPException;
}
