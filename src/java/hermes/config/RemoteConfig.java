//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.19 at 10:30:34 AM GMT 
//


package hermes.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RemoteConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RemoteConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="namingConfig" type="{}NamingConfig"/>
 *       &lt;/sequence>
 *       &lt;attribute name="binding" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoteConfig", propOrder = {
    "namingConfig"
})
public class RemoteConfig {

    @XmlElement(required = true)
    protected NamingConfig namingConfig;
    @XmlAttribute(name = "binding")
    protected String binding;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Gets the value of the namingConfig property.
     * 
     * @return
     *     possible object is
     *     {@link NamingConfig }
     *     
     */
    public NamingConfig getNamingConfig() {
        return namingConfig;
    }

    /**
     * Sets the value of the namingConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamingConfig }
     *     
     */
    public void setNamingConfig(NamingConfig value) {
        this.namingConfig = value;
    }

    /**
     * Gets the value of the binding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBinding() {
        return binding;
    }

    /**
     * Sets the value of the binding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBinding(String value) {
        this.binding = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
