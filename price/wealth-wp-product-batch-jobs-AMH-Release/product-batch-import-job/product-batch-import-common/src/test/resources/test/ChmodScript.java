/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		ChmodScript
 *
 * Creation Date	May 22, 2006
 * 
 * Abstract			This class provides file change mode functions. 
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	May 22, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wmd.wpc.graphql.script;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * This class provides file change mode functions.
 */
@Slf4j
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class ChmodScript {

    public static void chmodScript(String path, String mode, String chmodScriptPath) {
        
        String test = chmodScriptPath + " " + path + " " + mode;
        
        Runtime rtime = Runtime.getRuntime();
        try {
            rtime.exec(test);
        } catch (IOException e) {
            log.error("IOException: ChmodScript: " + e.getMessage());
        }     
    }

}
