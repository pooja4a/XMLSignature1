package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;



public class GenericHTTPS
{

 public GenericHTTPS()
 {
 }

 public static java.net.HttpURLConnection getHTTPConnection(java.lang.String sURL)
     throws java.io.IOException
 {
     java.net.URL u = new URL(sURL);
     java.net.URLConnection uc = u.openConnection();
     java.net.HttpURLConnection connection = (java.net.HttpURLConnection)uc;
     connection.setDoInput(true);
     connection.setDoOutput(true);
     connection.setRequestMethod("POST");
     connection.setRequestProperty("Content-Type", "text/xml");
     return connection;
 }

 public static void main(java.lang.String args[])
 {
     java.lang.String strRequest = "";
     String strXMLFilename="xml//digitallysignedreq.xml";
     java.lang.String strURL = "https://volt.amsl:6063/volt?ucId=REGISTER&feSessionId=33333333";
     java.lang.String strTrustStore = "D:\\keystore.jks";
     java.lang.String strTrustStorePassword = "password";
     java.lang.System.out.println(strRequest);
     java.lang.System.out.println(strURL);
     java.lang.System.out.println(strTrustStore);
     java.lang.System.out.println(strTrustStorePassword);
     java.lang.System.setProperty("javax.net.ssl.trustStore", strTrustStore);
     java.lang.System.setProperty("javax.net.ssl.trustStorePassword", strTrustStorePassword);
     java.lang.System.setProperty("javax.net.debug", "all");
    	
     
        File input = new File(strXMLFilename);
    	 
    	 PostMethod post = new PostMethod(strURL);

         // Request content will be retrieved directly
         // from the input stream
         // Per default, the request content needs to be buffered
         // in order to determine its length.
         // Request body buffering can be avoided when
         // content length is explicitly specified
         try {
			post.setRequestEntity(new InputStreamRequestEntity(
			         new FileInputStream(input), input.length()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         // Specify content type and encoding
         // If content encoding is not explicitly specified
         // ISO-8859-1 is assumed
         post.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");

         // Get HTTP client
         HttpClient httpclient =  new HttpClient();

         // Execute request
         try {

             int result =-1;
			try {
				result = httpclient.executeMethod(post);
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

             // Display status code
             System.out.println("Response status code: " + result);

             // Display response
             System.out.println("Response body: ");
             try {
				System.out.println(post.getResponseBodyAsString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

         } finally {
             // Release current connection to the connection pool 
             // once you are done
             post.releaseConnection();
         }
     }
 }
 
 
    	 
//
//    	 java.net.HttpURLConnection connection = GenericHTTPS.getHTTPConnection(sURL);
//         java.io.OutputStream out = connection.getOutputStream();
//         java.io.Writer wout = new OutputStreamWriter(out);
//         wout.write(strRequest);
//         wout.flush();
//         wout.close();
//         java.io.InputStream instream = connection.getInputStream();
//         byte data[] = new byte[1024];
//         int iRead;
//         java.lang.String sResMsg;
//         for(sResMsg = ""; (iRead = instream.read(data)) != -1; sResMsg = (new StringBuilder(java.lang.String.valueOf(sResMsg))).append(new String(data)).toString());
//         instream.close();
//         java.lang.System.out.println((new StringBuilder("XML Response: ")).append(sResMsg).toString());
//     }
//     catch(java.net.UnknownHostException e)
//     {
//         e.printStackTrace();
//     }
//     catch(java.io.IOException e)
//     {
//         e.printStackTrace();
//     }
// }