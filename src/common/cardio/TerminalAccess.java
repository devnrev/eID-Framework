/*
 * CardTerminals.java
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

import common.util.Logger;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 27.07.12
 * Time: 11:12
 */

/**
 * This class uses smart card io get information about connected terminals and smart cards.
 * It uses a background thread to detect newly connected terminals and cards
 */
public class TerminalAccess implements ITerminalAccess, Runnable {
    private CardTerminals terminals_;
    private Map<String, CardTerminal> connectionMap_;
    private Thread terminalThread_;
    private Object lock = new Object();
    private final int SLEEPTIME_NO_TERMINAL_CONNECTED = 2000;
    private final int SLEEPTIME_TERMINAL_CONNECTED = 10000;

    public TerminalAccess() {
        terminals_ = TerminalFactory.getDefault().terminals();
        connectionMap_ = new HashMap<String, CardTerminal>();
    }

    @Override
    public ICardAccess getCardAccessor(String cardId) {
        synchronized (lock) {
            return new CardAccess(connectionMap_.get(cardId));
        }
    }

    @Override
    public List<Terminal> getTerminals() {
        List<Terminal> terminalList = new ArrayList<Terminal>();
        synchronized (lock) {
            for (Map.Entry<String, CardTerminal> entry : connectionMap_.entrySet()) {
                terminalList.add(new Terminal(entry.getValue().getName(), entry.getKey()));
            }
        }
        return terminalList;
    }

    @Override
    public void observeTerminals() {
        terminalThread_ = new Thread(this);
        terminalThread_.start();
    }

    @Override
    public List<String> getConnectedCards() {
        List<String> terminalIds = new ArrayList<String>();
        synchronized (lock) {
            for (Map.Entry<String, CardTerminal> entry : connectionMap_.entrySet()) {
                CardTerminal terminal = entry.getValue();
                try {
                    if (terminal.isCardPresent()) {
                        terminalIds.add(entry.getKey());
                    }
                } catch (CardException e) {

                }
            }
        }
        return terminalIds;
    }

    @Override
    public List<String> getConnectedCards(int waitTime, int retries) {
        Logger.log("wait for cards");
        List<String> terminalIds = getConnectedCards();
        while ((terminalIds.size() == 0) && (retries != 0)) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                break;
            }
            Logger.log("wait for cards");
            terminalIds = getConnectedCards();
            --retries;
        }
        return terminalIds;
    }

    @Override
    public void stop() {
        terminalThread_.interrupt();
        try {
            terminalThread_.join();
        } catch (InterruptedException e) {
            Logger.log("could not join terminal thread");
        }
    }

    private void updateTerminals(List<CardTerminal> terminalList) {
        synchronized (this) {
            for (CardTerminal terminal : terminalList) {
                if (!connectionMap_.containsValue(terminal)) {
                    Logger.log("put terminal: " + terminal.getName());
                    connectionMap_.put(UUID.randomUUID().toString().replaceAll("-", ""), terminal);
                }
            }
            final Iterator<Map.Entry<String, CardTerminal>> mapIter = connectionMap_.entrySet().iterator();
            while (mapIter.hasNext()) {
                CardTerminal terminal = mapIter.next().getValue();
                if (!terminalList.contains(terminal)) {
                    Logger.log("remove terminal: " + terminal.getName());
                    mapIter.remove();
                }
            }
        }
    }

    @Override
    public void run() {
        int exceptionCounter = 0;
        while (true) {
            try {
                List<CardTerminal> terminalList = terminals_.list();
                if (terminals_.list().size() > 0) {
                    updateTerminals(terminalList);
                    Thread.sleep(SLEEPTIME_TERMINAL_CONNECTED);
                } else {
                    if (connectionMap_.size() > 0) {
                        synchronized (lock) {
                            connectionMap_.clear();
                        }
                    }
                    Thread.sleep(SLEEPTIME_NO_TERMINAL_CONNECTED);
                }
            } catch (InterruptedException e) {
                return;
            } catch (CardException e) {
                Logger.log("error while getting terminals");
                if (exceptionCounter++ > 5) {
                    return;
                }
            }
        }
    }
}
