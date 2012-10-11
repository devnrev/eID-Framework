/*
 * XmlAccessor.java
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

package common.data;

import common.exceptions.ElementNotFoundException;
import common.exceptions.TranscodingException;
import org.w3c.dom.*;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 18.07.12
 * Time: 12:09
 */
public class XmlAccessor {
    protected Document document_;
    protected Node cursor_;
    private DocumentTraversal documentTraversal_;
    private NodeIterator seekIterator_;

    public static XmlAccessor createFromString(String ressource) throws TranscodingException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            Document document = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(ressource)));
            return new XmlAccessor(document);
        } catch (SAXException e) {
            e.printStackTrace();  //TODO
        } catch (IOException e) {
            e.printStackTrace();  //TODO
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //TODO
        }
        throw new TranscodingException("could not convert ressource into xml document");
    }

    public XmlAccessor(Document document) {
        document_ = document;
        cursor_ = document_.getDocumentElement();
        documentTraversal_ = (DocumentTraversal) document_;
    }

    public Element requireElement(NodeFilter filter) throws ElementNotFoundException {
        NodeIterator iterator = documentTraversal_.createNodeIterator(document_.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, filter, true);
        Node node = iterator.nextNode();
        if (node != null) {
            cursor_ = node;
            return (Element) node;
        }
        throw new ElementNotFoundException("no such node inside the document");
    }


    public void createSeek(NodeFilter filter) {
        seekIterator_ = documentTraversal_.createNodeIterator(document_.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, filter, true);

    }

    public Element seekNext() {
        Node node = seekIterator_.nextNode();
        if (node != null) {
            cursor_ = node;
        }
        return (Element) node;
    }

    public List<Element> getAllNodesAtCursorMatching(NodeFilter filter) {
        NodeIterator iterator = documentTraversal_.createNodeIterator(cursor_,
                NodeFilter.SHOW_ELEMENT, filter, true);
        List<Element> elementList = new ArrayList<Element>();
        for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
            elementList.add((Element) n);
        }
        return elementList;
    }

    public Element findElementAtCursorMatching(NodeFilter filter) throws ElementNotFoundException {
        NodeIterator iterator = documentTraversal_.createNodeIterator(cursor_,
                NodeFilter.SHOW_ELEMENT, filter, true);
        Node node = iterator.nextNode();
        if (node != null) {
            return (Element) node;
        }
        throw new ElementNotFoundException("no such node inside the document");
    }

    public Map<String, String> getAttributesAtCursor() {
        HashMap<String, String> attributeMap = new HashMap<String, String>();
        if (cursor_ != null) {
            NamedNodeMap namedNodeMap = cursor_.getAttributes();
            for (int i = 0; i < namedNodeMap.getLength(); ++i) {
                Node node = namedNodeMap.item(i);
                attributeMap.put(node.getNodeName(), node.getNodeValue());
            }
        }
        return attributeMap;
    }

    public boolean setCursorTo(Element element) {
        if (document_.compareDocumentPosition(element) == Node.DOCUMENT_POSITION_CONTAINED_BY) {
            cursor_ = element;
            return true;
        }
        return false;
    }

    public Element getCursor(){
        return (Element)cursor_;
    }


}
