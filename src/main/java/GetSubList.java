import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Path("/GetSubList")
public class GetSubList {

    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Path("/MSISDN={MSISDN}&PRODUCTID={PID}")
    @Produces("text/plain")
    public String checkListByPID(@PathParam("MSISDN") String MSISDN,@PathParam("PID") String PID ) {
        // Return some cliched textual content
        try {
            String soapEndpointUrl = "http://10.133.177.121:19312/IB/services/EndUserServices";

            SoapHandling soapHandling = new SoapHandling(soapEndpointUrl,createRequest(MSISDN));
            return soapHandling.getGetSubListResult(PID);
        }
        catch (Exception e){
            return ("-1,catchErr," + e.getMessage());
        }
    }

    @GET
    @Path("/{MSISDN}")
    @Produces("text/plain")
    public String getList(@PathParam("MSISDN") String MSISDN) throws Exception {//
        // Return some cliched textual content

            String soapEndpointUrl = "http://10.133.177.121:19312/IB/services/EndUserServices";

            SoapHandling soapHandling = new SoapHandling(soapEndpointUrl, createRequest(MSISDN));
            return soapHandling.getSoapResponse();

    }

    private String createRequest(String MSISDN){
        String reqXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoab.org/soap/envelope/\" xmlns:soap=\"http://soapheader.ib.sdp.huawei.com\" xmlns:end=\"http://enduserservices.ib.sdp.huawei.com\" xmlns:req=\"http://request.enduserservices.ib.sdp.huawei.com\" xmlns:sch=\"http://schema.ib.sdp.huawei.com\">\n" +
                "\t<soapenv:Body>\n" +
                "\t\t<end:getSubScriptionListReq>\n" +
                "\t\t\t<req:actionType>0</req:actionType>\n" +
                "\t\t\t<req:userID>\n" +
                "\t\t\t\t<sch:ID>" + MSISDN + "</sch:ID>\n" +
                "\t\t\t\t<sch:type>0</sch:type>\n" +
                "\t\t\t</req:userID>\n" +
                "\t\t</end:getSubScriptionListReq>\n" +
                "\t</soapenv:Body>\n" +
                "</soapenv:Envelope>";
        return reqXML;
    }

}