
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SoapHandling {

    private String soapResponse;
    private ArrayList<String> productIDList;
    private ArrayList<String> resultCodeList;
    private ArrayList<String> resultMessageList;

    public SoapHandling(String soapEndpointUrl, String reqXML) throws Exception {
        soapResponse = callSoapWebService(soapEndpointUrl,reqXML);
        NodeList nodList = xmlParser(soapResponse);
        productIDList = getTagValue(nodList,"productID");
        resultCodeList = getTagValue(nodList,"resultCode");
        resultMessageList = getTagValue(nodList,"resultMessage");
    }

    public String getSoapResponse() {
        return soapResponse;
    }

    public String getGetSubListResult(String PID){
        if (resultCodeList != null && resultCodeList.size() > 0) {
            String cRc = checkResultCode();
            if (cRc != null) return cRc;
            for (int i=0;i<productIDList.size();i++) {
                if(productIDList.get(i).equals(PID)){
                    return "0";
                }
            }
            return "1";
        }
        else {
            return "-1,NodeList is Null";
        }
    }

    private String checkResultCode() {
        for (int i=0;i<resultCodeList.size();i++) {
            if(!resultCodeList.get(i).equals("00000000")){
                return "-1," + resultCodeList.get(i) + "," + resultMessageList.get(i);
            }
        }
        return null;
    }

    public String getSubUnsubResult(){
        String cRc = checkResultCode();
        if (cRc != null) return cRc;
        return "0";
    }

    private String callSoapWebService(String soapEndpointUrl, String reqXML) throws Exception {
            URL oURL = new URL(soapEndpointUrl);
            HttpURLConnection con = (HttpURLConnection) oURL.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            //con.setRequestProperty("SOAPAction", "http://herongyang.com/MyService#MyMethod");

            OutputStream reqStream = con.getOutputStream();
            reqStream.write(reqXML.getBytes());

            InputStream resStream = con.getInputStream();

            return inputStreamToString(resStream);
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString();
    }

    private NodeList xmlParser(String xml){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = factory.newDocumentBuilder();
            Document document = null;
            document = builder.parse(new InputSource(new StringReader(xml)));
            Element rootElement = document.getDocumentElement();
            NodeList list = rootElement.getElementsByTagName("*");
            return list;
        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ParserConfigurationException e) {
            return null;
        }

    }

    private ArrayList<String> getTagValue(NodeList list,String tagName){
        ArrayList<String> sub = new ArrayList<String>();
        if (list != null && list.getLength() > 0) {
            for (int i=0;i<list.getLength();i++) {
                if(list.item(i).getNodeName().contains(tagName))
                    sub.add(list.item(i).getChildNodes().item(0).getNodeValue());
            }
        }
        return sub;
    }

}
