
package com.feedme.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ApplicationSevice", targetNamespace = "http://webservice.feedme.com/", wsdlLocation = "http://feedme-ws.us-east-1.elasticbeanstalk.com/ApplicationSevice?wsdl")
public class ApplicationSevice_Service
    extends Service
{

    private final static URL APPLICATIONSEVICE_WSDL_LOCATION;
    private final static WebServiceException APPLICATIONSEVICE_EXCEPTION;
    private final static QName APPLICATIONSEVICE_QNAME = new QName("http://webservice.feedme.com/", "ApplicationSevice");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://feedme-ws.us-east-1.elasticbeanstalk.com/ApplicationSevice?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        APPLICATIONSEVICE_WSDL_LOCATION = url;
        APPLICATIONSEVICE_EXCEPTION = e;
    }

    public ApplicationSevice_Service() {
        super(__getWsdlLocation(), APPLICATIONSEVICE_QNAME);
    }

    public ApplicationSevice_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), APPLICATIONSEVICE_QNAME, features);
    }

    public ApplicationSevice_Service(URL wsdlLocation) {
        super(wsdlLocation, APPLICATIONSEVICE_QNAME);
    }

    public ApplicationSevice_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, APPLICATIONSEVICE_QNAME, features);
    }

    public ApplicationSevice_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ApplicationSevice_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ApplicationSevice
     */
    @WebEndpoint(name = "ApplicationSevicePort")
    public ApplicationSevice getApplicationSevicePort() {
        return super.getPort(new QName("http://webservice.feedme.com/", "ApplicationSevicePort"), ApplicationSevice.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ApplicationSevice
     */
    @WebEndpoint(name = "ApplicationSevicePort")
    public ApplicationSevice getApplicationSevicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.feedme.com/", "ApplicationSevicePort"), ApplicationSevice.class, features);
    }

    private static URL __getWsdlLocation() {
        if (APPLICATIONSEVICE_EXCEPTION!= null) {
            throw APPLICATIONSEVICE_EXCEPTION;
        }
        return APPLICATIONSEVICE_WSDL_LOCATION;
    }

}
