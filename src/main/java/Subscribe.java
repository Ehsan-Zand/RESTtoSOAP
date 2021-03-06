import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/Sub")
public class Subscribe {
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Path("/MSISDN={MSISDN}&PRODUCTID={PID}")
    @Produces("text/plain")
    public String checkListByPID(@PathParam("MSISDN") String MSISDN, @PathParam("PID") String PID ) {
        // Return some cliched textual content
        try {
            String soapEndpointUrl = "http://10.133.177.121:19312/IB/services/BasicServices";

            SoapHandling soapHandling = new SoapHandling(soapEndpointUrl,createRequest(MSISDN,PID));
            return soapHandling.getSubUnsubResult();
        }
        catch (Exception e){
            return ("-1," + e.getMessage());
        }
    }

    @GET
    @Path("/{MSISDN}/{PID}")
    @Produces("text/plain")
    public String getResponse(@PathParam("MSISDN") String MSISDN, @PathParam("PID") String PID ) {//
        // Return some cliched textual content
        try {
            String soapEndpointUrl = "http://10.133.177.121:19312/IB/services/BasicServices";

            SoapHandling soapHandling = new SoapHandling(soapEndpointUrl, createRequest(MSISDN, PID));
            return soapHandling.getSoapResponse();
        }
        catch (Exception e){
            return ("-1," + e.getMessage());
        }
    }

    private String createRequest(String MSISDN, String PID){
        String reqXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soapheader.ib.sdp.huawei.com\" xmlns:bas=\"http://basicservices.ib.sdp.huawei.com\" xmlns:req=\"http://request.basicservices.ib.sdp.huawei.com\" xmlns:sch=\"http://schema.ib.sdp.huawei.com\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <bas:subscribeProductReq>\n" +
                "         <req:channelID>4</req:channelID>\n" +
                "         <req:subInfoList>\n" +
                "            <bas:item>\n" +
                "               <sch:payType>0</sch:payType>\n" +
                "               <sch:productID>" + PID + "</sch:productID>\n" +
                "               <sch:subType>1</sch:subType>\n" +
                "            </bas:item>\n" +
                "         </req:subInfoList>\n" +
                "         <req:userID>\n" +
                "            <sch:ID>" + MSISDN + "</sch:ID>\n" +
                "            <sch:type>0</sch:type>\n" +
                "         </req:userID>\n" +
                "      </bas:subscribeProductReq>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        return reqXML;
    }
}
