package com.c01;

import org.junit.Test;
import com.c01.InstructorMenu;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Shevlin on 11/19/2017.
 */

public class MimeTypeTest {

    @Test
    public void wordDocumentTest() throws Exception{
        InstructorMenu im = new InstructorMenu();

        String type = im.getMimeType("file.doc");
        assertEquals("application/msword", type);
    }

    @Test
    public void pdfTest() throws Exception{
        InstructorMenu im = new InstructorMenu();

        String type = im.getMimeType("anotherfile.pdf");
        assertEquals("application/pdf", type);
    }

    @Test
    public void imageTest() throws Exception{
        InstructorMenu im = new InstructorMenu();

        String type = im.getMimeType("wowanotherfile.jpg");
        assertEquals("image/jpeg", type);
    }

}
