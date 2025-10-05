package com.dummy.wpc.datadaptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.impl.tool.XSTCTester.TestCase;
import org.junit.Test;


public class EntryPointTest extends TestCase {
    
    @Test
    public void testManulGenericProdExcelUpload() {
        
        String[] args = new String[] {
            "AE", "BBME", "MANUAL.PROD.SN"
        };
        EntryPoint.main(args);
    }
    
    @Test
    public void testManulUtProdExcelUpload() {
        
        String[] args = new String[] {
            "AE", "BBME", "MANUAL.PROD.UT"
        };
        EntryPoint.main(args);
    }
    
    @Test
    public void testManulBondProdExcelUpload() {
        
        String[] args = new String[] {
            "AE", "BBME", "MANUAL.PROD.BOND"
        };
        EntryPoint.main(args);
    }
    
    @Test
    public void testGenSetterGetter() {
        
        String template = "seg.set%s(mappings.get(Const.%s));";
        InputStream is = getClass().getResourceAsStream("columns.txt");
        
        List<String> columns;
        try {
            columns = FileUtils.readLines(new File("C:/Workspace/WPC30/Data_Adaptor/test/com/dummy/wpc/datadaptor/columns.txt"));
            for (String coloumn : columns) {
                StringBuffer propName = new StringBuffer("");
                String[] propNames = StringUtils.split(coloumn, "_");
                for (String prop : propNames) {
                    if (prop.length() == 1) {
                        propName.append(prop);
                    } else {
                        propName.append(prop.charAt(0)).append(prop.substring(1).toLowerCase());
                    }
                }
                
                //                System.out.println(String.format(template, new Object[] {
                //                    propName.toString(), coloumn
                //                }));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
