/*
 * NodeNameFilter.java
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


import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.07.12
 * Time: 23:25
 */

/**
 * This class acts as a filter for nodes. It basically matches if a string appears in a node name.
 * Dependent on the matching type, the namespace of a node name can be ignored
 */
public class NodeNameFilter implements NodeFilter {

    private String name_;
    private Pattern pattern_;
    private MatchingRule  matchRule_;

    public NodeNameFilter(String name,MatchingRule matchRule){
        name_ = name;
        matchRule_ = matchRule;
        if (matchRule_ == MatchingRule.EXACT_IGNORE_NAMESPACE){
            pattern_ = Pattern.compile("(.*:)?("+name+")");
        }
    }

    public void setMatchingName(String name){
        name_ = name;
        pattern_ = Pattern.compile("(.*:)?("+name+")$");
    }

    @Override
    public short acceptNode(Node node) {
        String nodeName = node.getNodeName();
        switch (matchRule_) {
            case EXACT:
                if(nodeName.equals(name_)){
                    return FILTER_ACCEPT;
                }
                break;
            case CONTAINS:
                if(nodeName.contains(name_)){
                    return FILTER_ACCEPT;
                }
                break;
            case EXACT_IGNORE_NAMESPACE:
                Matcher match = pattern_.matcher(nodeName);
                if(match.find()){
                    return FILTER_ACCEPT;
                }
                break;
        }
        return FILTER_SKIP;
    }

    public enum MatchingRule{
        EXACT,
        CONTAINS,
        EXACT_IGNORE_NAMESPACE
    }

}
