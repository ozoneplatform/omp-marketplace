package ozone.test.stub;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class StubServer extends HttpServlet {

  protected static final String FULL_IMPORT_FILE = "/marketplaceImportStub-full.json"; 
  protected static final String DELTA_IMPORT_FILE = "/marketplaceImportStub-delta1.json"; 

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {

    res.setContentType("text/json");
    PrintWriter out = res.getWriter();


    String dataFile = FULL_IMPORT_FILE;
    long delay = 0l;

    // Look for a file request
    String fileName = req.getParameter("file");
    if (fileName != null) {
        if (fileName.charAt(0) != '/') {
            fileName = '/' + fileName;
        }
        dataFile = fileName;
    }
    String delayStr = req.getParameter("delay");
    if (delayStr != null) {
        // Try to parse as a numeric
        try {
            delay = Long.parseLong(delayStr);
            // if this was already given as ms, do not expand to ms
            if (delay < 1000) {
                delay = delay * 1000;
            }
        }
        catch (Exception e) {
            System.err.println("Error parsing delay value: "+delayStr);
        }
    }

    /*
    // HOLD: there is currently no point in taking in this field unless this app starts saving state
    // Look for a delta update request
    String updateDateStr = req.getParameter("updateSince");
    
    if (updateDateStr != null) {
        dataFile = DELTA_IMPORT_FILE;
    }
    */
    
    // See if we need to delay
    if (delay > 0) {
        try {
            Thread.currentThread().sleep(delay);
        }
        catch (Exception e) {}
    }

    // Get content
    String buff = null;
    try {
       buff = load(dataFile);
    }
    catch (Exception e) {
        System.err.println("Error loading file: "+dataFile);
        e.printStackTrace();
    }

    // WRITE CONTENT
    if (buff != null && buff.length() > 0) {
       out.println(buff);
    }
    else {
        out.println("success:'false',message:'stubServer error loading; see log file for details'");
    }
  }

  /*
   *  Load the given file. Should be a location on the classpath.
   */
  private String load(String fileName) throws Exception {
    ClassLoader loader = this.getClass().getClassLoader();
    java.net.URL url = loader.getResource(fileName); 
    String result = null;

    if (url != null) {
        java.io.File fullFile = null;
        
        try {
           fullFile = new java.io.File(url.toURI());
        }
        catch (Exception e) {
           System.err.println("ERROR: could not open resource: " + e);
        }

        if (fullFile != null) {
            // Read content and return
            FileReader reader = new FileReader(fullFile);
           if (reader != null) {
                StringBuilder sb = new StringBuilder(4096);
                int l;
                int cnt = 0;
                try {
                   while ((l = reader.read()) != -1) {
                       ++cnt;
                       //log.trace "[$cnt] Val: [0x${Integer.toHexString(l)}]"
                       sb.append((char)l);
                   }
                } finally {
                    reader.close();
                }
                //log.debug "Stubbed content length: " + sb.length()
                result = sb.toString();
            }
            else {
                 System.err.println("ERROR: could not open reader");
            }
            
        }
        else {
           System.err.println("ERROR: could not open file: "+ fileName);
        }

    }
    else {
        System.err.println("ERROR: could not find resource: "+ fileName);
    }
    return result;
  }
  

  public void doPost (HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
     doGet (req, res);
  }

  public String getServletInfo() {
     return "Stub servlet for Ozone Marketplace import testing";
  }
}

