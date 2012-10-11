/*
 * TcTokenHandler.java
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

package eid.network.handler;

import common.data.NodeNameFilter;
import common.data.XmlAccessor;
import common.exceptions.ElementNotFoundException;
import common.exceptions.ParsingException;
import common.exceptions.TranscodingException;
import eid.network.data.PathSecurityData;
import eid.network.data.TcTokenData;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.w3c.dom.Element;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 10.07.12
 * Time: 12:51
 */

/**
 * This class is used to retrieve the TcToken from a web page, either in xml or html format
 */
public class TcTokenHandler {
    private String url_;

    /**
     * constructor
     * @param url URL to the TcToken
     */
    public TcTokenHandler(String url) {
        url_ = url;
    }

    /**
     * Return the content of a web page as a string
     * @param url URL to the web page
     * @return Data as string
     * @throws IOException
     */
    private String getUrlContents(String url) throws IOException {
        URL page = new URL(url);
        StringBuffer content = new StringBuffer();
        HttpsURLConnection conn = (HttpsURLConnection) page.openConnection();
        conn.connect();
        InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
        BufferedReader buff = new BufferedReader(in);
        String line;
        while ((line = buff.readLine())!= null){
            content.append(line + "\n");
        }
        return content.toString();
    }

    /**
     * Try to parse the TcToken from the string which is expected to in XML format
     * @param resource Data
     * @return TcTokenData object
     * @throws ParsingException
     */
    private TcTokenData parseTcTokenFromXml(String resource) throws ParsingException {
        XmlAccessor accessor = null;
        try {
            accessor = XmlAccessor.createFromString(resource);
        } catch (TranscodingException e) {
            e.printStackTrace();  //TODO
            throw new ParsingException("could not read xml of TcToken from the resource");
        }
        NodeNameFilter nodeNameFilter = new NodeNameFilter("SessionIdentifier",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        try{
            Element sessionId = accessor.requireElement(nodeNameFilter);
            nodeNameFilter.setMatchingName("ServerAddress");
            Element serverAddress = accessor.requireElement(nodeNameFilter);
            nodeNameFilter.setMatchingName("RefreshAddress");
            Element refreshAddress = accessor.requireElement(nodeNameFilter);
            nodeNameFilter.setMatchingName("PathSecurity-Protocol");
            Element pathSecProtocol = accessor.requireElement(nodeNameFilter);
            nodeNameFilter.setMatchingName("Binding");
            Element binding = accessor.requireElement(nodeNameFilter);
            nodeNameFilter.setMatchingName("PSK");
            Element psk = accessor.requireElement(nodeNameFilter);
            return new TcTokenData(serverAddress.getTextContent(),
                    sessionId.getTextContent(),
                    refreshAddress.getTextContent(),
                    binding.getTextContent(),
                    new PathSecurityData(pathSecProtocol.getTextContent(),
                            DatatypeConverter.parseHexBinary(psk.getTextContent())));
        }catch(ElementNotFoundException e){
            throw new ParsingException("Incomplete TCToken");
        }
    }

    /**
     * Try to parse the TcToken from the string which is expected to in HTML format
     * @param resource Data
     * @return TcTokenData object
     * @throws ParsingException
     */
    private TcTokenData parseTcTokenFromHtmlPage(String resource) throws ParsingException {
        Parser parser = Parser.createParser(resource,"UTF-8");
        TagNameFilter tagNameFilter = new TagNameFilter("object");
        try{
        NodeList tags = parser.extractAllNodesThatMatch(tagNameFilter);
        if(tags.size()>0){
            NodeIterator iterator = tags.elements();
            while (iterator.hasMoreNodes()) {
                Node n = iterator.nextNode();
                if (n instanceof TagNode) {
                    TagNode tag = (TagNode) n;
                    String type = tag.getAttribute("type");
                    if (type != null || type.equals("application/vnd.ecard-client")) {
                        NodeList params = tag.getChildren();
                        try {
                            String serverAddress = extractParamValueFromNodeList("ServerAddress", params);
                            String sessionIdentifier = extractParamValueFromNodeList("SessionIdentifier", params);
                            String binding = extractParamValueFromNodeList("Binding", params);
                            String pathSecProtocol = extractParamValueFromNodeList("PathSecurity-Protocol", params);
                            String pathSecParams = extractParamValueFromNodeList("PathSecurity-Parameters", params);
                            String refreshAddress = extractParamValueFromNodeList("RefreshAddress", params);
                            Pattern pskPattern = Pattern.compile("<PSK>(.*?)</PSK>");
                            Matcher match = pskPattern.matcher(pathSecParams);
                            if (match.find()) {
                                String psk = match.group(1);
                                PathSecurityData psd = new PathSecurityData(pathSecProtocol,
                                        DatatypeConverter.parseHexBinary(psk));
                                return new TcTokenData(serverAddress, sessionIdentifier, refreshAddress, binding, psd);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        } catch (ParserException e) {
            e.printStackTrace();  //TODO
        }
        throw new ParsingException("something went wrong while parsing the tctoken");
    }

    /**
     * Initiate the retrieval of the TcToken and return it as an object
     * @return TcTokenData object
     * @throws ParsingException
     */
    public TcTokenData getTcToken() throws ParsingException {

        String content = null;
        try {
            content = getUrlContents(url_);
        } catch (IOException e) {
            e.printStackTrace();  //TODO
            throw new ParsingException("could not fetch contents of the url");
        }
        if(content.contains("<TCTokenType>")){
            return parseTcTokenFromXml(content);
        }else{
            return parseTcTokenFromHtmlPage(content);
        }

    }

    /**
     * Search for node inside a node list which contains a "name" attribute matching the given
     * name and return its tag value
     * @param param Attribute name to match
     * @param nodeList The Node list
     * @return Tag value
     * @throws Exception
     */
    private String extractParamValueFromNodeList(String param, NodeList nodeList) throws Exception {
        NodeIterator iterator = nodeList.elements();
        while (iterator.hasMoreNodes()) {
            Node n = iterator.nextNode();
            if (n instanceof TagNode) {
                TagNode tag = (TagNode) n;
                if (tag.getAttribute("name").equals(param)) {
                    return tag.getAttribute("value");
                }
            }
        }
        throw new Exception("Element not found or multiple occurences");

    }
}
